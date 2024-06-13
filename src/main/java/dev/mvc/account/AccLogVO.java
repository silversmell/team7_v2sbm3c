package dev.mvc.account;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원로그 테이블 설계
 * 
CREATE TABLE ACC_LOG(
		ACC_LOG_NO                    		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		NUMBER(10)		 NULL ,
		ACC_LOG_IP                    		VARCHAR2(15)		 NOT NULL,
		ACC_LOG_TIME                  		VARCHAR2(30)		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);
*/

@Getter @Setter
public class AccLogVO {

	/** 회원 로그 번호 */
	private int acc_log_no;
	/** 회원 번호 */
	private int acc_no;
	/** 접속 아이피 */
	private String acc_log_ip = "";
	/** 접속 시간 */
	private String acc_log_time = "";
	
}
