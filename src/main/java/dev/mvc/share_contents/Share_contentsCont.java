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
		
		ArrayList<Contents_tagVO> list1 = this.sconProc.read_contents_tag(scon_no); //hashtag vo 들어오면 변경할 것
		ArrayList<String> list2 = new ArrayList<>();
		for(int i = 0;i<list1.size();i++) {
			HashtagVO list3=this.sconProc.select_hashname(list1.get(i).getTag_no());
			list2.add(list3.getTag_name());
		}
		model.addAttribute("list2",list2);
		
		int cnt1 = this.sconProc.comment_search(scon_no);
		model.addAttribute("cnt", cnt1);
		
		ArrayList<Share_commentsVO> list = this.sconProc.read_comment(scon_no);

		model.addAttribute("list", list);
		model.addAttribute("acc_no",1);

		ArrayList<Contents_urlVO> url_list = this.sconProc.only_url(scon_no);
		

		for(int i = 0;i<url_list.size();i++) {
			model.addAttribute("url_list"+i,url_list.get(i).getUrl_link());
		}

		return "scontents/read";
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

	@GetMapping("/update_text")
	public String update_text_form(Model model, int scon_no) {
		
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);
		
		ArrayList<Contents_urlVO> url_list = this.sconProc.only_url(scon_no);
		for(int i = 0;i<url_list.size();i++) {
			model.addAttribute("url_list"+i,url_list.get(i).getUrl_link());
		}
		return "scontents/update_text";
	}

	@PostMapping("/update_text")
	public String update_text(Model model, Share_contentsVO scontentsVO, RedirectAttributes ra,int scon_no,String url_link) {

		int cnt = this.sconProc.update_text(scontentsVO);
		//System.out.println("url_link -> " + url_link);

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
		System.out.println("-> tag_no :" +list.get(0).getTag_no());
		model.addAttribute("list",list);

		return "scontents/create";
	}

	@PostMapping("/create")
	public String create(Model model, Share_contentsVO scontentsVO, String url_link, RedirectAttributes ra, List<MultipartFile> fnamesMF,
	                          @RequestParam("tag_no") int[] tag_no) {   
	  
		String[] url_sub_link = {"1","1","1","1","1"};
	      System.out.println(fnamesMF);
		int cnt = this.sconProc.create(scontentsVO);
		ArrayList<Share_contentsVO> list1 = this.sconProc.list_all();

		Share_contentsVO scontentsVO1 = list1.get(list1.size() - 1); //직전 등록한 Share_contentsVO 가져오기 ->scon_no를 사용하기 위해
		int scon_no = scontentsVO1.getScon_no();

	      String file_origin_name="";
	      String file_upload_name="";
	      String file_thumb_name="";
	      long file_size=0;
	      
		Share_imageVO share_imageVO = new Share_imageVO();
	      String upDir = Contents.getUploadDir(); // 파일을 업로드할 폴더 준비

	      System.out.println(fnamesMF);
	      
	      share_imageVO.setFnamesMF(fnamesMF);
	      int count = fnamesMF.size();
	      System.out.println("-> count: " +count);
	      if(count>0) {
	    	  for(MultipartFile multipartFile: fnamesMF) {
	    		  file_size = multipartFile.getSize();
	    		  System.out.println("-> file_size: " +file_size);
	    		  
	    		  if(file_size>0) {
	    			  file_origin_name=multipartFile.getOriginalFilename();
	    			  file_upload_name=Upload.saveFileSpring(multipartFile, upDir);
	    			  
	    			  if(Tool.isImage(file_origin_name)) {
	    				  file_thumb_name=Tool.preview(upDir, file_upload_name, 200, 150);
	    			  }
	    		  }
	    		  share_imageVO.setFile_no(scon_no);
	    		  share_imageVO.setFile_origin_name(file_origin_name);
	    		  share_imageVO.setFile_thumb_name(file_thumb_name);
	    		  share_imageVO.setFile_upload_name(file_upload_name);
	    		  share_imageVO.setFile_size(count);
	    		  
	    		  int image_cnt=this.sconProc.attach_create(share_imageVO);
	    		  System.out.println("-> image_cnt: " + image_cnt);
	    	  }
	      }
	      

		HashMap<String, Object> map1 = new HashMap<>();
		for(int i = 0;i<tag_no.length;i++) {
		  map1.put("tag_no",tag_no[i]);
      map1.put("scon_no", scon_no);
      int cnt1 = this.sconProc.insert_tag(map1);
      //System.out.println("해시태그 등록 성공: "+tag_no[i]);
		}

		HashMap<String, Object> map = new HashMap<>();
		String[] list = url_link.split(",");
		System.out.println("list.length: "+ list.length);

		for (int i = 0; i < list.length; i++) {

			list[i] = list[i].trim();
			map.put("url_link", list[i]);
			map.put("scon_no", scon_no);
			this.sconProc.create_url(map);
		}
		if(list.length<url_sub_link.length) {
			for(int i = list.length;i<url_sub_link.length;i++) {
				map.put("url_link", url_sub_link[i]);
				map.put("scon_no",scon_no);
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
		int cnt = this.sconProc.delete(scon_no);

		return "redirect:/scontents/list_by_search";
	}
	 @GetMapping("/read_hashtag")
	  public String read_hashtag(int tag_no,Model model) {
	   System.out.println("-> tag_not :" + tag_no);
	    ArrayList<Contents_tagVO> sconno_list = this.sconProc.select_sconno(tag_no); //tag_no에 따른 scon_no
	    int[] sconno = new int[sconno_list.size()];
	    for(int i = 0;i<sconno.length;i++) {
	      sconno[i] = sconno_list.get(i).getScon_no();
	      System.out.println("-> sconno[i] :" + sconno[i]);
	    }
	    ArrayList<Share_contentsVO> list = new ArrayList<>(); //scon_no에 따른 Share_contentsVO
	    for(int i = 0;i<sconno_list.size();i++) {
	      list.addAll(this.sconProc.list_by_sconno(sconno[i]));
	    }
	    model.addAttribute("list",list);
	    return "scontents/list_by_search_paging";
	    
	  }


	@GetMapping("/list_by_search")
	public String list_by_search(Model model, @RequestParam(name = "word", defaultValue = "") String word,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page) {

		word = Tool.checkNull(word).trim();

		HashMap<String, Object> map = new HashMap<>();
		map.put("word", word);
		map.put("now_page", now_page);
		ArrayList<HashtagVO> list_hashtag = this.sconProc.select_hashtag();
		model.addAttribute("list_hashtag", list_hashtag);
		
		ArrayList<Share_contentsVO> list = this.sconProc.list_by_contents_search_paging(map);
		model.addAttribute("list", list);
		
		model.addAttribute("word", word);
		
		ArrayList<Share_imageVO> list_image = this.sconProc.list_all_image();
		for(int i  =0 ;i<list_image.size();i++) {
		  System.out.println("-> list_image:" + list_image.get(i).getFile_thumb_name());
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
