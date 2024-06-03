/**********************************/
/* Table Name: 관리자 */
/**********************************/
DROP TABLE ADMIN CASCADE CONSTRAINTS; -- 자식 무시하고 삭제 가능
DROP TABLE ADMIN;

CREATE TABLE ADMIN(
		ADM_NO NUMBER(10) NOT NULL PRIMARY KEY,
        CATE_NO NUMBER(10), -- FK
        ACC_NO NUMBER(20), -- FK  
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE ADMIN is '관리자';
COMMENT ON COLUMN ADMIN.ADM_NO is '관리자 번호';
COMMENT ON COLUMN ADMIN.CATE_NO is '카테고리 번호';
COMMENT ON COLUMN ADMIN.CATE_NO is '회원 번호';
COMMENT ON COLUMN ADMIN.ACC_GRADE is '회원 등급';

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

-- 삽입
INSERT INTO ADMIN (ADM_NO, CATE_NO, ACC_NO)
VALUES (ADMIN_SEQ.nextval, 1, 1);

INSERT INTO ADMIN (ADM_NO, CATE_NO, ACC_NO)
VALUES (ADMIN_SEQ.nextval, 2, 2);

-- 조회
SELECT ADM_NO, CATE_NO, ACC_NO
FROM admin
WHERE acc_no = 2;

-- 삭제
DELETE FROM admin