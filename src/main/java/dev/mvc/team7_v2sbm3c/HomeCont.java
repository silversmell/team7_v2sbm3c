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
import dev.mvc.recommend.RecommendProcInter;
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
	@Qualifier("dev.mvc.recommend.RecommendProc")
	private RecommendProcInter recommendProc;
	
	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private Share_contentsProc sconProc;

	@GetMapping(value = "/") // http://localhost:9093
	public String home(Model model, HttpSession session) {
		Integer acc_no = (Integer) session.getAttribute("acc_no");
		// System.out.println("--> acc_no:" + session.getAttribute("acc_no"));
		
		ArrayList<HashtagVO> list_hashtag = this.sconProc.select_hashtag();
		model.addAttribute("list_hashtag", list_hashtag);
	
		/** 랭킹글 */
		ArrayList<Share_contentsVO> ranking_list = this.rankingProc.ranking();
		model.addAttribute("ranking_list", ranking_list);
		
		ArrayList<Share_imageVO> ranking_image = new ArrayList<>();
		for(Share_contentsVO list1:ranking_list) {
			ranking_image.addAll(this.sconProc.distinct_image(list1.getScon_no()));
		}
		model.addAttribute("ranking_image",ranking_image);
		
		/** 추천글 */
		ArrayList<Share_contentsVO> recom_list = new ArrayList<Share_contentsVO>();
		
		if(acc_no != null) {
			recom_list = this.recommendProc.list(acc_no);
		} else {
			recom_list = this.recommendProc.random_list();
		}
		model.addAttribute("recom_list", recom_list);
		
		ArrayList<Share_imageVO> recom_image = new ArrayList<>();
		for(Share_contentsVO list1:recom_list) {
			recom_image.addAll(this.sconProc.distinct_image(list1.getScon_no()));
		}
		model.addAttribute("recom_image",recom_image);

		return "/index";
	}
}
