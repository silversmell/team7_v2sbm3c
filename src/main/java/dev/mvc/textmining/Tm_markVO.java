package dev.mvc.textmining;

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
public class Tm_markVO {
  /** 북마크 번호 */
  private Integer qmark_no;
  /** 회원 번호 */
  private Integer acc_no;
  /** 질문게시글 번호 */
  private Integer qcon_no;
}
