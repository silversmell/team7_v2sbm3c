package dev.mvc.qna_contents;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE dalle(
//    dalle_no   NUMBER(8)    NOT NULL PRIMARY KEY,
//    acc_no     NUMBER(10)   NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼
//    dalle_origin          VARCHAR2(100) NOT NULL,
//    dalle_thumb           VARCHAR2(100) NOT NULL,
//    dalle_size            INT DEFAULT 0 NOT NULL,
//    ddate                 DATE          NOT NULL, -- 생성일
//    prompt                VARCHAR2(300) NOT NULL,
//    FOREIGN KEY (acc_no) REFERENCES account (acc_no)
//  );
@Getter @Setter
public class Qna_dalleVO {
  
  /* 달리 번호 */
  private int dalle_no;
  
  /* 회원 번호 */
  private int acc_no;
  
  /* AI 이미지 원본 */
  private String dalle_origin;
  
  /* AI 이미지 썸네일 */
  private String dalle_thumb;
  
  /* AI 이미지 용량 */
  private int dalle_size;
  
  /* 생성일 */
  private String ddate;
  
  /* 생성어 */
  private String prompt;
  
}
