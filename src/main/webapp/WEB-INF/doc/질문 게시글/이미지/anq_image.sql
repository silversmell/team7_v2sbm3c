/**********************************/
/* Table Name: 질문이미지 */
/**********************************/
DROP TABLE QNA_IMAGE;

CREATE TABLE QNA_IMAGE(
		FILE_NO NUMBER(10) NOT NULL PRIMARY KEY,
		QCON_NO NUMBER(10), -- FK
		FILE_ORIGIN_NAME VARCHAR2(100) NOT NULL,
		FILE_UPLOAD_NAME VARCHAR2(100) NOT NULL,
		FILE_THUMB_NAME VARCHAR2(100) NOT NULL,
		FILE_SIZE INT DEFAULT 0 NOT NULL,
		FILE_DATE DATE NOT NULL,
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO)
);

COMMENT ON TABLE QNA_IMAGE is '질문 이미지';
COMMENT ON COLUMN QNA_IMAGE.FILE_NO is '첨부파일 번호';
COMMENT ON COLUMN QNA_IMAGE.QCON_NO is '질문게시글 번호';
COMMENT ON COLUMN QNA_IMAGE.FILE_ORIGIN_NAME is '원본 파일명';
COMMENT ON COLUMN QNA_IMAGE.FILE_UPLOAD_NAME is '업로드 파일명';
COMMENT ON COLUMN QNA_IMAGE.FILE_THUMB_NAME is 'THUB 파일명';
COMMENT ON COLUMN QNA_IMAGE.FILE_SIZE is '파일 크기';
COMMENT ON COLUMN QNA_IMAGE.FILE_DATE is '등록일';


DROP SEQUENCE QNA_IMAGE_SEQ;

CREATE SEQUENCE QNA_IMAGE_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지
  
commit;
SELECT * FROM qna_image;

-- 이미지 삽입
INSERT INTO QNA_IMAGE(file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date)
VALUES(qna_image_seq.nextval, 1, 'qna1.jpg', 'desk.jpg', 'desk.jpg_t', 100, sysdate);

INSERT INTO QNA_IMAGE(file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date)
VALUES(qna_image_seq.nextval, 1, 'qna2.jpg', 'arm.jpg', 'arm.jpg_t', 100, sysdate);

INSERT INTO QNA_IMAGE(file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date)
VALUES(qna_image_seq.nextval, 1, 'qna3.jpg', 'monitor.jpg', 'monitor.jpg_t', 100, sysdate);

-- 특정 이미지 변경
UPDATE qna_image
SET file_origin_name='QNA1.jpg', file_upload_name='qna.jpg', file_thumb_name='qna.jpg_t'
WHERE file_no=1;

-- 특정 이미지 삭제
DELETE FROM qna_image
WHERE file_no=1;

-- 전체 이미지
SELECT file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM qna_image;
             
-- 2) 목록
SELECT file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM qna_image
ORDER BY qcon_no DESC, file_no ASC;

-- 3) 글별 파일 목록(contentsno 기준 내림 차순, attachfileno 기준 오르차순)
SELECT file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM qna_image
WHERE qcon_no = 1
ORDER BY file_origin_name ASC;

-- 4) 하나의 파일 삭제
DELETE FROM qna_image
WHERE file_no = 1;


-- 5) FK contentsno 부모키 별 조회
SELECT file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM qna_image
WHERE qcon_no=1;

-- 부모키별 갯수 산출
SELECT COUNT(*) as cnt
FROM qna_image
WHERE qcon_no=1;
  
-- 6) FK 부모 테이블별 레코드 삭제
DELETE FROM qna_image
WHERE qcon_no=1;

   
-- 7) qna_contents, qna_image join
    SELECT c.qcon_name, 
               i.file_no, i.qcon_no, i.file_origin_name, i.file_upload_name, i.file_thumb_name, i.file_size, i.file_date
    FROM qna_contents c, qna_image i
    WHERE c.qcon_no = i.qcon_no
    ORDER BY c.qcon_no DESC, i.file_no ASC;


-- 8) 조회
SELECT file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM qna_image
WHERE file_no=1;

