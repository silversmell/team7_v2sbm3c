-- 테이블 삭제
DROP TABLE summary;

-- 테이블 생성
CREATE TABLE summary(
  summary_no   NUMBER(8)    NOT NULL PRIMARY KEY,
  acc_no     NUMBER(10)   NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼
  article                VARCHAR2(1000) NOT NULL,
  response        VARCHAR2(800) NOT NULL,
  sdate                 DATE          NOT NULL, -- 생성일
  FOREIGN KEY (acc_no) REFERENCES account (acc_no)
);

COMMENT ON TABLE  summary is '요약서비스AI';
COMMENT ON COLUMN summary.summary_no is '요약 번호';
COMMENT ON COLUMN summary.acc_no is '회원 번호';
COMMENT ON COLUMN summary.article is '생성어';
COMMENT ON COLUMN summary.response is '요약된생성어';
COMMENT ON COLUMN summary.sdate is '등록일';


DROP SEQUENCE summary_seq;

CREATE SEQUENCE summary_seq
  START WITH 1        -- 시작 번호
  INCREMENT BY 1      -- 증가값
  MAXVALUE 99999999   -- 최대값: 99999999 --> NUMBER(8) 대응
  CACHE 2             -- 2번은 메모리에서만 계산
  NOCYCLE;            -- 다시 1부터 생성되는 것을 방지
COMMIT;

INSERT INTO summary(summary_no, acc_no, article, response, sdate)
VALUES(summary_seq.nextval, 3, '사무실 책상을 꾸미려는데요 책상이 저 사진보다 쪼금 밝은 색이에요 근데 저는 연보라색으로 캐주얼한 감성으로 꾸미고싶어서 흰색바탕이 예쁠거같은데.. 이런 경우에는 보통 어떻게하나용 흰색 아크릴같은거 사이즈맞춰 까는게 나을지 고민이네요..', 
                                '사무실 책상 꾸미기 좋은 아이디 추천해주세요.', sysdate);
            
-- 실시간 요약 본인 로그 조회                    
SELECT su.summary_no, su.acc_no, su.article, su.response, su.sdate, ac.acc_id
FROM summary su
INNER JOIN account ac ON su.acc_no = ac.acc_no
WHERE su.acc_no = 7
ORDER BY su.sdate ASC;

-- 실시간 요약 전체 로그 조회
SELECT su.summary_no, su.acc_no, su.article, su.response, su.sdate, ac.acc_id
FROM summary su
INNER JOIN account ac ON su.acc_no = su.acc_no
ORDER BY su.sdate ASC;
                                
select * FROM summary;

COMMIT;