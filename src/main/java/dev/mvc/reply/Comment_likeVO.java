package dev.mvc.reply;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Comment_likeVO {
	// 좋아요 번호
	private int like_no;
	
	// 댓글번호
	private int scmt_no;
	
	//회원번호
	private int acc_no;
	
	// 좋아요 마크
	private char mark='N';
	
	private String scon_no;

}
