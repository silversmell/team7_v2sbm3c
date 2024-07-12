package dev.mvc.tip_contents;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

/**
 * 팁 게시글 테이블 설계
 * 
CREATE TABLE TIP_CONTENTS(
		TCON_NO                       	    NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		CATE_NO                       		NUMBER(10)		 NOT NULL,
		ACC_NO                        		NUMBER(10)		 NOT NULL,
		TCON_TITLE                     		VARCHAR2(100)		 NOT NULL,
		TCON_CONTENTS                 		VARCHAR2(3000)		 NOT NULL,
		TCON_VIEWS                    		NUMBER(7)		 DEFAULT 0		 NOT NULL,
		TCON_DATE                     		DATE		 NOT NULL,
		TCON_IMG                      		VARCHAR2(100)		 NULL ,
		TCON_SAVED_IMG                		VARCHAR2(100)		 NULL ,
		TCON_THUMB_IMG                		VARCHAR2(100)		 NULL ,
		TCON_IMG_SIZE                 		NUMBER(38)		 NULL ,
        TCON_PASSWD                         VARCHAR2(30)    NOT NULL,
        WORD                                VARCHAR2(30)    NULL,
		YOUTUBE                       		VARCHAR2(1000)		 NULL ,
  FOREIGN KEY (CATE_NO) REFERENCES CATEGORY (CATE_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);
*/

@Setter @Getter
public class TipContentsVO {
	/** 팁 게시글 번호 */
	private int tcon_no;
	/** 카테고리 번호 */
	private int cate_no;
	/** 회원 번호 */
	private int acc_no;
	
	/** 제목 */
	private String tcon_title = "";
	/** 내용 */
	private String tcon_contents = "";
	/** 조회수 */
	private int tcon_views = 0;
	/** 등록일 */
	private String tcon_date = "";
	
	/** 업로드 이미지 파일 */
	private MultipartFile img_mf = null;
	/** 메인 이미지 크기 단위, 파일 크기 */
	private String img_size_label = "";
	/** 이미지 */
	private String tcon_img = "";
	/** 저장된 이미지 */
	private String tcon_saved_img = "";
	/** thumb 이미지 */
	private String tcon_thumb_img = "";
	/** 이미지 크기 */
	private long tcon_img_size = 0;
	
	/** 비밀번호 */
	private String tcon_passwd = "";
	/** 검색어 */
	private String word = "";

	/** 유튜브 링크 */
	private String youtube = "";
}
