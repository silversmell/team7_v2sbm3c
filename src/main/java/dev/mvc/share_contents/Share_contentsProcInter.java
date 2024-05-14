package dev.mvc.share_contents;

import java.util.ArrayList;

import dev.mvc.share_contentsdto.Share_contentsVO;

public interface Share_contentsProcInter {
	/**
	 * 목록
	 * @return ArrayList<share_contentsVO>
	 */
	public ArrayList list_all(); 
	
	
	/**
	 * 목록
	 * @return ArrayList<share_imageVO>
	 */
	public ArrayList list_all_image();
	
	/**
	 * 조회
	 * @param scon_no
	 * @return scon_no에 해댕하는 게시글
	 */
	public Share_contentsVO read(int scon_no);
	
	 /**
	  * 조회(read) 할 때마다 조회수 올리기
	  * @param scon_no
	  * @return int
	  */	
	public int update_view(int scon_no);
}
