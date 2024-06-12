/**********************************/
/* Table Name: 질문북마크 */
/**********************************/
DROP TABLE QNA_MARK CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE QNA_MARK;

CREATE TABLE QNA_MARK(
        QMARK_NO                      NUMBER(10) NOT NULL PRIMARY KEY,
        ACC_NO                        NUMBER(10) NULL ,
        QCON_NO                       NUMBER(10) NULL ,
    FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
    FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO)
);

COMMENT ON TABLE QNA_MARK is '질문북마크';
COMMENT ON COLUMN QNA_MARK.QMARK_NO is '질문북마크번호';
COMMENT ON COLUMN QNA_MARK.ACC_NO is '회원번호';
COMMENT ON COLUMN QNA_MARK.QCON_NO is '질문게시글번호';


DROP SEQUENCE QNA_MARK_SEQ;

 CREATE SEQUENCE QNA_MARK_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;          
   
   
-- 전체 데이터 조회
SELECT qmark_no, acc_no, qcon_no
FROM qna_mark;

COMMIT;


INSERT INTO qna_mark(qmark_no, acc_no, qcon_no)
VALUES(qna_mark_seq.nextval, 2, 1);

INSERT INTO qna_mark(qmark_no, acc_no, qcon_no)
VALUES(qna_mark_seq.nextval, 3, 1);

DELETE FROM qna_mark 
WHERE qcon_no =1 AND acc_no=3;


SELECT count(*)
FROM qna_mark
WHERE qcon_no=1

   