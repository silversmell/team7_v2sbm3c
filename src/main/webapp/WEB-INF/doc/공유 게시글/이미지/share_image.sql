
CREATE TABLE share_image(
        file_no            NUMBER(10)     NOT NULL,
        scon_no            NUMBER(10)     NOT NULL,
        file_origin_name   VARCHAR(100)  NOT NULL,
        file_upload_name   VARCHAR(100)  NOT NULL,
        file_thumb_name   VARCHAR(100)  NOT NULL,
        file_size   INT  NOT NULL,
        file_date   DATE  NOT NULL,
        FOREIGN KEY (scon_no) REFERENCES share_contents (scon_no),
        PRIMARY KEY (file_no)
);
COMMENT ON TABLE share_image is '공유이미지';
COMMENT ON COLUMN share_image.file_no is '첨부파일번호';
COMMENT ON COLUMN share_image.scon_no is '공유게시글번호';
COMMENT ON COLUMN share_image.file_origin_name is '원본파일명';
COMMENT ON COLUMN share_image.file_upload_name is '업로드파일명';
COMMENT ON COLUMN share_image.file_thumb_name is  'Thumb파일명';
COMMENT ON COLUMN share_image.file_size is '파일크기';
COMMENT ON COLUMN share_image.file_date is '등록일';

DROP SEQUENCE share_image_seq;

CREATE SEQUENCE share_image_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

--삽입
INSERT INTO share_image(file_no,scon_no,file_origin_name,file_upload_name,file_thumb_name,file_size,file_date)
VALUES(share_image_SEQ.nextval,2,'penguins.jpg','cat.jpg','cat_t.jpg',100,sysdate);

INSERT INTO share_image(file_no,scon_no,file_origin_name,file_upload_name,file_thumb_name,file_size,file_date)
VALUES(share_image_SEQ.nextval,2,'cat.jpg','cat.jpg','cat_t.jpg',100,sysdate);

--해당 파일 no에 사진 변경
UPDATE share_image 
SET file_origin_name='minsoo.jpg',file_upload_name='minsoo.jpg',file_thumb_name='minsoo.jpg',file_size=200
where file_no=1;

--해당 파일 no에 사진 삭제
delete from share_image
where file_no=1;

--해당게시글 파일 모두 삭제
delete from share_image
where scon_no=2;