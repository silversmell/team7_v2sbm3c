package dev.mvc.qna_contents;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//CREATE TABLE QNA_CONTENTS(
//    QCON_NO NUMBER(10) NOT NULL PRIMARY KEY,
//    CATE_NO NUMBER(10),     -- FK
//    ACC_NO NUMBER(10),      -- FK
//    QCON_NAME VARCHAR2(100) NOT NULL,
//    QCON_CONTENTS VARCHAR2(3000),
//    QCON_VIEWS NUMBER(7) DEFAULT 0 NOT NULL,
//    QCON_BOOKMARK NUMBER(7) DEFAULT 0 NOT NULL,
//    QCON_COMMENT NUMBER(7) DEFAULT 0 NOT NULL,
//    QCON_DATE DATE NOT NULL,
//        WORD VARCHAR2(30)             NULL,
//  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
//  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
//);

@Getter @Setter
public class Qna_contentsVO {

  /** 질문 게시글 번호 */
  private Integer qcon_no;
  
  /** 카테고리 번호 */
  private Integer cate_no;
  
  /** 회원 번호 */
  private Integer acc_no;
  
  /** 질문 게시글 제목 */
  @NotEmpty(message="질문게시글 제목은 필수입력 항목입니다.")
  @Size(min=1, max=30, message="질문게시글 제목의 입력글자 수는 최소 1자에서 최대 30자(한글 10자) 가능합니다.")
  private String qcon_name="";
  
  /** 질문 게시글 내용 */
  @NotEmpty(message = "질문게시글 내용은 필수입력 항목입니다.")
  private String qcon_contents="";
  
  /** 질문 게시글 조회수 */
  @Min(value=0)
  @Max(value=1000000)
  private Integer qcon_views=0;
  
  /** 질문 게시글 북마크 수 */
  @Min(value=0)
  @Max(value=1000000)
  private Integer qcon_bookmark=0;
  
  /** 질문 게시글 댓글 수 */
  @Min(value=0)
  @Max(value=1000000)
  private Integer qcon_comment=0;
  
  /** 질문 게시글 등록일 */
  private String qcon_date = "";
  
  /** 질문게시글 검색어 */
  private String word="";
}
