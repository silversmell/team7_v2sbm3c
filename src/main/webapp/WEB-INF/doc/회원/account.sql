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

INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user1','1111','박은향',sysdate,1);
INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user2','1111','장영은',sysdate,1);
INSERT INTO ACCOUNT(acc_no, acc_id, acc_pw, acc_name, acc_date, acc_grade)
VALUES(ACCOUNT_SEQ.nextval,'user3','1111','김수진',sysdate,1);


--------------------------------------------------------------------------------

-- 전체 회원 조회

SELECT acc_no, acc_id, acc_pw, acc_name, acc_tel, acc_age, acc_date, acc_grade, 
       acc_img, acc_saved_img, acc_thumb_img, acc_img_size
FROM account
ORDER BY acc_no ASC;

SELECT ACCOUNT_SEQ.currval AS acc_no FROM dual;

-- 특정 회원 조회

SELECT acc_no, acc_id, acc_pw, acc_name, acc_tel, acc_age, acc_date, acc_grade, 
       acc_img, acc_saved_img, acc_thumb_img, acc_img_size
FROM account
WHERE acc_id = 'user2';


-- 아이디 중복 검사
SELECT COUNT(acc_id) AS cnt
FROM account
WHERE acc_id='user2';

SELECT COUNT(acc_id) AS cnt
FROM account
WHERE acc_id='notExist';


------------

-- 회원 전체 삭제 
DELETE FROM account;
 
-- 회원 탈퇴 (특정 회원 삭제)
DELETE FROM account
WHERE acc_no=4;

SELECT * FROM account;

COMMIT;













