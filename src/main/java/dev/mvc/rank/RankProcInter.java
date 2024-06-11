package dev.mvc.rank;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.share_contentsdto.Share_contentsVO;

public interface RankProcInter {
	
	 /**
	  * 검색에 따른 컨텐츠 목록, 페이징
	  * @param word
	  * @return arrayList
	  */
	 public ArrayList ranking();

}
