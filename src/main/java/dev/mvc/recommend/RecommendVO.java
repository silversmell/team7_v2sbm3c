package dev.mvc.recommend;

import lombok.Getter;
import lombok.Setter;

/**
 * 추천 테이블 설계
 * 
CREATE TABLE RECOMMEND(
		RECOM_NO NUMERIC(10) NOT NULL PRIMARY KEY,
		ACC_NO NUMERIC(10),
		TAG_NO NUMERIC(10),
  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
  FOREIGN KEY (TAG_NO) REFERENCES HASHTAG (TAG_NO)
);
*/

@Getter @Setter
public class RecommendVO {
	/** 추천번호 */
	private int recom_no;
	/** 회원번호 */
	private int acc_no;
	/** 해시태그 번호 */
	private int tag_no;

}
