package dev.mvc.qna_contents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//CREATE TABLE QNA_COMMENT(
//    QCMT_NO NUMBER(10) NOT NULL PRIMARY KEY,
//    ACC_NO NUMBER(10),  -- FK
//    QCON_NO NUMBER(10), -- FK
//    QCMT_CONTENTS VARCHAR2(300) NOT NULL,
//    QCMT_DATE DATE NOT NULL,
//  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO),
//  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
//);

@Getter @Setter
public class Qna_commentVO {

  /** 질문게시글 댓글 번호 */
  private Integer qcmt_no;
  
  /** 회원 번호 */
  private Integer acc_no;
  
  /** 질문 게시글 번호 */
  private Integer qcon_no;
  
  /** 질문게시글 댓글 내용 */
  @NotEmpty(message="댓글 내용은 필수입력 항목입니다,")
  @Size(min=1, max=300, message = "최소 1자에서 최대 300자(한글 100)자 가능합니다.")
  private String qcmt_contents="";
  
  /** 질문 게시글 댓글 등록일 */
  private String qcmt_date = "";
}
