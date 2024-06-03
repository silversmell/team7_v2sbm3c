package dev.mvc.reply;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/reply")
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
  @PostMapping(value="/login")
  public String create(@RequestBody Share_commentsVO share_commentVO) {
	  System.out.println(share_commentVO);
	  
	  JSONObject json = new JSONObject();
	  json.put("res", "등록테스트");
	  return json.toString();
  }
}


