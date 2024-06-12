package dev.mvc.rank;

import java.util.ArrayList;
import java.util.HashMap;

public interface RankDAOInter {
	 /**
	  * 검색에 따른 컨텐츠 목록, 페이징
	  * @param word
	  * @return arrayList
	  */
	 public ArrayList ranking();
	 
	 /**
	  * 랭킹에 따른 해시태그 분류
	  * @return
	  */
	 public ArrayList ranking_tag(int tag_no);
	 
	 /**
	  * 랭킹에 따른 해시태그 갯수
	  * @param tag_no
	  * @return
	  */
	 public int ranking_tag_count(int tag_no);
	 
	 /**
	  * 랭킹에 따른 해시태그 분류
	  * @param tag_no
	  * @return
	  */
	 public ArrayList ranking_tag_list(int tag_no);


}
