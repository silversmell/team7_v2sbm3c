-- 1) 테이블 생성 및 데이터 준비
DROP TABLE PAGE;
 
CREATE TABLE PAGE(
  no    NUMBER(5) NOT NULL,
  title  VARCHAR(20) NOT NULL,
  PRIMARY KEY(no)
);

-- 일련번호를 sequence를 사용하지 않고 subquery를 이용하여 생성하는 경우
-- 서브 쿼리: SQL 내부에 SQL 사용, ()가 존재해야함, 독립적으로 실행 가능.
-- (SELECT NVL(MAX(no ), 0) +1 as no  FROM PAGE)
-- MAX(): 최대값을 가져오는 함수, 레코드가 없으면 null을 리턴
-- NVL(MAX(no ), 0): MAX 함수가 null이면 특정값으로 대체
-- 레코드가 없는 경우 null을 리턴
SELECT MAX(no) as no  FROM PAGE;

-- 레코드가 없는 경우 null에 대한 사칙연산은 null을 리턴
SELECT MAX(no) + 1 as no  FROM PAGE;

-- NVL(null, 0): null이 0으로 변경됨으로 +1 정상적으로 진행됨.
SELECT NVL(MAX(no), 0)+1 as no  FROM PAGE;


-- PK용 일련번호는 Subquery만 가능한 것이 아님.
-- 쿼리안에 쿼리가 있는 서브 쿼리의 사용, 서브 쿼리가 먼저 실행됨.
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '1');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '2');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '3');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '4');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '5');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '6');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '7');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '8');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '9');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '10');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '11');
INSERT INTO PAGE(no , title) VALUES((SELECT NVL(MAX(no), 0) +1 as no  FROM PAGE), '12');

COMMIT;

SELECT no, title FROM PAGE;

-- 분기별 페이징(1분기:1~3월, 2분기:4~6월, 3분기:7~9월, 4분기:10~12월)
-- 1분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM PAGE
    ORDER BY no ASC
    )
)
WHERE r>=1 AND r<=3;

-- 2분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM PAGE
    ORDER BY no ASC
    )
)
WHERE r>=4 AND r<=6;

-- 3분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM PAGE
    ORDER BY no ASC
    )
)
WHERE r>=7 AND r<=9;

-- 4분기
SELECT no, title, r
FROM(
  SELECT no, title, rownum as r
  FROM(
    SELECT no, title
    FROM PAGE
    ORDER BY no ASC
    )
)
WHERE r>=10 AND r<=12;