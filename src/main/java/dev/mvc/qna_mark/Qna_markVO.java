package dev.mvc.qna_mark;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE QNA_MARK(
//    QMARK_NO                      NUMBER(10) NOT NULL PRIMARY KEY,
//    ACC_NO                        NUMBER(10) NULL ,
//    QCON_NO                       NUMBER(10) NULL ,
//FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
//FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO)
//);

@Getter @Setter
public class Qna_markVO {
  /** 북마크 번호 */
  private int qmark_no;
  /** 회원 번호 */
  private int acc_no;
  /** 질문게시글 번호 */
  private int qcon_no;
}
