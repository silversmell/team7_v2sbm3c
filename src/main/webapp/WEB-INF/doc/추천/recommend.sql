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

-- 추천 정보 삭제
DELETE FROM recommend WHERE acc_no = 4;

--------------------------------------------------------------------------------

SELECT * FROM recommend;

commit;





