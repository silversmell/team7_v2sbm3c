DROP TABLE ACCOUNT CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE ACCOUNT;

CREATE TABLE ACCOUNT(
		ACC_NO NUMBER(10) NOT NULL PRIMARY KEY,
		ACC_ID VARCHAR2(60) NOT NULL,
		ACC_PW VARCHAR2(100) NOT NULL,
		ACC_NAME VARCHAR2(20) NOT NULL,
		ACC_TEL VARCHAR2(14),
		ACC_AGE VARCHAR2(6),
		ACC_DATE DATE NOT NULL,
		ACC_GRADE NUMBER(2) NOT NULL,
		ACC_IMG VARCHAR2(100),
		ACC_SAVED_IMG VARCHAR2(100),
		ACC_THUMB_IMG VARCHAR2(100),
		ACC_IMG_SIZE NUMBER(10)
);

COMMENT ON TABLE ACCOUNT is '회원';
COMMENT ON COLUMN ACCOUNT.ACC_NO is '회원번호';
COMMENT ON COLUMN ACCOUNT.ACC_ID is '아이디';
COMMENT ON COLUMN ACCOUNT.ACC_PW is '비밀번호';
COMMENT ON COLUMN ACCOUNT.ACC_NAME is '이름';
COMMENT ON COLUMN ACCOUNT.ACC_TEL is '전화번호';
COMMENT ON COLUMN ACCOUNT.ACC_AGE is '연령대';
COMMENT ON COLUMN ACCOUNT.ACC_DATE is '가입일';
COMMENT ON COLUMN ACCOUNT.ACC_GRADE is '등급';
COMMENT ON COLUMN ACCOUNT.ACC_IMG is '프로필사진';
COMMENT ON COLUMN ACCOUNT.ACC_SAVED_IMG is '저장된프로필사진';
COMMENT ON COLUMN ACCOUNT.ACC_THUMB_IMG is '프로필사진THUMB';
COMMENT ON COLUMN ACCOUNT.ACC_IMG_SIZE is '프로필사진크기';

DROP SEQUENCE ACCOUNT_SEQ;

 CREATE SEQUENCE ACCOUNT_SEQ
   START WITH 1        
   INCREMENT BY 1       
   MAXVALUE 9999999999 
   CACHE 2              
   NOCYCLE;             

--------------------------------------------------------------------------------


-- 회원 가입
-- password: 1111 -- encoding --> P+faf8xV+BRvnALZIp7rcQ==

INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user1','P+faf8xV+BRvnALZIp7rcQ==','박은향',sysdate,1);
INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user2','P+faf8xV+BRvnALZIp7rcQ==','장영은',sysdate,1);
INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user3','P+faf8xV+BRvnALZIp7rcQ==','김수진',sysdate,1);

INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user4','P+faf8xV+BRvnALZIp7rcQ==','RECO',sysdate,1);
INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user5','P+faf8xV+BRvnALZIp7rcQ==','RECO2',sysdate,1);
INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user6','P+faf8xV+BRvnALZIp7rcQ==','RECO3',sysdate,1);

commit;
--------------------------------------------------------------------------------

-- 전체 회원 조회

SELECT acc_no, acc_id, acc_pw, acc_name, acc_tel, acc_age, acc_date, acc_grade, 
       acc_img, acc_saved_img, acc_thumb_img, acc_img_size
FROM account
ORDER BY acc_grade ASC, acc_id ASC;

SELECT ACCOUNT_SEQ.currval AS acc_no FROM dual;

-- 특정 회원 조회
SELECT acc_no, acc_id, acc_pw, acc_name, acc_tel, acc_age, acc_date, acc_grade, 
       acc_img, acc_saved_img, acc_thumb_img, acc_img_size
FROM account
WHERE acc_id = 'user2';


--------------------------------------------------------------------------------


-- 아이디 중복 검사
SELECT COUNT(acc_id) AS cnt
FROM account
WHERE acc_id='user2';

SELECT COUNT(acc_id) AS cnt
FROM account
WHERE acc_id='notExist';


--------------------------------------------------------------------------------


-- 회원 정보 수정
UPDATE account 
SET acc_name='수정됨', acc_tel='010-9999-1110', acc_age='20대'
WHERE acc_no=4;


--------------------------------------------------------------------------------

-- 회원 전체 삭제 
DELETE FROM account;
 
-- 특정 회원 삭제
DELETE FROM account
WHERE acc_no=4;

-- 회원 탈퇴 (update문)


--------------------------------------------------------------------------------

-- 프로필 사진 업데이트

UPDATE account
SET acc_img = 'test', acc_saved_img = 'test', acc_thumb_img = 'test', acc_img_size = 100
WHERE acc_no=4;

--------------------------------------------------------------------------------

-- 현재 비밀번호 확인
SELECT COUNT(acc_no) as cnt
FROM account
WHERE acc_no=1 AND acc_pw='1111';

SELECT COUNT(acc_no) as cnt
FROM account
WHERE acc_no=1 AND acc_pw='1234';

-- 비밀번호 변경
SELECT * FROM account;

UPDATE account
SET acc_pw='3333'
WHERE acc_no=2;



--------------------------------------------------------------------------------

-- 로그인
SELECT COUNT(acc_no) as cnt
FROM account
WHERE acc_id='user2' AND acc_pw='1111';

SELECT COUNT(acc_no) as cnt
FROM account
WHERE acc_id='user2' AND acc_pw='1234';

--------------------------------------------------------------------------------

-- 내가 쓴 게시글
SELECT scon_no, scon_title, scon_contents, scon_date
FROM share_contents
WHERE acc_no = 1
ORDER BY scon_date ASC;

-- 나의 북마크 모음(공유 게시글)
SELECT sc.scon_no, sc.scon_title, sc.scon_contents, sc.scon_date, sc.scon_views, sc.scon_priority
FROM account a
INNER JOIN share_mark sm ON sm.acc_no = a.acc_no
INNER JOIN share_contents sc ON sc.scon_no = sm.scon_no
WHERE a.acc_no = 2
ORDER BY sc.scon_date ASC;

-- 나의 북마크 모음(질문 게시글)
SELECT qc.qcon_no, qc.qcon_name, qc.qcon_contents, qc.qcon_date, qc.qcon_views
FROM account a
INNER JOIN qna_mark qm ON qm.acc_no = a.acc_no
INNER JOIN qna_contents qc ON qc.qcon_no = qm.qcon_no
WHERE a.acc_no = 2
ORDER BY qc.qcon_date ASC;

-- 북마크 삭제(공유 게시글)
DELETE FROM share_mark
WHERE acc_no = 2
AND scon_no = 9;

-- 북마크 삭제(질문 게시글)
DELETE FROM qna_mark
WHERE acc_no = 2
AND qcon_no = 21;

-- 북마크 저장(공유 게시글)
INSERT INTO share_mark(smark_no, acc_no, scon_no)
VALUES(SHARE_MARK_SEQ.nextval, 2, 12);

-- 북마크 저장(질문 게시글)
INSERT INTO qna_mark(qmark_no, acc_no, qcon_no)
VALUES(QNA_MARK_SEQ.nextval, 2, 21);

commit;

--------------------------------------------------------------------------------

SELECT * FROM account;

COMMIT;













