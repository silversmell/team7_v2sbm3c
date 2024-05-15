DROP TABLE share_comment CASCADE CONSTRAINTS; 
 
CREATE TABLE share_comment(
        scmt_no        NUMBER(10)     NOT NULL,
        scon_no        NUMBER(10)     NOT NULL,
        scmt_comment   VARCHAR(300)  NOT NULL,
        scmt_date      DATE           NOT NULL,
        pro_no       NUMBER(10)       NOT NULL,
        FOREIGN KEY (scon_no) REFERENCES share_contents (scon_no),
        FOREIGN KEY (pro_no)  REFERENCES PROFILE (pro_no),
        PRIMARY KEY (scmt_no)
);

COMMENT ON TABLE share_comment is '공유게시글댓글';
COMMENT ON COLUMN share_comment.scmt_no is '댓글번호';
COMMENT ON COLUMN share_comment.scon_no is '공유게시글 번호';
COMMENT ON COLUMN share_comment.scmt_comment is '내용';
COMMENT ON COLUMN share_comment.scmt_date is '등록일';

DROP SEQUENCE share_comment_seq;

CREATE SEQUENCE share_comment_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  

INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,2,'좋은 정보 감사합니다.',sysdate,1);

INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,2,'감사합니다.',sysdate,1);

INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,2,'좋아요',sysdate,1);

INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,2,'별로에요',sysdate,2);


INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,3,'너무 유용해요.',sysdate,3);

INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,3,'감사합니다.',sysdate,1);

INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,3,'그냥 그래요',sysdate,3);

INSERT INTO share_comment(scmt_no, scon_no, scmt_comment,scmt_date, pro_no)
VALUES(share_comment_SEQ.nextval,2,'별로에요',sysdate,3);


select scmt_comment
from share_comment
where  scon_no=2;

select share_comment