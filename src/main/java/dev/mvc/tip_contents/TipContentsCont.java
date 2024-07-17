package dev.mvc.tip_contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.account.AccountProcInter;
import dev.mvc.admin.AdminProcInter;
import dev.mvc.category.CategoryProcInter;
import dev.mvc.category.CategoryVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/tcontents")
@Controller
public class TipContentsCont {

	@Autowired
	@Qualifier("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;

	@Autowired
	@Qualifier("dev.mvc.admin.AdminProc")
	private AdminProcInter adminProc;

	@Autowired
	@Qualifier("dev.mvc.category.CategoryProc")
	private CategoryProcInter categoryProc;

	@Autowired
	@Qualifier("dev.mvc.tip_contents.TipContentsProc")
	private TipContentsProcInter tcontentsProc;

	public TipContentsCont() {
		System.out.println("-> TipContentsCont created.");
	}

	/**
	 * 글 등록 폼
	 * 
	 * @param session
	 * @param model
	 * @param tcontentsVO
	 * @param cate_no
	 * @return
	 */
	@GetMapping(value = "/create") //////// ******* 수정해야함 isAdminMembeR!! account 테이블의 컨텐츠 관리자가 관리하는 걸로 변경
	public String create(HttpSession session, Model model, TipContentsVO tcontentsVO) {

		int cate_no = 3;
		if (this.accountProc.isMemberAdmin(session)) {
			model.addAttribute("adm_no", session.getAttribute("adm_no"));

			CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
			model.addAttribute("categoryVO", categoryVO);
			model.addAttribute("tcontentsVO", tcontentsVO);

			return "tcontents/tcon_create";
		} else {
			return "admin/msg"; // code: permission_denied
		}

//		int cate_no = 3;
//		System.out.println("\n현재 관리자 세션: " + session.getAttribute("adm_no") + session.getAttribute("adm_cate"));
//		
//		if (this.adminProc.isAdmin(session) && (Integer)session.getAttribute("adm_cate") == 3) {
//			model.addAttribute("adm_no", session.getAttribute("adm_no"));
//
//			CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
//			model.addAttribute("categoryVO", categoryVO);
//			model.addAttribute("tcontentsVO", tcontentsVO);
//
//			return "tcontents/create";
//		} else {
//			return "admin/msg"; // code: permission_denied
//		}

	}

	@PostMapping(value = "/create")
	public String create(HttpServletRequest request, HttpSession session, RedirectAttributes ra, Model model,
			List<MultipartFile> img_mf, TipContentsVO tcontentsVO) {

		String upDir = TipContents.getUploadDir();
		StringBuilder tcon_img_sb = new StringBuilder();
		StringBuilder tcon_saved_img_sb = new StringBuilder();
		StringBuilder tcon_thumb_img_sb = new StringBuilder();
		long total_img_size = 0;

		for (MultipartFile mf : img_mf) {
			if (mf != null && !mf.isEmpty()) {
				String img = mf.getOriginalFilename();
				long img_size = mf.getSize();

				if (img_size > 0 && Tool.isImage(img)) {
					String saved_img = Upload.saveFileSpring(mf, upDir);
					String thumb_img = Tool.preview(upDir, saved_img, 200, 150);

					if (tcon_img_sb.length() > 0)
						tcon_img_sb.append(";");
					if (tcon_saved_img_sb.length() > 0)
						tcon_saved_img_sb.append(";");
					if (tcon_thumb_img_sb.length() > 0)
						tcon_thumb_img_sb.append(";");

					tcon_img_sb.append(img);
					tcon_saved_img_sb.append(saved_img);
					tcon_thumb_img_sb.append(thumb_img);

					total_img_size += img_size;
				}
			}
		}

		tcontentsVO.setTcon_img(tcon_img_sb.toString());
		tcontentsVO.setTcon_saved_img(tcon_saved_img_sb.toString());
		tcontentsVO.setTcon_thumb_img(tcon_thumb_img_sb.toString());
		tcontentsVO.setTcon_img_size(total_img_size);

		int acc_no = (int) session.getAttribute("acc_no");
		tcontentsVO.setAcc_no(acc_no);

		// YouTube URL 처리
		String youtube = tcontentsVO.getYoutube();

		if (youtube != null && youtube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
			youtube = Tool.youtubeResize(youtube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
			tcontentsVO.setYoutube(youtube); // 변경된 youtube URL 설정
		}

		tcontentsVO.setTcon_contents(tcontentsVO.getTcon_contents().replace("\r\n", "<br>").replace("\n", "<br>"));

		int cnt = this.tcontentsProc.create(tcontentsVO);

		if (cnt == 1) { // DB 등록 성공
			return "redirect:/tcontents/list";
		} else { // DB 등록 실패
			ra.addFlashAttribute("code", "contents_upload_fail");
			ra.addFlashAttribute("cnt", 0);
			ra.addFlashAttribute("url", "/admin/msg");
			return "redirect:/admin/msg";
		}
	}

	/**
	 * 갤러리형 목록 + 검색
	 * 
	 * @return
	 */
	@GetMapping(value = "/list")
	public String list(HttpSession session, Model model, @RequestParam(name = "word", defaultValue = "") String word) {

		Integer acc_no = (Integer) session.getAttribute("acc_no");

		CategoryVO categoryVO = this.categoryProc.cate_read(3);
		model.addAttribute("categoryVO", categoryVO);

		word = Tool.checkNull(word).trim();

		HashMap<String, Object> map = new HashMap<>();
		map.put("word", word);

		ArrayList<TipContentsVO> list = this.tcontentsProc.list(map);

		if (acc_no != null) {
			for (TipContentsVO content : list) {
				Map<String, Object> likeMap = new HashMap<>();
				likeMap.put("acc_no", acc_no);
				likeMap.put("tcon_no", content.getTcon_no());

				boolean isLiked = this.tcontentsProc.isLiked(likeMap);
				content.setLiked(isLiked);
			}
		}

		for (TipContentsVO content : list) {
			int like_cnt = this.tcontentsProc.like_count(content.getTcon_no());
			content.setLikeCnt(like_cnt);
		}

		model.addAttribute("list", list);
		// model.addAttribute("tconImages",tconImages);

		// System.out.println("-> size: " + list.size());
		model.addAttribute("word", word);

		int list_count = this.tcontentsProc.list_count(map);
		model.addAttribute("list_count", list_count);

		return "tcontents/list";
	}

	/**
	 * 조회
	 * 
	 * @return
	 */
	@GetMapping(value = "/read")
	public String read(HttpSession session, Model model, int tcon_no) {

		Integer acc_no = (Integer) session.getAttribute("acc_no");

		TipContentsVO tcontentsVO = this.tcontentsProc.read(tcon_no);

//	    String title = contentsVO.getTitle();
//	    String content = contentsVO.getContent();
		//
//	    title = Tool.convertChar(title);  // 특수 문자 처리
//	    content = Tool.convertChar(content); 
		//
//	    contentsVO.setTitle(title);
//	    contentsVO.setContent(content);
		
		// String img_size_label = Tool.unit(img_size); // 필요에 따라 크기 단위를 변환하여 사용
		// tcontentsVO.setImg_size_label(img_size_label);

		tcontentsVO.setTcon_date(tcontentsVO.getTcon_date().toString());

		CategoryVO categoryVO = this.categoryProc.cate_read(tcontentsVO.getCate_no());
		model.addAttribute("categoryVO", categoryVO);

		if (acc_no != null) {
			Map<String, Object> likeMap = new HashMap<>();
			likeMap.put("acc_no", acc_no);
			likeMap.put("tcon_no", tcon_no);
			boolean isLiked = this.tcontentsProc.isLiked(likeMap);
			tcontentsVO.setLiked(isLiked);

			int like_cnt = this.tcontentsProc.like_count(tcontentsVO.getTcon_no());
			tcontentsVO.setLikeCnt(like_cnt);
		}

		model.addAttribute("tcontentsVO", tcontentsVO);
		this.tcontentsProc.updateViews(tcon_no); // 조회수 증가

		return "/tcontents/read";
	}

	/**
	 * 글 수정 폼
	 * 
	 * @param session
	 * @param model
	 * @param tcon_no
	 * @return
	 */
	@GetMapping(value = "/update")
	public String update(HttpSession session, Model model, int tcon_no) {
		Integer acc_no = (Integer) session.getAttribute("acc_no");

		if ((acc_no != 0) && acc_no == session.getAttribute("acc_no")) {
			TipContentsVO tcontentsVO = this.tcontentsProc.read(tcon_no);
			tcontentsVO.setTcon_contents(tcontentsVO.getTcon_contents().replace("<br>", "\n"));
			model.addAttribute("tcontentsVO", tcontentsVO);
		} else { // acc_no가 session 값과 다름
			model.addAttribute("code", "diff_acc");
		}

		return "tcontents/update"; // /templates/account/create.html
	}

	/**
	 * 글 수정 처리 
	 * 
	 * @param session
	 * @param model
	 * @param tcon_no
	 * @param tcon_title
	 * @param tcon_contents
	 * @param youtube
	 * @param img_mf
	 * @return
	 */
	@PostMapping("/update")
	public String update(HttpSession session, Model model, int tcon_no, String tcon_title, String tcon_contents,
			String youtube, List<MultipartFile> img_mf) {

		TipContentsVO tcontentsVO = this.tcontentsProc.read(tcon_no);
		model.addAttribute("tcontentsVO", tcontentsVO);

		tcontentsVO.setTcon_title(tcon_title);
		tcontentsVO.setTcon_contents(tcon_contents.replace("\r\n", "<br>").replace("\n", "<br>"));

	    if (!youtube.equals(tcontentsVO.getYoutube())) {
	        youtube = Tool.youtubeResize(youtube, 640);
	        tcontentsVO.setYoutube(youtube);
	    }

		if (img_mf != null && !img_mf.isEmpty() && img_mf.get(0).getSize() > 0) {
			String upDir = TipContents.getUploadDir();
			StringBuilder tcon_img_sb = new StringBuilder();
			StringBuilder tcon_saved_img_sb = new StringBuilder();
			StringBuilder tcon_thumb_img_sb = new StringBuilder();
			long total_img_size = 0;

			for (MultipartFile mf : img_mf) {
				if (mf != null && !mf.isEmpty()) {
					String img = mf.getOriginalFilename();
					long img_size = mf.getSize();

					if (img_size > 0 && Tool.isImage(img)) {
						String saved_img = Upload.saveFileSpring(mf, upDir);
						String thumb_img = Tool.preview(upDir, saved_img, 200, 150);

						if (tcon_img_sb.length() > 0)
							tcon_img_sb.append(";");
						if (tcon_saved_img_sb.length() > 0)
							tcon_saved_img_sb.append(";");
						if (tcon_thumb_img_sb.length() > 0)
							tcon_thumb_img_sb.append(";");

						tcon_img_sb.append(img);
						tcon_saved_img_sb.append(saved_img);
						tcon_thumb_img_sb.append(thumb_img);

						total_img_size += img_size;
					}
				}
			}

			tcontentsVO.setTcon_img(tcon_img_sb.toString());
			tcontentsVO.setTcon_saved_img(tcon_saved_img_sb.toString());
			tcontentsVO.setTcon_thumb_img(tcon_thumb_img_sb.toString());
			tcontentsVO.setTcon_img_size(total_img_size);
		}

		int cnt = this.tcontentsProc.update(tcontentsVO);
		
		if (cnt > 0) {
			return "redirect:/tcontents/read?tcon_no=" + tcon_no;
		} else {
			model.addAttribute("code", "update_fail");
			return "tcontents/msg";
		}
	}

	/**
	 * 글 삭제 폼
	 * 
	 * @param session
	 * @param model
	 * @param tcon_no
	 * @return
	 */
	@GetMapping(value = "/delete")
	public String delete(HttpSession session, Model model, int tcon_no) {

		if (this.accountProc.isMemberAdmin(session)) {
			TipContentsVO tcontentsVO = this.tcontentsProc.read(tcon_no);
			model.addAttribute("tcontentsVO", tcontentsVO);
			return "tcontents/delete";
		} else {
			model.addAttribute("code", "permission_denied");
			return "tcontents/msg";
		}
	}

	/**
	 * 글 삭제 처리
	 * 
	 * @param tcon_no
	 * @return
	 */
	@PostMapping(value = "/delete")
	public String delete(int tcon_no) {
		TipContentsVO tcontentsVO = this.tcontentsProc.read(tcon_no);

		String tcon_saved_img = tcontentsVO.getTcon_saved_img();
		String tcon_thumb_img = tcontentsVO.getTcon_thumb_img();

		String upDir = TipContents.getUploadDir();
		Tool.deleteFile(upDir, tcon_saved_img);
		Tool.deleteFile(upDir, tcon_thumb_img);

		this.tcontentsProc.delete(tcon_no);

		return "redirect:/tcontents/list";
	}

	/**
	 * 좋아요 저장
	 * 
	 * @param session
	 * @param tcon_no
	 * @return
	 */
	@GetMapping("/insertlike")
	@ResponseBody
	public Map<String, Object> insertLike(HttpSession session, @RequestParam("tcon_no") int tcon_no) {

		Integer acc_no = (Integer) session.getAttribute("acc_no");

		Map<String, Object> response = new HashMap<>();
		response.put("cnt", 0);

		if (acc_no != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("acc_no", acc_no);
			map.put("tcon_no", tcon_no);

			this.tcontentsProc.insertLike(map);

			response.put("cnt", 1);
		}

		return response;
	}

	/**
	 * 좋아요 삭제
	 * 
	 * @param session
	 * @param tcon_no
	 * @return
	 */
	@GetMapping("/deletelike")
	@ResponseBody
	public Map<String, Object> deleteLike(HttpSession session, @RequestParam("tcon_no") int tcon_no) {

		Integer acc_no = (Integer) session.getAttribute("acc_no");

		Map<String, Object> response = new HashMap<>();
		response.put("cnt", 0);

		if (acc_no != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("acc_no", acc_no);
			map.put("tcon_no", tcon_no);

			this.tcontentsProc.deleteLike(map);

			response.put("cnt", 1);
		}

		return response;
	}

}
