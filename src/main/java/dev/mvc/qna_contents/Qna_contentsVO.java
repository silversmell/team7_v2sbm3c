package dev.mvc.qna_contents;

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
  
  /** 질문게시글 번호 */
  private Integer qcon_no;
  
  /** 카테고리 번호 */
  private Integer cate_no;
  
  /** 회원 번호 */
  private Integer acc_no; 
 
  /** 제목 */
  private String qcon_name = "";
  
  /** 내용 */
  private String qcon_contents = "";
  
  /** 조회수 */
  private Integer qcon_views = 0;
  
  /** 북마크 수 */
  private Integer qcon_bookmark = 0;
  
  /** 댓글 수 */
  private Integer qcon_comment = 0;
  
  /** 등록일 */
  private String qcon_date="";
  
  /** 검색어 */
  private String word="";
}
