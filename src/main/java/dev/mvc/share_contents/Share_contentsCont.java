package dev.mvc.share_contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.recommend.HashtagVO;
import dev.mvc.share_contents.Contents;
import dev.mvc.share_contentsdto.Contents_tagVO;
import dev.mvc.share_contentsdto.Contents_urlVO;
import dev.mvc.share_contentsdto.Share_commentsVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@RequestMapping("/scontents")
@Controller
public class Share_contentsCont {

	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private Share_contentsProc sconProc;

	public Share_contentsCont() {
		System.out.println(" ->Share_contentsCont created");
	}

	@GetMapping("/list_all")
	public String list_all(Model model) {
		// System.out.println("list_all 생성");
		ArrayList<Share_contentsVO> list = this.sconProc.list_all();
		model.addAttribute("list", list);

		return "scontents/list_all";
	}

	@GetMapping("/read")
	public String read(Model model, int scon_no) {
		int cnt = this.sconProc.update_view(scon_no);

		model.addAttribute("scon_views", cnt);

		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);
		System.out.println("-> mark: " +scontentsVO.getMark());
		System.out.println("-> priority : "+scontentsVO.getScon_priority());

		ArrayList<Contents_tagVO> list1 = this.sconProc.read_contents_tag(scon_no); // hashtag vo 들어오면 변경할 것
		ArrayList<String> list2 = new ArrayList<>();
		for (int i = 0; i < list1.size(); i++) {
			HashtagVO list3 = this.sconProc.select_hashname(list1.get(i).getTag_no());
			list2.add(list3.getTag_name());
		}
		model.addAttribute("list2", list2);

		int cnt1 = this.sconProc.comment_search(scon_no);
		model.addAttribute("cnt", cnt1);

		ArrayList<Share_commentsVO> list = this.sconProc.read_comment(scon_no);

		model.addAttribute("list", list);
		model.addAttribute("acc_no", 1);

		ArrayList<Contents_urlVO> url_list = this.sconProc.only_url(scon_no);

		for (int i = 0; i < url_list.size(); i++) {
			model.addAttribute("url_list" + i, url_list.get(i).getUrl_link());
		}
		ArrayList<Share_imageVO> share_imageVO = this.sconProc.read_image(scon_no);

		model.addAttribute("share_imageVO", share_imageVO);

		return "scontents/read";
	}
	
	@GetMapping("/up_priority/{scon_no}")
	public String up_priority(@PathVariable("scon_no") Integer scon_no) {
		
		this.sconProc.up_priority(scon_no);
		int cnt = this.sconProc.y_mark(scon_no);
		HashMap<String,Object> map = new HashMap<>();
		map.put("scon_no", scon_no);
		map.put("acc_no",1); // acc_no 들어오면 바꿔야 할 것!
		int cnt1 = this.sconProc.bookmarK_create(map);
		
		if(cnt1 == 1) {
			System.out.println("북마크에 등록 성공");
		}
//		if(cnt==1) {
//			System.out.println("y_mark 성공");
//		}
		
		//System.out.println("up_priority created");
		return "redirect:/scontents/read?scon_no=" +scon_no;
	}
	
	@GetMapping("/down_priority/{scon_no}")
	public String down_priority(@PathVariable("scon_no") Integer scon_no) {
		int cnt = this.sconProc.down_priority(scon_no);
		int mark_down = this.sconProc.n_mark(scon_no);
//		if(mark_down=='N') {
//			System.out.println("MARK_N 성공");
//		}
		int cnt1 = this.sconProc.bookmark_delete(scon_no);
		if(cnt1==1) {
			System.out.println("북마크 삭제 성공");
		}
//		if(cnt==1) {
//			System.out.println("scon_priority down 성공");
//		}
		System.out.println("down_priority created");
		return "redirect:/scontents/read?scon_no=" +scon_no;
	}
	

	@GetMapping("/create_comment")
	@ResponseBody
	public String create_comment_form(String scmt_comment, int scon_no, int acc_no) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("scmt_comment", scmt_comment);
		map.put("scon_no", scon_no);
		map.put("acc_no", acc_no);
		int cnt = this.sconProc.create_comment(map);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		return obj.toString();
	}
	
	@GetMapping("/update_comment")
	@ResponseBody
	public String update_comment_form(String scmt_comment,int scon_no) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("scmt_comment",scmt_comment);
		map.put("scon_no", scon_no);
		
		int cnt = this.sconProc.read_scmtno(map);
		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);
		
		return obj.toString();
	}

	@GetMapping("/update_text")
	public String update_text_form(Model model, int scon_no) {

		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);

		ArrayList<Contents_urlVO> url_list = this.sconProc.only_url(scon_no);
		for (int i = 0; i < url_list.size(); i++) {
			String url = url_list.get(i).getUrl_link();
			if (url.equals("1")) {
				url_list.get(i).setUrl_link(" ");
			}
			model.addAttribute("url_list" + i, url_list.get(i).getUrl_link());
		}
		return "scontents/update_text";
	}

	@GetMapping("/update_file") // 파일 수정
	public String update_file_form(Model model, int scon_no) {
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);

		ArrayList<Share_imageVO> simage = this.sconProc.read_image(scon_no);
		for (int i = 0; i < simage.size(); i++) {
			// long size = simage.getgetFile_size();
			long size = simage.get(i).getFile_size();
			String silze_label = Tool.unit(size);
			simage.get(i).setFlabel(silze_label);
		}

		model.addAttribute("share_imageVO", simage);

		return "scontents/update_file";

	}

	@PostMapping("/update_file")
	public String update_text(Model model, RedirectAttributes ra, int scon_no, List<MultipartFile> fnamesMF) {

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
						file_thumb_name = Tool.preview(upDir, file_upload_name, 200, 150);
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
		return "redirect:/scontents/list_by_search";
	}

	@PostMapping("/update_text")
	public String update_text(Model model, Share_contentsVO scontentsVO, RedirectAttributes ra, int scon_no,
			String url_link, List<MultipartFile> fnamesMF) {
		int cnt = this.sconProc.update_text(scontentsVO);
		// System.out.println("url_link -> " + url_link);

		HashMap<String, Object> map = new HashMap<>();

		String[] list = url_link.split(",");
		ArrayList<Contents_urlVO> arr = this.sconProc.url_read(scon_no);

		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].trim();
			map.put("url_link", list[i]);
			map.put("scon_no", scon_no);
			map.put("url_no", arr.get(i).getUrl_no());
			this.sconProc.update_url(map);
		}

		return "redirect:/scontents/list_by_search";
	}

	@GetMapping("/create")
	public String create_form(Model model, Share_contentsVO scontentsVO) {
		model.addAttribute("scontentsVO", scontentsVO);

		ArrayList<HashtagVO> list = this.sconProc.select_hashtag();
		System.out.println("-> tag_no :" + list.get(0).getTag_no());
		model.addAttribute("list", list);

		return "scontents/create";
	}

	@PostMapping("/create")
	public String create(Model model, Share_contentsVO scontentsVO, String url_link, RedirectAttributes ra,
			List<MultipartFile> fnamesMF, @RequestParam("tag_no") int[] tag_no) {
		System.out.println("-> fnamesMF :" + fnamesMF);
		String[] url_sub_link = { "1", "1", "1", "1", "1" };
		
		int cnt = this.sconProc.create(scontentsVO);

		if (cnt == 1) {
			System.out.println("등록 성공");
		}
		ArrayList<Share_contentsVO> list1 = this.sconProc.list_all();
		for (Share_contentsVO list : list1) {
			System.out.println("->scon_no: " + list.getScon_no());

		}
		System.out.println(" -> list1.size : " + list1.size());

		Share_contentsVO scontentsVO1 = list1.get(list1.size() - 1); // 직전 등록한 Share_contentsVO 가져오기 ->scon_no를 사용하기 위해

		System.out.println("-> create 한 후 scon_no : " + scontentsVO1.getScon_no());
		int scon_no = scontentsVO1.getScon_no();
		System.out.println(" -> scon_no" + scon_no);

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
						file_thumb_name = Tool.preview(upDir, file_upload_name, 200, 150);
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

		HashMap<String, Object> map = new HashMap<>();
		String[] list = url_link.split(",");

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

		return "redirect:/scontents/list_by_search";
	}

	@GetMapping("/delete")
	public String delete(int scon_no, Model model) {
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);

		return "scontents/delete";

	}

	@PostMapping("/delete")
	public String delete(int scon_no, RedirectAttributes ra) {
		this.sconProc.delete_comments(scon_no);
		this.sconProc.delete_url(scon_no);

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
		System.out.println(" -> 삭제 한 scon_no:" + scon_no);
		return "redirect:/scontents/list_by_search";
	}

	@GetMapping("/read_hashtag")
	public String read_hashtag(int tag_no, Model model, @RequestParam(name = "word", defaultValue = "") String word,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page) {
		ArrayList<Contents_tagVO> sconno_list = this.sconProc.select_sconno(tag_no); // tag_no에 따른 scon_no
		int[] sconno = new int[sconno_list.size()];

		for (int i = 0; i < sconno.length; i++) {
			sconno[i] = sconno_list.get(i).getScon_no();
			// System.out.println("->read_hashtag: " + sconno[i]);
		}

		ArrayList<Share_contentsVO> list = new ArrayList<>(); // scon_no에 따른 Share_contentsVO
		for (int i = 0; i < sconno_list.size(); i++) {
			list.addAll(this.sconProc.list_by_sconno(sconno[i]));
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

		ArrayList<Contents_tagVO> tag_sconno = this.sconProc.select_sconno(tag_no);
		
		ArrayList<Share_imageVO> list_image = new ArrayList<>();
		for (Contents_tagVO scon : tag_sconno) {
			list_image.addAll(this.sconProc.read_image(scon.getScon_no()));
		}
		model.addAttribute("list_image", list_image);

		int search_count = this.sconProc.list_by_cateno_search_count(map);
		String paging = this.sconProc.pagingBox(now_page, word, "/scontents/list_by_search", search_count,
				Contents.RECORD_PER_PAGE, Contents.PAGE_PER_BLOCK);

		model.addAttribute("paging", paging);
		model.addAttribute("now_page", now_page);

		model.addAttribute("search_count", search_count);

		// 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
		int no = search_count - ((now_page - 1) * Contents.RECORD_PER_PAGE);
		model.addAttribute("no", no);

		return "scontents/list_by_search_paging";

	}

	@GetMapping("/list_by_search")
	public String list_by_search(Model model, @RequestParam(name = "word", defaultValue = "") String word,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page) {

		word = Tool.checkNull(word).trim();

		HashMap<String, Object> map = new HashMap<>();
		map.put("word", word);
		map.put("now_page", now_page);

		ArrayList<Share_contentsVO> list = this.sconProc.list_by_contents_search_paging(map);
		model.addAttribute("list", list);

		ArrayList<HashtagVO> list_hashtag = this.sconProc.select_hashtag();
		model.addAttribute("list_hashtag", list_hashtag);

		model.addAttribute("word", word);
		
		// 사진 하나만 나오게 하기
		ArrayList<Share_imageVO> list_image1 = this.sconProc.distinct_sconno();
		ArrayList<Share_imageVO> list_image = new ArrayList<>();
		for(Share_imageVO distinct_list: list_image1) {
		System.out.println("->중복 아이디 :" + distinct_list.getScon_no() );
		ArrayList<Share_imageVO> distinct_image = this.sconProc.read_image(distinct_list.getScon_no()) ;
		list_image.add(distinct_image.get(0)); //첫번째 것
		}
		model.addAttribute("list_image",list_image);
		
		int search_count = this.sconProc.list_by_cateno_search_count(map);
		String paging = this.sconProc.pagingBox(now_page, word, "/scontents/list_by_search", search_count,
				Contents.RECORD_PER_PAGE, Contents.PAGE_PER_BLOCK);

		model.addAttribute("paging", paging);
		model.addAttribute("now_page", now_page);

		model.addAttribute("search_count", search_count);

		// 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
		int no = search_count - ((now_page - 1) * Contents.RECORD_PER_PAGE);
		model.addAttribute("no", no);

		return "scontents/list_by_search_paging";

	}

}
