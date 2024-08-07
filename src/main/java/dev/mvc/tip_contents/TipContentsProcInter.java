package dev.mvc.tip_contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface TipContentsProcInter {

	/**
	 * 글 등록
	 * 
	 * @param tContentsVO
	 * @return
	 */
	public int create(TipContentsVO tContentsVO);

	/**
	 * youtube
	 * 
	 * @param youtube
	 * @return 수정된 레코드 갯수
	 */
	public int youtube(HashMap<String, Object> map);

	/**
	 * 게시글 수
	 * 
	 * @param map
	 * @return
	 */
	public int list_count(HashMap<String, Object> map);

	/**
	 * 글 목록
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
	
	/**
	 * 조회수 증가 
	 * 
	 * @param tcon_no
	 * @return
	 */
	public int updateViews(int tcon_no);

	/**
	 * 글 수정 
	 * 
	 * @param tcontentsVO
	 * @return
	 */
	public int update(TipContentsVO tcontentsVO);
	
	/**
	 * 게시글 삭제
	 * 
	 * @param tcon_no
	 * @return
	 */
	public int delete(int tcon_no);
	
	/**
	 * 좋아요 상태 확인
	 * 
	 * @param map
	 * @return
	 */
	public boolean isLiked(Map<String, Object> map);
	
	/**
	 * 좋아요 저장
	 * 
	 * @param map
	 * @return
	 */
	public int insertLike(Map<String, Object> map);
	
	/** 
	 * 좋아요 삭제
	 * 
	 * @param map
	 * @return
	 */
	public int deleteLike(Map<String, Object> map);
	
	/**
	 * 좋아요 수
	 * 
	 * @param tcon_no
	 * @return
	 */
	public int like_count(int tcon_no);
	
}
