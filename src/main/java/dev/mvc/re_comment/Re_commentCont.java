package dev.mvc.re_comment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.account.AccountProcInter;
import dev.mvc.account.AccountVO;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/recomment")
@Controller
public class Re_commentCont {
  @Autowired
  @Qualifier("dev.mvc.re_comment.Re_commentProc")
  private Re_commentProcInter re_commentProc;
  
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;
	

	public  Re_commentCont() {
		System.out.println("->Re_commentCont created");
	}
	
	@PostMapping("/create")
	@ResponseBody
	public String create(@RequestBody Share_recommentVO re_commentVO) {
		System.out.println("들어옴");
		HashMap<String,Object> map = new HashMap<>();
		System.out.println("-> re_comment :" + re_commentVO.getSrecmt_contents());
		System.out.println("-> scon_no :" + re_commentVO.getScon_no());
		System.out.println("->scmt_no :" + re_commentVO.getScmt_no());
		System.out.println("-> acc_no :" + re_commentVO.getAcc_no());
		map.put("srecmt_contents",re_commentVO.getSrecmt_contents());
		map.put("scon_no",re_commentVO.getScon_no());
		map.put("scmt_no", re_commentVO.getScmt_no());
		map.put("acc_no",re_commentVO.getAcc_no());
		
		int cnt = this.re_commentProc.create(map);
		JSONObject json = new JSONObject();	  
		json.put("cnt", cnt);
		return json.toString();
	}
	
	@GetMapping("/read")
	@ResponseBody
	public String read(int scmt_no) {
		HashMap<String,Object> map = new HashMap<>();
		map.put("scmt_no", scmt_no);
		ArrayList<Share_recommentVO> list = this.re_commentProc.read_recomment(map);
		if(list.size()>0) {
			System.out.println("조회 성공");
		}
		for(int i = 0;i<list.size();i++) {
			System.out.println("->대댓글 :" + list.get(i).getSrecmt_contents());
			System.out.println("->acc_no:" + list.get(i).getAcc_no());
			System.out.println("->add_id:" + list.get(i).getAcc_id());
		}
		JSONObject json = new JSONObject();	
		json.put("res",list);
		return json.toString();
	}
	@GetMapping("/read_account")
	@ResponseBody
	public String read_account(int acc_no) {
		JSONObject json = new JSONObject();	
		AccountVO account=this.accountProc.read(acc_no);
		json.put("acc_id", account.getAcc_id());
		System.out.println("-> account read 시 acc_id" +account.getAcc_id());
		return json.toString();
	}
	
	
	//recomment_no,scmt_no,scon_no,re_comment,re_comment_date
	@GetMapping("/read_comment")
	@ResponseBody
	public String read_comment(int srecmt_no) {
		System.out.println("->recomment_no:" + srecmt_no);
		Share_recommentVO re_comment = this.re_commentProc.read_comment(srecmt_no);
		
		JSONObject row = new JSONObject();
		
		System.out.println(re_comment.getSrecmt_contents());
		System.out.println(re_comment.getScmt_no());
		System.out.println(re_comment.getScon_no());
		System.out.println(re_comment.getSrecmt_no());
		System.out.println(re_comment.getSrecmt_date());
		
		row.put("recomment_no",re_comment.getSrecmt_no());
		row.put("scmt_no",re_comment.getScmt_no());
		row.put("scon_no", re_comment.getScon_no());
		row.put("srecmt_contents", re_comment.getSrecmt_contents());
		row.put("re_comment_date", re_comment.getSrecmt_date());
		
		  JSONObject json = new JSONObject();
		  json.put("res",row);
		  
		  return json.toString();
		
	}
	
	@PostMapping("/update")
	@ResponseBody
	public String update(@RequestBody Share_recommentVO re_commentVO,HttpSession session) {
		System.out.println("->수정시 회원번호:"+re_commentVO.getSrecmt_contents());
		if(re_commentVO.getAcc_no()==(int)session.getAttribute("acc_no")){
			System.out.println("회원이 같음");
			
			System.out.println("->수정시 re_comment:"+re_commentVO.getSrecmt_no());
			System.out.println("->수정시 re_commentno"+re_commentVO.getSrecmt_no());
			
			  JSONObject json = new JSONObject();
			  HashMap<String,Object> map = new HashMap<>();
			  map.put("srecmt_no",re_commentVO.getSrecmt_no());
			  map.put("srecmt_contents",re_commentVO.getSrecmt_contents());
			  int cnt = this.re_commentProc.update(map);
			  if(cnt>0) {
				  System.out.println("업데이트 성공");
			  }
			  json.put("res", cnt);
			  return json.toString();
		}else {
			 JSONObject json = new JSONObject();
			 json.put("res", 0);
			 return json.toString();
		}
		
	}
	@PostMapping("/delete")
	@ResponseBody
	public String delete(@RequestBody Share_recommentVO re_commentVO,HttpSession session) {
		if((int)session.getAttribute("acc_no") == re_commentVO.getAcc_no()) {
			System.out.println(" 삭제시 acc_no가 같음");
			JSONObject json = new JSONObject();
			System.out.println("->삭제시 recomment_no:" + re_commentVO.getSrecmt_no());
			
			Share_recommentVO recomment = this.re_commentProc.read_comment(re_commentVO.getSrecmt_no());
			json.put("scmt_no", recomment.getScmt_no());

			int cnt = this.re_commentProc.delete(re_commentVO.getSrecmt_no());
			if(cnt>0) {
				System.out.println("대댓글 삭제 성공");
			}
			json.put("res",cnt);
			return json.toString();

		}else {
			JSONObject json = new JSONObject();
			json.put("res", 0);
			return json.toString();
		}
	}


}
