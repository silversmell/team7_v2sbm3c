package dev.mvc.share_contents;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import dev.mvc.account.AccountProcInter;
import dev.mvc.account.AccountVO;
import dev.mvc.category.CategoryProcInter;
import dev.mvc.category.CategoryVO;
import dev.mvc.re_comment.Re_commentProcInter;
import dev.mvc.re_comment.Share_recommentVO;
import dev.mvc.recommend.HashtagVO;
import dev.mvc.share_contents.Contents;
import dev.mvc.share_contentsdto.Contents_tagVO;
import dev.mvc.share_contentsdto.Contents_urlVO;
import dev.mvc.share_contentsdto.NotificationVO;
import dev.mvc.reply.ReplyProcInter;
import dev.mvc.reply.Share_commentVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/scontents") // http://localhost:9093/scontents/list_by_search?cate_no=1
@Controller
public class Share_contentsCont {
	
	  @Autowired
	  @Qualifier("dev.mvc.re_comment.Re_commentProc")
	  private Re_commentProcInter re_commentProc;
	
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;
	
	@Autowired
	@Qualifier("dev.mvc.reply.ReplyProc")
	private ReplyProcInter replyProc;
	
	@Autowired
	@Qualifier("dev.mvc.category.CategoryProc")
	private CategoryProcInter categoryProc;
	

	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private Share_contentsProc sconProc;

	public Share_contentsCont() {
		System.out.println(" ->Share_contentsCont created");
	}


	
	@GetMapping("/read") //글 조회
	public String read(Model model, int scon_no, int cate_no, HttpSession session,@RequestParam(name = "word", defaultValue = "") String word,RedirectAttributes ra,
			 @RequestParam(name = "now_page", defaultValue = "1") int now_page) { // acc_no 필요(session)
		
		if(this.accountProc.isMember(session)) {
		
		model.addAttribute("word",word);
		model.addAttribute("now_page",now_page);
		model.addAttribute("acc_id",session.getAttribute("acc_id"));
		model.addAttribute("acc_no",session.getAttribute("acc_no"));
		// 카테고리 가져오기
		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no); // 카테고리 읽어옴
		model.addAttribute("categoryVO", categoryVO);
		int count1 = this.sconProc.bookmark_count(scon_no);
		model.addAttribute("count1",count1);
		int cnt = this.sconProc.update_view(scon_no); // 조회수 업데이트

		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		HashMap<String,Object>map = new HashMap<>();
		map.put("acc_no", (int)session.getAttribute("acc_no"));
		map.put("scon_no", scon_no);
		if(this.sconProc.mark_check(map).size()>0) { //회원에 따라 마크 다르게 보이게 하기
			scontentsVO.setscon_bookmark("Y");
		}else {
			scontentsVO.setscon_bookmark("N");
		}
		
		String writer = this.sconProc.writer(map);
		model.addAttribute("writer",writer);
		model.addAttribute("scontentsVO", scontentsVO);
		//System.out.println("작성날짜 : "+scontentsVO.getScon_date());
		model.addAttribute("member_no",scontentsVO.getacc_no());
		int count = this.replyProc.comment_search(scon_no);
		model.addAttribute("count",count); //댓글수
		ArrayList<HashtagVO> list1 = this.sconProc.sconno_hashtag(scon_no);
		
		model.addAttribute("list2", list1);

		int cnt1 = this.replyProc.comment_search(scon_no);
		model.addAttribute("cnt", cnt1);
		
		ArrayList<Share_commentVO> list = this.replyProc.read_comment(scon_no); // 댓글 등록가져옴
		model.addAttribute("list", list);

		ArrayList<Contents_urlVO> url_list = this.sconProc.only_url(scon_no);

		for (int i = 0; i < url_list.size(); i++) {
			if (url_list.get(i).getUrl_link().equals(" ")) {
				url_list.get(i).setUrl_link("1");
			}
			model.addAttribute("url_list" + i, url_list.get(i).getUrl_link());
		}
		AccountVO img_acc = this.sconProc.read_img(scon_no);
		model.addAttribute("img_acc",img_acc);
		
		ArrayList<Share_imageVO> share_imageVO = this.sconProc.read_image(scon_no);

		model.addAttribute("share_imageVO", share_imageVO);
		ArrayList<HashtagVO> list_tag_no = this.sconProc.sconno_hashtag(scon_no);
		//System.out.println(scon_no+"에 대한 tag_np"+list_tag_no.get(0).getTag_name());
		ArrayList<Share_imageVO> tag_share_imageVO = this.sconProc.related_image(list_tag_no.get(0).getTag_no());
//		for(int i = 0;i<tag_share_imageVO.size();i++) {
//			System.out.println(tag_share_imageVO.get(i).getFile_thumb_name());
//		}
		model.addAttribute("tag_share_imageVO",tag_share_imageVO);
		int related_count = this.sconProc.related_image_count();
		model.addAttribute("related_count",related_count);
		return "scontents/read";
		}else {
			return "redirect:/account/login";
		}
//		try {
//			String url = "/scontents/list_by_search?cate_no=1";
//		    return "redirect:/account/login?url="+ URLDecoder.decode(url, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//		    e.printStackTrace();
//		    return "errorPage";
//		}
	}

	
	@GetMapping("/up_priority/{scon_no}")
	@ResponseBody
	public String up_priority(@PathVariable("scon_no") Integer scon_no, int cate_no, RedirectAttributes ra,HttpSession session) {
		
		if(this.accountProc.isMember(session)) {
			//System.out.println("up_priority");
		JSONObject obj = new JSONObject();
		this.sconProc.up_priority(scon_no);
		int cnt = this.sconProc.y_mark(scon_no);
		HashMap<String, Object> map = new HashMap<>();
		map.put("scon_no", scon_no);
		map.put("acc_no", session.getAttribute("acc_no")); 
		int cnt1 = this.sconProc.bookmark_create(map);

		if (cnt1 == 1) {
			System.out.println("북마크에 등록 성공");
		}
		obj.put("cnt", cnt);
		return obj.toString();
		}else {
			JSONObject obj = new JSONObject();
			obj.put("cnt",0);
			return obj.toString();
		}

	}
	

	@GetMapping("/down_priority/{scon_no}")
	@ResponseBody
	public String down_priority(@PathVariable("scon_no") Integer scon_no, int cate_no, RedirectAttributes ra,HttpSession session) {
		if(this.accountProc.isMember(session)) {
		JSONObject obj = new JSONObject();
		int cnt = this.sconProc.down_priority(scon_no);
		int mark_down = this.sconProc.n_mark(scon_no);

		HashMap<String, Object> map = new HashMap<>();
		map.put("scon_no", scon_no);
		map.put("acc_no", session.getAttribute("acc_no")); 
		int cnt1 = this.sconProc.bookmark_delete(map);
		if (cnt1 == 1) {
			System.out.println("북마크 삭제 성공");
		}

		System.out.println("down_priority created");
		obj.put("cnt", cnt);
		return obj.toString();
		}else {
			JSONObject obj = new JSONObject();
			obj.put("cnt",0);
			return obj.toString();
			
		}

	}
	
  @PostMapping("/create_comment") 
  public String create_comment(String scmt_comment, int scon_no, @RequestParam(name = "acc_no", defaultValue = "0") int acc_no, RedirectAttributes ra, int cate_no,HttpSession session) {
	 if(this.accountProc.isMember(session)) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("scmt_comment", scmt_comment);
    map.put("scon_no", scon_no);
    map.put("acc_no", session.getAttribute("acc_no"));
    int cnt = this.replyProc.create_comment(map);
    
    AccountVO accountVO = this.accountProc.read(acc_no);
    
    String acc_id= accountVO.getAcc_id();
    ra.addAttribute("scon_no", scon_no);
    ra.addAttribute("cate_no", cate_no);
    ra.addAttribute("acc_no", session.getAttribute("acc_no"));
    ra.addAttribute("acc_id",acc_id);
    
    return "redirect:/scontents/read";
	 }else {
		 return "account/login";
	 }
  }

//  @GetMapping("/create_comment")
//  @ResponseBody
//  public String create_comment_form(String scmt_comment, int scon_no, int acc_no,int cate_no) {
//    HashMap<String, Object> map = new HashMap<String, Object>();
//    map.put("scmt_comment", scmt_comment);
//    map.put("scon_no", scon_no);
//    map.put("acc_no", acc_no);
//    int cnt = this.sconProc.create_comment(map);
//    
//    AccountVO accountVO = this.accountProc.read(acc_no);
//    JSONObject obj = new JSONObject();
//    obj.put("acc_id", accountVO.getAcc_id());
//    obj.put("cnt", cnt);
//    
//    return obj.toString();
//  }

	@GetMapping("/update_comment/{acc_no}/{scmt_no}") //세션, 댓글 아이디와 같아야함
	public String update_comment_form(Model model, @PathVariable("scmt_no") Integer scmt_no,@PathVariable("acc_no") Integer acc_no, int cate_no,HttpSession session,int scon_no) {
		if(this.accountProc.isMember(session) && (acc_no==session.getAttribute("acc_no"))) {
		AccountVO accountVO = this.accountProc.read((int)session.getAttribute("acc_no"));
		model.addAttribute("acc_id",accountVO.getAcc_id());
		
		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);
		model.addAttribute("scon_no",scon_no);
		model.addAttribute("scmt_no", scmt_no);
		return "scontents/update_comment";
		}else {
			return "account/login";
		}
	}

	@PostMapping("/update_comment")
	public String update_comment_forn(@RequestParam("scmt_no") int scmt_no, String acc_id,int scon_no,
			@RequestParam("scmt_comment") String scmt_comment, RedirectAttributes ra, int cate_no,HttpSession session) {
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("scmt_no", scmt_no);
		map.put("scmt_comment", scmt_comment);

		int cnt = this.replyProc.update_comment(map);

		if (cnt == 1) {
			System.out.println("댓글 수정 성공");
		}
		System.out.println("-> scon_no:" + scon_no);
		ra.addAttribute("scon_no", scon_no);
		ra.addAttribute("cate_no", cate_no);
		ra.addAttribute("acc_id", acc_id);
		return "redirect:/scontents/read";
	}

	@PostMapping("/delete_comment")
	public String delete_comment(int scmt_no, RedirectAttributes ra, int cate_no,HttpSession session,int scon_no) {
	 if(this.accountProc.isMember(session)) {
	  System.out.println("-> scmt_no :" + scmt_no);
	  System.out.println("->cate_no:" +cate_no);
		int cnt = this.replyProc.delete_scmtno(scmt_no);
		if (cnt == 1) {
			System.out.println("댓글 삭제 성공");
		}
		ra.addAttribute("cate_no", cate_no);
		ra.addAttribute("scon_no", scon_no);
		return "redirect:/scontents/read";
	 }else {
		 return "account/login";
	 }
	}
	
	@PostMapping("/select_delete")
	@ResponseBody
	public String select_delete(@RequestBody Map<String, Object> scon_no, HttpSession session) {
		System.out.println("->cate_no:" +scon_no.get("cate_no"));
		System.out.println("select_delete");
		JSONObject obj = new JSONObject();
		int cnt = 0;
		if(this.accountProc.isMemberAdmin(session)) {
		List<Integer> sconNoList = (List<Integer>) scon_no.get("scon_no");
		
		int delete_notice=this.sconProc.sdelete_notice(sconNoList);
		if(delete_notice>0) {
			System.out.println("알림 삭제 성공");
		}
		int re_comment = this.re_commentProc.sconno_delete(sconNoList);
		if(re_comment>0) {
			System.out.println("대댓글 삭제 성공");
		}
		int image =this.sconProc.sdelete_image(sconNoList);
//		if(image>0) {
//			System.out.println("이미지 삭제 성공");
//		}
		int tag = this.sconProc.sdelete_tag(sconNoList);
//		if(tag>0) {
//			System.out.println("태그 삭제 성공");
//		}
		int bookmark = this.sconProc.sdelete_bookmark(sconNoList);
		if(bookmark>0) {
			System.out.println("북마크 삭제 성공");
		}
		int comment = this.replyProc.sdelete_comment(sconNoList);
//		if(comment>0) {
//			System.out.println("comment 삭제 성공");
//		}
		int url =this.sconProc.sdelete_url(sconNoList);
//		if(url>0) {
//			System.out.println("url 삭제 성공");
//		}
		cnt = this.sconProc.delete_sconno(sconNoList);
		for(int i =0;i<sconNoList.size();i++) {
			this.categoryProc.cnt_minus((int)scon_no.get("cate_no"));
		}

		obj.put("cnt", cnt);
		return obj.toString();
		}else {
			obj.put("cnt", cnt);
			return obj.toString();
		}
	}

	@GetMapping("/update_text/{member_no}") // 글 수정
	public String update_text_form(Model model,@PathVariable("member_no") Integer acc_no, int scon_no, int cate_no,HttpSession session) {

		if(this.accountProc.isMemberAdmin(session) || (acc_no==(int)session.getAttribute("acc_no"))) {
			//System.out.println("들어옴");
		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);

		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);
		model.addAttribute("member_no",scontentsVO.getacc_no());
		model.addAttribute("acc_no",session.getAttribute("acc_no"));
		ArrayList<Contents_urlVO> url_list = this.sconProc.only_url(scon_no);
		for (int i = 0; i < url_list.size(); i++) {
			String url = url_list.get(i).getUrl_link();
			System.out.println("url[i]" + url_list.get(i).getUrl_link());

			if (url.equals("1")) {
				url_list.get(i).setUrl_link(" ");
			}

			model.addAttribute("url_list" + i, url_list.get(i).getUrl_link());
		}
		return "scontents/update_text";
		}else {
			return "account/login";
		}
		
	}

	@PostMapping("/update_text") // url 수정, 등록, 삭제
	public String update_text(Model model, Share_contentsVO scontentsVO, RedirectAttributes ra, int scon_no,
			int cate_no, String url_link) {

		int cnt = this.sconProc.update_text(scontentsVO);

		HashMap<String, Object> map = new HashMap<>();

		ArrayList<Contents_urlVO> arr = this.sconProc.url_read(scon_no);

		String[] list = url_link.split(",");

		System.out.println("-> list의 사이즈:" + list.length);

//    for(int i = 0;i<list.length;i++) { //만약 순서대로 삭제 하지 않았을 경우 1을 넣음
//    	 System.out.println("현재 url_link: " + list[i]);
////    	if(list[i].equals("")) {
////    		list[i]="1";
////    	}
//    }

		for (int i = 0; i < list.length; i++) { // list가 5개 다 있을 경우 하나를 삭제했을 때
			if (list[i].equals("")) {
				list[i] = "1";
			}
			map.put("url_link", list[i].trim());
			map.put("scon_no", scon_no);
			map.put("url_no", arr.get(i).getUrl_no());
			this.sconProc.update_url(map);
		}

		if (list.length != arr.size()) { // list.length와 arr.size 가 다를 경우
			for (int i = arr.size() - 1; i >= list.length; i--) { // url_link가 가 없는 경우
				map.put("url_link", "1");
				map.put("scon_no", scon_no);
				map.put("url_no", arr.get(i).getUrl_no());
				this.sconProc.update_url(map);
				System.out.println("1로 변환");

			}
		}
		ra.addAttribute("cate_no", cate_no);
		return "redirect:/scontents/list_by_search";
	}

	@GetMapping("/update_file/{member_no}") // 파일 수정
	public String update_file_form(Model model, int scon_no, int cate_no,HttpSession session,@PathVariable("member_no") Integer acc_no) {
		if(acc_no==0) {
			return "account/login";
		}
		if(this.accountProc.isMemberAdmin(session) || (acc_no==(int)session.getAttribute("acc_no"))) {
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);
		model.addAttribute("member_no",scontentsVO.getacc_no());
		model.addAttribute("acc_no",session.getAttribute("acc_no"));

		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);

		ArrayList<Share_imageVO> simage = this.sconProc.read_image(scon_no);
		for (int i = 1; i < simage.size(); i++) {
			// long size = simage.getgetFile_size();
			long size = simage.get(i).getFile_size();
			String silze_label = Tool.unit(size);
			simage.get(i).setFlabel(silze_label);
		}

		model.addAttribute("share_imageVO", simage);

		return "scontents/update_file";
	  }else {
	    return "account/login";

	}
	}

	@PostMapping("/update_file")
	public String update_file(Model model, RedirectAttributes ra, int scon_no, List<MultipartFile> fnamesMF,
			int cate_no) {

		ArrayList<Share_imageVO> image_list_old = this.sconProc.read_image(scon_no);
		for (Share_imageVO image : image_list_old) {
			System.out.println(" -> image size : " + image_list_old.size());
			// 파일 삭제
			String file1saved = image.getFile_upload_name();
			String thumb = image.getFile_thumb_name();

			String upDir = Contents.getUploadDir();
			Tool.deleteFile(upDir, file1saved);
			Tool.deleteFile(upDir, thumb);
		}

		long size1 = 0;
		// 파일 전송
		Share_imageVO share_imageVO = new Share_imageVO();
		String upDir = Contents.getUploadDir(); // 파일을 업로드할 폴더 준비
		String file_origin_name = "";
		String file_upload_name = "";
		String file_thumb_name = "";

		long file_size = 0;
		share_imageVO.setFnamesMF(fnamesMF);
		int count = fnamesMF.size();
		System.out.println("-> count: " + count);

		if (count > 0) {
			int cnt1 = 0;
			for (MultipartFile multipartFile : fnamesMF) {
				file_size = multipartFile.getSize();
				if (file_size > 0) {
					file_origin_name = multipartFile.getOriginalFilename();
					file_upload_name = Upload.saveFileSpring(multipartFile, upDir);

					if (Tool.isImage(file_origin_name)) {
						file_thumb_name = Tool.preview(upDir, file_upload_name, 800, 450);
					}
				}

				// System.out.println("-> cnt1: " + cnt1 + ", image_list_old.size(): " +
				// image_list_old.size());
				if (image_list_old.size() <= cnt1) { // 수정할 이미지 갯수가 원래 이미지 갯수보다 많을 경우
					share_imageVO.setScon_no(scon_no);
					share_imageVO.setFile_origin_name(file_origin_name);
					share_imageVO.setFile_thumb_name(file_thumb_name);
					share_imageVO.setFile_upload_name(file_upload_name);
					share_imageVO.setFile_size(count);

					int image_cnt = this.sconProc.attach_create(share_imageVO);
					// System.out.println("image 수정 중 create 완료");
				} else {
					share_imageVO.setFile_no(image_list_old.get(cnt1).getFile_no());
					share_imageVO.setFile_origin_name(file_origin_name);
					share_imageVO.setFile_thumb_name(file_thumb_name);
					share_imageVO.setFile_upload_name(file_upload_name);
					share_imageVO.setFile_size(count);
					int image_cnt = this.sconProc.update_file(share_imageVO);
					System.out.println("-> image_cnt: " + image_cnt);

				}
				cnt1++;
			}

		}
		ra.addAttribute("cate_no", cate_no);
		return "redirect:/scontents/list_by_search";
	}

	@GetMapping("/create")
	public String create_form(Model model, Share_contentsVO scontentsVO, int cate_no,HttpSession session) {
	  if(this.accountProc.isMember(session)) {
	    //System.out.println("->session.getno:" + session.getAttribute("acc_no"));
		model.addAttribute("scontentsVO", scontentsVO);

		// 카테고리 가져오기
		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);
		model.addAttribute("acc_no",session.getAttribute("acc_no")); //로그인 시에 session 들어올 것
		ArrayList<HashtagVO> list = this.sconProc.select_hashtag();
		//System.out.println("-> tag_no :" + list.get(0).getTag_no());
		model.addAttribute("list", list);

		return "scontents/create";
	  }else {
	    return "account/login";
	}
	}

	@PostMapping("/create")
	public String create(Model model, Share_contentsVO scontentsVO, String url_link, RedirectAttributes ra,HttpSession session,
			List<MultipartFile> fnamesMF, @RequestParam("tag_no") int[] tag_no) {
	  //System.out.println(" -> create acc_no :" +scontentsVO.getacc_no()); //로그인 시에 session 들어올 것
		System.out.println("-> fnamesMF :" + fnamesMF);
		String[] url_sub_link = { "1", "1", "1", "1", "1" };

		int cnt = this.sconProc.create(scontentsVO);
		ArrayList<Share_contentsVO> list1 = this.sconProc.list_all();
		System.out.println(" -> list1.size : " + list1.size());

		Share_contentsVO scontentsVO1 = list1.get(list1.size() - 1); // 직전 등록한 Share_contentsVO 가져오기 ->scon_no를 사용하기 위해

		System.out.println("-> create 한 후 scon_no : " + scontentsVO1.getScon_no());
		int scon_no = scontentsVO1.getScon_no();

		if (!url_link.trim().isEmpty()) { // url이 있는 경우에 url 을 등록 할 것
			HashMap<String, Object> map = new HashMap<>();
			String[] list = url_link.split(",");
			System.out.println("url link에 들어옴");
			for (int i = 0; i < list.length; i++) {
				list[i] = list[i].trim();
				map.put("url_link", list[i]);
				map.put("scon_no", scon_no);
				this.sconProc.create_url(map);
			}

			if (list.length < url_sub_link.length) {
				for (int i = list.length; i < url_sub_link.length; i++) {
					map.put("url_link", url_sub_link[i]);
					map.put("scon_no", scon_no);
					this.sconProc.create_url(map);
				}
			}
		} else {
			System.out.println("url 패스");
		}

		String file_origin_name = "";
		String file_upload_name = "";
		String file_thumb_name = "";
		long file_size = 0;

		Share_imageVO share_imageVO = new Share_imageVO();
		String upDir = Contents.getUploadDir(); // 파일을 업로드할 폴더 준비

		System.out.println(fnamesMF);

		// System.out.println("-> scon_no : " +scon_no);
		share_imageVO.setFnamesMF(fnamesMF);
		int count = fnamesMF.size();

		if (count > 0) {
			for (MultipartFile multipartFile : fnamesMF) {
				file_size = multipartFile.getSize();

				if (file_size > 0) {
					file_origin_name = multipartFile.getOriginalFilename();
					file_upload_name = Upload.saveFileSpring(multipartFile, upDir);

					if (Tool.isImage(file_origin_name)) {
						file_thumb_name = Tool.preview(upDir, file_upload_name, 800, 450);
					}
				}
				share_imageVO.setScon_no(scon_no);
				share_imageVO.setFile_origin_name(file_origin_name);
				share_imageVO.setFile_thumb_name(file_thumb_name);
				share_imageVO.setFile_upload_name(file_upload_name);
				share_imageVO.setFile_size(count);

				int image_cnt = this.sconProc.attach_create(share_imageVO);
			}
		}

		HashMap<String, Object> map1 = new HashMap<>();
		for (int i = 0; i < tag_no.length; i++) {
			map1.put("tag_no", tag_no[i]);
			map1.put("scon_no", scon_no);
			int cnt1 = this.sconProc.insert_tag(map1);
		}

		if (cnt == 1) {
			System.out.println("등록 성공");
			System.out.println("-> cate_no: " + scontentsVO.getCate_no());
			this.categoryProc.cnt_plus(scontentsVO.getCate_no()); // 관련 글 수 증가
		}

		// redirect로 넘겨주는 cate_no
		ra.addAttribute("cate_no", scontentsVO.getCate_no());

		return "redirect:/scontents/list_by_search";
	}

	@GetMapping("/delete/{member_no}")
	public String delete(int scon_no, Model model, int cate_no,HttpSession session,@PathVariable("member_no") Integer acc_no) {
		if(acc_no==0) {
			return "account/login";
		}
		if(this.accountProc.isMemberAdmin(session) || ((int)session.getAttribute("acc_no")==acc_no)){
		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);
		model.addAttribute("acc_no", (int)session.getAttribute("acc_no"));
		
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);
		model.addAttribute("member_no",scontentsVO.getacc_no());

		return "scontents/delete";
		}else {
			return "account/login";
		}
	}
//	
//	@PostMapping("/delete")
//	public String delete(int scon_no, RedirectAttributes ra, int cate_no) {
//
//		Share_contentsVO share_contentsVO = this.sconProc.read(scon_no); // scon_no 가져오기
//
//		this.sconProc.delete_comments(scon_no);
//		this.sconProc.delete_url(scon_no);
//		this.sconProc.delete_tag(scon_no);
//		this.sconProc.bookmark_delete(scon_no);
//		ArrayList<Share_imageVO> list = this.sconProc.read_image(scon_no);
//		for (Share_imageVO image : list) {
//			String file_saved = image.getFile_upload_name();
//			String thumb = image.getFile_thumb_name();
//
//			String uploadDir = Contents.getUploadDir();
//			Tool.deleteFile(uploadDir, file_saved);
//			Tool.deleteFile(uploadDir, thumb);
//		}
//		int cnt_image = this.sconProc.delete_image(scon_no);
//		if (cnt_image > 0) {
//			System.out.println("이미지 삭제 성공");
//		}
//		int cnt = this.sconProc.delete(scon_no);
//
//		this.categoryProc.cnt_minus(share_contentsVO.getCate_no()); // 관련 글 수 감소
//		System.out.println(" -> 삭제 한 scon_no:" + scon_no);
//
//		ra.addAttribute("cate_no", cate_no);
//		return "redirect:/scontents/list_by_search";
//	}
	
	@PostMapping("/delete")
	@ResponseBody
	public String delete(@RequestBody String json_src) {
		
		String cleanedJsonSrc = json_src.replace(";", "");
		 JSONObject src = new JSONObject(cleanedJsonSrc);
		 System.out.println(src);
		System.out.println("ok");
		int cate_no = src.getInt("cate_no");
		int scon_no = (int) src.getInt("scon_no");
		System.out.println("-> cate_no:" +cate_no);
		System.out.println("-> scon_no:" +scon_no);
		Share_contentsVO share_contentsVO = this.sconProc.read(scon_no); // scon_no 가져오기
		List<Integer> scon = new ArrayList<>();
		scon.add(scon_no);
		int cnt1 = this.sconProc.bookmarall_delete(scon_no);
		int cnt_notice = this.sconProc.delete_notice(scon_no);
		if(cnt_notice>0) {
			System.out.println("알림 삭제 성공");
		}

		int re_commentcnt = this.re_commentProc.sconno_delete(scon);
		if(re_commentcnt>0) {
			System.out.println("대댓글 삭제");
		}
		
		int cnt_comment=this.replyProc.delete_comment(scon_no);
		if(cnt_comment>0) {
			System.err.println("comment 삭제");
		}

		int cnt_url=this.sconProc.delete_url(scon_no);
		if(cnt_url>0) {
			System.out.println("url 삭제");
		}

		int cnt_delete=this.sconProc.delete_tag(scon_no);
		if(cnt_delete>0) {
			System.out.println("태그 삭제");
		}

		ArrayList<Share_imageVO> list = this.sconProc.read_image(scon_no);
		for (Share_imageVO image : list) {
			String file_saved = image.getFile_upload_name();
			String thumb = image.getFile_thumb_name();

			String uploadDir = Contents.getUploadDir();
			Tool.deleteFile(uploadDir, file_saved);
			Tool.deleteFile(uploadDir, thumb);
		}
		int cnt_image = this.sconProc.delete_image(scon_no);
		if (cnt_image > 0) {
			System.out.println("이미지 삭제 성공");
		}
		
		int cnt = this.sconProc.delete(scon_no);
		if(cnt>0) {
			System.out.println("게시글 삭제 성공");
		}
		this.categoryProc.cnt_minus(share_contentsVO.getCate_no()); // 관련 글 수 감소
		//System.out.println(" -> 삭제 한 scon_no:" + scon_no);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);
		//System.out.println(obj.get("cnt"));
		return obj.toString();
	}

	@GetMapping("/read_hashtag/{tag_no}")
	public String read_hashtag(@PathVariable("tag_no") Integer tag_no, Model model, @RequestParam(name = "word", defaultValue = "") String word,
			int cate_no, @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);

		model.addAttribute("tag_no", tag_no);
		
		ArrayList<HashtagVO> list_hashtag = this.sconProc.select_hashtag();
		model.addAttribute("list_hashtag", list_hashtag);

		HashMap<String,Object> map1 = new HashMap<String,Object>();
		map1.put("now_page",now_page);
	    int begin_of_page = ((int) map1.get("now_page") - 1) * Contents.RECORD_PER_PAGE;
	    int start_num = begin_of_page + 1;
	    int end_num = begin_of_page + Contents.RECORD_PER_PAGE;

//	     System.out.println("begin_of_page: " + begin_of_page);
//	     System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);
	    map1.put("start_num", start_num);
	    map1.put("end_num", end_num);
			
		map1.put("tag_no", tag_no);
		ArrayList<Contents_tagVO> sconno_list = this.sconProc.contents_tag_search_paging(map1); // tag_no에 따른 scon_no
		
		int[] sconno = new int[sconno_list.size()];

		for (int i = 0; i < sconno.length; i++) {
			sconno[i] = sconno_list.get(i).getScon_no();
			// System.out.println("->read_hashtag: " + sconno[i]);
		}

		ArrayList<Share_contentsVO> list = new ArrayList<>(); // scon_no에 따른 Share_contentsVO
		for (int i = 0; i < sconno_list.size(); i++) {
			list.addAll(this.sconProc.list_by_sconno(sconno[i])); //
			// System.out.println("-> list_by_sconno 이후 : " + list.get(i).getScon_no());
		}

		model.addAttribute("list", list);

		word = Tool.checkNull(word).trim();

		HashMap<String, Object> map = new HashMap<>();
		map.put("word", word);
		map.put("now_page", now_page);

		// ArrayList<Share_contentsVO> list1 =
		// this.sconProc.list_by_contents_search_paging(map);
		// model.addAttribute("list", list1);
		model.addAttribute("word", word);

		HashtagVO hash = this.sconProc.read_hashtag_name(tag_no); //tag_no로 hashtag_name 가져오기
//		 for(int i = 0;i<hashtag_VO.size();i++) {
//			 System.out.println("-> tag_name:"+ hashtag_VO.get(i).getTag_name());
//		 }
		System.out.println(hash.getTag_name());
		model.addAttribute("hashtag", hash);

		int count = this.sconProc.tag_count(tag_no);
		model.addAttribute("count", count);

		//ArrayList<Contents_tagVO> tag_sconno = this.sconProc.select_sconno(tag_no);

		ArrayList<Share_imageVO> list_image = new ArrayList<>();
		for(Share_contentsVO list1:list) {
			list_image.addAll(this.sconProc.distinct_image(list1.getScon_no())); //한 행만 image 로 나오게
		}
		
		model.addAttribute("list_image", list_image);
		//int search_count = this.sconProc.list_by_cateno_search_count(map);
		String url ="/scontents/read_hashtag/"+tag_no; 
		int tag_count = this.sconProc.tag_count(tag_no);
		String paging = this.sconProc.pagingBox(now_page, word, url, tag_count,
				Contents.RECORD_PER_PAGE, cate_no, Contents.PAGE_PER_BLOCK);

		model.addAttribute("paging", paging);
		model.addAttribute("now_page", now_page);

		model.addAttribute("search_count", tag_count);

		// 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
		int no = tag_count - ((now_page - 1) * Contents.RECORD_PER_PAGE);
		model.addAttribute("no", no);

		return "scontents/list_by_search_paging";
	}

	@GetMapping("/list_by_search")
	public String list_by_search(Model model, int cate_no, @RequestParam(name = "word", defaultValue = "") String word,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page,HttpSession session) {
		//System.out.println("->acc_no:" +session.getAttribute("acc_no"));
		word = Tool.checkNull(word).trim();
		
		// cate_no를 가져오기 위한 카테고리 가져오기
		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);
		
		ArrayList<HashtagVO> list_hashtag = this.sconProc.select_hashtag();
		model.addAttribute("list_hashtag", list_hashtag);

		HashMap<String, Object> map = new HashMap<>();
		map.put("word", word);
		map.put("now_page", now_page);

		ArrayList<Share_contentsVO> list = this.sconProc.list_by_contents_search_paging(map);
		model.addAttribute("list", list);
		model.addAttribute("word", word);
		for(int i = 0;i<list.size();i++) {
		  this.replyProc.count_comment(list.get(i).getScon_no()); //갯수
		}

		ArrayList<Share_imageVO> list_image = new ArrayList<>();
		for(Share_contentsVO list1:list) {
			list_image.addAll(this.sconProc.distinct_image(list1.getScon_no()));
		}
		model.addAttribute("list_image",list_image);

		int search_count = this.sconProc.list_by_cateno_search_count(map);
		String paging = this.sconProc.pagingBox(now_page, word, "/scontents/list_by_search", search_count,
				Contents.RECORD_PER_PAGE, cate_no, Contents.PAGE_PER_BLOCK);
   //System.out.println(paging);
		model.addAttribute("paging", paging);
		model.addAttribute("now_page", now_page);

		model.addAttribute("search_count", search_count);

		// 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
		int no = search_count - ((now_page - 1) * Contents.RECORD_PER_PAGE);
		model.addAttribute("no", no);

		return "scontents/list_by_search_paging";

	}
	@GetMapping("/notice/{id}")
	public String notice(@PathVariable("id") int acc_no) {
		return "/scontents/notice";
	}
	@PostMapping("/notice/delete")
	@ResponseBody
	public String notice_delete(@RequestBody NotificationVO notice) {
		System.out.println("notice_delete 들어옴");
		JSONObject json = new JSONObject();
		//System.out.println("notice.id:" +notice.getId());
		int cnt = this.sconProc.id_delete(notice.getId());
		if(cnt>0) {
			System.out.println("알림삭제성공");
		}
		json.put("cnt", cnt);
		return json.toString();
	}
	
	
}
