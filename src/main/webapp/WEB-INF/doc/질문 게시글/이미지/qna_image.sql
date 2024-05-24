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

-- 특정 이미지 조회
SELECT file_no, qcon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM qna_image
WHERE qcon_no=1;

-- 특정 이미지 변경
UPDATE qna_image
SET file_origin_name='QNA1.jpg', file_upload_name='qna.jpg', file_thumb_name='qna.jpg_t'
WHERE file_no=1;

-- 특정 이미지 삭제
DELETE FROM qna_image
WHERE file_no=1;

