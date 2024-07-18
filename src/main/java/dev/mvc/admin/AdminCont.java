package dev.mvc.admin;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dev.mvc.account.AccountProc;
import dev.mvc.account.AccountProcInter;
import dev.mvc.account.AccountVO;
import dev.mvc.account.PicUpload;
import dev.mvc.category.CategoryProcInter;
import dev.mvc.category.CategoryVO;
import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;
import dev.mvc.tool.MailTool;
import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/admin")
@Controller
public class AdminCont {
	@Autowired
	@Qualifier("dev.mvc.admin.AdminProc")
	private AdminProcInter adminProc;

	@Autowired
	@Qualifier("dev.mvc.category.CategoryProc")
	private CategoryProcInter categoryProc;
	
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;

	@Autowired
	Security security;

	public AdminCont() {
		System.out.println("-> AdminCont created.");
	}

	/**
	 * 메인 화면
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "") // http://localhost:9093/admin
	public String admin_main(Model model) {

		return "index_admin";

	}
//	@GetMapping(value = "") // http://localhost:9093/admin
//	public String admin_main(HttpSession session, Model model) {
//
//		Integer adm_no = (Integer) session.getAttribute("adm_no");
//		System.out.println("admin_main session ==> adm_no: " + adm_no);
//
//		if (adm_no == null) {
//			return "admin/login";
//		} else {
//			return "admin/index";
//		}
//		
//	}

	/**
	 * 아이디 중복 확인
	 * 
	 * @param adm_id
	 * @return
	 */
	@GetMapping(value = "/checkID") // http://localhost:9093/admin/checkID?adm_id=admin2
	@ResponseBody
	public String checkID(String adm_id) {
		System.out.println("---> checkID adm_id: " + adm_id);

		int cnt = this.adminProc.checkID(adm_id);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		return obj.toString();
	}

	/**
	 * 이름 중복 확인
	 * 
	 * @param adm_name
	 * @return
	 */
	@GetMapping(value = "/checkName") // http://localhost:9093/admin/checkName?adm_name=admin2
	@ResponseBody
	public String checkName(String adm_name) {
		System.out.println("---> checkName adm_name: " + adm_name);

		int cnt = this.adminProc.checkName(adm_name);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		System.out.println("---> checkName cnt: " + cnt);

		return obj.toString();
	}

	/**
	 * 이메일 중복 확인
	 * 
	 * @param adm_email
	 * @return
	 */
	@GetMapping(value = "/checkEmail") // http://localhost:9093/admin/checkEmail?adm_email=ua@desk.tour
	@ResponseBody
	public String checkEmail(String adm_email) {
		System.out.println("---> checkEmail adm_email: " + adm_email);

		int cnt = this.adminProc.checkEmail(adm_email);

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
		System.out.println("---> AdminCont RECEIVER : " + receiver);

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
	public Map<String, Object> verifyCode(@RequestParam String code, @RequestParam String adm_id) {
		Map<String, Object> response = new HashMap<>();

		if (verify_code != null && verify_code.equals(code)) {
			response.put("status", "success");
			response.put("acc_id", adm_id);
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
	 * @param adminVO
	 * @return
	 */
	@GetMapping(value = "/create") // http://localhost:9093/admin/create
	public String create(Model model, AdminVO adminVO) {
		List<CategoryVO> cate_list = this.categoryProc.cate_list_all();
		for (int i = 0; i < cate_list.size(); i++) {
			System.out.println("category: " + cate_list.get(i).getCate_name());
		}
		model.addAttribute("cate_list", cate_list);

		// String[] cateList = this.adminProc.cateList().split(",");
		// List<String> cate_names = Arrays.asList(cateList);
		// System.out.println("--> cate_names: " + cate_names);
		// model.addAttribute("cate_names", cate_names);

		return "admin/create";
	}

	/**
	 * 회원 가입 처리
	 * 
	 * @param adminVO
	 * @return
	 */
	@PostMapping(value = "/create")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> create_proc(AdminVO adminVO) {
		Map<String, Object> response = new HashMap<>();

		int checkID_cnt = this.adminProc.checkID(adminVO.getAdm_id());
		int checkName_cnt = this.adminProc.checkName(adminVO.getAdm_name());
		int checkEmail_cnt = this.adminProc.checkEmail(adminVO.getAdm_email());

		if ((checkID_cnt == 0) && (checkName_cnt == 0)) {
			int cnt = this.adminProc.create(adminVO);

			if (cnt != 0) { // 회원 가입 성공
				response.put("code", "create_success");
//				response.put("acc_id", accountVO.getAcc_id());
//				response.put("acc_name", accountVO.getAcc_name());

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

		} else if (checkEmail_cnt != 0) {
			response.put("code", "duplicate_email");
			response.put("cnt", 0);
		}

		return ResponseEntity.ok(response);
	}

	/**
	 * 회원 가입 메시지
	 * 
	 * @param model
	 * @param adminVO
	 * @param code
	 * @param cnt
	 * @return
	 */
	@GetMapping(value = "/msg")
	public String msg(Model model, AdminVO adminVO,
					@RequestParam(value = "code") String code,
					@RequestParam(value = "cnt") int cnt) {
		System.out.println("--> cnt: " + cnt);

		model.addAttribute("code", code);
		model.addAttribute("cnt", cnt);

		return "admin/msg";
	}
	
	/**
	 * 로그인 폼 (쿠키 기반)
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/login")
	public String login_form(Model model, HttpServletRequest request, AdminLogVO adminLogVO, String url)  {

		/* Cookie */
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;

		String ck_adm_id = ""; // adm_id 저장
		String ck_id_save = ""; // adm_id 저장 여부 확인

		if (cookie != null) { // 쿠키 존재
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("--> cookies[" + i + "]" + cookies[i]);
				cookie = cookies[i]; // 쿠키 객체 추출

				if (cookie.getName().equals("ck_adm_id")) {
					ck_adm_id = cookie.getValue(); // id
				} else if (cookie.getName().equals("ck_id_save")) {
					ck_id_save = cookie.getValue(); // Y, N
				}
			}
		}

		model.addAttribute("ck_id", ck_adm_id);
		model.addAttribute("ck_id_save", ck_id_save);
		model.addAttribute("url", url);
		
		return "admin/login";
	}
	
	
	/**
	 * 로그인 처리 (쿠키 기반)
	 * 
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/login")
	public String login_proc(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model,
			AdminLogVO adminLogVO, String url, String adm_id, String adm_pw,
			@RequestParam(value = "id_save", defaultValue = "") String id_save) {
		session.removeAttribute("acc_no");
		
		String ip = request.getRemoteAddr(); // IP
		System.out.println("---> 접속한 IP: " + ip);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("adm_id", adm_id);
		map.put("adm_pw", this.security.aesEncode(adm_pw));

		int cnt = this.adminProc.login(map);
		System.out.println("---> admin login_proc cnt: " + cnt);

		model.addAttribute("cnt", cnt);

		if (id_save.equals("Y")) { // Checkbox 체크, acc_id 저장
			Cookie ck_adm_id = new Cookie("adm_id", adm_id);
			ck_adm_id.setPath("/"); // root 폴더에 쿠키를 기록 -> 모든 경로에서 쿠키 접근 가능
			ck_adm_id.setMaxAge(60 * 60 * 24 * 30); // 30 days, 초단위
			response.addCookie(ck_adm_id); // acc_id 저장
		} else { // N, Checkbox 해제, acc_id 미저장
			Cookie ck_adm_id = new Cookie("ck_id", "");
			ck_adm_id.setPath("/");
			ck_adm_id.setMaxAge(0);
			response.addCookie(ck_adm_id);
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
			AdminVO adminVO = this.adminProc.readById(adm_id);
			session.setAttribute("adm_no", adminVO.getAdm_no());
			// int adm_no = (int)session.getAttribute("adm_no"); // Session에서 가져오기

			session.setAttribute("adm_id", adminVO.getAdm_id());
			session.setAttribute("adm_name", adminVO.getAdm_name());
			session.setAttribute("adm_cate", adminVO.getCate_no());

			if (adminVO.getAdm_grade() >= 1 && adminVO.getAdm_grade() <= 10) {
				session.setAttribute("adm_grade", "admin"); // 관리자(활동 가능 상태)
			} else if (adminVO.getAdm_grade() >= 30 && adminVO.getAdm_grade() <= 39) {
				session.setAttribute("adm_grade", "suspended"); // 정지 상태
			} else if (adminVO.getAdm_grade() == 99) {
				session.setAttribute("adm_grade", "withdrawn"); // 탈퇴 상태
			} else {
				session.setAttribute("adm_grade", "member");
			}

			/* 관리자 로그 저장 */
			adminLogVO.setAdm_no(adminVO.getAdm_no());
			adminLogVO.setAdm_log_ip(ip);
			int log_cnt = this.adminProc.recordLog(adminLogVO);
			System.out.println("---> admin Record_log_cnt: " + log_cnt);

			/* Cookie - adm_id 관련 쿠키 저장 */
			Cookie ck_adm_id = new Cookie("adm_id", adm_id);
			ck_adm_id.setPath("/"); // root 폴더에 쿠키를 기록 -> 모든 경로에서 쿠키 접근 가능
			ck_adm_id.setMaxAge(60 * 60 * 24 * 30); // 30 days, 초단위
			response.addCookie(ck_adm_id); // acc_id 저장

			return "redirect:/admin";
		} else {
			model.addAttribute("code", "login_fail");
			model.addAttribute("cnt", 0);
			return "admin/msg";
		}

	}
	
//	/**
//	 * 관리자 목록
//	 * 
//	 * @param session
//	 * @param model
//	 * @return
//	 */
//	@GetMapping(value = "/list")
//	public String list(HttpSession session, Model model) {
//		ArrayList<AdminVO> list = this.adminProc.list();
//		model.addAttribute("list", list);
//
//		return "admin/list";
//	}
	
	/**
	 * 관리자 목록 (검색)
	 * 
	 * 관리자 검색 1) 담당 2) 관리자 아이디 3) 관리자 이름 4) 가입일
	 * 
	 * @param session
	 * @param model
	 * @param selected_cate
	 * @param word_id
	 * @param word_name
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	@GetMapping(value = "/list")
	public String list(HttpSession session, Model model,
			@RequestParam(name = "selected_cate", required = false) Integer selected_cate,
			@RequestParam(name = "word_id", defaultValue = "") String word_id,
			@RequestParam(name = "word_name", defaultValue = "") String word_name,
			@RequestParam(name = "start_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
			@RequestParam(name = "end_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date) {
		
	    word_id = Tool.checkNull(word_id).trim();
	    word_name = Tool.checkNull(word_name).trim();
	    
	    Map<String, Object> words = new HashMap<>();
	    
	    if (selected_cate != null) {
	        words.put("selected_cate", selected_cate);
	    }
	    if (word_id != null && !word_id.isEmpty()) {
	        words.put("word_id", word_id);
	    }
	    if (word_name != null && !word_name.isEmpty()) {
	        words.put("word_name", word_name);
	    }
	    if (start_date != null && end_date != null) {
	    	words.put("start_date", start_date.toString());
	    	words.put("end_date", end_date.toString());
	    }

	    ArrayList<AdminVO> list = this.adminProc.searchList(words);

	    model.addAttribute("word_id", word_id);
	    model.addAttribute("word_name", word_name);
	    model.addAttribute("selected_cate", selected_cate);
	    model.addAttribute("start_date", start_date);
	    model.addAttribute("end_date", end_date);
	    model.addAttribute("list", list);

		return "admin/list";
	}


	/**
	 * 관리자 로그 목록 (검색 + 페이징)
	 * 
	 * 로그 검색
	 * 1) 관리자 아이디
	 * 2) 아이피
	 * 3) 기간
	 * 
	 * @param session
	 * @param model
	 * @param word_id
	 * @param word_ip
	 * @param start_date
	 * @param end_date
	 * @param now_page
	 * @return
	 */
	@GetMapping(value = "/log_list")
	public String pagingList(HttpSession session, Model model,
			@RequestParam(name = "word_id", defaultValue = "") String word_id,
			@RequestParam(name = "word_ip", defaultValue = "") String word_ip,
	        @RequestParam(name = "start_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
	        @RequestParam(name = "end_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page) {

	    word_id = Tool.checkNull(word_id).trim();
	    word_ip = Tool.checkNull(word_ip).trim();

	    HashMap<String, Object> map = new HashMap<>();
		if (!word_id.isEmpty()) {
			map.put("word_id", word_id);
		}
		if (!word_ip.isEmpty()) {
			map.put("word_ip", word_ip);
		}
	    if (start_date != null && end_date != null) {
	    	map.put("start_date", start_date.toString());
	    	map.put("end_date", end_date.toString());
	    }
	    map.put("now_page", now_page);

		ArrayList<Map<String, Object>> logs = this.adminProc.pagingList(map);
		model.addAttribute("logs", logs);
		
	    model.addAttribute("word_id", word_id);
	    model.addAttribute("word_ip", word_ip);
	    model.addAttribute("start_date", start_date);
	    model.addAttribute("end_date", end_date);
	    
	    int search_count = this.adminProc.searchCount(map);
	    
	    String start_date_str = (start_date != null) ? start_date.toString() : "";
	    String end_date_str = (end_date != null) ? end_date.toString() : "";
	    
	    String paging = this.adminProc.pagingBox(now_page, word_id, word_ip, start_date_str, end_date_str,
	            "/admin/log_list", search_count, AdminProc.RECORD_PER_PAGE, AdminProc.PAGE_PER_BLOCK);

		model.addAttribute("paging", paging);
		model.addAttribute("now_page", now_page);
		model.addAttribute("search_count", search_count);

		// 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
		int no = search_count - ((now_page - 1) * AccountProc.RECORD_PER_PAGE);
		model.addAttribute("no", no);

		return "admin/log_list";
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
		
		System.out.println("---> 관리자 로그아웃");
		return "redirect:/admin";
	}
	
//	/**
//	 * 회원 목록
//	 * 
//	 * @param model
//	 * @return
//	 */
//	@GetMapping(value = "/acc_list")
//	public String acc_list(HttpSession session, Model model) {
//		ArrayList<AccountVO> list = this.adminProc.accList();
//		model.addAttribute("list", list);
//
//		return "admin/acc_list";
//	}
	
	/**
	 * 회원 목록 (검색)
	 * 
	 * 회원 검색 1) 회원 등급(상태) 2) 회원 아이디 3) 회원 이름 4) 가입일
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/acc_list")
	public String acc_list(HttpSession session, Model model,
			@RequestParam(name = "selected_grade", required = false) String selected_grade,
			@RequestParam(name = "word_id", defaultValue = "") String word_id,
			@RequestParam(name = "word_name", defaultValue = "") String word_name,
			@RequestParam(name = "start_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
			@RequestParam(name = "end_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date) {
		
	    word_id = Tool.checkNull(word_id).trim();
	    word_name = Tool.checkNull(word_name).trim();
	    
	    Map<String, Object> words = new HashMap<>();

	    if (selected_grade != null && !selected_grade.isEmpty()) {
	        int grade = 0;
	        switch (selected_grade) {
	            case "member":
	                grade = 15;
	                break;
	            case "dormant":
	                grade = 25;
	                break;
	            case "pause":
	                grade = 35;
	                break;
	            case "withdrawn":
	                grade = 99;
	                break;
	        }
	        words.put("selected_grade", grade);
	    }
	    if (word_id != null && !word_id.isEmpty()) {
	        words.put("word_id", word_id);
	    }
	    if (word_name != null && !word_name.isEmpty()) {
	        words.put("word_name", word_name);
	    }
	    if (start_date != null && end_date != null) {
	    	words.put("start_date", start_date.toString());
	    	words.put("end_date", end_date.toString());
	    }

	    ArrayList<AccountVO> list = this.adminProc.accSearchList(words);

	    model.addAttribute("word_id", word_id);
	    model.addAttribute("word_name", word_name);
	    model.addAttribute("selected_grade", selected_grade);
	    model.addAttribute("start_date", start_date);
	    model.addAttribute("end_date", end_date);
	    model.addAttribute("list", list);

		return "admin/acc_list";
	}
	
	/**
	 * 회원 정보 조회
	 * 
	 * @param model
	 * @param acc_no
	 * @return
	 */
	@GetMapping(value = "/acc_read")
	public String acc_read(HttpSession session, Model model, int acc_no) {

		AccountVO accountVO = this.adminProc.accRead(acc_no);

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

		return "admin/acc_read";
	}
	
	/**
	 * 회원 정보 수정
	 * 
	 * @param model
	 * @param accountVO
	 * @param recommendVO
	 * @param selected_hashtags
	 * @return
	 */
	@PostMapping(value = "/acc_update")
	public String acc_update(Model model, AccountVO accountVO, RecommendVO recommendVO,
			@RequestParam(value = "selected_hashtags", required = false) List<Integer> selected_hashtags) {
		
		System.out.println("====>" + accountVO.getAcc_grade());

		int checkName_cnt = this.accountProc.checkName(accountVO.getAcc_name());
		if (checkName_cnt <= 1) {
			int cnt = this.adminProc.accUpdate(accountVO);
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
				model.addAttribute("cnt", 2);
			} else {
				model.addAttribute("code", "update_fail");
				model.addAttribute("cnt", cnt);
			}
		} else {
			model.addAttribute("code", "duplicate_name");
			model.addAttribute("cnt", 0);
		}
		

		return "admin/msg";
	}

	/**
	 * 프로필 사진 업데이트
	 * 
	 * @param model
	 * @param accountVO
	 * @param acc_no
	 * @return
	 */
	@PostMapping(value = "/acc_delete_pic")
	public String acc_delete_pic(Model model, int acc_no) {
		System.out.println("---> RequestParam acc_no: " + acc_no);
		
		AccountVO accountVO = this.adminProc.accRead(acc_no);

		int cnt = this.adminProc.accDeletePic(acc_no);
		
		if(cnt != 0) {
			model.addAttribute("accountVO", accountVO);
			model.addAttribute("code", "file_delete_success");
			model.addAttribute("cnt", 2);
		} else {
			model.addAttribute("code", "file_delete_fail");
			model.addAttribute("cnt", 2);
		}

		return "/admin/msg";
	}
	

	/**
	 * 회원 로그 목록 (검색 + 페이징)
	 * 
	 * 로그 검색 1) 회원 아이디 2) 아이피 3) 기간
	 * 
	 * @param session
	 * @param model
	 * @param word_id
	 * @param word_ip
	 * @param start_date
	 * @param end_date
	 * @param now_page
	 * @return
	 */
	@GetMapping(value = "/acc_log_list")
	public String accPagingList(HttpSession session, Model model,
			@RequestParam(name = "word_id", defaultValue = "") String word_id,
			@RequestParam(name = "word_ip", defaultValue = "") String word_ip,
			@RequestParam(name = "start_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
			@RequestParam(name = "end_date", defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page) {

		word_id = Tool.checkNull(word_id).trim();
		word_ip = Tool.checkNull(word_ip).trim();

		HashMap<String, Object> map = new HashMap<>();
		if (!word_id.isEmpty()) {
			map.put("word_id", word_id);
		}
		if (!word_ip.isEmpty()) {
			map.put("word_ip", word_ip);
		}
		if (start_date != null && end_date != null) {
			map.put("start_date", start_date.toString());
			map.put("end_date", end_date.toString());
		}
		map.put("now_page", now_page);

		ArrayList<Map<String, Object>> logs = this.adminProc.accPagingList(map);
		model.addAttribute("logs", logs);
		// list를 logs로 바꿈 (html에 참고)

		model.addAttribute("word_id", word_id);
		model.addAttribute("word_ip", word_ip);
		model.addAttribute("start_date", start_date);
		model.addAttribute("end_date", end_date);

		int search_count = this.adminProc.accSearchCount(map);

		String start_date_str = (start_date != null) ? start_date.toString() : "";
		String end_date_str = (end_date != null) ? end_date.toString() : "";

		String paging = this.adminProc.pagingBox(now_page, word_id, word_ip, start_date_str, end_date_str,
				"/admin/acc_log_list", search_count, AccountProc.RECORD_PER_PAGE, AccountProc.PAGE_PER_BLOCK);

		model.addAttribute("paging", paging);
		model.addAttribute("now_page", now_page);
		model.addAttribute("search_count", search_count);

		// 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
		int no = search_count - ((now_page - 1) * AccountProc.RECORD_PER_PAGE);
		model.addAttribute("no", no);

		return "admin/acc_log_list";
	}
	
	/**
	 * 회원 정보 삭제 페이지
	 * 
	 * @param model
	 * @param acc_no
	 * @return
	 */
	@GetMapping(value = "/acc_delete")
	public String accDelete(Model model, int acc_no) {
		AccountVO accountVO = this.accountProc.read(acc_no);
		model.addAttribute("accountVO", accountVO);

		return "admin/acc_delete";
	}

	/**
	 * 회원 정보 삭제 처리
	 * 
	 * @param model
	 * @param acc_no
	 * @return
	 */
	@PostMapping(value = "/acc_delete")
	public String delete_proc(Model model, Integer acc_no) {
		int cnt = this.adminProc.accDelete(acc_no);

		if (cnt == 1) { // 삭제 성공
			return "redirect:/admin/acc_list";
		} else {
			model.addAttribute("code", "delete_fail");
			return "admin/msg";
		}
	}


}
