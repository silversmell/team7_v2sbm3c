/**********************************/
/* Table Name: 좋아요 */
/**********************************/

DROP TABLE TIP_LIKE CASCADE CONSTRAINTS;
DROP TABLE TIP_LIKE;

CREATE TABLE TIP_LIKE(
		LIKE_NO                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		NUMBER(10)		 NULL ,
		TCON_NO                       		NUMBER(10)		 NULL ,
        LIKE_DATE                     		DATE		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
  FOREIGN KEY (TCON_NO) REFERENCES TIP_CONTENTS (TCON_NO)
);

COMMENT ON TABLE TIP_LIKE is '좋아요';
COMMENT ON COLUMN TIP_LIKE.LIKE_NO is '좋아요번호';
COMMENT ON COLUMN TIP_LIKE.ACC_NO is '회원번호';
COMMENT ON COLUMN TIP_LIKE.TCON_NO is '팁게시글번호';
COMMENT ON COLUMN TIP_LIKE.LIKE_DATE is '등록일';


--------------------------------------------------------------------------------

DROP SEQUENCE LIKE_SEQ;

CREATE SEQUENCE LIKE_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

COMMIT;

--------------------------------------------------------------------------------

-- 조회
SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS is_liked
FROM tip_like
WHERE acc_no = 2
AND tcon_no = 5;

-- 좋아요 등록
INSERT INTO tip_like(like_no, acc_no, tcon_no, like_date)
VALUES(LIKE_SEQ.nextval, 2, 5, sysdate);
INSERT INTO tip_like(like_no, acc_no, tcon_no, like_date)
VALUES(LIKE_SEQ.nextval, 2, 10, sysdate);
INSERT INTO tip_like(like_no, acc_no, tcon_no, like_date)
VALUES(LIKE_SEQ.nextval, 2, 11, sysdate);
INSERT INTO tip_like(like_no, acc_no, tcon_no, like_date)
VALUES(LIKE_SEQ.nextval, 2, 12, sysdate);

COMMIT;

SELECT * FROM tip_like;

-- 좋아요 삭제
DELETE FROM tip_like
WHERE acc_no = 2
AND tcon_no = 5;

COMMIT;

SELECT * FROM tip_like;











