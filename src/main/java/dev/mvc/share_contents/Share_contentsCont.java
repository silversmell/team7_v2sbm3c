package dev.mvc.share_contents;

import java.util.ArrayList;
import java.util.HashMap;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.share_contents.Contents;
import dev.mvc.share_contentsdto.Share_commentsVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.tool.Tool;

@RequestMapping("/scontents")
@Controller
public class Share_contentsCont {

	
	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private  Share_contentsProc sconProc;
	
	public Share_contentsCont() {
		System.out.println(" ->Share_contentsCont created");
	}

	
	@GetMapping("/list_all")
	public String list_all(Model model) {
		//System.out.println("list_all 생성");
		ArrayList<Share_contentsVO> list = this.sconProc.list_all();
		model.addAttribute("list",list);
		
		return "scontents/list_all";
	}
	
	@GetMapping("/read")
	public String read(Model model,int scon_no) {
		int cnt = this.sconProc.update_view(scon_no);
		//if(cnt==1) {
			//System.out.println("조회수 +1");
		//}
		model.addAttribute("scon_views",cnt);
		//System.out.println("read 생성");
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO",scontentsVO);
	  ArrayList<Share_commentsVO> list =this.sconProc.read_comment(scon_no);
	  model.addAttribute("list",list);
		return "scontents/read";
	}
	
	@GetMapping("/update_text")
	public String update_text_form(Model model, int scon_no) {
	   Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
	    model.addAttribute("scontentsVO",scontentsVO);
	    
	   return "scontents/update_text"; 
	  
	}
	 @PostMapping("/update_text")
	  public String update_text(Model model, Share_contentsVO scontentsVO
	                                    ,RedirectAttributes ra) {
	      
	     int cnt = this.sconProc.update_text(scontentsVO);
//	     System.out.println("scontentsVO.getScon_no -> " + scontentsVO.getScon_no());
	     ra.addAttribute("scon_no",scontentsVO.getScon_no());
	     return "redirect:/scontents/read"; 
	    
	  }
	 @GetMapping("/create")
	 public String create_form(Model model,Share_contentsVO scontentsVO) {
	   model.addAttribute("scontentsVO",scontentsVO);
	   
	   return "scontents/create";
	 }
	 @PostMapping("/create")
	 public String create(Model model,Share_contentsVO scontentsVO
	                             ,RedirectAttributes ra) {
	   
	   int cnt = this.sconProc.create(scontentsVO);

	   
	   return "redirect:/scontents/list_all";
	 }
	 
	 @GetMapping("/delete")
	 public String delete(int scon_no,Model model) {
	   Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
	   model.addAttribute("scontentsVO",scontentsVO);
	   
	   return "scontents/delete";
	   
	 }
	 @PostMapping("/delete")
	 public String delete(int scon_no ,RedirectAttributes ra) {
	   int cnt = this.sconProc.delete(scon_no);
	   if(cnt==1) {
	     System.out.println("삭제 성공");
	   }
	   return "redirect:/scontents/list_all";
	   

	 }
	 @GetMapping("/create_comment")
	 @ResponseBody
	 public String create_comment_form(String scmt_comment,int scon_no) {
	   HashMap<String, Object> map = new HashMap<String, Object>();
	   map.put("scmt_comment",scmt_comment);
	   map.put("scon_no", scon_no);

	   int cnt = this.sconProc.create_comment(map);
	   
	    JSONObject obj = new JSONObject();
	    obj.put("cnt", cnt);
	   
	   return obj.toString();
	 }
	 @GetMapping("/list_by_search")
	 public String list_by_search(Model model, 
	     @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
	   
	   /**
	   Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
	    model.addAttribute("scontentsVO",scontentsVO);
	    **/
	    word = Tool.checkNull(word).trim();
	    
	    HashMap<String, Object> map = new HashMap<>();
	    map.put("word", word);
	    map.put("now_page", now_page);
	    
	    ArrayList<Share_contentsVO> list = this.sconProc.list_by_contents_search_paging(map);
	    model.addAttribute("list", list);
	    
	    model.addAttribute("word", word);
	    
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
