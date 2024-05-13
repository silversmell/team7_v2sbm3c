/**********************************/
/* Table Name: 팁게시글 */
/**********************************/
DROP TABLE TIP_CONTENTS;

CREATE TABLE TIP_CONTENTS(
		TCON_NO NUMBER(10) NOT NULL PRIMARY KEY,
		CATE_NO NUMBER(10), -- FK
		ADM_NO NUMBER(10),  -- FK
		TCON_IMAGE VARCHAR2(100),
		TCON_SAVED_IMAGE VARCHAR2(100),
		TCON_THUMB_IMAGE VARCHAR2(100),
		TCON_IMAGE_SIZE NUMBER(10) DEFAULT 0 NULL,
		YOUTUBE VARCHAR2(1000),
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ADM_NO) REFERENCES ADMIN (ADM_NO)
);

COMMENT ON TABLE TIP_CONTENTS is '팁 게시글';
COMMENT ON COLUMN TIP_CONTENTS.TCON_NO is '팁게시글 번호';
COMMENT ON COLUMN TIP_CONTENTS.CATE_NO is '카테고리 번호';
COMMENT ON COLUMN TIP_CONTENTS.ADM_NO is '관리자 번호';
COMMENT ON COLUMN TIP_CONTENTS.TCON_IMAGE is '이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_SAVED_IMAGE is '저장된 이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_THUMB_IMAGE is 'THUMB 이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_IMAGE_SIZE is '이미지 크기';
COMMENT ON COLUMN TIP_CONTENTS.YOUTUBE is '유튜브 링크';


DROP SEQUENCE TIP_CONTENTS_SEQ;

CREATE SEQUENCE TIP_CONTENTS_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

commit;
SELECT * FROM tip_contents;
-- 등록 화면 유형 1: 커뮤니티(tip)글 등록
INSERT INTO tip_contents(tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size) 
VALUES(tip_contents_seq.nextval, 1, 1, 'desktour.jpg', 'desktour_1.jpg', 'desktour_t.jpg', 1000);

INSERT INTO tip_contents(tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size) 
VALUES(tip_contents_seq.nextval, 2, 1, 'deskitem.jpg', 'deskitem_1.jpg', 'deskitem_t.jpg', 1000);


-- 전체 목록, 내림차순
SELECT tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size
FROM tip_contents
ORDER BY tcon_no DESC;


-- 카테고리별(공유) 목록 등록
INSERT INTO tip_contents(tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size) 
VALUES(tip_contents_seq.nextval, 1, 1, 'desktour.jpg', 'desktour_1.jpg', 'desktour_t.jpg', 1000);

-- 카테고리별(질문) 목록 등록
INSERT INTO tip_contents(tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size) 
VALUES(tip_contents_seq.nextval, 2, 1, 'deskitem.jpg', 'deskitem_1.jpg', 'deskitem_t.jpg', 1000);


COMMIT;


-- tip 게시글 전체 목록, 내림차순
SELECT tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size, youtube
FROM tip_contents
ORDER BY tcon_no DESC;

-- 1번 cate_no만 출력 = 공유 글만 출력, 내림차순
SELECT tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size, youtube
FROM tip_contents
WHERE cate_no=1
ORDER BY tcon_no DESC;

-- 2번 cateno만 출력 = 질문 글만 출력, 내림차순
SELECT tcon_no, cate_no, adm_no, tcon_image, tcon_saved_image, tcon_thumb_image, tcon_image_size, youtube
FROM tip_contents
WHERE cate_no=2
ORDER BY tcon_no DESC;


commit;


-- 모든 레코드 삭제
DELETE FROM tip_contents;
rollback;

-- 특정 tip 게시글 삭제
DELETE FROM tip_contents
WHERE tcon_no = 1;

rollback;

-- 카테고리가 공유 게시글인 tip 게시글 삭제
DELETE FROM tip_contents
WHERE cate_no=1 AND tcon_no <= 99;

-- 카테고리가 질문 게시글인 tip 게시글 삭제
DELETE FROM tip_contents
WHERE cate_no=2 AND tcon_no <= 99;

commit;

-- ----------------------------------------------------------------------------
-- Youtube, 먼저 레코드가 등록되어 있어야함.
-- youtube                                   VARCHAR2(1000)         NULL ,
-- ----------------------------------------------------------------------------
-- youtube 등록/수정
UPDATE tip_contents SET youtube='Youtube 스크립트' WHERE tcon_no=1;

-- youtube 삭제
UPDATE tip_contents SET youtube='' WHERE tcon_no=1;

commit;

-- 패스워드 검사, id="password_check"
SELECT COUNT(*) as cnt 
FROM tip_contents
WHERE tcon_no=1 AND passwd='1234';