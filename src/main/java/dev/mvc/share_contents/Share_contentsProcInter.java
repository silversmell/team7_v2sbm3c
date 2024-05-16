package dev.mvc.share_contents;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.share_contentsdto.Contents_urlVO;
import dev.mvc.share_contentsdto.Share_commentsVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;

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
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param cateno 카테고리 번호
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int now_page, String word, String list_file, int search_count, 
                                      int record_per_page, int page_per_block);   
  
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
	 
	 /**
	  * url만 갖고옴
	  * @param scon_no
	  * @return 
	  */
	 public ArrayList only_url(int scon_no);





}
