

DROP TABLE share_contents CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능

DROP SEQUENCE share_contents_seq;

CREATE TABLE share_contents (
  scon_no          NUMBER(10) NOT NULL PRIMARY KEY, -- share_contents 번호
  scon_title       VARCHAR(100)   NOT NULL, -- 제목
  scon_contents    VARCHAR(3000)   NOT NULL, -- 내용
  scon_views       NUMBER(30) DEFAULT 0 NOT NULL, --조회수
  scon_bookmark    NUMBER(10)   NOT NULL, -- bookmark 번호 외래키
  scon_comment     NUMBER(10), -- 댓글수
  scon_date        DATE       NOT NULL, -- 등록일
  scon_priority    NUMBER(10)       NULL, -- 우선순위
  pro_no           NUMBER(10)             NOT NULL, -- 회원번호    
  cate_no          NUMBER(10)     NOT NULL, -- 카테번호
  word             VARCHAR(20),  
  
  FOREIGN KEY (pro_no) REFERENCES profile(pro_no),
  FOREIGN KEY (cate_no) REFERENCES category(cate_no)
);

COMMENT ON TABLE share_contents is '공유게시글';
COMMENT ON COLUMN share_contents.scon_no  is '공유게시글번호';
COMMENT ON COLUMN share_contents.scon_title  is '제목';
COMMENT ON COLUMN share_contents.scon_contents is '내용';
COMMENT ON COLUMN share_contents.scon_views is '조회수';
COMMENT ON COLUMN share_contents.scon_bookmark is '북마크수';
COMMENT ON COLUMN share_contents.scon_comment is '댓글수';
COMMENT ON COLUMN share_contents.scon_date is '등록일';
COMMENT ON COLUMN share_contents.scon_priority is '우선순위';
COMMENT ON COLUMN share_contents.pro_no is '회원번호';
COMMENT ON COLUMN share_contents.cate_no is '카테고리번호';


CREATE SEQUENCE share_contents_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
 

---삽입--
INSERT INTO share_contents(scon_no, scon_title, scon_contents,scon_bookmark, scon_views,scon_date, pro_no ,cate_no)
VALUES(share_contents_SEQ.nextval,'데스트 테리어 1일차','데스크 투어하러 오세요 ',1,0,sysdate,1,1);
INSERT INTO share_contents(scon_no, scon_title, scon_contents,scon_bookmark,scon_views,scon_priority, scon_date, pro_no ,cate_no)
VALUES(share_contents_SEQ.nextval,'화이트계열로 꾸미기','화이트 ',2,0,0,sysdate,2,1);

INSERT INTO share_contents(scon_no, scon_title, scon_contents,scon_bookmark,scon_views,scon_priority,scon_date,scon_comment,pro_no ,cate_no)
VALUES(share_contents_SEQ.nextval,'블랙계열로 꾸미기','블랙 ',3,0,0,sysdate,1,3,1);

--목록--
SELECT scon_no, scon_title, scon_contents, scon_views, scon_date, pro_no ,cate_no 
FROM share_contents
where cate_no=1;
commit;

-- 해당 컨텐츠 조회
SELECT scon_no, scon_title, scon_contents, scon_views, scon_date, pro_no ,cate_no 
FROM share_contents
where scon_no=1;

--모두 삭제
delete from share_contents;

--해당 게시글 삭제
delete from share_contents
where scon_no=1;

--글 ,제목 수정
update share_contents
set scon_name='데스크 투어',scon_contents='데스크데스크데스크'
where scon_no=1;

--우선순위 올리기
update share_contents
set scon_priority=scon_priority+1
where scon_no=2;

--우선순위 내리기
update share_contents
set scon_priority=scon_priority-1
where scon_no=2;

--조회수 올리기
update share_contents
set scon_views=scon_views+1
where scon_no=2;

select scon_views from share_contents;