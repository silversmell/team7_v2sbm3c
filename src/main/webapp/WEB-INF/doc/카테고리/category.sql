/**********************************/
/* Table Name: 카테고리 */
/**********************************/
DROP TABLE CATEGORY CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE CATEGORY;

CREATE TABLE CATEGORY(
		CATE_NO NUMBER(10) NOT NULL PRIMARY KEY,
		CATE_NAME VARCHAR2(30) NOT NULL,
		CATE_CNT NUMBER(7) DEFAULT 0 NOT NULL,
		CATE_SEQNO NUMBER(5) NOT NULL,
		CATE_VISIBLE CHAR(1) DEFAULT 'Y' NOT NULL
);

COMMENT ON TABLE CATEGORY is '카테고리';
COMMENT ON COLUMN CATEGORY.CATE_NO is '카테고리 번호';
COMMENT ON COLUMN CATEGORY.CATE_NAME is '카테고리 이름';
COMMENT ON COLUMN CATEGORY.CATE_CNT is '관련 자료수';
COMMENT ON COLUMN CATEGORY.CATE_SEQNO is '출력 순서';
COMMENT ON COLUMN CATEGORY.CATE_VISIBLE is '출력 모드';


DROP SEQUENCE CATEGORY_SEQ;

CREATE SEQUENCE CATEGORY_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지
  

commit;
-- Create, 등록: 1건 이상
INSERT INTO category(cate_no, cate_name, cate_cnt, cate_seqno, cate_visible)
VALUES(category_seq.nextval, '공유 게시글', 1, 1, 'Y');

INSERT INTO category(cate_no, cate_name, cate_cnt, cate_seqno, cate_visible)
VALUES(category_seq.nextval, '질문 게시글', 1, 2, 'Y');

INSERT INTO category(cate_no, cate_name, cate_cnt, cate_seqno, cate_visible)
VALUES(4, '랭킹 게시글', 0, 0, 'Y');

-- List, 목록
SELECT cate_no, cate_name, cate_cnt, cate_seqno, cate_visible
FROM category
ORDER BY cate_no ASC;


-- Read, 조회
SELECT cate_no, cate_name, cate_cnt, cate_seqno, cate_visible
FROM category
WHERE cate_no = 1;


commit;
rollback;
-- 수정
UPDATE category
SET cate_name='나의 데스크 소개하기', cate_seqno = 1, cate_visible='Y'
WHERE cate_no = 1;

 
-- 조회 + 수정폼 + 삭제폼
SELECT cate_no, cate_name, cate_cnt, cate_seqno, cate_visible
FROM category
WHERE cate_no = 1;
 

commit;
rollback;
-- 삭제 처리
DELETE FROM category
WHERE cate_no = 1;


-- 전체 조회
SELECT * FROM category;

 
-- cate_seqno 정렬, ASC: 오름차순, DESC: 내림차순  
-- 출력 순서에따른 전체 목록
SELECT cate_no, cate_name, cate_cnt, cate_seqno, cate_visible
FROM category
ORDER BY cate_seqno ASC;

commit;
rollback;
-- 출력 순서 올림(상향), 10 ─> 1
UPDATE category
SET cate_seqno = cate_seqno - 1
WHERE cate_no = 1;

commit;
rollback;
-- 출력순서 내림(하향), 1 ─> 10
UPDATE category
SET cate_seqno = cate_seqno + 1
WHERE cate_no = 1;


commit;
rollback;
-- 출력 모드의 변경 -> Y
UPDATE category
SET cate_visible='Y'
WHERE cate_no=1;

-- 출력 모드의 변경 -> N
UPDATE category
SET cate_visible='N'
WHERE cate_no=1;

/**
-- 카테고리 관련 자료수 갯수
SELECT 
    CASE 
        WHEN cate_no = 1 THEN (SELECT COUNT(*) FROM share_contents)
        WHEN cate_no = 2 THEN (SELECT COUNT(*) FROM qna_contents)
        ELSE 0 
    END AS cnt
FROM category
WHERE cate_no = 1;

-- 공유글 자료수 갯수
SELECT COUNT(*) FROM share_contents
WHERE cate_no=1;

-- 질문글 자료수 갯수
SELECT COUNT(*) FROM qna_contents
WHERE cate_no=2;
*/

-- 관련 글수 증가
UPDATE category
SET cate_cnt = cate_cnt + 1
WHERE cate_no = 1;

-- 관련 글수 감소
UPDATE category
SET cate_cnt = cate_cnt - 1
WHERE cate_no = 1;

rollback;
commit;