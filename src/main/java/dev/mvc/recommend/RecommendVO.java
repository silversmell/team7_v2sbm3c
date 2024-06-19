package dev.mvc.recommend;

import lombok.Getter;
import lombok.Setter;

/**
 * 추천 테이블 설계
 * 
CREATE TABLE RECOMMEND(
		RECOM_NO NUMBER(10) NOT NULL PRIMARY KEY,
		ACC_NO NUMBER(10)   NOT NULL,
		TAG_NO NUMBER(10)   NOT NULL,
        RECOM_SEQ NUMBER(1) NOT NULL,
        RECOM_DATE DATE     NOT NULL,
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
	/** 우선 순위 */
	private int recom_seq;
	/** 추천 생성일 */
	private String recom_date;

}
