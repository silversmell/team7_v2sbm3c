DROP TABLE contents_url CASCADE CONSTRAINTS; 

CREATE TABLE contents_url(
        url_no        NUMBER(10)     NOT NULL,
        scon_no        NUMBER(10)     NOT NULL,
        url_link      VARCHAR(1000)  NULL,

        FOREIGN KEY (scon_no) REFERENCES share_contents (scon_no),
        PRIMARY KEY (url_no)
);

COMMENT ON TABLE contents_url is '게시글url';
COMMENT ON COLUMN contents_url.url_link is 'url주소';
COMMENT ON COLUMN contents_url.scon_no is '공유게시글 번호';

DROP SEQUENCE contents_url_SEQ;

CREATE SEQUENCE contents_url_SEQ
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
  commit;

INSERT INTO contents_url(url_no,scon_no,url_link)
VALUES(contents_url_SEQ.nextval,2,'www.naver.com,www.google.com');
INSERT INTO contents_url(url_no,scon_no,url_link)
VALUES(contents_url_SEQ.nextval,2,'https://chat.openai.com/');

commit;

-- url 등록 --
INSERT INTO contents_url(url_no,scon_no,url_link)
VALUES(contents_url_SEQ.nextval,2,'https://chat.openai.com/');
INSERT INTO contents_url(url_no,scon_no,url_link)
VALUES(contents_url_SEQ.nextval,3,'https://chat.openai.com/');

-- 해당하는 게시글의 url 출력--
select * from contents_url;

--scon_no에 따른 url 출력
SELECT url_no,scon_no,url_link
FROM contents_url
where scon_no=25;



--해당하는 게시글의 url 수정
update contents_url
set url_link='https://www.coupang.com/'
where scon_no=25;

-- url_link만 출력
SELECT url_link,url_no
FROM contents_url
where scon_no=5;
select * from contents_url;

		SELECT scon_no, scon_title, scon_contents, scon_views,
		scon_comment, scon_date,scon_priority
		FROM share_contents;
