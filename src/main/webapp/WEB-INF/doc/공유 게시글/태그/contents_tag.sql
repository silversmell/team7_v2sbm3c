ALTER TABLE contents_tag ADD tag_no NUMBER(10) NOT NULL;
alter table contents_tag add foreign key(tag_no) references hashtag(tag_no);

-- 삽입
INSERT INTO contents_tag(contents_tag_no,scon_no,tag_no)
VALUES (contents_tag_SEQ.nextval,2,1);
INSERT INTO contents_tag(contents_tag_no,scon_no,tag_no)
VALUES (contents_tag_SEQ.nextval,2,2);

INSERT INTO contents_tag(contents_tag_no,scon_no,tag_no)
VALUES (contents_tag_SEQ.nextval,3,2);

--해당하는 태그의 게시글
select scon_no
from contents_tag
where tag_no=2;