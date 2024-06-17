DROP TABLE share_comment CASCADE CONSTRAINTS; 
 

CREATE TABLE SHARE_RECOMMENT(
SRECMT_NO                     		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
SCMT_NO                       		NUMBER(10)		 NULL ,
SCON_NO                       		NUMBER(10)		 NULL ,
ACC_NO                        		NUMBER(10)		 NULL ,
SRECMT_CONTENTS               		VARCHAR2(300)		 NOT NULL,
SRECMT_DATE                   		DATE		 NOT NULL,
FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO),
FOREIGN KEY (SCMT_NO) REFERENCES SHARE_COMMENT (SCMT_NO)
);

COMMENT ON TABLE SHARE_RECOMMENT is '공유대댓글';
COMMENT ON COLUMN SHARE_RECOMMENT.SRECMT_NO is '공유대댓글번호';
COMMENT ON COLUMN SHARE_RECOMMENT.SCMT_NO is '댓글번호';
COMMENT ON COLUMN SHARE_RECOMMENT.SCON_NO is '공유게시글번호';
COMMENT ON COLUMN SHARE_RECOMMENT.ACC_NO is '회원번호';
COMMENT ON COLUMN SHARE_RECOMMENT.SRECMT_CONTENTS is '내용';
COMMENT ON COLUMN SHARE_RECOMMENT.SRECMT_DATE is '등록일';

DROP SEQUENCE re_comment_seq;

CREATE SEQUENCE share_recomment_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
COMMIT;

select * from share_recomment;

--대댓글 등록
INSERT INTO SHARE_RECOMMENT(srecmt_no,scmt_no,scon_no,srecmt_contents,acc_no,srecmt_date)
VALUES(share_recomment_seq.nextval,47,55,'좋아요',1,sysdate);

--전체조회
SELECT srecmt_no,scmt_no,scon_no,srecmt_contents,acc_no,srecmt_date
FROM share_recomment;

--대댓글 조회
SELECT rc.srecmt_no,rc.scmt_no,rc.scon_no,rc.srecmt_contents,ac.acc_no,rc.srecmt_date,ac.acc_id
FROM share_recomment rc, account ac
where rc.acc_no = ac.acc_no AND rc.scmt_no = 58;

--대댓글 수정
update share_recomment
set share_comment = '좋은 정보 감사합니다.'
where srecmt_no=10;

delete from share_recomment
where srecmt_no = 10;

delete from share_recomment
where scmt_no = 47;

delete from rshare_recomment
where scon_no=49;

delete from share_recomment
where scon_no in (49,50);

