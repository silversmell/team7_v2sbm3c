/**********************************/
/* Table Name: 질문게시글 */
/**********************************/
DROP TABLE QNA_CONTENTS CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE QNA_CONTENTS;

CREATE TABLE QNA_CONTENTS(
		QCON_NO NUMBER(10) NOT NULL PRIMARY KEY,
		CATE_NO NUMBER(10),     -- FK
		ACC_NO NUMBER(10),      -- FK
		QCON_NAME VARCHAR2(100) NOT NULL,
		QCON_CONTENTS VARCHAR2(3000),
		QCON_VIEWS NUMBER(7) DEFAULT 0 NOT NULL,
		QCON_BOOKMARK NUMBER(7) DEFAULT 0 NOT NULL,
		QCON_COMMENT NUMBER(7) DEFAULT 0 NOT NULL,
		QCON_DATE DATE NOT NULL,
        WORD VARCHAR2(30)             NULL,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE QNA_CONTENTS is '질문게시글';
COMMENT ON COLUMN QNA_CONTENTS.QCON_NO is '질문게시글 번호';
COMMENT ON COLUMN QNA_CONTENTS.CATE_NO is '카테고리 번호';
COMMENT ON COLUMN QNA_CONTENTS.ACC_NO is '회원 번호';
COMMENT ON COLUMN QNA_CONTENTS.QCON_NAME is '제목';
COMMENT ON COLUMN QNA_CONTENTS.QCON_CONTENTS is '내용';
COMMENT ON COLUMN QNA_CONTENTS.QCON_VIEWS is '조회수';
COMMENT ON COLUMN QNA_CONTENTS.QCON_BOOKMARK is '북마크수';
COMMENT ON COLUMN QNA_CONTENTS.QCON_COMMENT is '댓글수';
COMMENT ON COLUMN QNA_CONTENTS.QCON_DATE is '등록일';
COMMENT ON COLUMN QNA_CONTENTS.WORD is '검색어';


DROP SEQUENCE QNA_CONTENTS_SEQ;

CREATE SEQUENCE QNA_CONTENTS_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지
  
commit;
SELECT * FROM qna_contents;
-- Create, 등록: 1건 이상
INSERT INTO QNA_CONTENTS(qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date, 
                        word)
VALUES(qna_contents_seq.nextval, 2, 1, '찾아주세요!', '사진 속 물건과 비슷한게 있을까요?', 0, 0, 0, sysdate, '질문');

INSERT INTO QNA_CONTENTS(qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date, word)
VALUES(qna_contents_seq.nextval, 2, 2, '가성비', '화이트 데스크테리어에 어울릴만한 가성비 아이템 추천해주세요.', 0, 0, 0, sysdate, '화이트');

INSERT INTO QNA_CONTENTS(qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date, word)
VALUES(qna_contents_seq.nextval, 2, 3, '대신 골라주삼', '내 책상과 어울릴만한 아이템 하나 골라주라', 0, 0, 0, sysdate, '추천');


-- 전체 목록
SELECT qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date
FROM qna_contents
ORDER BY qcon_no desc;

-- 특정 회원이 작성한 게시글 목록 조회, 최신순
SELECT qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date
FROM qna_contents
WHERE acc_no=1
ORDER BY sysdate desc;

-- 특정 회원이 작성한 게시글 목록 조회, 등록순
SELECT qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date
FROM qna_contents
WHERE acc_no=1
ORDER BY sysdate asc;


rollback;
-- 모든 글 삭제
DELETE FROM qna_contents;

-- 특정 글 삭제
DELETE FROM qna_contents
WHERE qcon_no = 1;

-- 질문 카테고리의 글 삭제
DELETE FROM qna_contents
WHERE cate_no=2 AND qcon_no <= 99;

-- 검색
-- word 컬럼의 존재 이유: 검색 정확도를 높이기 위하여 중요 단어를 명시
-- 글에 'swiss'라는 단어만 등장하면 한글로 '스위스'는 검색 안됨.
-- 이런 문제를 방지하기위해 'swiss,스위스,스의스,수의스,유럽' 검색어가 들어간 word 컬럼을 추가함.
SELECT qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date, word
FROM qna_contents
WHERE cate_no=2 AND word LIKE '%질문%'
ORDER BY qcon_no DESC;

-- QCON_NAME, qcon_contents, word column search(키워드: 가성비)
SELECT qcon_no, cate_no, acc_no, qcon_name, qcon_contents, qcon_views, qcon_bookmark, qcon_comment, qcon_date, word
FROM qna_contents
WHERE cate_no=2 AND (qcon_name LIKE '%가성비%' OR qcon_contents LIKE '%가성비%' OR word LIKE '%가성비%')
ORDER BY qcon_no DESC;


-----------------------------------------------------------
-- FK cate_no 컬럼에 대응하는 필수 SQL
-----------------------------------------------------------
-- 특정 카테고리에 속한 레코드 갯수를 리턴
SELECT COUNT(*) as cnt 
FROM qna_contents 
WHERE cate_no=1;
  
-- 질문 카테고리에 속한 모든 레코드 삭제
DELETE FROM qna_contents
WHERE cate_no=2;

-----------------------------------------------------------
-- FK acc_no 컬럼에 대응하는 필수 SQL
-----------------------------------------------------------
-- 특정 회원에 속한 레코드 갯수를 리턴
SELECT COUNT(*) as cnt 
FROM qna_contents 
WHERE acc_no=1;
  
-- 특정 회원에 속한 모든 레코드 삭제
DELETE FROM qna_contents
WHERE acc_no=1;