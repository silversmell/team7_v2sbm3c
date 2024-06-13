-- 테이블 삭제
DROP TABLE dalle;

-- 테이블 생성
CREATE TABLE dalle(
  dalle_no   NUMBER(8)    NOT NULL PRIMARY KEY,
  acc_no     NUMBER(10)   NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼
  dalle_origin          VARCHAR2(100) NOT NULL,
  dalle_thumb           VARCHAR2(100) NOT NULL,
  dalle_size            INT DEFAULT 0 NOT NULL,
  ddate                 DATE          NOT NULL, -- 생성일
  prompt                VARCHAR2(300) NOT NULL,
  FOREIGN KEY (acc_no) REFERENCES account (acc_no)
);

COMMENT ON TABLE  dalle is '이미지생성AI';
COMMENT ON COLUMN dalle.dalle_no is '달리 번호';
COMMENT ON COLUMN dalle.acc_no is '회원 번호';
COMMENT ON COLUMN dalle.dalle_origin is 'AI이미지원본';
COMMENT ON COLUMN dalle.dalle_thumb is 'AI이미지썸네일';
COMMENT ON COLUMN dalle.dalle_size is 'AI이미지용량';
COMMENT ON COLUMN dalle.ddate is '등록일';
COMMENT ON COLUMN dalle.prompt is '생성어';

DROP SEQUENCE dalle_seq;

CREATE SEQUENCE dalle_seq
  START WITH 1        -- 시작 번호
  INCREMENT BY 1      -- 증가값
  MAXVALUE 99999999   -- 최대값: 99999999 --> NUMBER(8) 대응
  CACHE 2             -- 2번은 메모리에서만 계산
  NOCYCLE;            -- 다시 1부터 생성되는 것을 방지
commit;

INSERT INTO dalle(dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate)
VALUES(dalle_seq.nextval, 3, '3D 이미지로 이쁜 컴퓨터 책상을 보여줘.', '달리.jpg', '달리_t.jgp', 30956, sysdate);

COMMIT;

SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate FROM dalle ORDER BY dalle_no DESC;

-- 조회
SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate
FROM dalle
WHERE dalle_no=1;

-- 2024-06-12일자 채팅만 출력
-- 날짜 형 변환
SELECT ddate, TO_CHAR(ddate), SUBSTR(ddate, 1, 10), SUBSTR(TO_CHAR(ddate), 1, 10)
FROM dalle
WHERE SUBSTR(TO_CHAR(ddate), 1, 10) = '2024-06-12';

-- 2024-06-12일자 채팅만 출력
-- 레코드 출력
SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate
FROM dalle
WHERE SUBSTR(ddate, 1, 10) = '2024-06-12';
-- 레코드 출력과 같음
SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate
FROM dalle
WHERE TO_CHAR(ddate, 'YYYY-MM-DD') = '2024-06-12';
  
-- 2024-06-12, acc_no가 3인 레코드만 출력
SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate
FROM dalle
WHERE acc_no=3 and SUBSTR(ddate, 1, 10) = '2024-06-12';

-- 시분초 일치하지 않음, 조회안됨 X
--SELECT dalle_no, acc_no, dalle_origin, dalle_thumb, dalle_size, ddate
--FROM dalle
--WHERE acc_no=3 and ddate = TO_DATE('2024-06-12', 'YYYY-MM-DD');

-- 문열로 변경하는 가능함
SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate
FROM dalle
WHERE acc_no=3 and TO_CHAR(ddate, 'YYYY-MM-DD') = '2024-06-12';

-- 조인(LIKE) 조회
SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate
FROM dalle
WHERE acc_no=3 and dalle_origin LIKE '%달리%';

-- 원본명 바꾸기
UPDATE dalle
SET dalle_origin='dalle.jpg'
WHERE dalle_no=1;

COMMIT;

SELECT dalle_no, acc_no, prompt, dalle_origin, dalle_thumb, dalle_size, ddate
FROM dalle ORDER BY dalle_no DESC;

-- 삭제
DELETE FROM dalle
WHERE dalle_no=1;

-- 전체 삭제
DELETE FROM dalle;

rollback;
COMMIT;