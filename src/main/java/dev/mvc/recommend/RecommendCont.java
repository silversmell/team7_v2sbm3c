package dev.mvc.recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/recommend")
@Controller
public class RecommendCont {

	@Autowired
	@Qualifier("dev.mvc.recommend.RecommendProc")
	private RecommendProcInter recommendProc;

	public RecommendCont() {
		System.out.println("-> RecommendCont created.");
	}

	/**
	 * 추천글 목록
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String list(HttpServletRequest request, HttpSession session, Model model) {

		Integer acc_no = (Integer) session.getAttribute("acc_no");
		System.out.println("recommend/list session ==> acc_no: " + acc_no);

		if (acc_no != null) {
			ArrayList<Share_contentsVO> list = this.recommendProc.list(acc_no);

			// 각 게시글에 대한 이미지
			Map<Integer, List<Share_imageVO>> contentImages = new HashMap<>();
			for (Share_contentsVO content : list) {
				List<Share_imageVO> image = this.recommendProc.contentImages(content.getScon_no());
				contentImages.put(content.getScon_no(), image);
			}

			model.addAttribute("list", list);
			model.addAttribute("contentImages", contentImages);
			model.addAttribute("count", list.size());

			ArrayList<HashtagVO> hashtags = this.recommendProc.selectedTags(acc_no);
			model.addAttribute("hashtags", hashtags);
			
		} else {
			model.addAttribute("code", "login_need");
		}
		
		return "recommend/list";
	}

}
