package dev.mvc.re_comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Share_recommentVO {
	//대댓글 번호
	private int srecmt_no ;
	
	// 해당 댓글 번호
	private int scmt_no;
	
	//해당 게시글 번호
	private int scon_no;
	
	//회원 번호
	private int acc_no;
	
	//회원 아이디
	private String acc_id;
	
	//대댓글 내용
	private String srecmt_contents;
	
	//대댓글 날짜
	private String srecmt_date;
	

}
