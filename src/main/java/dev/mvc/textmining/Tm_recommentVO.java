package dev.mvc.textmining;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE QNA_RECOMMENT(
//    QRECMT_NO                         NUMBER(10)     NOT NULL PRIMARY KEY,
//    QCMT_NO                           NUMBER(10)     NULL ,
//    QCON_NO                           NUMBER(10)     NULL ,
//    ACC_NO                            NUMBER(10)     NULL ,
//    QRECMT_CONTENTS                   VARCHAR2(300)    NOT NULL,
//    QRECMT_DATE                       DATE     NOT NULL,
//    FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
//    FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO),
//    FOREIGN KEY (QCMT_NO) REFERENCES QNA_COMMENT (QCMT_NO)
//);

@Getter @Setter
public class Tm_recommentVO {
  
  /** 대댓글 번호 */
  private int qrecmt_no;
  
  /** 질문글 댓글 번호 */
  private int qcmt_no;
  
  /** 질문글 번호 */
  private int qcon_no;
  
  /** 회원 번호 */
  private int acc_no;
  
  /** 질문글 대댓글 내용 */
  private String qrecmt_contents;
  
  /** 질문글 대댓글 생성일 */
  private String qrecmt_date;

  /** 회원 아이디 */
  private String acc_id;
}
