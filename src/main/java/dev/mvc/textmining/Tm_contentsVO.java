package dev.mvc.textmining;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import groovy.transform.ToString;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//CREATE TABLE TM_CONTENTS(
//    TCON_NO NUMBER(10) NOT NULL PRIMARY KEY,
//    CATE_NO NUMBER(10),     -- FK
//    ACC_NO NUMBER(10),      -- FK
//    TCON_NAME VARCHAR2(100) NOT NULL,
//    TCON_CONTENTS VARCHAR2(3000),
//    TCON_VIEWS NUMBER(7) DEFAULT 0 NOT NULL,
//    TCON_BOOKCNT NUMBER(7) DEFAULT 0 NOT NULL,
//        TCON_BOOKMARK CHAR(1) DEFAULT 'N' NOT NULL,
//    TCON_COMMENT NUMBER(7) DEFAULT 0 NOT NULL,
//    TCON_DATE DATE NOT NULL,
//        WORD VARCHAR2(30)             NULL,
//  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
//  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
//);

@Getter @Setter @ToString
public class Tm_contentsVO {

  /** 질문 게시글 번호 */
  private Integer tcon_no;
  
  /** 카테고리 번호 */
  private Integer cate_no;
  
  /** 회원 번호 */
  private Integer acc_no;
  
  /** 질문 게시글 제목 */
  @NotEmpty(message="텍스트마이닝 제목은 필수입력 항목입니다.")
  @Size(min=1, max=100, message="텍스트마이닝 제목의 입력글자 수는 최소 1자에서 최대 30자(한글 10자) 가능합니다.")
  private String tcon_name="";
  
  /** 질문 게시글 내용 */
  @NotEmpty(message = "텍스트마이닝 내용은 필수입력 항목입니다.")
  @Size(min=1, max=3000, message="텍스트마이닝 내용의 입력글자 수는 최소 1자에서 최대 3000자(한글 1000자) 가능합니다.")
  private String tcon_contents="";
  
  /** 질문 게시글 조회수 */
  private Integer tcon_views=0;
  
  /** 질문 게시글 북마크 수 */
  private Integer tcon_bookcnt;
  
  /** 질문 게시글 댓글 수 */
  private Integer tcon_comment;
  
  /** 질문 게시글 북마크 */
  private String tcon_bookmark="N";
  
  /** 질문 게시글 등록일 */
  private String tcon_date = "";
  
  /** 질문게시글 검색어 */
  private String word="";

  
  
}
