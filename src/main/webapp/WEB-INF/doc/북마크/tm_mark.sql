/**********************************/
/* Table Name: 텍스트마이닝 북마크 */
/**********************************/
DROP TABLE TM_MARK CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE TM_MARK;

CREATE TABLE TM_MARK(
        TMARK_NO                      NUMBER(10) NOT NULL PRIMARY KEY,
        ACC_NO                        NUMBER(10), -- FK
        TCON_NO                       NUMBER(10), -- FK
    FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
    FOREIGN KEY (TCON_NO) REFERENCES TM_CONTENTS (TCON_NO)
);

COMMENT ON TABLE TM_MARK is '텍스트마이닝 북마크';
COMMENT ON COLUMN TM_MARK.TMARK_NO is '텍스트마이닝 북마크 번호';
COMMENT ON COLUMN TM_MARK.ACC_NO is '회원 번호';
COMMENT ON COLUMN TM_MARK.TCON_NO is '텍스트마이닝 번호';


DROP SEQUENCE TM_MARK_SEQ;

 CREATE SEQUENCE TM_MARK_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;          
commit;   
   
-- 전체 데이터 조회
SELECT tmark_no, acc_no, tcon_no
FROM tm_mark;

