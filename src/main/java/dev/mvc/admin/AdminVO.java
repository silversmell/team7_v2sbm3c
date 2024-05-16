package dev.mvc.admin;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE ADMIN(
//ADM_NO NUMBER(10) NOT NULL PRIMARY KEY,
//CATE_NO NUMBER(10), -- FK
//ADM_ID VARCHAR2(30) NOT NULL,
//ADM_PW VARCHAR2(100) NOT NULL,
//ADM_NAME VARCHAR2(10) NOT NULL,
//ADM_DATE DATE NOT NULL,
//ADM_GRADE NUMBER(2) NOT NULL, -- 등급(1~10: 관리자, 11~20: 회원, 40~49: 정지 회원, 99: 탈퇴 회원), 99까지 가능.
//FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO)
//);

@Setter @Getter
public class AdminVO {
  
  /** 관리자 번호 */
  private Integer adm_no;
  
  /** 카테고리 번호 */
  private Integer cate_no;
  
  /** 관리자 아이디 */
  private String adm_id="";
  
  /** 관리자 비밀번호 */
  private String adm_pw="";
  
  /** 관리자 이름 */
  private String adm_name="";
  
  /** 가입일 */
  private String adm_date="";
  
  /** 관리자 등급 */
  private Integer adm_grade=1;
  
  
  
}
