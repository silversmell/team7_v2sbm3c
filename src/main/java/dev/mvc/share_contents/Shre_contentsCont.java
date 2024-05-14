package dev.mvc.share_contents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/scontents")
@Controller
public class Shre_contentsCont {
	
	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private Share_contentsProc sconProc;
	
	public Shre_contentsCont() {
		System.out.println("Share_contentsCont created");
	}
	

}
