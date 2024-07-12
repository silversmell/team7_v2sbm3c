/**********************************/
/* Table Name: 팁게시글 */
/**********************************/

DROP TABLE TIP_CONTENTS CASCADE CONSTRAINTS;
DROP TABLE TIP_CONTENTS;

CREATE TABLE TIP_CONTENTS(
		TCON_NO                       	    NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		NUMBER(10)		 NOT NULL,
		ACC_NO                        		NUMBER(10)		 NOT NULL,
		TCON_TITLE                     		VARCHAR2(100)		 NOT NULL,
		TCON_CONTENTS                 		VARCHAR2(3000)		 NOT NULL,
		TCON_VIEWS                    		NUMBER(7)		 DEFAULT 0		 NOT NULL,
		TCON_DATE                     		DATE		 NOT NULL,
		TCON_IMG                      		VARCHAR2(100)		 NULL ,
		TCON_SAVED_IMG                		VARCHAR2(100)		 NULL ,
		TCON_THUMB_IMG                		VARCHAR2(100)		 NULL ,
		TCON_IMG_SIZE                 		NUMBER(38)		 NULL ,
        TCON_PASSWD                         VARCHAR2(30)    NOT NULL,
        WORD                                VARCHAR2(30)    NULL,
		YOUTUBE                       		VARCHAR2(1000)		 NULL ,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE TIP_CONTENTS is '팁게시글';
COMMENT ON COLUMN TIP_CONTENTS.TCON_NO is '팁게시글번호';
COMMENT ON COLUMN TIP_CONTENTS.CATE_NO is '카테고리번호';
COMMENT ON COLUMN TIP_CONTENTS.ACC_NO is '회원번호';
COMMENT ON COLUMN TIP_CONTENTS.TCON_TITLE is '제목';
COMMENT ON COLUMN TIP_CONTENTS.TCON_CONTENTS is '내용';
COMMENT ON COLUMN TIP_CONTENTS.TCON_VIEWS is '조회수';
COMMENT ON COLUMN TIP_CONTENTS.TCON_DATE is '등록일';
COMMENT ON COLUMN TIP_CONTENTS.TCON_IMG is '이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_SAVED_IMG is '저장된이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_THUMB_IMG is 'THUMB이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_IMG_SIZE is '이미지크기';
COMMENT ON COLUMN TIP_CONTENTS.TCON_PASSWD is '비밀번호';
COMMENT ON COLUMN TIP_CONTENTS.WORD is '검색어';
COMMENT ON COLUMN TIP_CONTENTS.YOUTUBE is '유튜브링크';

--------------------------------------------------------------------------------

DROP SEQUENCE TIP_CONTENTS_SEQ;

CREATE SEQUENCE TIP_CONTENTS_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

COMMIT;
--------------------------------------------------------------------------------

-- 등록
INSERT INTO TIP_CONTENTS(tcon_no, cate_no, acc_no, tcon_title, tcon_contents, tcon_views, tcon_date, tcon_img, tcon_saved_img, tcon_thumb_img, tcon_img_size, tcon_passwd, word)
VALUES(TIP_CONTENTS_SEQ.nextval, 3, 2, '제목1', '내용1', 0, sysdate, '이미지', '저장된 이미지', 'thumb 이미지', 1000, '1111', '검색어1, 검색어2');

INSERT INTO TIP_CONTENTS(tcon_no, cate_no, acc_no, tcon_title, tcon_contents, tcon_views, tcon_date, tcon_img, tcon_saved_img, tcon_thumb_img, tcon_img_size, tcon_passwd, word)
VALUES(TIP_CONTENTS_SEQ.nextval, 3, 2, '제목2', '내용2', 0, sysdate, '이미지', '저장된 이미지', 'thumb 이미지', 1000, '1111', '검색어1, 검색어2');

COMMIT;

-- 조회
SELECT tcon_no, cate_no, acc_no, tcon_title, tcon_contents, tcon_views, tcon_date
FROM tip_contents
ORDER BY tcon_no;

SELECT tcon_no, cate_no, acc_no, tcon_title, tcon_contents, tcon_views, tcon_date, tcon_img, tcon_saved_img, tcon_thumb_img, tcon_img_size, tcon_passwd, word
FROM tip_contents
ORDER BY tcon_no;

-- 팁 게시글 이미지 가져오기
SELECT tcon_no, tcon_img, tcon_saved_img, tcon_thumb_img, tcon_img_size, tcon_date
FROM tip_contents
ORDER BY tcon_date ASC

SELECT tcon_no, tcon_img, tcon_saved_img, tcon_thumb_img, tcon_img_size, tcon_date
FROM tip_contents
WHERE tcon_no = 2
ORDER BY tcon_date ASC


-- 삭제
DELETE FROM tip_contents;









