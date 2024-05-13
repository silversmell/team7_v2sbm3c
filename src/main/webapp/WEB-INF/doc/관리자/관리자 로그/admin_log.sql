/**********************************/
/* Table Name: 관리자로그 */
/**********************************/
DROP TABLE ADMIN_LOG;

CREATE TABLE ADMIN_LOG(
		ADMIN_LOG_NO NUMBER(10),
		ADM_NO NUMBER(10), -- FK
		ADMIN_LOG_IP VARCHAR2(15) NOT NULL,
		ADMIN_LOG_DATE DATE NOT NULL,
  FOREIGN KEY (ADM_NO) REFERENCES ADMIN (ADM_NO)
);

COMMENT ON TABLE ADMIN_LOG is '관리자 로그';
COMMENT ON COLUMN ADMIN_LOG.ADMIN_LOG_NO is '관리자로그 번호';
COMMENT ON COLUMN ADMIN_LOG.ADM_NO is '관리자 번호';
COMMENT ON COLUMN ADMIN_LOG.ADMIN_LOG_IP is '접속 아이피';
COMMENT ON COLUMN ADMIN_LOG.ADMIN_LOG_DATE is '접속일';


DROP SEQUENCE ADMIN_LOG_SEQ;

CREATE SEQUENCE ADMIN_LOG_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지
  
commit;
SELECT * FROM admin_log;

-- 등록 (임시)
INSERT INTO admin_log(admin_log_no, adm_no, admin_log_ip, admin_log_date)
VALUES(admin_log_seq.nextval, 3, '123.111.10.142', sysdate);

INSERT INTO admin_log(admin_log_no, adm_no, admin_log_ip, admin_log_date)
VALUES(admin_log_seq.nextval, 4, '123.111.10.142', sysdate);


-- 관리자 로그인한 내역 조회
SELECT admin_log_no, adm_no, admin_log_ip, admin_log_date
FROM admin_log;


-- 특정 관리자 계정/IP가 로그인한 내역 조회
SELECT admin_log_no, adm_no, admin_log_ip, admin_log_date
FROM admin_log
WHERE admin_log_ip='123.111.10.142';