package dev.mvc.admin;

import lombok.Getter;
import lombok.Setter;

/**
 * 관리자로그 테이블 설계
 * 
CREATE TABLE ADMIN_LOG(
		ADM_LOG_NO                    		NUMBER(10)		 NULL ,
		ADM_NO                        		NUMBER(10)		 NULL ,
		ADM_LOG_IP                    		VARCHAR2(15)		 NOT NULL,
		ADM_LOG_TIME                  		VARCHAR2(30)		 NOT NULL,
  FOREIGN KEY (ADM_NO) REFERENCES ADMIN (ADM_NO)
);
*/

@Getter @Setter
public class AdminLogVO {

	/** 관리자 로그 번호 */
	private int adm_log_no;
	/** 관리자 번호 */
	private int adm_no;
	/** 접속 아이피 */
	private String adm_log_ip = "";
	/** 접속 시간 */
	private String adm_log_time = "";
}
