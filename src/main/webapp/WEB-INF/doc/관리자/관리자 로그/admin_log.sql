/**********************************/
/* Table Name: 관리자로그 */
/**********************************/

DROP TABLE ADMIN_LOG CASCADE CONSTRAINTS;
DROP TABLE ADMIN_LOG;

CREATE TABLE ADMIN_LOG(
		ADM_LOG_NO                    		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		ADM_NO                        		NUMBER(10)		 NOT NULL,
		ADM_LOG_IP                    		VARCHAR2(15)		 NOT NULL,
		ADM_LOG_TIME                  		TIMESTAMP(6)		 NOT NULL,
  FOREIGN KEY (ADM_NO) REFERENCES ADMIN (ADM_NO)
);

COMMENT ON TABLE ADMIN_LOG is '관리자로그';
COMMENT ON COLUMN ADMIN_LOG.ADM_LOG_NO is '관리자로그번호';
COMMENT ON COLUMN ADMIN_LOG.ADM_NO is '관리자번호';
COMMENT ON COLUMN ADMIN_LOG.ADM_LOG_IP is '접속아이피';
COMMENT ON COLUMN ADMIN_LOG.ADM_LOG_TIME is '접속시간';


DROP SEQUENCE ADMIN_LOG_SEQ;

CREATE SEQUENCE ADMIN_LOG_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지


--------------------------------------------------------------------------------

-- 로그 저장

INSERT INTO ADMIN_LOG(adm_log_no, adm_no, adm_log_ip, adm_log_time)
VALUES(ADMIN_LOG_SEQ.nextval, 1, '127.0.0.1', CURRENT_TIMESTAMP);
INSERT INTO ADMIN_LOG(adm_log_no, adm_no, adm_log_ip, adm_log_time)
VALUES(ADMIN_LOG_SEQ.nextval, 2, '127.168.23.1', CURRENT_TIMESTAMP);
INSERT INTO ADMIN_LOG(adm_log_no, adm_no, adm_log_ip, adm_log_time)
VALUES(ADMIN_LOG_SEQ.nextval, 1, '127.45.33.11', CURRENT_TIMESTAMP);

--INSERT INTO ADMIN_LOG(adm_log_no, adm_no, adm_log_ip, adm_log_time)
--VALUES(ADMIN_LOG_SEQ.nextval, 2, '127.311.222.331', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));
--INSERT INTO ADMIN_LOG(adm_log_no, adm_no, adm_log_ip, adm_log_time)
--VALUES(ADMIN_LOG_SEQ.nextval, 2, '127.168.23.1', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));
--INSERT INTO ADMIN_LOG(adm_log_no, adm_no, adm_log_ip, adm_log_time)
--VALUES(ADMIN_LOG_SEQ.nextval, 2, '127.45.33.111', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));

commit;

--------------------------------------------------------------------------------

-- 전체 로그 조회
SELECT adm_log_no, adm_no, adm_log_ip, adm_log_time
FROM admin_log
WHERE ROWNUM<=100;


-- 전체 로그 조회(관리자 아이디)
SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
WHERE ROWNUM<=100;

SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
ORDER BY l.adm_log_time;


commit;


--------------------------------------------------------------------------------

-- 검색

-- 관리자 아이디 검색 
SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
WHERE a.adm_id LIKE '%useradmin%'
ORDER BY l.adm_log_time;

SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
WHERE LOWER(a.adm_id) LIKE '%' || LOWER('userADmin') || '%'
ORDER BY l.adm_log_time;

-- 관리자 아이피 검색
SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
WHERE l.adm_log_ip LIKE '127.168.23.1'
ORDER BY l.adm_log_time;

-- 기간 검색
SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
WHERE l.adm_log_time BETWEEN '2024-06-01' AND '2024-06-30'
ORDER BY l.adm_log_time;

-- 로그 검색 (통합, mybatis)
SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
WHERE LOWER(a.adm_id) LIKE '%' || LOWER('userADmin') || '%'
AND l.adm_log_ip LIKE '%' || '127.0.0.1' || '%'
AND l.adm_log_time BETWEEN '2024-06-01' AND '2024-06-05'
ORDER BY l.adm_log_time;

SELECT l.adm_log_no, a.adm_no, a.adm_id, l.adm_log_ip, l.adm_log_time
FROM admin_log l
INNER JOIN admin a ON a.adm_no = l.adm_no
WHERE LOWER(a.adm_id) LIKE '%' || LOWER('userADmin') || '%'
AND l.adm_log_ip LIKE '%' || '127.0.0.1' || '%'
AND l.adm_log_time BETWEEN TO_TIMESTAMP('2024-06-01', 'YYYY-MM-DD') 
                      AND TO_TIMESTAMP('2024-06-05', 'YYYY-MM-DD')
ORDER BY l.adm_log_time;


