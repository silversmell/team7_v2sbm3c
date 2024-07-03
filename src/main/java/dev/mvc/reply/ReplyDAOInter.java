package dev.mvc.reply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ReplyDAOInter {

	/**
	 * 댓글 작성
	 * @param share_commentsVO
	 * @return int
	 */
	public int create_comment(HashMap<String, Object> map);
	
	/**
	 * 댓글 전체 목록 조회
	 * @return
	 */
	public ArrayList<Share_commentVO> list(); 
	
	/**
	 * 해당 게시글의 댓글 목록
	 * 
	 * @param scon_no
	 * @return int
	 */
	public ArrayList read_comment(int scon_no);
	
	/**
	 * 댓글 수정
	 * 
	 * @param map
	 * @return int
	 */
	public int update_comment(HashMap<String, Object> map);
	
	/**
	 * scon_no에 따른 comment 삭제 (여러개 삭제)
	 * 
	 * @param scon_no
	 * @return int
	 */
	public int sdelete_comment(List<Integer> scon_no);
	
	/**
	 * sctm_no에 따른 댓글 삭제
	 * 
	 * @param scmt_no
	 * @return
	 */
	public int delete_scmtno(int scmt_no);

	/**
	 * 해당하는 게시글 댓글 삭제
	 * 
	 * @param scon_no
	 * @return int
	 */
	public int delete_comment(int scon_no);

	/*
	 * 각 게시글의 댓글 개수
	 * 
	 * @param scon_no
	 * @return int count
	 */
	public int comment_search(int scon_no);
	
	/**
	 * scmt_no에 따른 scon_no 구하기
	 * @param scomt_no
	 * @return
	 */
	public int scon_comment(int scomt_no);
	
	/**
	 * scon_no 에 따른 출력
	 * @param scon_no
	 * @return
	 */
	public List<ReplyMemberVO> list_by_contentsno_join_500(HashMap<String,Object> map);
	
	/**
   * scon_no 에 따른 출력 작성순
   * @param scon_no
   * @return
   */
  public List<ReplyMemberVO> asc_list_by_smt_no_join_500(int scon_no);
	
	/**
	 * 댓글 번호에 따른 댓글 찾기
	 * @param scmt_no
	 * @return
	 */
	public Share_commentVO read(int scmt_no);
	/**
	 * 댓글 좋아요
	 * @param scmt_no
	 * @return
	 */
	public int like(HashMap<String,Object> map);
	
	/**
	 * 댓글 좋아요 회원 중복
	 * @param map
	 * @return
	 */
	public int like_check(HashMap<String,Object> map);
	
	/**
	 * 댓글 좋아요시 mark 'Y'로 표시
	 * @param map
	 * @return
	 */
	public int like_update(HashMap<String,Object> map);
	
	/**
	 * 댓글 좋아요시 mark 'N'로 표시
	 * @param map
	 * @return
	 */
	public int like_cancel(HashMap<String,Object> map);
	
	/**
	 * scon_no, acc_no에 따라서 좋아요 조회
	 * @param map
	 * @return
	 */
	public List<Comment_likeVO>scon_no_read(HashMap<String,Object> map);
	
	/**
	 * 댓글 삭제시 좋아요 삭제
	 * @param scmt_no
	 * @return
	 */
	public int like_delete(int scmt_no);
	
	/**
	 * 해당 게시글의 첫번째 댓글
	 * @param scon_no
	 * @return
	 */
	public int first_scmtno(int scon_no);
	
	
	
//	/**
//	 * 댓글 나빠요
//	 * @param scmt_no
//	 * @return
//	 */
//	public int dislike(int scmt_no);
//	
//	/**
//	 * 댓글 좋아요 갯수
//	 * @param scmt_no
//	 * @return
//	 */
//	public int like_count(int scmt_no);
//	
//	/**
//	 * 댓글 나빠요 갯수
//	 * @param scmt_no
//	 * @return
//	 */
//	public int dislike_count(int scmt_no);


}
