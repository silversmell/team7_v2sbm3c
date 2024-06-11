/**********************************/
/* Table Name: 추천 */
/**********************************/

DROP TABLE RECOMMEND CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE RECOMMEND;

CREATE TABLE RECOMMEND(
		RECOM_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		ACC_NO NUMERIC(10),
		TAG_NO NUMERIC(10),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
  FOREIGN KEY (TAG_NO) REFERENCES HASHTAG (TAG_NO)
);

COMMENT ON TABLE RECOMMEND is '추천';
COMMENT ON COLUMN RECOMMEND.RECOM_NO is '추천번호';
COMMENT ON COLUMN RECOMMEND.ACC_NO is '회원번호';
COMMENT ON COLUMN RECOMMEND.TAG_NO is '해시태그 번호';


DROP SEQUENCE RECOMMEND_SEQ;

 CREATE SEQUENCE RECOMMEND_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;             

--------------------------------------------------------------------------------


-- 회원가입 시 선택한 해시태그 정보 저장 
INSERT INTO RECOMMEND(recom_no, acc_no, tag_no)
VALUES(RECOMMEND_SEQ.nextval, 2, 3);


--------------------------------------------------------------------------------


-- 회원이 선택한 해시태그 조회(회원 정보 조회)
SELECT LISTAGG(h.tag_name, ',') WITHIN GROUP (ORDER BY tag_code DESC) AS tag_name
FROM recommend r
INNER JOIN hashtag h ON h.tag_no = r.tag_no
WHERE r.acc_no = 4;

SELECT h.tag_name
FROM recommend r
INNER JOIN hashtag h ON h.tag_no = r.tag_no
WHERE r.acc_no = 4;


-- [참고] 3개 테이블 JOIN
SELECT h.tag_name
FROM recommend r
INNER JOIN account a ON a.acc_no = r.acc_no
INNER JOIN hashtag h ON h.tag_no = r.tag_no
WHERE r.acc_no = 9;


--------------------------------------------------------------------------------


-- 나의 추천글 목록
SELECT DISTINCT sc.scon_no, sc.scon_title, sc.scon_contents, sc.scon_views, sc.mark, sc.scon_comment, sc.scon_date, sc.scon_priority
FROM recommend r
JOIN contents_tag ct ON r.tag_no = ct.tag_no
JOIN share_contents sc ON ct.scon_no = sc.scon_no
WHERE r.acc_no = 2
ORDER BY sc.scon_no ASC;

-- 나의 추천글 목록(페이징)
SELECT sc.scon_no, sc.scon_title, sc.scon_contents, sc.scon_views, sc.mark, sc.scon_comment, sc.scon_date, sc.scon_priority, p
FROM (
    SELECT sc.scon_no, sc.scon_title, sc.scon_contents, sc.scon_views, sc.mark, sc.scon_comment, sc.scon_date, sc.scon_priority, ROWNUM AS p
    FROM (
        SELECT DISTINCT sc.scon_no, sc.scon_title, sc.scon_contents, sc.scon_views, sc.mark, sc.scon_comment, sc.scon_date, sc.scon_priority
        FROM recommend r
        JOIN contents_tag ct ON r.tag_no = ct.tag_no
        JOIN share_contents sc ON ct.scon_no = sc.scon_no
        WHERE r.acc_no = 2
        ORDER BY sc.scon_no ASC
    )
) WHERE p >=1 AND p <= 5;


        
SELECT DISTINCT sc.scon_no, sc.scon_title, sc.scon_contents, sc.scon_views, sc.mark, sc.scon_comment, sc.scon_date, sc.scon_priority
FROM recommend r
JOIN contents_tag ct ON r.tag_no = ct.tag_no
JOIN share_contents sc ON ct.scon_no = sc.scon_no
JOIN account a ON r.acc_no = a.acc_no
WHERE a.acc_id = 'user3'
ORDER BY sc.scon_no ASC;


-- 이미지를 포함하여 추천글 목록 가져오기
SELECT sc.scon_no, sc.scon_title, sc.scon_contents, sc.scon_views, sc.mark, sc.scon_comment, sc.scon_date, sc.scon_priority,
       si.file_no, si.file_origin_name, si.file_upload_name, si.file_thumb_name, si.file_size, si.file_date
FROM recommend r
JOIN contents_tag ct ON r.tag_no = ct.tag_no
JOIN share_contents sc ON ct.scon_no = sc.scon_no
LEFT JOIN share_image si ON sc.scon_no = si.scon_no
WHERE r.acc_no = 3
ORDER BY sc.scon_no ASC;

-- 컨텐츠 이미지 가져오기
SELECT file_no, scon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM share_image
WHERE scon_no = 2;

SELECT file_no, scon_no, file_origin_name, file_upload_name, file_thumb_name, file_size, file_date
FROM share_image
WHERE scon_no = 2
AND ROWNUM<=1
ORDER BY file_no ASC;

-- 선택된 해시태그 목록
SELECT h.tag_no, h.tag_code, h.tag_name
FROM recommend r
JOIN hashtag h ON h.tag_no = r.tag_no
JOIN account a ON a.acc_no = r.acc_no
WHERE a.acc_id = 'user2';

SELECT h.tag_no, h.tag_code, h.tag_name
FROM recommend r
JOIN hashtag h ON h.tag_no = r.tag_no
JOIN account a ON a.acc_no = r.acc_no
WHERE r.acc_no = 2;





--------------------------------------------------------------------------------


--해당하는 태그의 게시글
select *
from contents_tag;


--------------------------------------------------------------------------------

-- 추천 정보 삭제
DELETE FROM recommend WHERE acc_no = 4;

DELETE FROM recommend;

--------------------------------------------------------------------------------

SELECT * FROM recommend;

commit;


--------------------------------------------------------------------------------

INSERT INTO share_contents(scon_no, scon_title, scon_contents,scon_bookmark,scon_views,scon_priority,scon_date,scon_comment, acc_no ,cate_no)
VALUES(share_contents_SEQ.nextval,'추천글 테스트 1 제목','추천글 테스트 1 내용 ', 2, 0, 0,sysdate, 0, 3, 1);
INSERT INTO share_contents(scon_no, scon_title, scon_contents,scon_bookmark,scon_views,scon_priority,scon_date,scon_comment, acc_no ,cate_no)
VALUES(share_contents_SEQ.nextval,'추천글 테스트 2 제목','추천글 테스트 2 내용 ', 1, 0, 0,sysdate, 0, 2, 1);
INSERT INTO share_contents(scon_no, scon_title, scon_contents,scon_bookmark,scon_views,scon_priority,scon_date,scon_comment, acc_no ,cate_no)
VALUES(share_contents_SEQ.nextval,'추천글 테스트 3 제목','추천글 테스트 2 내용 ', 2, 0, 0,sysdate, 0, 1, 1);




