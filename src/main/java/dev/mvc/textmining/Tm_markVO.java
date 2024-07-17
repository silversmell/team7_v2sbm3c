package dev.mvc.textmining;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE TM_MARK(
//    TMARK_NO                      NUMBER(10) NOT NULL PRIMARY KEY,
//    ACC_NO                        NUMBER(10), -- FK
//    TCON_NO                       NUMBER(10), -- FK
//FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
//FOREIGN KEY (TCON_NO) REFERENCES TM_CONTENTS (TCON_NO)
//);

@Getter @Setter
public class Tm_markVO {
  /** 북마크 번호 */
  private Integer tmark_no;
  /** 회원 번호 */
  private Integer acc_no;
  /** 질문게시글 번호 */
  private Integer tcon_no;
}
