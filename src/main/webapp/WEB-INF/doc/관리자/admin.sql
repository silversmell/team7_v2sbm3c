/**********************************/
/* Table Name: 관리자 */
/**********************************/
DROP TABLE ADMIN CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE ADMIN;

CREATE TABLE ADMIN(
		ADM_NO NUMBER(10) NOT NULL PRIMARY KEY,
        CATE_NO NUMBER(10), -- FK
        ACC_NO NUMBER(10), -- FK  
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE ADMIN is '관리자';
COMMENT ON COLUMN ADMIN.ADM_NO is '관리자 번호';
COMMENT ON COLUMN ADMIN.CATE_NO is '카테고리 번호';
COMMENT ON COLUMN ADMIN.CATE_NO is '회원 번호';

DROP SEQUENCE ADMIN_SEQ;

CREATE SEQUENCE ADMIN_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지
  

commit;
SELECT * FROM admin;
-- Create, 등록: 1건 이상(임시 통합 관리자 계정)


INSERT INTO admin(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_date, adm_grade)
VALUES(admin_seq.nextval, 2, 'admin1@gmail.com', '1234', '박은향', sysdate, 1);

INSERT INTO admin(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_date, adm_grade)
VALUES(admin_seq.nextval, 1, 'kim20267878@gmail.com', '1234', '김수진', sysdate, 1);

INSERT INTO admin(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_date, adm_grade)
VALUES(admin_seq.nextval, 2, 'kim20267878@gmail.com', '1234', '김수진', sysdate, 1);

INSERT INTO admin(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_date, adm_grade)
VALUES(admin_seq.nextval, 1, 'admin3@gmail.com', '1234', '장영은', sysdate, 1);

INSERT INTO admin(adm_no, cate_no, adm_id, adm_pw, adm_name, adm_date, adm_grade)
VALUES(admin_seq.nextval, 2, 'admin3@gmail.com', '1234', '장영은', sysdate, 1);

-- 목록
-- 검색을 하지 않는 경우, 전체 목록 출력, 오름차순
SELECT adm_no, cate_no, adm_id, adm_pw, adm_name, adm_date
FROM admin
ORDER BY adm_grade ASC, adm_id ASC;
     
     
COMMIT;
rollback;
-- 수정
-- PK: 변경 못함, UNIQUE: 변경을 권장하지 않음(id)
UPDATE admin 
SET adm_name='박은향', adm_id='admin@gmail.com', adm_pw='1111', adm_grade=0
WHERE adm_no=1;


COMMIT;
rollback;
-- 삭제
-- 모두 삭제
DELETE FROM admin;
 
-- 특정 관리자 계정 삭제
DELETE FROM admin
WHERE adm_no=1;


-- 로그인
SELECT COUNT(adm_no) as cnt
FROM admin
WHERE adm_id='admin1@gmail.com' AND adm_pw='1234';


-- 패스워드
-- 패스워드 검사
SELECT COUNT(adm_no) as cnt
FROM admin
WHERE adm_no=1 AND adm_pw='1234';

COMMIT;
rollback;
-- 패스워드 수정
UPDATE admin
SET adm_pw='1111'
WHERE adm_no=1;


COMMIT;
rollback;
-- 등급 변경
UPDATE admin
SET adm_grade=2
WHERE adm_no=1;