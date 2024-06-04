package dev.mvc.rank;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.account.AccountProcInter;
import dev.mvc.category.CategoryProcInter;
import dev.mvc.category.CategoryVO;
import dev.mvc.recommend.HashtagVO;
import dev.mvc.share_contents.Contents;
import dev.mvc.share_contents.Share_contentsProc;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
@RequestMapping("/scontents")

@Controller
public class RankCont {
  public RankCont() {
	    System.out.println("-> RankCont created.");  
	  }
	

	@Autowired
	@Qualifier("dev.mvc.share_contents.Share_contentsProc")
	private Share_contentsProc sconProc;
	
	@Autowired
	@Qualifier("dev.mvc.category.CategoryProc")
	private CategoryProcInter categoryProc;
	
	@Autowired
	@Qualifier("dev.mvc.rank.RankProc")
	private RankProcInter rankingProc;
	
	
	@GetMapping("/rank")
	public String list_by_search(Model model, int cate_no, @RequestParam(name = "word", defaultValue = "") String word,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page,HttpSession session) {
		//System.out.println("->acc_no:" +session.getAttribute("acc_no"));
		word = Tool.checkNull(word).trim();
		
		// cate_no를 가져오기 위한 카테고리 가져오기
		CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
		model.addAttribute("categoryVO", categoryVO);

		// System.out.println("-> categoryVO.cate_no :" +categoryVO.getCate_no());

		ArrayList<HashtagVO> list_hashtag = this.sconProc.select_hashtag();
		model.addAttribute("list_hashtag", list_hashtag);

		HashMap<String, Object> map = new HashMap<>();
		map.put("word", word);
		map.put("now_page", now_page);

		ArrayList<Share_contentsVO> list = this.rankingProc.ranking(map);
		model.addAttribute("list", list);
		model.addAttribute("word", word);

		ArrayList<Share_imageVO> list_image = new ArrayList<>();
		for(Share_contentsVO list1:list) {
			list_image.addAll(this.sconProc.distinct_image(list1.getScon_no()));
		}
		model.addAttribute("list_image",list_image);

		int search_count = this.sconProc.list_by_cateno_search_count(map);
		String paging = this.sconProc.pagingBox(now_page, word, "/scontents/list_by_search", 8,
				Contents.RECORD_PER_PAGE, cate_no, Contents.PAGE_PER_BLOCK);

		model.addAttribute("paging", paging);
		model.addAttribute("now_page", now_page);

		model.addAttribute("search_count", 8);

		// 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
		int no = search_count - ((now_page - 1) * Contents.RECORD_PER_PAGE);
		model.addAttribute("no", no);

		return "scontents/ranking";

	}
}
	

