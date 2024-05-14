package dev.mvc.share_contents;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.share_contentsdto.Share_contentsVO;

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
		//System.out.println("list_all 생성");
		ArrayList<Share_contentsVO> list = this.sconProc.list_all();
		model.addAttribute("list",list);
		for(int i = 0;i<list.size();i++) {
			System.out.println(list.get(i).getScon_title());
		}
		
		return "scontents/list_all";
	}
	
	@GetMapping("/read")
	public String read(Model model,int scon_no) {
		//System.out.println("read 생성");
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO",scontentsVO);

		
		return "scontents/read";
	}
	

}
