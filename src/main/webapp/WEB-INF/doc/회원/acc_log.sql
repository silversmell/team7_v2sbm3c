/**********************************/
/* Table Name: 회원로그 */
/**********************************/

DROP TABLE ACC_LOG CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE ACC_LOG;

CREATE TABLE ACC_LOG(
		ACC_LOG_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		ACC_NO NUMERIC(10),
		ACC_LOG_IP VARCHAR(15) NOT NULL,
		ACC_LOG_DATE DATE NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);


COMMENT ON TABLE ACC_LOG is '회원로그';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_NO is '회원로그번호';
COMMENT ON COLUMN ACC_LOG.ACC_NO is '회원번호';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_IP is '접속아이피';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_DATE is '접속일';


DROP SEQUENCE ACC_LOG_SEQ;

 CREATE SEQUENCE ACC_LOG_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;             


--------------------------------------------------------------------------------


-- 로그 저장

INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_date)
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', '24/06/01');
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_date)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.168.23.1', '24/06/01');
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_date)
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', sysdate);


-- 전체 로그 조회




