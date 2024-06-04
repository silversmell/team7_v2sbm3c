package dev.mvc.reply;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;


@RequestMapping("/reply") //
@Controller
public class ReplyCont {
  public ReplyCont() {
    System.out.println("-> ReplyCont created.");  
  }
  
  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc")
  private ReplyProcInter replyProc;
  
  //댓글 등록은 부모글 조회에서 진행하므로 구현 안함.
  
  //부모글 조회 -> Ajax -> json -> POST
  @PostMapping(value="/create")
  @ResponseBody
  public String create(@RequestBody Share_commentVO share_commentVO,HttpSession session) {
	  //System.out.println("->수신 데이터:"+ share_commentVO.toString());
	  //System.out.println("-> acc_no:" +session.getAttribute("acc_no"));
	  int acc_no = (int)session.getAttribute("acc_no");
	 // System.out.println("-> acc_no:"  + (int)session.getAttribute("acc_no"));
	  share_commentVO.setacc_no(acc_no);
	  
	  HashMap<String,Object> map = new HashMap<>();
	  map.put("acc_no", acc_no);
	  map.put("scon_no", share_commentVO.getScon_no());
	  map.put("scmt_comment", share_commentVO.getScmt_comment());
	  
	  
	  int cnt = this.replyProc.create_comment(map);
	  if(cnt>0) {
		  //System.out.println("등록 성공");
	  }
	  else {
		  //System.out.println("등록 실패");
	  }
	  
	  JSONObject json = new JSONObject();	  
	  json.put("res", cnt);
	  return json.toString();
  }
  
  @GetMapping("/list_by_contentsno_join") //http://localhost:9093/reply/list_by_contentsno_join?scon_no=5
  @ResponseBody
  public String list_by_contentsno_join(int scon_no) {
	  //System.out.println("-> scon_no:" + scon_no);
	  List<ReplyMemberVO> list=this.replyProc.list_by_contentsno_join_500(scon_no);
	  JSONObject json = new JSONObject();	  
	  json.put("res", list);
	  
	  return json.toString();
  }
  
  @GetMapping("/read")
  @ResponseBody
  public String read(int scmt_no) {
	  System.out.println("->scmt_no:" + scmt_no);
	  Share_commentVO share_commentVO = this.replyProc.read(scmt_no);
	  
	  JSONObject row = new JSONObject();
	  
	  row.put("scmt_no", share_commentVO.getScmt_no());
	  row.put("scon_no", share_commentVO.getScon_no());
	  row.put("acc_no", share_commentVO.getacc_no());
	  row.put("scmt_comment", share_commentVO.getScmt_comment());
	  row.put("scmt_date",share_commentVO.getScmt_no());
	  
	  JSONObject json = new JSONObject();
	  json.put("res",row);
	  
	  return json.toString();
  }
  
  @PostMapping("/update")
  @ResponseBody
  public String update(@RequestBody Share_commentVO share_commentVO,HttpSession session) {
	  if(share_commentVO.getacc_no() == (int)session.getAttribute("acc_no")) {
		  //System.out.println("회원이 같음");
		 // System.out.println("->shar_comment scmt_no:" + share_commentVO.getScmt_no());
		  JSONObject json = new JSONObject();
		  HashMap<String,Object> map = new HashMap<>();
		  map.put("scmt_comment", share_commentVO.getScmt_comment());
		  map.put("scmt_no", share_commentVO.getScmt_no());
		  int cnt = this.replyProc.update_comment(map);
		  if(cnt>0) {
			  System.out.println("업데이트 성공");
		  }
		  json.put("res", cnt);
		  return json.toString();
	  }
		  JSONObject json = new JSONObject();
		  json.put("res",0);
		  return json.toString();
	  
  }
  @PostMapping("/delete")
  @ResponseBody
  public String delete(@RequestBody Share_commentVO share_commentVO,HttpSession session) {
	  if((int)session.getAttribute("acc_no")==share_commentVO.getacc_no()) {
		  JSONObject json = new JSONObject();
		  int cnt = this.replyProc.delete_scmtno(share_commentVO.getScmt_no());
		  json.put("res", cnt);
		  return json.toString();
	  }
	  JSONObject json = new JSONObject();
	  json.put("res", 0);
	  return json.toString();
  }
  
}


