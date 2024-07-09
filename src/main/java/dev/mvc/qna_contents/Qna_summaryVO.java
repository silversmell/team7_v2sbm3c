package dev.mvc.qna_contents;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE summary(
//    summary_no   NUMBER(8)    NOT NULL PRIMARY KEY,
//    acc_no     NUMBER(10)   NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼
//    article                VARCHAR2(1000) NOT NULL,
//    response        VARCHAR2(800) NOT NULL,
//    sdate                 DATE          NOT NULL, -- 생성일
//    FOREIGN KEY (acc_no) REFERENCES account (acc_no)
//  );

@Getter @Setter
public class Qna_summaryVO {
  /** 요약 번호 */
  public int summary_no;
  
  /** 회원 번호 */
  public int acc_no;
  
  /** 회원 아이디 */
  public String acc_id;
  
  /** 생성어 */
  public String article;
  
  /** 요약된 생성어*/
  public String response;
  
  /** 생성일 */
  public String sdate;
}
