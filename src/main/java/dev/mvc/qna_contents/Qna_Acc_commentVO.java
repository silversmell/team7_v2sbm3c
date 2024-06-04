package dev.mvc.qna_contents;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ToString
public class Qna_Acc_commentVO {
  
  /** 아이디(이메일) */
  private String id = "";
  
  /** 댓글 번호 */
  private int qcmt_no;
  
  /** 글 번호 */
  private int qcon_no;
  
  /** 회원 번호 */
  private int acc_no;
  
  /** 내용 */
  private String qcmt_contents;
  
  /** 등록일 */
  private String qcmt_date;
}
