/**********************************/
/* Table Name: 관리자 */
/**********************************/

DROP TABLE ADMIN CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE ADMIN;

CREATE TABLE ADMIN(
		ADM_NO                        		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		NUMBER(10)		 NULL ,
		ADM_ID                        		VARCHAR2(20)		 NOT NULL,
		ADM_PW                        		VARCHAR2(100)		 NOT NULL,
		ADM_NAME                      		VARCHAR2(20)		 NOT NULL,
		ADM_EMAIL                     		VARCHAR2(60)		 NOT NULL,
		ADM_TEL                       		VARCHAR2(14)		 NOT NULL,
		ADM_DATE                      		DATE		 NOT NULL,
		ADM_GRADE                     		NUMBER(2)   DEFAULT 1   NOT NULL,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO)
);

COMMENT ON TABLE ADMIN is '관리자';
COMMENT ON COLUMN ADMIN.ADM_NO is '관리자번호';
COMMENT ON COLUMN ADMIN.CATE_NO is '카테고리번호';
COMMENT ON COLUMN ADMIN.ADM_ID is '아이디';
COMMENT ON COLUMN ADMIN.ADM_PW is '비밀번호';
COMMENT ON COLUMN ADMIN.ADM_NAME is '이름';
COMMENT ON COLUMN ADMIN.ADM_EMAIL is '이메일';
COMMENT ON COLUMN ADMIN.ADM_TEL is '전화번호';
COMMENT ON COLUMN ADMIN.ADM_DATE is '가입일';
COMMENT ON COLUMN ADMIN.ADM_GRADE is '등급';

DROP SEQUENCE ADMIN_SEQ;

CREATE SEQUENCE ADMIN_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지
  
--------------------------------------------------------------------------------

-- 관리자 등록(회원가입)
INSERT INTO ADMIN(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_email, adm_tel, adm_date)
VALUES(ADMIN_SEQ.nextval, 1, 'admin1', 'P+faf8xV+BRvnALZIp7rcQ==', '박은향', 'eh@desk.tour', '010-1111-1111', sysdate);
INSERT INTO ADMIN(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_email, adm_tel, adm_date)
VALUES(ADMIN_SEQ.nextval, 2, 'admin2', 'P+faf8xV+BRvnALZIp7rcQ==', '김수진', 'sj@desk.tour', '010-2222-2222', sysdate);
INSERT INTO ADMIN(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_email, adm_tel, adm_date, adm_grade)
VALUES(ADMIN_SEQ.nextval, 3, 'useradmin', 'P+faf8xV+BRvnALZIp7rcQ==', '회원 관리자', 'ua@desk.tour', '010-3333-3333', sysdate, 1);
INSERT INTO ADMIN(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_email, adm_tel, adm_date, adm_grade)
VALUES(ADMIN_SEQ.nextval, 4, 'admin4', 'P+faf8xV+BRvnALZIp7rcQ==', '장영은', 'ye@desk.tour', '010-4444-4444', sysdate, 1);

commit;

SELECT * FROM admin;

--------------------------------------------------------------------------------

-- 카테고리 이름 목록 가져오기
SELECT LISTAGG(cate_name, ',') WITHIN GROUP (ORDER BY cate_no ASC) AS cate_names
FROM category;

SELECT * FROM category;

SELECT cate_no, cate_name, cate_cnt, cate_seqno, cate_visible
FROM category
ORDER BY cate_no ASC;


-- 아이디 중복 검사
SELECT COUNT(adm_id) AS cnt
FROM admin
WHERE adm_id='useradmin';

-- 이름 중복 검사
SELECT COUNT(adm_name) AS cnt
FROM admin
WHERE adm_name='회원 관리자';

-- 이메일 중복 검사
SELECT COUNT(adm_email) AS cnt
FROM admin
WHERE adm_email='ua@desk.tour';

commit;

-- 아이디로 관리자 정보 조회
SELECT adm_no, cate_no, adm_id, adm_pw, adm_name, adm_email, adm_tel, adm_date, adm_grade
FROM admin
WHERE adm_id = 'admin2';


-- 로그인
SELECT COUNT(adm_no) as cnt
FROM admin
WHERE adm_id='admintest' AND adm_pw='P+faf8xV+BRvnALZIp7rcQ==';

SELECT COUNT(adm_no) as cnt
FROM admin
WHERE adm_id='admintest' AND adm_pw='fS/kjO+fuEKk06Zl7VYMhg==';

-- READ: List
SELECT adm_no, adm_id,adm_pw, adm_name, adm_date, adm_grade FROM admin ORDER BY adm_no ASC;

-- READ         
SELECT adm_no, adm_id,adm_pw, adm_name, adm_date, adm_grade
FROM admin
WHERE adm_no=1;

-- READ by id
SELECT adm_no, adm_id,adm_pw, adm_name, adm_date, adm_grade
FROM admin
WHERE adm_id='admin1';


COMMIT;

-- DELETE
DELETE FROM admin WHERE adminno=3;
         
-- 로그인, 1: 로그인 성공, 0: 로그인 실패
SELECT COUNT(*) as cnt
FROM admin
WHERE id='admin1' AND passwd='1234'; 
       CNT
----------
         1
         





-- 삭제
DELETE FROM admin













