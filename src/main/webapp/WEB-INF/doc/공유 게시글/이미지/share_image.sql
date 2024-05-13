
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