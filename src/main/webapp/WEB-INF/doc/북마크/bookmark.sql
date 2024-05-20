/**********************************/
/* Table Name: 북마크 */
/**********************************/

DROP TABLE BOOKMARK CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE BOOKMARK;

CREATE TABLE BOOKMARK(
		MARK_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		ACC_NO NUMERIC(10),
		SCON_NO NUMERIC(10),
		QCON_NO NUMERIC(10),
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO),
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE BOOKMARK is '추천';
COMMENT ON COLUMN BOOKMARK.MARK_NO is '북마크 번호';
COMMENT ON COLUMN BOOKMARK.SCON_NO is '공유게시글 번호';
COMMENT ON COLUMN BOOKMARK.QCON_NO is '질문게시글 번호';


DROP SEQUENCE BOOKMARK_SEQ;

 CREATE SEQUENCE BOOKMARK_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;             


--------------------------------------------------------------------------------

-- 임시 데이터 추가
-- 주의) FK 테이블에 데이터가 존재하는지 확인

INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 1, 1, null);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 1, 2, null);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 1, null, 1);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 1, null, 2);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 1, null, 3);

INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 2, null, 1);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 2, 1, null);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 2, 2, null);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 2, 3, null);

INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 3, null, 1);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 3, null, 2);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 3, 1, null);
INSERT INTO BOOKMARK(mark_no, acc_no, scon_no, qcon_no)
VALUES(BOOKMARK_SEQ.nextval, 3, 2, null);

--------------------------------------------------------------------------------

-- 전체 데이터 조회
SELECT mark_no, acc_no, scon_no, qcon_no
FROM bookmark;

COMMIT;








