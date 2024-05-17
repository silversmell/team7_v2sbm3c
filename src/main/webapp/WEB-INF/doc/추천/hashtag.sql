/**********************************/
/* Table Name: 해시태그 */
/**********************************/

DROP TABLE HASHTAG CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE HASHTAG;

CREATE TABLE HASHTAG(
		TAG_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		TAG_CODE VARCHAR(2) NOT NULL,
		TAG_NAME VARCHAR(10) NOT NULL
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




