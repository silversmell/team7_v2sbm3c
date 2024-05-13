INSERT INTO contents_url(url_no,scon_no,url_link)
VALUES(contents_url_SEQ.nextval,2,'www.naver.com,www.google.com');
INSERT INTO contents_url(url_no,scon_no,url_link)
VALUES(contents_url_SEQ.nextval,2,'https://chat.openai.com/');


-- 해당하는 게시글의 url 출력--
select url_link
from contents_url
where scon_no=2;

--해당하는 게시글의 url 수정
update contents_url
set url_link='https://www.coupang.com/'
where scon_no=2;

select * from contents_url;