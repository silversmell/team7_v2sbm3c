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
        WORD                                VARCHAR2(20)    NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE ACC_LOG is '회원로그';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_NO is '회원로그번호';
COMMENT ON COLUMN ACC_LOG.ACC_NO is '회원번호';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_IP is '접속아이피';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_TIME is '접속시간';
COMMENT ON COLUMN ACC_LOG.WORD is '검색어';

DROP SEQUENCE ACC_LOG_SEQ;

 CREATE SEQUENCE ACC_LOG_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;             

commit;

--------------------------------------------------------------------------------

-- 로그 저장

INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.0.0.1', '2024-06-01');
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.0.0.1', '2024-06-02');
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.0.0.1', '2024-06-04');
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.0.0.1', '2024-06-05');
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.0.0.1', '2024-06-10');
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.0.0.1', '2024-06-12');

INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.168.23.1', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', TO_CHAR(sysdate, 'YYYY/MM/DD HH24:MI:SS'));

commit;

--------------------------------------------------------------------------------

-- 전체 로그 조회
SELECT acc_log_no, acc_no, acc_log_ip, acc_log_time
FROM acc_log
WHERE ROWNUM<=100;

-- 전체 로그 조회(회원 아이디)
SELECT l.acc_log_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE ROWNUM<=100;

SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
ORDER BY l.acc_log_no;

SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
ORDER BY l.acc_log_time;


-- 전체 로그 조회
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
ORDER BY l.acc_log_time;

-- 현재 로그인한 사용자의 로그 기록(나의 로그 기록)
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE a.acc_no=2
ORDER BY l.acc_log_time;


--------------------------------------------------------------------------------

-- 검색

-- 회원 아이디 검색 
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE a.acc_id LIKE '%user2%'
ORDER BY l.acc_log_time;

-- 회원 아이디 검색 (mybatis)
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('UsEr2') || '%'
ORDER BY l.acc_log_time;

-- 회원 아이피 검색(mybatis)
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE l.acc_log_ip LIKE '127.168.23.1'
ORDER BY l.acc_log_time;



-- 기간 검색
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE l.acc_log_time BETWEEN '2024-06-01' AND '2024-06-30'
ORDER BY l.acc_log_time;

-- 특정 회원의 기간 검색
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE a.acc_id LIKE '%user2%' AND l.acc_log_time 
BETWEEN '2024-06-01' AND '2024-06-31'
ORDER BY l.acc_log_time;


--------------------------------------------------------------------------------


-- 검색 레코드 갯수

-- 전체 레코드 갯수, 집계 함수
SELECT COUNT(*) as cnt
FROM acc_log;

--  CNT  <- 컬럼명
-- -----
--    9


-- 회원 아이디로 검색된 레코드 갯수
SELECT COUNT(*) as cnt
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE a.acc_id LIKE '%user2%';

SELECT COUNT(*) as cnt
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('UsEr2') || '%'
ORDER BY l.acc_log_time;





-- SUBSTR(컬럼명, 시작 index(1부터 시작), 길이), 부분 문자열 추출
SELECT contentsno, SUBSTR(title, 1, 4) as title
FROM contents
WHERE cateno=2 AND (content LIKE '%급여%');

-- SQL은 대소문자를 구분하지 않으나 WHERE문에 명시하는 값은 대소문자를 구분하여 검색
SELECT contentsno, title, word
FROM contents
WHERE cateno=1 AND (word LIKE '%FOOD%');

SELECT contentsno, title, word
FROM contents
WHERE cateno=1 AND (word LIKE '%food%'); 

SELECT contentsno, title, word
FROM contents
WHERE cateno=1 AND (LOWER(word) LIKE '%food%'); -- 대소문자를 일치 시켜서 검색

-- ||: 문자열 연결
-- LIKE '%' || UPPER('FOOD') || '%' -> LIKE '%FOOD%'
SELECT contentsno, title, word
FROM contents
WHERE cateno=1 AND (UPPER(word) LIKE '%' || UPPER('FOOD') || '%'); -- 대소문자를 일치 시켜서 검색 ★

SELECT contentsno, title, word
FROM contents
WHERE cateno=1 AND (LOWER(word) LIKE '%' || LOWER('Food') || '%'); -- 대소문자를 일치 시켜서 검색

SELECT contentsno || '. ' || title || ' 태그: ' || word as title -- 컬럼의 결합, ||
FROM contents
WHERE cateno=1 AND (LOWER(word) LIKE '%' || LOWER('Food') || '%'); -- 대소문자를 일치 시켜서 검색


SELECT UPPER('한글') FROM dual; -- dual: 오라클에서 SQL 형식을 맞추기위한 시스템 테이블





