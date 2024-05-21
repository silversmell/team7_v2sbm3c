/**********************************/
/* Table Name: 해시태그 */
/**********************************/

DROP TABLE HASHTAG CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE HASHTAG;

CREATE TABLE HASHTAG(
		TAG_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		TAG_CODE VARCHAR(12) NOT NULL,
		TAG_NAME VARCHAR(12) NOT NULL
);

COMMENT ON TABLE HASHTAG is '해시태그';
COMMENT ON COLUMN HASHTAG.TAG_NO is '해시태그 번호';
COMMENT ON COLUMN HASHTAG.TAG_CODE is '코드';
COMMENT ON COLUMN HASHTAG.TAG_NAME is '이름';


DROP SEQUENCE HASHTAG_SEQ;

 CREATE SEQUENCE HASHTAG_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999
   CACHE 2              
   NOCYCLE;             

--------------------------------------------------------------------------------

-- A: 색상 B: 분위기 C: 연령

INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '색상', '아이보리'); 
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '색상', '브라운'); 
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '색상', '실버');

INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '분위기', '공부'); 
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '분위기', '자연'); 
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '분위기', '모던');

INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '컨셉', '키치'); 
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '컨셉', 'y2k'); 
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '컨셉', '레트로');
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '컨셉', '작업실');
INSERT INTO HASHTAG(tag_no, tag_code, tag_name)
VALUES(HASHTAG_SEQ.nextval, '컨셉', '서재');

--------------------------------------------------------------------------------

-- 조회

SELECT * FROM hashtag;

SELECT DISTINCT tag_code
FROM hashtag;

SELECT tag_no, tag_code, tag_name
FROM hashtag
GROUP BY tag_code;

SELECT LISTAGG(tag_code, ',') WITHIN GROUP (ORDER BY tag_code DESC) AS tag_codes
FROM (
    SELECT DISTINCT tag_code
    FROM hashtag
);


SELECT LISTAGG(tag_code, ',') WITHIN GROUP (ORDER BY tag_no) AS tag_codes
FROM hashtag;

		

commit;

