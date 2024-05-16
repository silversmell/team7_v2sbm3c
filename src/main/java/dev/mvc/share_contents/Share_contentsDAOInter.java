package dev.mvc.share_contents;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.share_contentsdto.Contents_urlVO;
import dev.mvc.share_contentsdto.Share_commentsVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;

public interface Share_contentsDAOInter {
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
	 
	 /**
	  * 해당 게시글의 제목, 내용 수정
	  * @param share_contentsVO
	  * @return int
	  */
	 public int update_text(Share_contentsVO share_contentsVO);
	 /**
	  * 등록
	  * @param share_contentsVO
	  * @return int
	  */
	 public int create(Share_contentsVO share_contentsVO);
	 
	 /**
	  * 해당 게시글 삭제
	  * @param scon_no
	  * @return int
	  */
	 public int delete(int scon_no);
	 
	 /**
	  * 댓글 작성
	  * @param share_commentsVO
	  * @return int
	  */
	 public int create_comment(HashMap<String, Object> map);
	 
	 /**
	  * 해당 게시글의 댓글 목록
	  * @param scon_no
	  * @return int
	  */
	 public ArrayList read_comment(int scon_no);
	 
	 /**
	  * 이미지 등록
	  * @param share_imageVO
	  * @return int
	  */
	 public int create_image(Share_imageVO share_imageVO);
	 
	 /**
	  * 검색에 따른 컨텐츠 목록, 페이징
	  * @param word
	  * @return arrayList
	  */
	 public ArrayList list_by_contents_search_paging(HashMap<String,Object> map);
	 
   /**
    * 검색에 따른 컨텐츠 목록, 페이징 개수
    * @param word
    * @return int
    */
	 public int list_by_cateno_search_count(HashMap<String,Object> map);
	 
	 /**
	  * 각 게시글의 댓글 개수
	  * @param scon_no
	  * @return int count
	  */
	 public int comment_search(int scon_no);
	 
	 /**
	  * url create
	  * @param HashMap
	  * @return int
	  */
	 public int create_url(HashMap<String,Object> map);
	 
	 /**
	  * url 조회
	  * @param scon_no
	  * @return ArrayList
	  */
	 public ArrayList url_read(int scon_no);


}
