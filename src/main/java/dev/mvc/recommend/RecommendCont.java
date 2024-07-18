package dev.mvc.recommend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	 * 추천 글 목록
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String list(HttpSession session, Model model) {

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
	
	/**
	 * OpenAI 추천 기능
	 * @param session
	 * @return
	 */
	@GetMapping(value="/desk")
	public String taste(HttpSession session, Model model, int acc_no) {
		// Integer acc_no = (Integer) session.getAttribute("acc_no");
		List<String> filenames = getFilenamesFromDirectory("src/main/resources/static/recommend/images");
		
        // Shuffle the filenames list
        Collections.shuffle(filenames);
		
		model.addAttribute("acc_no", acc_no);
		model.addAttribute("filenames", filenames);
		
		return "recommend/desk";
	}
	
    private List<String> getFilenamesFromDirectory(String directory) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory), 1)) {
            return paths.filter(Files::isRegularFile)
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of(); // Empty list in case of an error
        }
    }

}
