-- SQL을 직접 적용하는 경우 테스트
-- SQL을 직접 적용하는 경우 테스트
/**********************************/
/* Table Name: 직원 */
/**********************************/
DROP TABLE EMPLOYEE;

CREATE TABLE EMPLOYEE(
    EMPLOYEENO NUMBER(5)     NOT NULL,
    ID         VARCHAR(20)   NOT NULL UNIQUE, -- 아이디, 중복 안됨, 레코드를 구분 
    PASSWD     VARCHAR(15)   NOT NULL, -- 패스워드, 영숫자 조합
    MNAME      VARCHAR(20)   NOT NULL, -- 성명, 한글 10자 저장 가능
    RDATE      DATE          NOT NULL, -- 가입일    
    GRADE      NUMBER(2)     NOT NULL, -- 등급(1~10: 관리자, 정지 관리자: 20, 탈퇴 관리자: 99)    
    PRIMARY KEY (EMPLOYEENO)
);

COMMENT ON TABLE EMPLOYEE is '직원';
COMMENT ON COLUMN EMPLOYEE.EMPLOYEENO is '직원 번호';
COMMENT ON COLUMN EMPLOYEE.ID is '아이디';
COMMENT ON COLUMN EMPLOYEE.PASSWD is '패스워드';
COMMENT ON COLUMN EMPLOYEE.MNAME is '성명';
COMMENT ON COLUMN EMPLOYEE.RDATE is '가입일';
COMMENT ON COLUMN EMPLOYEE.GRADE is '등급';

DROP SEQUENCE EMPLOYEE_SEQ;

CREATE SEQUENCE EMPLOYEE_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999999
  CACHE 2                     -- 2번은 메모리에서만 계산
  NOCYCLE;                   -- 다시 1부터 생성되는 것을 방지

-- CREATE
INSERT INTO employee(employeeno, id, passwd, mname, rdate, grade)
VALUES(employee_seq.nextval, 'emp1', '1234', '직원1', sysdate, 1);

INSERT INTO employee(employeeno, id, passwd, mname, rdate, grade)
VALUES(employee_seq.nextval, 'emp2', '1234', '직원2', sysdate, 1);

INSERT INTO employee(employeeno, id, passwd, mname, rdate, grade)
VALUES(employee_seq.nextval, 'emp3', '1234', '직원3', sysdate, 1);

commit;

-- READ: List
SELECT employeeno, id, passwd, mname, rdate, grade FROM employee ORDER BY employeeno ASC;
EMPLOYEENO ID                   PASSWD          MNAME                RDATE                    GRADE
---------- -------------------- --------------- -------------------- ------------------- ----------
         1 emp1                 1234            직원1                2024-06-25 04:29:59          1
         2 emp2                 1234            직원2                2024-06-25 04:29:59          1
         3 emp3                 1234            직원3                2024-06-25 04:29:59          1

-- READ         
SELECT adminno, id, passwd, mname, mdate, grade 
FROM admin
WHERE adminno=1;
   ADMINNO ID                   PASSWD          MNAME                MDATE                    GRADE
---------- -------------------- --------------- -------------------- ------------------- ----------
         1 admin1               1234            관리자1              2022-10-06 11:47:56          1

-- READ by id
SELECT adminno, id, passwd, mname, mdate, grade 
FROM admin
WHERE id='admin1';
   ADMINNO ID                   PASSWD          MNAME                MDATE                    GRADE
---------- -------------------- --------------- -------------------- ------------------- ----------
         1 admin1               1234            관리자1              2022-10-06 11:47:56          1

-- UPDATE
UPDATE admin
SET passwd='1234', mname='관리자1', mdate=sysdate, grade=1
WHERE ADMINNO=1;

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
         