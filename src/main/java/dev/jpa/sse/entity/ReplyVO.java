package dev.jpa.sse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="share_comment")
public class ReplyVO {

	@Id
	@Column(name="scmt_no")
	  private int scmtno;
	
	/** 회원 아이디 */
	@Column(name="acc_id")
	 private String accid;
	
	  /** 관련 글 번호 */
	@Column(name="scon_no")
	  private int sconno;
	  
	  /** 회원 번호 */
	@Column(name="acc_no")
	  private int accno;
	  
	  /** 내용 */
	@Column(name="scmt_comment")
	  private String scmtcomment;
//	  
//	  /** 등록일 */
//	  private String scmt_date;
	  
}