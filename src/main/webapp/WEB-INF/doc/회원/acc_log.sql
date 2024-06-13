/**********************************/
/* Table Name: 회원로그 */
/**********************************/

DROP TABLE ACC_LOG CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE ACC_LOG;

CREATE TABLE ACC_LOG(
		ACC_LOG_NO                    		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		NUMBER(10)		 NULL ,
		ACC_LOG_IP                    		VARCHAR2(15)		 NOT NULL,
		ACC_LOG_TIME                  		VARCHAR2(30)		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE ACC_LOG is '회원로그';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_NO is '회원로그번호';
COMMENT ON COLUMN ACC_LOG.ACC_NO is '회원번호';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_IP is '접속아이피';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_TIME is '접속시간';

DROP SEQUENCE ACC_LOG_SEQ;

 CREATE SEQUENCE ACC_LOG_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;             


--------------------------------------------------------------------------------


-- 로그 저장

INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.168.23.1', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));


-- 전체 로그 조회
SELECT acc_log_no, acc_no, acc_log_ip, acc_log_time
FROM acc_log
WHERE ROWNUM<=100;


-- 전체 로그 조회(회원 아이디)
SELECT l.acc_log_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE ROWNUM<=100;


SELECT h.tag_name
FROM recommend r
INNER JOIN hashtag h ON h.tag_no = r.tag_no
WHERE r.acc_no = 4;

commit;

