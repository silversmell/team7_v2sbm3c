package dev.mvc.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@RequestMapping("/account")
@Controller
public class AccountCont {
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc") // @Service("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;

	public AccountCont() {
		System.out.println("-> AccountCont created.");
	}

	/**
	 * 아이디(이메일) 중복 확인
	 * 
	 * @param acc_id
	 * @return
	 */
	@GetMapping(value = "/checkID") // http://localhost:9093/account/checkID?acc_id=user1
	@ResponseBody
	public String checkID(String acc_id) {
		// System.out.println("---> acc_id: " + acc_id);

		int cnt = this.accountProc.checkID(acc_id);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		return obj.toString();
	}

	/**
	 * 이름(닉네임) 중복 확인
	 * 
	 * @param acc_name
	 * @return
	 */
	@GetMapping(value = "/checkName") // http://localhost:9093/account/checkName?acc_name=user1
	@ResponseBody
	public String checkName(String acc_name) {
		// System.out.println("---> acc_name: " + acc_name);

		int cnt = this.accountProc.checkName(acc_name);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		return obj.toString();
	}

	/**
	 * 회원 가입 폼
	 * 
	 * @param model
	 * @param accountVO
	 * @param recommendVO
	 * @return
	 */
	@GetMapping(value = "/create") // http://localhost:9093/account/create
	public String create_form(Model model, AccountVO accountVO, RecommendVO recommendVO) {
		List<HashtagVO> hashtag_list = this.accountProc.hashtagList(); // 해시태그 목록 조회
		model.addAttribute("hashtag_list", hashtag_list); // 모델에 해시태그 목록 추가
		// System.out.println("hashtags: " + hashtag_list.get(0).getTag_name()); //
		// '브라운'

		String[] tagCodeList = this.accountProc.tagCodeList().split(",");
		List<String> tag_codes = Arrays.asList(tagCodeList);
		model.addAttribute("tag_codes", tag_codes);

		// System.out.println("--> tag_codes: " + tag_codes.get(0)); // '분위기'

		return "account/create"; // /templates/account/create.html
	}

	/**
	 * 회원 가입 처리
	 * 
	 * @param accountVO
	 * @param recommendVO
	 * @param selected_hashtags
	 * @return
	 */
	@PostMapping(value = "/create")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> create_proc(AccountVO accountVO, RecommendVO recommendVO,
			@RequestParam(value = "selected_hashtags", required = false) List<Integer> selected_hashtags) {
		Map<String, Object> response = new HashMap<>();

		int checkID_cnt = this.accountProc.checkID(accountVO.getAcc_id());
		int checkName_cnt = this.accountProc.checkName(accountVO.getAcc_name());

		System.out.println("====> selected_hashtags: " + selected_hashtags);

		if ((checkID_cnt == 0) && (checkName_cnt == 0)) {
			accountVO.setAcc_grade(15); // 15: 일반 회원
			int acc_no = this.accountProc.create(accountVO);

			if (acc_no != 0) { // 회원 가입 성공
				response.put("code", "create_success");
				response.put("acc_id", accountVO.getAcc_id());
				response.put("acc_name", accountVO.getAcc_name());
				// 암호화 미완
				response.put("acc_pw", accountVO.getAcc_pw());

				// 선택된 해시태그 저장
				System.out.println("====> selected_hashtags: " + selected_hashtags);
				for (Integer tag_no : selected_hashtags) {
					System.out.println("accountVO.getAcc_no() = " + accountVO.getAcc_no());
					System.out.println("tag_no = " + tag_no);

					recommendVO.setAcc_no(accountVO.getAcc_no());
					recommendVO.setTag_no(tag_no);
					this.accountProc.insertRecommend(recommendVO);
				}
			} else {
				response.put("code", "create_fail");
				response.put("cnt", 0);
			}
			response.put("cnt", 1);
		} else if (checkID_cnt != 0) { // 아이디 중복
			response.put("code", "duplicate_id");
			response.put("cnt", 0);
		} else if (checkName_cnt != 0) {
			response.put("code", "duplicate_name");
			response.put("cnt", 0);
		}

		return ResponseEntity.ok(response);
	}

	/**
	 * 회원가입 메시지
	 * 
	 * @param model
	 * @param accountVO
	 * @param code
	 * @param cnt
	 * @return
	 */
	@GetMapping(value = "/msg") // http://localhost:9093/account/msg
	public String msg(Model model, AccountVO accountVO, @RequestParam(value = "code") String code,
			@RequestParam(value = "cnt") int cnt) {
		System.out.println("--> cnt: " + cnt);

		model.addAttribute("code", code);
		model.addAttribute("cnt", cnt);

		return "account/msg"; // /templates/account/msg.html
	}

	/**
	 * 회원 목록 조회(관리자) (미완 - 세션 구현 => 로그인한 회원이 관리자인지 확인 필요)
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/list")
	public String list(Model model) { // 아직 로그인 세션 구현 X, HttpSession session

		ArrayList<AccountVO> list = this.accountProc.list();
		model.addAttribute("list", list);

		return "account/list";
	}

	/**
	 * 회원 정보 조회(회원 목록, 마이페이지)
	 * 
	 * @param model
	 * @param acc_no
	 * @return
	 */
	@GetMapping(value = "/read")
	public String read(Model model, int acc_no) { // 아직 세션 구현 X, HttpSession session

		AccountVO accountVO = this.accountProc.read(acc_no);
		model.addAttribute("accountVO", accountVO);

		return "account/read";
	}

	/**
	 * 회원 정보 수정(회원 목록, 마이페이지)
	 * 
	 * @param model
	 * @param accountVO
	 * @return
	 */
	@PostMapping(value = "/update")
	public String update_proc(Model model, AccountVO accountVO) {

		int checkName_cnt = this.accountProc.checkName(accountVO.getAcc_name());
		if (checkName_cnt == 0) {
			int cnt = this.accountProc.update(accountVO);
			if (cnt == 1) { // 수정 성공
				model.addAttribute("acc_name", accountVO.getAcc_name());
				model.addAttribute("acc_tel", accountVO.getAcc_tel());
				model.addAttribute("acc_age", accountVO.getAcc_age());

				model.addAttribute("code", "update_success");
				model.addAttribute("cnt", cnt);
			} else {
				model.addAttribute("code", "update_fail");
				model.addAttribute("cnt", cnt);
			}
		} else {
			model.addAttribute("code", "duplicate_name");
			model.addAttribute("cnt", 0);
		}

		return "account/msg";
	}

}
