package dev.mvc.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;
import dev.mvc.tool.MailTool;
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

	/** 인증 번호 저장 변수 */
	private String verify_code;
	
	/**
	 * 인증 메일 전송
	 * 
	 * @return
	 */
	@PostMapping(value = "/mail")
	@ResponseBody
	public Map<String, Object> mail(String receiver) {
		System.out.println("Cont RECEIVER ---> : " + receiver);
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			/* 인증 번호 생성 */
			Random random = new Random();
			int code = random.nextInt(999999);
			verify_code = String.format("%06d", code);
			System.out.println("---> verify code : " + code);
			
			MailTool mailTool = new MailTool();
			mailTool.send(receiver, "-", "[DESK TOUR] 이메일 인증입니다.", "Verify Code : " + code); // 메일 전송
			response.put("status", "success");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("msg", e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 인증 번호 확인
	 * 
	 * @param code
	 * @return
	 */
	@PostMapping(value="/verifyCode")
	@ResponseBody
	public Map<String, Object> verifyCode(@RequestParam String code) {
		Map<String, Object> response = new HashMap<>();
		
		if(verify_code != null && verify_code.equals(code)) {
			response.put("status", "success");
		} else {
			response.put("status", "error");
			response.put("msg", "인증번호가 일치하지 않습니다.");
		}
		
		return response;
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
	public String create(Model model, AccountVO accountVO, RecommendVO recommendVO) {
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
	 * 회원 가입 메시지
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
	 * 회원 목록(관리자) (미완 - 세션 구현 => 로그인한 회원이 관리자인지 확인 필요)
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

		/* 프로필 사진 불러오기 */
		long acc_img_size = accountVO.getAcc_img_size();
		String img_size_label = Tool.unit(acc_img_size);
		accountVO.setImg_size_label(img_size_label);

		model.addAttribute("accountVO", accountVO);

		/* 해시태그 폼 */
		List<HashtagVO> hashtag_list = this.accountProc.hashtagList(); // 해시태그 목록 조회
		model.addAttribute("hashtag_list", hashtag_list); // 모델에 해시태그 목록 추가

		String[] tagCodeList = this.accountProc.tagCodeList().split(",");
		List<String> tag_codes = Arrays.asList(tagCodeList);
		model.addAttribute("tag_codes", tag_codes);

		/* 회원가입 시 선택한 해시태그들 */
		String selectedTagsStr = this.accountProc.selectedTags(acc_no);
		List<String> selected_tags;

		if (selectedTagsStr != null) {
			String[] selectedTags = selectedTagsStr.split(",");
			selected_tags = Arrays.asList(selectedTags);
		} else {
			selected_tags = new ArrayList<>(); // 선택한 해시태그가 없을 경우 빈 리스트로 초기화
		}
		model.addAttribute("selected_tags", selected_tags);

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
	public String update_proc(Model model, AccountVO accountVO, RecommendVO recommendVO,
			@RequestParam(value = "selected_hashtags", required = false) List<Integer> selected_hashtags) {

		int checkName_cnt = this.accountProc.checkName(accountVO.getAcc_name());
		if (checkName_cnt <= 1) {
			int cnt = this.accountProc.update(accountVO);
			if (cnt == 1) { // 수정 성공
				model.addAttribute("acc_name", accountVO.getAcc_name());
				model.addAttribute("acc_tel", accountVO.getAcc_tel());
				model.addAttribute("acc_age", accountVO.getAcc_age());

				// 기존의 추천 해시태그 삭제
				this.accountProc.deleteRecommend(accountVO.getAcc_no());

				// 새로운 해시태그 추천 추가
				for (Integer tag_no : selected_hashtags) {
					recommendVO.setAcc_no(accountVO.getAcc_no());
					recommendVO.setTag_no(tag_no);
					this.accountProc.insertRecommend(recommendVO);
				}

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

	@PostMapping(value = "/updatePic")
	public String updatePic(Model model, AccountVO accountVO, int acc_no) {
		System.out.println("---> RequestParam acc_no: " + acc_no);

		/*
		 * <form name="pic_update_frm" id="pic_update_frm" th:object="${accountVO}"
		 * method="post" action="/account/updatePic" onsubmit="return checkImg()"
		 * enctype="multipart/form-data">
		 */

		String acc_img = ""; // 원본 파일명
		String acc_saved_img = ""; // 저장된 파일명
		String acc_thumb_img = ""; // 미리보기 파일명

		String upDir = PicUpload.getUploadDir();
		System.out.println("---> upDir: " + upDir);

		// 전송 파일이 없어도 MF 객체 생성
		MultipartFile mf = accountVO.getAcc_img_mf();

		acc_img = mf.getOriginalFilename(); // 원본 파일명
		System.out.println("---> acc_img(원본파일): " + acc_img);

		long acc_img_size = mf.getSize(); // 파일 크기
		if (acc_img_size > 0) { // 파일 크기 체크
			if (Tool.isImage(acc_img) == true) { // 업로드 가능한 이미지 파일인지 검사
				/* 파일 저장 후 업로드된 파일명 리턴 */
				acc_saved_img = Upload.saveFileSpring(mf, upDir);
				acc_thumb_img = Tool.preview(upDir, acc_saved_img, 500, 500);

				accountVO.setAcc_no(accountVO.getAcc_no());
				System.out.println("---> accountVO.getAcc_no(): " + accountVO.getAcc_no());
				accountVO.setAcc_img(acc_img); // 순수 원본 파일명
				accountVO.setAcc_saved_img(acc_saved_img); // 저장된 파일명(중복 파일명 처리)
				accountVO.setAcc_thumb_img(acc_thumb_img); // 미리보기 파일명
				accountVO.setAcc_img_size(acc_img_size); // 파일 크기

				// 저장된 파일 경로 로그 출력
				String fullSavedPath = upDir + acc_saved_img;
				System.out.println("---> Full saved img path: " + fullSavedPath);
			} else { // 이미지 파일 외 형식
				model.addAttribute("code", "check_upload_file_fail");
				model.addAttribute("cnt", 0);
				return "/account/msg";
			}
		}

		int cnt = this.accountProc.updatePic(accountVO);
		System.out.println("---> updatePic cnt: " + cnt);
		model.addAttribute("code", "file_upload_success");
		model.addAttribute("cnt", 2);
		return "/account/msg";
	}

	/** 비밀번호 확인 */

	/** 비밀번호 변경 */

	/**
	 * 회원 정보 삭제 페이지
	 * 
	 * @param model
	 * @param acc_no
	 * @return
	 */
	@GetMapping(value = "/delete")
	public String delete(Model model, int acc_no) {
		AccountVO accountVO = this.accountProc.read(acc_no);
		model.addAttribute("accountVO", accountVO);

		return "account/delete";
	}

	/**
	 * 회원 정보 삭제 처리
	 * 
	 * @param model
	 * @param acc_no
	 * @return
	 */
	@PostMapping(value = "/delete")
	public String delete_proc(Model model, Integer acc_no) {
		int cnt = this.accountProc.delete(acc_no);

		if (cnt == 1) { // 삭제 성공
			return "redirect:/account/list";
		} else {
			model.addAttribute("code", "delete_fail");
			return "account/msg";
		}
	}

}
