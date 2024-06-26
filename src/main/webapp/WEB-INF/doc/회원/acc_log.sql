/**********************************/
/* Table Name: 회원로그 */
/**********************************/

DROP TABLE ACC_LOG CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE ACC_LOG;

CREATE TABLE ACC_LOG(
		ACC_LOG_NO                    		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		NUMBER(10)		 NULL ,
		ACC_LOG_IP                    		VARCHAR2(15)		 NOT NULL,
		ACC_LOG_TIME                  		TIMESTAMP		 NOT NULL,
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
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', CURRENT_TIMESTAMP);
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 2, '127.168.23.1', CURRENT_TIMESTAMP);
INSERT INTO ACC_LOG(acc_log_no, acc_no, acc_log_ip, acc_log_time)
VALUES(ACC_LOG_SEQ.nextval, 1, '127.0.0.1', CURRENT_TIMESTAMP);

commit;

--------------------------------------------------------------------------------

-- 전체 로그 조회
SELECT acc_log_no, acc_no, acc_log_ip, FROM_TZ(acc_log_time, SESSIONTIMEZONE) AS acc_log_time_with_tz 
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

-- 회원 아이디 검색
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('UsEr2') || '%'
ORDER BY l.acc_log_time;

-- 회원 아이피 검색
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

-- 로그 검색 (통합, mybatis)
SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('uSeR2') || '%'
AND l.acc_log_ip LIKE '%' || '127.0.0.1' || '%'
AND l.acc_log_time BETWEEN '2024-06-01' AND '2024-06-05'
ORDER BY l.acc_log_time;

SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('uSeR2') || '%'
AND l.acc_log_ip LIKE '%' || '127.0.0.1' || '%'
AND l.acc_log_time BETWEEN TO_TIMESTAMP('2024-06-01', 'YYYY-MM-DD') 
                      AND TO_TIMESTAMP('2024-06-05', 'YYYY-MM-DD')
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

SELECT COUNT(*) as cnt
FROM acc_log l INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('uSeR2') || '%'
AND l.acc_log_ip LIKE '%' || '127.0.0.1' || '%'
AND l.acc_log_time BETWEEN TO_TIMESTAMP('2024-06-01', 'YYYY-MM-DD') 
                      AND TO_TIMESTAMP('2024-06-05', 'YYYY-MM-DD')
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


-- ----------------------------------------------------------------------------------------------------
-- 검색 + 페이징 + 메인 이미지
-- ----------------------------------------------------------------------------------------------------

-- step 1 검색

SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('uSeR2') || '%'
AND l.acc_log_ip LIKE '%' || '127.0.0.1' || '%'
AND l.acc_log_time BETWEEN TO_TIMESTAMP('2024-06-01', 'YYYY-MM-DD')
AND TO_TIMESTAMP('2024-06-05', 'YYYY-MM-DD')
ORDER BY l.acc_log_time;


-- step 2 서브쿼리를 사용한 ROW_NUMBER() 추가
-- 검색 결과에 ROW_NUMBER()를 사용하여 각 행에 순번을 부여

SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time,
       ROW_NUMBER() OVER (ORDER BY l.acc_log_time) AS r
FROM acc_log l
INNER JOIN account a ON a.acc_no = l.acc_no
WHERE LOWER(a.acc_id) LIKE '%' || LOWER('uSeR2') || '%'
AND l.acc_log_ip LIKE '%' || '127.0.0.1' || '%'
AND l.acc_log_time BETWEEN TO_TIMESTAMP('2024-06-01', 'YYYY-MM-DD')
AND TO_TIMESTAMP('2024-06-05', 'YYYY-MM-DD')
ORDER BY l.acc_log_time;


-- step 3
-- 페이징 조건 추가
-- ROW_NUMBER()를 기반으로 특정 범위의 데이터만 조회하여 페이징을 구현

-- 1 page
SELECT * FROM (
    SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time,
           ROW_NUMBER() OVER (ORDER BY l.acc_log_time) AS r
    FROM acc_log l
    INNER JOIN account a ON a.acc_no = l.acc_no
    WHERE LOWER(a.acc_id) LIKE '%' || LOWER('uSeR2') || '%'
    AND l.acc_log_ip LIKE '%' || '127.0.0.1' || '%'
    AND l.acc_log_time BETWEEN TO_TIMESTAMP('2024-06-01', 'YYYY-MM-DD')
    AND TO_TIMESTAMP('2024-06-05', 'YYYY-MM-DD')
) 
WHERE r BETWEEN 1 AND 3;


-- 2 page
SELECT * FROM (
    SELECT l.acc_log_no, a.acc_no, a.acc_id, l.acc_log_ip, l.acc_log_time,
           ROW_NUMBER() OVER (ORDER BY l.acc_log_time) AS r
    FROM acc_log l
    INNER JOIN account a ON a.acc_no = l.acc_no
    WHERE LOWER(a.acc_id) LIKE '%' || LOWER('uSeR2') || '%'
    AND l.acc_log_ip LIKE '%' || '127.0.0.1' || '%'
    AND l.acc_log_time BETWEEN TO_TIMESTAMP('2024-06-01', 'YYYY-MM-DD')
    AND TO_TIMESTAMP('2024-06-05', 'YYYY-MM-DD')
) 
WHERE r BETWEEN 4 AND 6;






