package dev.mvc.share_contents;

import java.util.ArrayList;

public interface share_conetentsDAOInter {
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
}
