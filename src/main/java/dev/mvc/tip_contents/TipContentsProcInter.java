package dev.mvc.tip_contents;

import java.util.ArrayList;
import java.util.HashMap;

public interface TipContentsProcInter {

	/**
	 * 글 등록
	 * 
	 * @param tContentsVO
	 * @return
	 */
	public int create(TipContentsVO tContentsVO);
	
	/**
	 * youtube 등록, 수정, 삭제
	 * 
	 * @param youtube
	 * @return 수정된 레코드 갯수
	 */
	public int youtube(HashMap<String, Object> map);

	/**
	 * 카테고리별 검색된 레코드 갯수
	 * 
	 * @param map
	 * @return
	 */
	public int list_count(HashMap<String, Object> map);

	/**
	 * 검색 목록
	 * 
	 * @param map
	 * @return
	 */
	public ArrayList<TipContentsVO> list(HashMap<String, Object> map);

	/**
	 * 팁 게시글 사진 가져오기
	 * 
	 * @param tcon_no
	 * @return
	 */
	public ArrayList<TipContentsVO> tconImages(int tcon_no);
	
	/**
	 * 조회
	 * 
	 * @param tcon_no
	 * @return
	 */
	public TipContentsVO read(int tcon_no);
}
