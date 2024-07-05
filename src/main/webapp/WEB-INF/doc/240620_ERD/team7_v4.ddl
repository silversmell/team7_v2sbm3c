/**********************************/
/* Table Name: 카테고리 */
/**********************************/
CREATE TABLE CATEGORY(
		CATE_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NAME                     		VARCHAR2(30)		 NOT NULL,
		CATE_CNT                      		INTEGER(7)		 DEFAULT 0		 NOT NULL,
		CATE_SEQNO                    		INTEGER(5)		 NOT NULL,
		CATE_VISIBLE                  		CHAR(1)		 DEFAULT Y		 NOT NULL
);

COMMENT ON TABLE CATEGORY is '카테고리';
COMMENT ON COLUMN CATEGORY.CATE_NO is '카테고리번호';
COMMENT ON COLUMN CATEGORY.CATE_NAME is '카테고리명';
COMMENT ON COLUMN CATEGORY.CATE_CNT is '관련 자료수';
COMMENT ON COLUMN CATEGORY.CATE_SEQNO is '출력순서';
COMMENT ON COLUMN CATEGORY.CATE_VISIBLE is '출력모드';


/**********************************/
/* Table Name: 관리자 */
/**********************************/
CREATE TABLE ADMIN(
		ADM_NO                        		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		INTEGER(10)		 NOT NULL,
		ADM_ID                        		VARCHAR2(20)		 NOT NULL,
		ADM_PW                        		VARCHAR2(100)		 NOT NULL,
		ADM_NAME                      		VARCHAR2(20)		 NOT NULL,
		ADM_EMAIL                     		VARCHAR2(60)		 NOT NULL,
		ADM_TEL                       		VARCHAR2(14)		 NOT NULL,
		ADM_DATE                      		DATE		 NOT NULL,
		ADM_GRADE                     		INTEGER(2)		 NOT NULL,
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


/**********************************/
/* Table Name: 회원 */
/**********************************/
CREATE TABLE ACCOUNT(
		ACC_NO                        		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_ID                        		VARCHAR2(60)		 NOT NULL,
		ACC_PW                        		VARCHAR2(100)		 NOT NULL,
		ACC_NAME                      		VARCHAR2(20)		 NOT NULL,
		ACC_TEL                       		VARCHAR2(14)		 NULL ,
		ACC_AGE                       		VARCHAR2(6)		 NULL ,
		ACC_DATE                      		DATE		 NOT NULL,
		ACC_GRADE                     		INTEGER(2)		 NOT NULL,
		ACC_IMG                       		VARCHAR2(100)		 NULL ,
		ACC_SAVED_IMG                 		VARCHAR2(100)		 NULL ,
		ACC_THUMB_IMG                 		VARCHAR2(100)		 NULL ,
		ACC_IMG_SIZE                  		NUMBER(38)		 NULL 
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


/**********************************/
/* Table Name: 해시태그 */
/**********************************/
CREATE TABLE HASHTAG(
		TAG_NO                        		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		TAG_CODE                      		VARCHAR2(12)		 NOT NULL,
		TAG_NAME                      		VARCHAR2(12)		 NOT NULL
);

COMMENT ON TABLE HASHTAG is '해시태그';
COMMENT ON COLUMN HASHTAG.TAG_NO is '해시태그번호';
COMMENT ON COLUMN HASHTAG.TAG_CODE is '코드';
COMMENT ON COLUMN HASHTAG.TAG_NAME is '이름';


/**********************************/
/* Table Name: 추천 */
/**********************************/
CREATE TABLE RECOMMEND(
		RECOM_NO                      		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		TAG_NO                        		INTEGER(10)		 NOT NULL,
		RECOM_SEQ                     		INTEGER(1)		 NOT NULL,
		RECOM_DATE                    		DATE		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
  FOREIGN KEY (TAG_NO) REFERENCES HASHTAG (TAG_NO)
);

COMMENT ON TABLE RECOMMEND is '추천';
COMMENT ON COLUMN RECOMMEND.RECOM_NO is '추천번호';
COMMENT ON COLUMN RECOMMEND.ACC_NO is '회원번호';
COMMENT ON COLUMN RECOMMEND.TAG_NO is '해시태그번호';
COMMENT ON COLUMN RECOMMEND.RECOM_SEQ is '우선순위';
COMMENT ON COLUMN RECOMMEND.RECOM_DATE is '등록일';


/**********************************/
/* Table Name: 공유게시글 */
/**********************************/
CREATE TABLE SHARE_CONTENTS(
		SCON_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		INTEGER(10)		 NOT NULL,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		SCON_NAME                     		VARCHAR2(100)		 NOT NULL,
		SCON_CONTENTS                 		VARCHAR2(3000)		 NOT NULL,
		SCON_VIEWS                    		INTEGER(30)		 DEFAULT 0		 NOT NULL,
		SCON_BOOKMARK                 		INTEGER(10)		 DEFAULT 0		 NOT NULL,
		SCON_COMMENT                  		INTEGER(10)		 DEFAULT 0		 NULL ,
		SCON_DATE                     		DATE		 NOT NULL,
		SCON_PRIORITY                 		INTEGER(10)		 DEFAULT 0		 NULL ,
		WORD                          		VARCHAR2(20)		 NULL ,
		MARK                          		CHAR(1)		 DEFAULT N		 NULL ,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE SHARE_CONTENTS is '공유게시글';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_NO is '공유게시글번호';
COMMENT ON COLUMN SHARE_CONTENTS.CATE_NO is '카테고리번호';
COMMENT ON COLUMN SHARE_CONTENTS.ACC_NO is '회원번호';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_NAME is '제목';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_CONTENTS is '내용';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_VIEWS is '조회수';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_BOOKMARK is '북마크수';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_COMMENT is '댓글수';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_DATE is '등록일';
COMMENT ON COLUMN SHARE_CONTENTS.SCON_PRIORITY is '우선순위';
COMMENT ON COLUMN SHARE_CONTENTS.WORD is '검색어';
COMMENT ON COLUMN SHARE_CONTENTS.MARK is '북마크표시';


/**********************************/
/* Table Name: 질문게시글 */
/**********************************/
CREATE TABLE QNA_CONTENTS(
		QCON_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		INTEGER(10)		 NOT NULL,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		QCON_NAME                     		VARCHAR2(100)		 NOT NULL,
		QCON_CONTENTS                 		VARCHAR2(3000)		 NULL ,
		QCON_VIEWS                    		INTEGER(7)		 DEFAULT 0		 NOT NULL,
		QCON_BOOKCNT                  		INTEGER(7)		 DEFAULT 0		 NOT NULL,
		QCON_BOOKMARK                 		CHAR(1)		 DEFAULT N		 NOT NULL,
		QCON_COMMENT                  		INTEGER(7)		 DEFAULT 0		 NOT NULL,
		QCON_DATE                     		DATE		 NOT NULL,
		WORD                          		VARCHAR2(30)		 NULL ,
		QCON_PASSWD                   		VARCHAR2(30)		 NOT NULL,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE QNA_CONTENTS is '질문게시글';
COMMENT ON COLUMN QNA_CONTENTS.QCON_NO is '질문게시글번호';
COMMENT ON COLUMN QNA_CONTENTS.CATE_NO is '카테고리번호';
COMMENT ON COLUMN QNA_CONTENTS.ACC_NO is '회원번호';
COMMENT ON COLUMN QNA_CONTENTS.QCON_NAME is '제목';
COMMENT ON COLUMN QNA_CONTENTS.QCON_CONTENTS is '내용';
COMMENT ON COLUMN QNA_CONTENTS.QCON_VIEWS is '조회수';
COMMENT ON COLUMN QNA_CONTENTS.QCON_BOOKCNT is '북마크수';
COMMENT ON COLUMN QNA_CONTENTS.QCON_BOOKMARK is '북마크수';
COMMENT ON COLUMN QNA_CONTENTS.QCON_COMMENT is '댓글수';
COMMENT ON COLUMN QNA_CONTENTS.QCON_DATE is '등록일';
COMMENT ON COLUMN QNA_CONTENTS.WORD is '검색어';
COMMENT ON COLUMN QNA_CONTENTS.QCON_PASSWD is '비밀번호';


/**********************************/
/* Table Name: 공유이미지 */
/**********************************/
CREATE TABLE SHARE_IMAGE(
		FILE_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		SCON_NO                       		INTEGER(10)		 NOT NULL,
		FILE_ORIGIN_NAME              		VARCHAR2(100)		 NOT NULL,
		FILE_UPLOAD_NAME              		VARCHAR2(100)		 NOT NULL,
		FILE_THUMB_NAME               		VARCHAR2(100)		 NOT NULL,
		FILE_SIZE                     		NUMBER(38)		 DEFAULT 0		 NOT NULL,
		FILE_DATE                     		DATE		 NOT NULL,
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO)
);

COMMENT ON TABLE SHARE_IMAGE is '공유이미지';
COMMENT ON COLUMN SHARE_IMAGE.FILE_NO is '첨부파일번호';
COMMENT ON COLUMN SHARE_IMAGE.SCON_NO is '공유게시글번호';
COMMENT ON COLUMN SHARE_IMAGE.FILE_ORIGIN_NAME is '원본파일명';
COMMENT ON COLUMN SHARE_IMAGE.FILE_UPLOAD_NAME is '업로드파일명';
COMMENT ON COLUMN SHARE_IMAGE.FILE_THUMB_NAME is 'Thumb파일명';
COMMENT ON COLUMN SHARE_IMAGE.FILE_SIZE is '파일크기';
COMMENT ON COLUMN SHARE_IMAGE.FILE_DATE is '등록일';


/**********************************/
/* Table Name: 공유게시글댓글 */
/**********************************/
CREATE TABLE SHARE_COMMENT(
		SCMT_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		SCON_NO                       		INTEGER(10)		 NOT NULL,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		SCMT_COMMENT                  		VARCHAR2(300)		 NOT NULL,
		SCMT_DATE                     		DATE		 NOT NULL,
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE SHARE_COMMENT is '공유게시글댓글';
COMMENT ON COLUMN SHARE_COMMENT.SCMT_NO is '댓글번호';
COMMENT ON COLUMN SHARE_COMMENT.SCON_NO is '공유게시글번호';
COMMENT ON COLUMN SHARE_COMMENT.ACC_NO is '회원번호';
COMMENT ON COLUMN SHARE_COMMENT.SCMT_COMMENT is '내용';
COMMENT ON COLUMN SHARE_COMMENT.SCMT_DATE is '등록일';


/**********************************/
/* Table Name: 질문북마크 */
/**********************************/
CREATE TABLE QNA_MARK(
		QMARK_NO                      		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		QCON_NO                       		INTEGER(10)		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO)
);

COMMENT ON TABLE QNA_MARK is '질문북마크';
COMMENT ON COLUMN QNA_MARK.QMARK_NO is '질문북마크번호';
COMMENT ON COLUMN QNA_MARK.ACC_NO is '회원번호';
COMMENT ON COLUMN QNA_MARK.QCON_NO is '질문게시글번호';


/**********************************/
/* Table Name: 관리자로그 */
/**********************************/
CREATE TABLE ADMIN_LOG(
		ADM_LOG_NO                    		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ADM_NO                        		INTEGER(10)		 NOT NULL,
		ADM_LOG_IP                    		VARCHAR2(15)		 NOT NULL,
		ADM_LOG_TIME                  		TIMESTAMP(6)		 NOT NULL,
  FOREIGN KEY (ADM_NO) REFERENCES ADMIN (ADM_NO)
);

COMMENT ON TABLE ADMIN_LOG is '관리자로그';
COMMENT ON COLUMN ADMIN_LOG.ADM_LOG_NO is '관리자로그번호';
COMMENT ON COLUMN ADMIN_LOG.ADM_NO is '관리자번호';
COMMENT ON COLUMN ADMIN_LOG.ADM_LOG_IP is '접속아이피';
COMMENT ON COLUMN ADMIN_LOG.ADM_LOG_TIME is '접속시간';


/**********************************/
/* Table Name: 회원로그 */
/**********************************/
CREATE TABLE ACC_LOG(
		ACC_LOG_NO                    		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		ACC_LOG_IP                    		VARCHAR2(15)		 NOT NULL,
		ACC_LOG_TIME                  		TIMESTAMP(6)		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE ACC_LOG is '회원로그';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_NO is '회원로그번호';
COMMENT ON COLUMN ACC_LOG.ACC_NO is '회원번호';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_IP is '접속아이피';
COMMENT ON COLUMN ACC_LOG.ACC_LOG_TIME is '접속시간';


/**********************************/
/* Table Name: 게시글태그 */
/**********************************/
CREATE TABLE CONTENTS_TAG(
		CONTENTS_TAG_NO               		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		SCON_NO                       		INTEGER(10)		 NOT NULL,
		TAG_NO                        		INTEGER(10)		 NOT NULL,
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO),
  FOREIGN KEY (TAG_NO) REFERENCES HASHTAG (TAG_NO)
);

COMMENT ON TABLE CONTENTS_TAG is '게시글태그';
COMMENT ON COLUMN CONTENTS_TAG.CONTENTS_TAG_NO is '게시글태그번호';
COMMENT ON COLUMN CONTENTS_TAG.SCON_NO is '공유게시글번호';
COMMENT ON COLUMN CONTENTS_TAG.TAG_NO is '해시태그번호';


/**********************************/
/* Table Name: 게시글URL */
/**********************************/
CREATE TABLE CONTENTS_URL(
		URL_NO                        		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		SCON_NO                       		INTEGER(10)		 NOT NULL,
		URL_LINK                      		VARCHAR2(1000)		 NULL ,
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO)
);

COMMENT ON TABLE CONTENTS_URL is '게시글URL';
COMMENT ON COLUMN CONTENTS_URL.URL_NO is '게시글URL번호';
COMMENT ON COLUMN CONTENTS_URL.SCON_NO is '공유게시글번호';
COMMENT ON COLUMN CONTENTS_URL.URL_LINK is 'URL주소';


/**********************************/
/* Table Name: 팁게시글 */
/**********************************/
CREATE TABLE TIP_CONTENTS(
		TCON_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		INTEGER(10)		 NOT NULL,
		ADM_NO                        		INTEGER(10)		 NOT NULL,
		TCON_NAME                     		VARCHAR2(100)		 NOT NULL,
		TCON_CONTENTS                 		VARCHAR2(3000)		 NOT NULL,
		TCON_VIEWS                    		INTEGER(7)		 DEFAULT 0		 NOT NULL,
		TCON_DATE                     		DATE		 NOT NULL,
		TCON_IMG                      		VARCHAR2(100)		 NULL ,
		TCON_SAVED_IMG                		VARCHAR2(100)		 NULL ,
		TCON_THUMB_IMG                		VARCHAR2(100)		 NULL ,
		TCON_IMG_SIZE                 		NUMBER(38)		 NULL ,
		YOUTUBE                       		VARCHAR2(1000)		 NULL ,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ADM_NO) REFERENCES ADMIN (ADM_NO)
);

COMMENT ON TABLE TIP_CONTENTS is '팁게시글';
COMMENT ON COLUMN TIP_CONTENTS.TCON_NO is '팁게시글번호';
COMMENT ON COLUMN TIP_CONTENTS.CATE_NO is '카테고리번호';
COMMENT ON COLUMN TIP_CONTENTS.ADM_NO is '관리자번호';
COMMENT ON COLUMN TIP_CONTENTS.TCON_NAME is '제목';
COMMENT ON COLUMN TIP_CONTENTS.TCON_CONTENTS is '내용';
COMMENT ON COLUMN TIP_CONTENTS.TCON_VIEWS is '조회수';
COMMENT ON COLUMN TIP_CONTENTS.TCON_DATE is '등록일';
COMMENT ON COLUMN TIP_CONTENTS.TCON_IMG is '이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_SAVED_IMG is '저장된이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_THUMB_IMG is 'THUMB이미지';
COMMENT ON COLUMN TIP_CONTENTS.TCON_IMG_SIZE is '이미지크기';
COMMENT ON COLUMN TIP_CONTENTS.YOUTUBE is '유튜브링크';


/**********************************/
/* Table Name: 질문이미지 */
/**********************************/
CREATE TABLE QNA_IMAGE(
		FILE_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		QCON_NO                       		INTEGER(10)		 NOT NULL,
		FILE_ORIGIN_NAME              		VARCHAR2(100)		 NULL ,
		FILE_UPLOAD_NAME              		VARCHAR2(100)		 NULL ,
		FILE_THUMB_NAME               		VARCHAR2(100)		 NULL ,
		FILE_SIZE                     		NUMBER(38)		 DEFAULT 0		 NOT NULL,
		FILE_DATE                     		DATE		 NOT NULL,
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO)
);

COMMENT ON TABLE QNA_IMAGE is '질문이미지';
COMMENT ON COLUMN QNA_IMAGE.FILE_NO is '첨부파일번호';
COMMENT ON COLUMN QNA_IMAGE.QCON_NO is '질문게시글번호';
COMMENT ON COLUMN QNA_IMAGE.FILE_ORIGIN_NAME is '원본파일명';
COMMENT ON COLUMN QNA_IMAGE.FILE_UPLOAD_NAME is '업로드파일명';
COMMENT ON COLUMN QNA_IMAGE.FILE_THUMB_NAME is 'Thumb파일명';
COMMENT ON COLUMN QNA_IMAGE.FILE_SIZE is '파일크기';
COMMENT ON COLUMN QNA_IMAGE.FILE_DATE is '등록일';


/**********************************/
/* Table Name: 질문게시글댓글 */
/**********************************/
CREATE TABLE QNA_COMMENT(
		QCMT_NO                       		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		QCON_NO                       		INTEGER(10)		 NULL ,
		QCMT_CONTENTS                 		VARCHAR2(300)		 NOT NULL,
		QCMT_DATE                     		DATE		 NOT NULL,
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE QNA_COMMENT is '질문게시글댓글';
COMMENT ON COLUMN QNA_COMMENT.QCMT_NO is '댓글번호';
COMMENT ON COLUMN QNA_COMMENT.ACC_NO is '회원번호';
COMMENT ON COLUMN QNA_COMMENT.QCON_NO is '질문게시글번호';
COMMENT ON COLUMN QNA_COMMENT.QCMT_CONTENTS is '내용';
COMMENT ON COLUMN QNA_COMMENT.QCMT_DATE is '등록일';


/**********************************/
/* Table Name: 장영은 */
/**********************************/
CREATE TABLE 장영은(

);

COMMENT ON TABLE 장영은 is '장영은';


/**********************************/
/* Table Name: 박은향 */
/**********************************/
CREATE TABLE 박은향(

);

COMMENT ON TABLE 박은향 is '박은향';


/**********************************/
/* Table Name: 김수진 */
/**********************************/
CREATE TABLE 김수진(

);

COMMENT ON TABLE 김수진 is '김수진';


/**********************************/
/* Table Name: 공유북마크 */
/**********************************/
CREATE TABLE SHARE_MARK(
		SMARK_NO                      		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		SCON_NO                       		INTEGER(10)		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO)
);

COMMENT ON TABLE SHARE_MARK is '공유북마크';
COMMENT ON COLUMN SHARE_MARK.SMARK_NO is '공유북마크번호';
COMMENT ON COLUMN SHARE_MARK.ACC_NO is '회원번호';
COMMENT ON COLUMN SHARE_MARK.SCON_NO is '공유게시글번호';


/**********************************/
/* Table Name: 달리AI */
/**********************************/
CREATE TABLE DALLE(
		DALLE_NO                      		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		DALLE_ORIGIN                  		VARCHAR2(100)		 NOT NULL,
		DALLE_THUMB                   		VARCHAR2(100)		 NOT NULL,
		DALLE_SIZE                    		NUMBER(38)		 DEFAULT 0		 NOT NULL,
		DDATE                         		DATE		 NOT NULL,
		PROMPT                        		VARCHAR2(300)		 NOT NULL,
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE DALLE is '달리AI';
COMMENT ON COLUMN DALLE.DALLE_NO is '달리 번호';
COMMENT ON COLUMN DALLE.ACC_NO is '회원번호';
COMMENT ON COLUMN DALLE.DALLE_ORIGIN is 'AI이미지원본';
COMMENT ON COLUMN DALLE.DALLE_THUMB is 'AI이미지썸네일';
COMMENT ON COLUMN DALLE.DALLE_SIZE is 'AI이미지용량';
COMMENT ON COLUMN DALLE.DDATE is '등록일';
COMMENT ON COLUMN DALLE.PROMPT is '생성어';


/**********************************/
/* Table Name: 질문대댓글 */
/**********************************/
CREATE TABLE QNA_RECOMMENT(
		QRECMT_NO                     		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		QCMT_NO                       		INTEGER(10)		 NOT NULL,
		QCON_NO                       		INTEGER(10)		 NOT NULL,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		QRECMT_CONTENTS               		VARCHAR2(300)		 NOT NULL,
		QRECMT_DATE                   		DATE		 NOT NULL,
  FOREIGN KEY (QCMT_NO) REFERENCES QNA_COMMENT (QCMT_NO),
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE QNA_RECOMMENT is '질문대댓글';
COMMENT ON COLUMN QNA_RECOMMENT.QRECMT_NO is '질문대댓글번호';
COMMENT ON COLUMN QNA_RECOMMENT.QCMT_NO is '댓글번호';
COMMENT ON COLUMN QNA_RECOMMENT.QCON_NO is '질문게시글번호';
COMMENT ON COLUMN QNA_RECOMMENT.ACC_NO is '회원번호';
COMMENT ON COLUMN QNA_RECOMMENT.QRECMT_CONTENTS is '내용';
COMMENT ON COLUMN QNA_RECOMMENT.QRECMT_DATE is '등록일';


/**********************************/
/* Table Name: 공유대댓글 */
/**********************************/
CREATE TABLE SHARE_RECOMMENT(
		SRECMT_NO                     		INTEGER(10)		 NOT NULL		 PRIMARY KEY,
		SCMT_NO                       		INTEGER(10)		 NOT NULL,
		SCON_NO                       		INTEGER(10)		 NOT NULL,
		ACC_NO                        		INTEGER(10)		 NOT NULL,
		SRECMT_CONTENTS               		VARCHAR2(300)		 NOT NULL,
		SRECMT_DATE                   		DATE		 NOT NULL,
  FOREIGN KEY (SCMT_NO) REFERENCES SHARE_COMMENT (SCMT_NO),
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);

COMMENT ON TABLE SHARE_RECOMMENT is '공유대댓글';
COMMENT ON COLUMN SHARE_RECOMMENT.SRECMT_NO is '질문대댓글번호';
COMMENT ON COLUMN SHARE_RECOMMENT.SCMT_NO is '댓글번호';
COMMENT ON COLUMN SHARE_RECOMMENT.SCON_NO is '공유게시글번호';
COMMENT ON COLUMN SHARE_RECOMMENT.ACC_NO is '회원번호';
COMMENT ON COLUMN SHARE_RECOMMENT.SRECMT_CONTENTS is '내용';
COMMENT ON COLUMN SHARE_RECOMMENT.SRECMT_DATE is '등록일';



