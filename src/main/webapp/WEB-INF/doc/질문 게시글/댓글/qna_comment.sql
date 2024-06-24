/**********************************/
/* Table Name: 질문게시글댓글 */
/**********************************/
DROP TABLE QNA_COMMENT;

CREATE TABLE QNA_COMMENT(
		QCMT_NO NUMBER(10) NOT NULL PRIMARY KEY,
		ACC_NO NUMBER(10),  -- FK
		QCON_NO NUMBER(10), -- FK
		QCMT_CONTENTS VARCHAR2(300) NOT NULL,
		QCMT_DATE DATE NOT NULL,
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE QNA_COMMENT is '질문게시글 댓글';
COMMENT ON COLUMN QNA_COMMENT.QCMT_NO is '댓글 번호';
COMMENT ON COLUMN QNA_COMMENT.ACC_NO is '회원 번호';
COMMENT ON COLUMN QNA_COMMENT.QCON_NO is '질문게시글 번호';
COMMENT ON COLUMN QNA_COMMENT.QCMT_CONTENTS is '내용';
COMMENT ON COLUMN QNA_COMMENT.QCMT_DATE is '등록일';


DROP SEQUENCE QNA_COMMENT_SEQ;

CREATE SEQUENCE QNA_COMMENT_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999       -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                   -- 2번은 메모리에서만 계산
  NOCYCLE;                  -- 다시 1부터 생성되는 것을 방지

commit;
SELECT * FROM qna_comment;

-- 댓글 등록
INSERT INTO QNA_COMMENT(qcmt_no, acc_no, qcon_no, qcmt_contents, qcmt_date)
VALUES(qna_comment_seq.nextval, 1, 1, '저도 궁금하네요.', sysdate);

INSERT INTO QNA_COMMENT(qcmt_no, acc_no, qcon_no, qcmt_contents, qcmt_date)
VALUES(qna_comment_seq.nextval, 1, 2, '북마크하고 갑니다.', sysdate);

INSERT INTO QNA_COMMENT(qcmt_no, acc_no, qcon_no, qcmt_contents, qcmt_date)
VALUES(qna_comment_seq.nextval, 2, 1, '이거 아닐까요?', sysdate);

-- 댓글 조회
SELECT qcmt_contents, qcmt_date
FROM qna_comment
WHERE qcon_no=1;

-- 댓글 목록
SELECT qcmt_no, acc_no, qcon_no, qcmt_contents, qcmt_date
FROM qna_comment
ORDER BY qcmt_no DESC;

-- 질문글에 따른 댓글 목록
SELECT qcmt_no, acc_no, qcon_no, qcmt_contents, qcmt_date
FROM qna_comment
WHERE qcon_no=1
ORDER BY qcmt_no DESC;

-- 특정 질문글 전체 댓글
SELECT count(*)
FROM qna_comment
WHERE qcon_no=5;

-- 댓글 작성자 프로필 이미지
SELECT ac.acc_no, ac.acc_id, ac.acc_img, ac.acc_saved_img, ac.acc_thumb_img, ac.acc_img_size
FROM qna_comment qcmt INNER JOIN account ac
ON qcmt.acc_no = ac.acc_no
WHERE qcmt.qcmt_no = 16;
