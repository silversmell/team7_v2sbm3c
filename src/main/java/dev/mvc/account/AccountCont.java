package dev.mvc.account;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/account")
@Controller
public class AccountCont {
	
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc")	// @Service("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;
	
	public AccountCont() {
		System.out.println("-> AccountCont created.");
	}
	
	/**
	 * 아이디 중복 확인
	 * 
	 * @param acc_id
	 * @return
	 */
	@GetMapping(value = "/checkID")	// http://localhost:9093/account/checkID?acc_id=user1
	@ResponseBody
	public String checkID(String acc_id) {
		System.out.println("---> acc_id: " + acc_id);
		
		int cnt = this.accountProc.checkID(acc_id);

		JSONObject obj = new JSONObject();
	    obj.put("cnt", cnt);
		
		return obj.toString();
	}
	
	/**
	 * 회원 가입 폼
	 * 
	 * @return
	 */
	@GetMapping(value="/create")	// http://localhost:9093/account/create
	public String create_form(Model model, AccountVO accountVO) {
		return "account/create";	// /templates/account/create.html
	}
	
	/**
	 * 회원 가입 처리
	 * 
	 * @param model
	 * @param accountVO
	 * @return
	 */
	@PostMapping(value="/create")
	public String create_proc(Model model, AccountVO accountVO) {
		int checkID_cnt = this.accountProc.checkID(accountVO.getAcc_id());
		
		if(checkID_cnt == 0) {
			accountVO.setAcc_grade(15);	// 15: 일반 회원
		    int cnt = this.accountProc.create(accountVO);

			if(cnt == 1) {	// 회원 가입 성공
				model.addAttribute("code", "create_success");
				model.addAttribute("acc_id", accountVO.getAcc_id());
				model.addAttribute("acc_name", accountVO.getAcc_name());
				// 암호화 미완
				model.addAttribute("acc_pw", accountVO.getAcc_pw());
			} else {
		        model.addAttribute("code", "create_fail");
			}
			
	        model.addAttribute("cnt", cnt);
		} else {	// 아이디 중복
			model.addAttribute("code", "duplicate_fail");
	        model.addAttribute("cnt", 0);
		}
		
		return "account/msg";	// /templates/account/msg.html
	}
	
	

}
