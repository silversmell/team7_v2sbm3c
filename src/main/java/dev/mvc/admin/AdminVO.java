package dev.mvc.admin;

import lombok.Getter;
import lombok.Setter;

/**
 *  관리자 테이블 설계
 *  
CREATE TABLE ADMIN(
		ADM_NO                        		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		NUMBER(10)		 NULL ,
		ADM_ID                        		VARCHAR2(20)		 NOT NULL,
		ADM_PW                        		VARCHAR2(100)		 NOT NULL,
		ADM_NAME                      		VARCHAR2(20)		 NOT NULL,
		ADM_EMAIL                     		VARCHAR2(60)		 NOT NULL,
		ADM_TEL                       		VARCHAR2(14)		 NOT NULL,
		ADM_DATE                      		DATE		 NOT NULL,
		ADM_GRADE                     		NUMBER(2)		 NOT NULL,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO)
);
 */

@Getter @Setter
public class AdminVO {
	/** 관리자 번호 */
	private int adm_no;
	/** 카테고리 번호 */
	private int cate_no;
	/** 아이디 */
	private String adm_id = "";
	/** 패스워드 */
	private String adm_pw = "";
	/** 이름 */
	private String adm_name = "";
	/** 이메일 */
	private String adm_email = "";
	/** 전화번호 */
	private String adm_tel = "";
	/** 가입일 */
	private String adm_date = "";
	/** 등급 */
	private int adm_grade = 0;
  
}
