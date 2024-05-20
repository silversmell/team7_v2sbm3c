package dev.mvc.bookmark;

import lombok.Getter;
import lombok.Setter;

/**
 * 북마크 테이블 설계
 * 
CREATE TABLE BOOKMARK(
		MARK_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		ACC_NO NUMERIC(10),
		SCON_NO NUMERIC(10),
		QCON_NO NUMERIC(10),
  FOREIGN KEY (SCON_NO) REFERENCES SHARE_CONTENTS (SCON_NO),
  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
);
*/

@Getter @Setter
public class BookmarkVO {
	/** 북마크 번호 */
	private int mark_no;
	/** 회원 번호 */
	private int acc_no;
	/** 공유게시글 번호 */
	private int scon_no;
	/** 질문게시글 번호 */
	private int qcon_no;
}
