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
import dev.mvc.share_contentsdto.Contents_urlVO;
import dev.mvc.share_contentsdto.Share_commentsVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.tool.Tool;

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
		// if(cnt==1) {
		// System.out.println("조회수 +1");
		// }
		model.addAttribute("scon_views", cnt);
		// System.out.println("read 생성");
		Share_contentsVO scontentsVO = this.sconProc.read(scon_no);
		model.addAttribute("scontentsVO", scontentsVO);
		
		int cnt1 = this.sconProc.comment_search(scon_no);
		model.addAttribute("cnt", cnt1);
		
		ArrayList<Share_commentsVO> list = this.sconProc.read_comment(scon_no);
		model.addAttribute("list", list);
		//ArrayList<Contents_urlVO> url_list1 = this.sconProc.url_read(scon_no);
		
		ArrayList<Contents_urlVO> url_list = this.sconProc.only_url(scon_no);
		for(int i = 0;i<url_list.size();i++) {
			model.addAttribute("url_list"+i,url_list.get(i).getUrl_link());
		}

		return "scontents/read";
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
		System.out.println("url_link -> " + url_link);

		HashMap<String, Object> map = new HashMap<>();
		
		String[] list = url_link.split(",");
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].trim();
			map.put("url_link", list[i]);
			map.put("scon_no", scon_no);
			int cnt1 = this.sconProc.update_url(map);
			//System.out.println(list[i]);
		}
		
		return "redirect:/scontents/list_by_search";

	}

	@GetMapping("/create")
	public String create_form(Model model, Share_contentsVO scontentsVO) {
		model.addAttribute("scontentsVO", scontentsVO);

		return "scontents/create";
	}

	@PostMapping("/create")
	public String create(Model model, Share_contentsVO scontentsVO, String url_link, RedirectAttributes ra) {

		int cnt = this.sconProc.create(scontentsVO);
		ArrayList<Share_contentsVO> list1 = this.sconProc.list_all();

		Share_contentsVO scontentsVO1 = list1.get(list1.size() - 1); //바로 등록한 Share_contentsVO 가져오기 ->scon_no를 사용하기 위해
		int scon_no = scontentsVO1.getScon_no();
		System.out.println("scon_no->" + scon_no);

		System.out.println("url_link -> " + url_link);

		HashMap<String, Object> map = new HashMap<>();
		String[] list = url_link.split(",");
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].trim();
			map.put("url_link", list[i]);
			map.put("scon_no", scon_no);
			int cnt1 = this.sconProc.create_url(map);
			//System.out.println(list[i]);
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
		int cnt = this.sconProc.delete(scon_no);
		if (cnt == 1) {
			System.out.println("삭제 성공");
		}
		return "redirect:/scontents/list_by_search";
	}

	@GetMapping("/create_comment")
	@ResponseBody
	public String create_comment_form(String scmt_comment, int scon_no) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("scmt_comment", scmt_comment);
		map.put("scon_no", scon_no);

		int cnt = this.sconProc.create_comment(map);

		JSONObject obj = new JSONObject();
		obj.put("cnt", cnt);

		return obj.toString();
	}

	@GetMapping("/list_by_search")
	public String list_by_search(Model model, @RequestParam(name = "word", defaultValue = "") String word,
			@RequestParam(name = "now_page", defaultValue = "1") int now_page) {

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
