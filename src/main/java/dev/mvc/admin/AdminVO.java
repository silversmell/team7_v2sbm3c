package dev.mvc.admin;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE ADMIN(
//    ADM_NO NUMBER(10) NOT NULL PRIMARY KEY,
//        CATE_NO NUMBER(10), -- FK
//        ACC_NO NUMBER(20), -- FK  
//  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
//  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
//);

@Getter @Setter
public class AdminVO {

  /** 관리자 번호 **/
  private int adm_no;
  
  /** 카테고리 번호 **/
  private int cate_no;
  
  /** 회원 번호 **/
  private int  acc_no;
}
