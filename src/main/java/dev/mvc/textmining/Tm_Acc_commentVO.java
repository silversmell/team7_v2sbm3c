package dev.mvc.textmining;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ToString
public class Tm_Acc_commentVO {
  
  /** 아이디(이메일) */
  private String acc_id = "";
  
  /** 댓글 번호 */
  private int tcmt_no;
  
  /** 글 번호 */
  private int tcon_no;
  
  /** 회원 번호 */
  private int acc_no;
  
  /** 내용 */
  private String tcmt_contents;
  
  /** 등록일 */
  private String tcmt_date;
}
