package dev.mvc.share_contents;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.share_contentsdto.Share_contentsVO;

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
//	   if(cnt==1) {
//	     System.out.println("create 성공");
//	   }
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
	
	

}
