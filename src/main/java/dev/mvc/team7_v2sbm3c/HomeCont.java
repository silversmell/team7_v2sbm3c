package dev.mvc.team7_v2sbm3c;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeCont {

	public HomeCont() {
		System.out.println("-> HomeCont created.");
	}

	@GetMapping(value = "/") // http://localhost:9093
	public String home(Model model) { // 파일명 return

		return "index"; // /templates/index.html
	}

}
