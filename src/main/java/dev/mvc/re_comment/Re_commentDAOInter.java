package dev.mvc.re_comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Re_commentDAOInter {
	
	/**
	 * 대댓글 작성
	 * @param map
	 * @return
	 */
	public int create(HashMap<String,Object>map);
	
	/**
	 * 대댓글 전체 조회
	 * @return
	 */
	public ArrayList<Re_commentVO> read();
	
	/**
	 * 해당 게시글, 해당 댓글 조회
	 * @param map
	 * @return
	 */
	public ArrayList<Re_commentVO> read_recomment(HashMap<String,Object>map);
	
	/**
	 * scmt_no에 따른 대댓글 삭제
	 * @param scmt_no
	 * @return
	 */
	public int scmtno_delete(int scmt_no);
	
	/**
	 * scon_no에 따른 대댓글 삭제
	 * @param scon_no
	 * @return
	 */
	public int sconno_delete(List<Integer>scon_no);
	
	/**
	 * 대댓글 수정
	 * @param map
	 * @return
	 */
	public int update(HashMap<String,Object>map);
	
	/**
	 * 대댓글 조회
	 * @param recomment_no
	 * @return
	 */
	public Re_commentVO read_comment(int recomment_no);
	
	/**
	 * 대댓글 삭제
	 * @param recomment_no
	 * @return
	 */
	public int delete(int recomment_no);

}
