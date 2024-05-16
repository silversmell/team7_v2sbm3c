package dev.mvc.category;

import java.util.ArrayList;
import java.util.Map;

public interface CategoryProcInter {

  /**
   * 등록
   * insert id="create" parameterType="dev.mvc.category.CategoryVO"
   * @param categoryVO
   * @return 등록한 레코드 갯수
   */
  public int create(CategoryVO categoryVO);
  
  /**
   * 전체 목록
   * select id="list_all" resultType="dev.mvc.category.CategoryVO"
   * @return 레코드 목록
   */
  public ArrayList<CategoryVO> list_all();
  
  /**
   * 카테고리 조회
   * select id="read" resultType="dev.mvc.category.CategoryVO" parameterType="int"
   * @param cate_no
   * @return 
   */
  public CategoryVO read(int cate_no);
  
  /**
   * 카테고리 수정
   * update id="update" parameterType="dev.mvc.category.CategoryVO"
   * @param categoryVO
   * @return 수정된 레코드 갯수
   */
  public int update(CategoryVO categoryVO);
  
  /**
   * 카테고리 삭제
   * delete id="delete" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int delete(int cate_no);
  
  /**
   * 우선순위 높임(10 -> 1)
   * update id="update_seqno_forward" parameterType="Integer"
   * @param cate_no
   * @return 수정된 레코드 갯수
   */
  public int update_seqno_forward(int cate_no);
  
  /**
   * 우선순위 낮춤(1-> 10)
   * update id="update_seqno_backward" parameterType="Integer"
   * @param cate_no
   * @return 수정된 레코드 갯수
   */
  public int update_seqno_backward(int cate_no);
  
  /**
   * 카테고리 공개 설정
   * update id="update_visible_y" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int update_visible_y(int cate_no);
  
  /**
   * 카테고리 비공개 설정
   * update id="update_visible_n" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int update_visible_n(int cate_no);
  
  /**
   * 회원/비회원에게 공개할 카테고리 목록
   * select id="list_all_cate_name" resultType="dev.mvc.category.CategoryVO" parameterType="String"
   * @return
   */
  public ArrayList<CategoryVO> list_all_cate_name();
  
  /**
   * 메뉴
   * @return
   */
  public ArrayList<CategoryVOMenu> menu();
  
  /**
   * 관리자용 검색 목록
   * @param word
   * @return
   */
  public ArrayList<CategoryVO> list_search(String word);
  
  /**
   * 검색된 레코드 수
   * @param word 검색어
   * @return
   */
  public int list_search_count(String word);
  
  /**
   * 검색 + 페이징 목록
   * @param word 검색어
   * @param now_page 현재 페이지
   * @param record_per_page 페이지당 레코드수
   * @return
   */
  public ArrayList<CategoryVO> list_search_paging(String word, int now_page, int record_per_page);
  
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int now_page, String word, String list_file, int search_count, 
                                      int record_per_page, int page_per_block);
  
  
}
