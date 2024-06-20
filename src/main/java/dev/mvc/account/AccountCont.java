package dev.mvc.account;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.category.CategoryProcInter;
import dev.mvc.category.CategoryVO;
import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;
import dev.mvc.share_contents.Contents;
import dev.mvc.share_contents.Share_contentsProc;
import dev.mvc.share_contentsdto.Contents_urlVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.MailTool;
import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/account")
@Controller
public class AccountCont {
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc") // @Service("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;

	@Autowired
	@Qualifier("dev.mvc.category.CategoryProc")
	private CategoryProcInter categoryProc;

	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private Share_contentsProc scontentsProc;

	@Autowired
	Security security;

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
		System.out.println("---> checkName acc_name: " + acc_name);

		int cnt = this.accountProc.checkName(acc_name);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		System.out.println("---> checkName cnt: " + cnt);

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
		System.out.println("---> Cont RECEIVER : " + receiver);

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
	@PostMapping(value = "/verifyCode")
	@ResponseBody
	public Map<String, Object> verifyCode(@RequestParam String code, @RequestParam String acc_id) {
		Map<String, Object> response = new HashMap<>();

		if (verify_code != null && verify_code.equals(code)) {
			response.put("status", "success");
			response.put("acc_id", acc_id);
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
	public String list(HttpSession session, Model model) {
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
	public String read(HttpSession session, Model model, int acc_no) {

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
	 * 로그인 폼 (쿠키 기반)
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/login")
	public String login_form(Model model, HttpServletRequest request, AccLogVO accLogVO, String url) {

		/* Cookie */
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;

		String ck_acc_id = ""; // acc_id 저장
		String ck_id_save = ""; // acc_id 저장 여부 확인

		if (cookie != null) { // 쿠키 존재
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("--> cookies[" + i + "]" + cookies[i]);
				cookie = cookies[i]; // 쿠키 객체 추출

				if (cookie.getName().equals("ck_acc_id")) {
					ck_acc_id = cookie.getValue(); // id(email)
				} else if (cookie.getName().equals("ck_id_save")) {
					ck_id_save = cookie.getValue(); // Y, N
				}
			}
		}

		model.addAttribute("ck_id", ck_acc_id);
		model.addAttribute("ck_id_save", ck_id_save);
		model.addAttribute("url", url);

		return "account/login";
	}

	/**
	 * 로그인 처리 (쿠키 기반)
	 * 
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/login")
	public String login_proc(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model,
			AccLogVO accLogVO, String url, String acc_id, String acc_pw,
			@RequestParam(value = "id_save", defaultValue = "") String id_save) {
		String ip = request.getRemoteAddr(); // IP
		System.out.println("---> 접속한 IP: " + ip);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("acc_id", acc_id);
		map.put("acc_pw", this.security.aesEncode(acc_pw));

		int cnt = this.accountProc.login(map);
		System.out.println("---> login_proc cnt: " + cnt);

		model.addAttribute("cnt", cnt);

		if (id_save.equals("Y")) { // Checkbox 체크, acc_id 저장
			Cookie ck_acc_id = new Cookie("acc_id", acc_id);
			ck_acc_id.setPath("/"); // root 폴더에 쿠키를 기록 -> 모든 경로에서 쿠키 접근 가능
			ck_acc_id.setMaxAge(60 * 60 * 24 * 30); // 30 days, 초단위
			response.addCookie(ck_acc_id); // acc_id 저장
		} else { // N, Checkbox 해제, acc_id 미저장
			Cookie ck_acc_id = new Cookie("ck_id", "");
			ck_acc_id.setPath("/");
			ck_acc_id.setMaxAge(0);
			response.addCookie(ck_acc_id);
		}

		/* Cookie - Checkbox 체크 확인 */
		Cookie ck_id_save = new Cookie("ck_id_save", id_save);
		ck_id_save.setPath("/");
		ck_id_save.setMaxAge(60 * 60 * 24 * 30); // 30 days
		response.addCookie(ck_id_save);
		if (url.length() > 0) {
			return "redirect:" + url;
		}

		if (cnt == 1) { // 로그인 성공
			// id를 이용한 회원 정보 조회
			AccountVO accountVO = this.accountProc.readById(acc_id);
			session.setAttribute("acc_no", accountVO.getAcc_no());
			// int acc_no = (int)session.getAttribute("acc_no"); // Session에서 가져오기

			session.setAttribute("acc_id", accountVO.getAcc_id());
			session.setAttribute("acc_name", accountVO.getAcc_name());

			if (accountVO.getAcc_grade() >= 1 && accountVO.getAcc_grade() <= 10) {
				session.setAttribute("acc_grade", "admin"); // 관리자
			} else if (accountVO.getAcc_grade() >= 11 && accountVO.getAcc_grade() <= 20) {
				session.setAttribute("acc_grade", "member"); // 일반 회원
			} else if (accountVO.getAcc_grade() >= 30 && accountVO.getAcc_grade() <= 39) {
				session.setAttribute("acc_grade", "suspended"); // 정지 회원
			} else if (accountVO.getAcc_grade() == 99) {
				session.setAttribute("acc_grade", "withdrawn"); // 탈퇴 회원
			} else {
				session.setAttribute("acc_grade", "guest");
			}

			/* 회원 로그 저장 */
			accLogVO.setAcc_no(accountVO.getAcc_no());
			accLogVO.setAcc_log_ip(ip);
			int log_cnt = this.accountProc.recordLog(accLogVO);
			System.out.println("---> Record_log_cnt: " + log_cnt);

			/* Cookie - acc_id 관련 쿠키 저장 (RecommendCont 전달) */
			Cookie ck_acc_id = new Cookie("acc_id", acc_id);
			ck_acc_id.setPath("/"); // root 폴더에 쿠키를 기록 -> 모든 경로에서 쿠키 접근 가능
			ck_acc_id.setMaxAge(60 * 60 * 24 * 30); // 30 days, 초단위
			response.addCookie(ck_acc_id); // acc_id 저장

			return "redirect:/";
		} else {
			model.addAttribute("code", "login_fail");
			model.addAttribute("cnt", 0);
			return "account/msg";
		}

	}

//	/**
//	 * 전체 회원 로그 목록
//	 * 
//	 * @param model
//	 * @return
//	 */
//	@GetMapping(value = "/log_list")
//	public String log_list(Model model) {
//		ArrayList<Map<String, Object>> logs = this.accountProc.logList();
//
//		/*
//		 * for (Map<String, Object> log : logs) { AccountVO account = (AccountVO)
//		 * log.get("account"); AccLogVO accLog = (AccLogVO) log.get("accLog");
//		 * 
//		 * System.out.println("Account acc_id: " + account.getAcc_id());
//		 * System.out.println("Log acc_log_ip: " + accLog.getAcc_log_ip()); }
//		 */
//
//		model.addAttribute("logs", logs);
//
//		//return "account/log_list";
//		return "account/log_list_by_search";
//	}

	/**
	 * 회원 로그 목록
	 * 
	 * 로그 검색
	 * 1) 회원 아이디
	 * 2) 아이피
	 * 3) 기간
	 * 
	 * @param session
	 * @param model
	 * @param word_id
	 * @param word_ip
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	@GetMapping(value = "/log_list")
	public String logListBySearch(HttpSession session, Model model,
			@RequestParam(name = "word_id", defaultValue = "") String word_id,
			@RequestParam(name = "word_ip", defaultValue = "") String word_ip,
	        @RequestParam(name = "start_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
	        @RequestParam(name = "end_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date) {

	    word_id = Tool.checkNull(word_id).trim();
	    word_ip = Tool.checkNull(word_ip).trim();

		Map<String, String> words = new HashMap<>();
		if (!word_id.isEmpty()) {
			words.put("word_id", word_id);
		}
		if (!word_ip.isEmpty()) {
			words.put("word_ip", word_ip);
		}
	    if (start_date != null && end_date != null) {
	    	words.put("start_date", start_date.toString());
	    	words.put("end_date", end_date.toString());
	    }

		ArrayList<Map<String, Object>> logs = this.accountProc.searchLogs(words);

	    model.addAttribute("word_id", word_id);
	    model.addAttribute("word_ip", word_ip);
	    model.addAttribute("start_date", start_date);
	    model.addAttribute("end_date", end_date);
	    model.addAttribute("logs", logs);

		return "account/log_list_by_search";
	}

	/**
	 * 로그아웃
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate(); // 모든 세션 변수 삭제
		return "redirect:/";
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

	/**
	 * 프로필 사진 업데이트
	 * 
	 * @param model
	 * @param accountVO
	 * @param acc_no
	 * @return
	 */
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

	/**
	 * 비밀번호 변경 폼
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/update_passwd")
	public String update_passwd_form(HttpSession session, Model model) {
		int acc_no = (int) session.getAttribute("acc_no"); // session

		AccountVO accountVO = this.accountProc.read(acc_no);
		model.addAttribute("accountVO", accountVO);

		return "account/update_passwd";
	}

	/**
	 * 현재 비밀번호 확인
	 * 
	 * @param session
	 * @param json_src
	 * @return
	 */
	@PostMapping(value = "/check_passwd")
	@ResponseBody
	public String checkPasswd(HttpSession session, @RequestBody String json_src) {
		System.out.println("---> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
		JSONObject src = new JSONObject(json_src); // String -> JSON
		String current_passwd = (String) src.get("current_passwd"); // 값 가져오기
		System.out.println("---> current_passwd:  " + current_passwd);

		int acc_no = (int) session.getAttribute("acc_no"); // session에서 가져오기
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("acc_no", acc_no);
		map.put("acc_pw", this.security.aesEncode(current_passwd));

		int cnt = this.accountProc.checkPasswd(map);
		System.out.println("---> check_passwd cnt: " + cnt);

		JSONObject json = new JSONObject();
		json.put("cnt", cnt);
		System.out.println("---> json.toString() cnt: " + json.toString());

		return json.toString();
	}

	/**
	 * 비밀번호 변경
	 * 
	 * @param session
	 * @param model
	 * @param current_passwd
	 * @param new_passwd
	 * @return
	 */
	@PostMapping(value = "/update_passwd")
	public String updatePasswd(HttpSession session, Model model, String current_passwd, String new_passwd) {

		if (this.accountProc.isMember(session)) {
			int acc_no = (int) session.getAttribute("acc_no"); // session

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("acc_no", acc_no);
			map.put("acc_pw", this.security.aesEncode(current_passwd));
			System.out.println("--> updatePasswd current_passwd: " + current_passwd);

			/* Form 없이 해커가 해킹했을 경우 대비 패스워드 재검사 */
			int cnt = this.accountProc.checkPasswd(map);

			if (cnt == 0) { // 현재 패스워드 불일치

			} else { // 현재 패스워드 일치
				HashMap<String, Object> map_new_passwd = new HashMap<String, Object>();
				map_new_passwd.put("acc_no", acc_no);
				map_new_passwd.put("acc_pw", this.security.aesEncode(new_passwd));
				System.out.println("--> updatePasswd new_passwd: " + new_passwd);

				int update_cnt = this.accountProc.updatePasswd(map_new_passwd);
				if (update_cnt == 1) {
					model.addAttribute("code", "passwd_update_success");
					model.addAttribute("cnt", update_cnt);
				} else {
					model.addAttribute("code", "passwd_update_fail");
					model.addAttribute("cnt", update_cnt);
				}
			}
			return "account/msg";
		} else {
			return "redirect:/account/login";
		}

	}

	/**
	 * 비밀번호 재설정(비밀번호 찾기) - 아이디 인증 페이지 - GET
	 * 
	 * @return
	 */
	@GetMapping(value = "/find_passwd")
	public String findPasswd() {

		return "account/find_passwd";
	}

	/**
	 * 비밀번호 재설정(비밀번호 찾기) - 아이디 인증 후 비밀번호 재설정 페이지로 이동 - POST(find_passwd에서
	 * reset_passwd로 accountVO(acc_id) 전달)
	 * 
	 * @param model
	 * @param acc_id
	 * @return
	 */
	@PostMapping(value = "/find_passwd")
	public String findPasswd(Model model, @RequestParam String acc_id) {
		AccountVO accountVO = this.accountProc.readById(acc_id);
		model.addAttribute("accountVO", accountVO);

		return "account/reset_passwd";
	}

	/**
	 * 비밀번호 재설정(비밀번호 찾기) - 아이디 재설정 - resetPasswd()
	 * 
	 * @param json_src
	 * @return
	 */
	@PostMapping(value = "/reset_passwd")
	public ResponseEntity<String> resetPasswd(@RequestBody String json_src) {

		System.out.println("---> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
		JSONObject src = new JSONObject(json_src); // String -> JSON
		String acc_id = (String) src.get("acc_id"); // 값 가져오기
		System.out.println("--> resetPasswd acc_id): " + acc_id);
		String new_passwd = (String) src.get("new_passwd"); // 값 가져오기
		System.out.println("--> resetPasswd new_passwd(plain): " + new_passwd);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("acc_id", acc_id);
		map.put("acc_pw", this.security.aesEncode(new_passwd));
		System.out.println("--> resetPasswd new_passwd(encoded): " + new_passwd);

		int cnt = this.accountProc.resetPasswd(map);
		System.out.println("---> resetPasswd cnt: " + cnt);

		JSONObject response = new JSONObject();
		response.put("cnt", cnt);

		if (cnt == 1) {
			response.put("code", "passwd_reset_success");
		} else {
			response.put("code", "passwd_update_fail");
		}

		return ResponseEntity.ok(response.toString());

	}

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

	/* 마이페이지 */

	/**
	 * 내가 쓴 게시글 - 목록
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/mycontents")
	public String myContents(HttpSession session, Model model) {
		Integer acc_no = (Integer) session.getAttribute("acc_no");
		System.out.println("account/mycontents session ==> acc_no: " + acc_no);

		if (acc_no != null) {
			ArrayList<Share_contentsVO> list = this.accountProc.myContents(acc_no);

			// 각 게시글에 대한 이미지
			Map<Integer, List<Share_imageVO>> contentImages = new HashMap<>();
			for (Share_contentsVO content : list) {
				List<Share_imageVO> image = this.accountProc.contentImages(content.getScon_no());
				contentImages.put(content.getScon_no(), image);
			}

			model.addAttribute("acc_no", acc_no);
			model.addAttribute("list", list);
			model.addAttribute("contentImages", contentImages);
			model.addAttribute("count", list.size());

		} else {
			model.addAttribute("code", "login_need");
		}

		return "account/my_contents";
	}

	/**
	 * 내가 쓴 게시글 - 수정 폼
	 * 
	 * @param session
	 * @param model
	 * @param scon_no
	 * @param cate_no
	 * @return
	 */
	@GetMapping("/updatemc")
	public String updateMyContents(HttpSession session, Model model, int scon_no, int cate_no) {
		Integer acc_no = (Integer) session.getAttribute("acc_no");
		System.out.println("account/update_mycontents session ==> acc_no: " + acc_no);

		if (acc_no != 0) {
			if (acc_no == session.getAttribute("acc_no")) {
				CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
				model.addAttribute("categoryVO", categoryVO);

				Share_contentsVO scontentsVO = this.scontentsProc.read(scon_no);
				model.addAttribute("scontentsVO", scontentsVO);

				ArrayList<Contents_urlVO> url_list = this.scontentsProc.only_url(scon_no);
				for (int i = 0; i < url_list.size(); i++) {
					String url = url_list.get(i).getUrl_link();
					System.out.println("url[i]" + url_list.get(i).getUrl_link());

					if (url.equals("1")) { // 구분자 확인
						url_list.get(i).setUrl_link(" ");
					}

					model.addAttribute("url_list" + i, url_list.get(i).getUrl_link());
				}

				ArrayList<Share_imageVO> share_image = this.scontentsProc.read_image(scon_no);
				for (int i = 1; i < share_image.size(); i++) {
					long size = share_image.get(i).getFile_size();
					String silze_label = Tool.unit(size);
					share_image.get(i).setFlabel(silze_label);
				}

				model.addAttribute("share_imageVO", share_image);
			}

		} else { // acc_no가 session 값과 다름
			model.addAttribute("code", "diff_acc");
		}

		return "account/update_mycontents";
	}

	/**
	 * 내가 쓴 게시글 - 수정 처리
	 * 
	 * @param ra
	 * @param model
	 * @param fnamesMF
	 * @param scontentsVO
	 * @param scon_no
	 * @param cate_no
	 * @param url_link
	 * @return
	 */

	@PostMapping("/updatemc")
	public String updateMyContents(RedirectAttributes ra, Model model, List<MultipartFile> fnamesMF,
			Share_contentsVO scontentsVO, int scon_no, int cate_no, String url_link) {

		int count = fnamesMF.size();
		System.out.println("/updatemc fnameMF.size(): " + count);
		if (count > 0) { // 새로운 파일이 선택됨
			// 기존 이미지 삭제
			ArrayList<Share_imageVO> image_list_old = this.scontentsProc.read_image(scon_no);
			String upDir = Contents.getUploadDir(); // 파일을 업로드할 폴더

			for (Share_imageVO image : image_list_old) {
				System.out.println("---> image_list_old size : " + image_list_old.size());

				String file1saved = image.getFile_upload_name();
				String thumb = image.getFile_thumb_name();

				// 파일 삭제
				Tool.deleteFile(upDir, file1saved);
				Tool.deleteFile(upDir, thumb);

				// 데이터베이스에서 삭제
				this.scontentsProc.delete_image(image.getScon_no());
			}

			// 새 이미지 추가
			Share_imageVO share_imageVO = new Share_imageVO();
			long file_size = 0;
			String file_origin_name = "";
			String file_upload_name = "";
			String file_thumb_name = "";

			for (MultipartFile multipartFile : fnamesMF) {
				file_size = multipartFile.getSize();
				if (file_size > 0) {
					file_origin_name = multipartFile.getOriginalFilename();
					file_upload_name = Upload.saveFileSpring(multipartFile, upDir);

					if (Tool.isImage(file_origin_name)) {
						file_thumb_name = Tool.preview(upDir, file_upload_name, 800, 450);
					}

					share_imageVO.setScon_no(scon_no);
					share_imageVO.setFile_origin_name(file_origin_name);
					share_imageVO.setFile_thumb_name(file_thumb_name);
					share_imageVO.setFile_upload_name(file_upload_name);
					share_imageVO.setFile_size((int) file_size);

					int image_cnt = this.scontentsProc.attach_create(share_imageVO);
					System.out.println("---> attach_create image_cnt: " + image_cnt);
				}

			}

		}

		// 게시글 수
		this.scontentsProc.update_text(scontentsVO);

		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Contents_urlVO> arr = this.scontentsProc.url_read(scon_no);
		String[] list = url_link.split(",");

		System.out.println("---> url_link 사이즈(개수): " + list.length);

		for (int i = 0; i < list.length; i++) { // list가 5개 다 있을 경우 하나를 삭제했을 때
			if (list[i].equals("")) {
				list[i] = "1";
			}
			map.put("url_link", list[i].trim());
			map.put("scon_no", scon_no);
			map.put("url_no", arr.get(i).getUrl_no());
			this.scontentsProc.update_url(map);
		}

		if (list.length != arr.size()) { // list.length와 arr.size 가 다를 경우
			for (int i = arr.size() - 1; i >= list.length; i--) { // url_link가 가 없는 경우
				map.put("url_link", "1");
				map.put("scon_no", scon_no);
				map.put("url_no", arr.get(i).getUrl_no());
				this.scontentsProc.update_url(map);
				System.out.println("1로 변환");

			}
		}
		ra.addAttribute("cate_no", cate_no);

		return "redirect:/scontents/list_by_search";
	}

}
