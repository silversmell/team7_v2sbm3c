package dev.mvc.re_comment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Re_commentVO {
	//대댓글 번호
	private int recomment_no;
	
	// 해당 댓글 번호
	private int scmt_no;
	
	//해당 게시글 번호
	private int scon_no;
	
	//대댓글 내용
	private String re_comment;
	
	//대댓글 날짜
	private String re_comment_date;
	
	//회원 번호
	private int acc_no;
	
	//회원 아이디
	private String acc_id;

}
