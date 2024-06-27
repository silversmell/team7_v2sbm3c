package dev.mvc.team7_v2sbm3c;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.category.CategoryProcInter;
import dev.mvc.category.CategoryVO;
import dev.mvc.rank.RankProcInter;
import dev.mvc.recommend.HashtagVO;
import dev.mvc.share_contents.Contents;
import dev.mvc.share_contents.Share_contentsProc;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeCont {

	public HomeCont() {
		System.out.println("-> HomeCont created.");
	}
	@Autowired
	@Qualifier("dev.mvc.category.CategoryProc")
	private CategoryProcInter categoryProc;
	
	@Autowired
	@Qualifier("dev.mvc.rank.RankProc")
	private RankProcInter rankingProc;
	
	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private Share_contentsProc sconProc;

	@GetMapping(value = "/") // http://localhost:9093
	public String home(Model model, HttpSession session) {
		//System.out.println("->acc_no:" +session.getAttribute("acc_no"));
		


		ArrayList<HashtagVO> list_hashtag = this.sconProc.select_hashtag();
		model.addAttribute("list_hashtag", list_hashtag);
	
		ArrayList<Share_contentsVO> list = this.rankingProc.ranking();
		model.addAttribute("list", list);
		
		ArrayList<Share_imageVO> list_image = new ArrayList<>();
		for(Share_contentsVO list1:list) {
			list_image.addAll(this.sconProc.distinct_image(list1.getScon_no()));
		}
		model.addAttribute("list_image",list_image);

		return "/index";
	}
}
