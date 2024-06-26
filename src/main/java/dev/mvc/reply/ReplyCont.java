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

import dev.mvc.account.AccountProcInter;
import dev.mvc.re_comment.Re_commentProcInter;
import jakarta.servlet.http.HttpSession;


@RequestMapping("/reply") //
@Controller
public class ReplyCont {
  public ReplyCont() {
    System.out.println("-> ReplyCont created.");  
  }
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;
	
  
  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc")
  private ReplyProcInter replyProc;
  
  @Autowired
  @Qualifier("dev.mvc.re_comment.Re_commentProc")
  private Re_commentProcInter re_commentProc;
  
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
		  System.out.println("등록 성공");
	  }
	  else {
		  //System.out.println("등록 실패");
	  }
	  
	  JSONObject json = new JSONObject();	  
	  json.put("res", cnt);
	  return json.toString();
  }
  
  @GetMapping("/list_by_contentsno_join") //http://localhost:9093/reply/list_by_contentsno_join?scon_no=5&acc_no=1
  @ResponseBody
  public String list_by_contentsno_join(int scon_no) {
	  HashMap<String,Object> map = new HashMap<>();
	  map.put("scon_no",scon_no);
	  
	  //System.out.println("-> scon_no:" + scon_no);
	  List<ReplyMemberVO> list=this.replyProc.list_by_contentsno_join_500(map);
	  JSONObject json = new JSONObject();	  
	  json.put("res", list);
	  return json.toString();
  }
  
  /**
   * 댓글 목록 작성순
   * @param scon_no
   * @return
   */
  @GetMapping(value="/asc_list_by_smt_no_join")
  @ResponseBody
  public String asc_list_by_smt_no_join(int scon_no) {
    List<ReplyMemberVO> list=this.replyProc.asc_list_by_smt_no_join_500(scon_no);
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
	  //System.out.println("들어옴");
	  if((int)session.getAttribute("acc_no")==share_commentVO.getacc_no()) {
		 System.out.println("acc_no가 같음");
		  JSONObject json = new JSONObject();
		  System.out.println("scmt_no의 번호: " +share_commentVO.getScmt_no());
		 int cnt1 = this.re_commentProc.scmtno_delete(share_commentVO.getScmt_no());
		 if(cnt1>0) {
			 System.out.println("대댓글 삭제 성공");
		 }
		  int cnt = this.replyProc.delete_scmtno(share_commentVO.getScmt_no());
		  if(cnt>0) {
			  System.out.println("삭제 성공");
		  }
//		  int cnt1 =this.replyProc.like_delete(share_commentVO.getScmt_no());
//		  if(cnt1>0) {
//			  System.out.println("좋아요 삭제 성공");
//		  }
		  json.put("res", cnt);
		  //System.out.println("댓글 삭제");
		  return json.toString();
	  }
	  JSONObject json = new JSONObject();
	  json.put("res", 0);
	  return json.toString();
  }
  
  @PostMapping("/like")
  @ResponseBody
  public String like(@RequestBody Comment_likeVO comment_likeVO ) {

	  JSONObject json = new JSONObject();
	  System.out.println("->comment_likeVO.ACCNO:" + comment_likeVO.getAcc_no());
	  System.out.println("->comment_likeVO.SCMT_NO:" + comment_likeVO.getScmt_no());
	 
	  HashMap<String,Object> map = new HashMap<>();
	  map.put("acc_no", comment_likeVO.getAcc_no());
	  map.put("scmt_no", comment_likeVO.getScmt_no());
	  map.put("scon_no", comment_likeVO.getScon_no());
	  
	  int cnt = this.replyProc.like(map);

//	  if(cnt>0) {
//		  System.out.println("좋아요 성공");
//	  }
	  
	  json.put("cnt", cnt);
	  return json.toString();

  }
  
  @PostMapping("/like_cancel")
  @ResponseBody
  public String like_cancel(@RequestBody Comment_likeVO comment_likeVO) {
	  JSONObject json = new JSONObject();
	  System.out.println("->comment_likeVO.ACCNO:" + comment_likeVO.getAcc_no());
	  System.out.println("->comment_likeVO.SCMT_NO:" + comment_likeVO.getScmt_no());
	  
	  HashMap<String,Object> map = new HashMap<>();
	  map.put("acc_no", comment_likeVO.getAcc_no());
	  map.put("scmt_no", comment_likeVO.getScmt_no());
	  map.put("scon_no", comment_likeVO.getScon_no());
	  
	  int cnt = this.replyProc.like_cancel(map);
	  if(cnt>0) {
		  System.out.println("좋아요 취소 성공");
	  }
	  json.put("cnt", cnt);
	  
	  return json.toString();
  }
  
  
}


