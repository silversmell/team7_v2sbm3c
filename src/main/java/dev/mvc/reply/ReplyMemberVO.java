package dev.mvc.reply;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ReplyMemberVO {

	/** 회원 아이디 */
	 private String acc_id = "";
	
	  /** 댓글 번호 */
	  private int scmt_no;

	  /** 관련 글 번호 */
	  private int scon_no;
	  
	  /** 회원 번호 */
	  private int acc_no;
	  
	  /** 내용 */
	  private String scmt_comment;
	  
	  /** 등록일 */
	  private String scmt_date;
	  
}
