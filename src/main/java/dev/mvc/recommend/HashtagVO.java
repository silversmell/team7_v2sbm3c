package dev.mvc.recommend;

import lombok.Getter;
import lombok.Setter;

/**
 * 해시태그 테이블 설계 
 *
CREATE TABLE HASHTAG(
		TAG_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		TAG_CODE VARCHAR(2) NOT NULL,
		TAG_NAME VARCHAR(10) NOT NULL
);
*/

@Getter @Setter
public class HashtagVO {
	/** 해시태그 번호 */
	private int tag_no;
	/** 해시태그 코드 */
	private String tag_code = "";
	/** 해시태그 이름 */
	private String tag_name = "";
}
