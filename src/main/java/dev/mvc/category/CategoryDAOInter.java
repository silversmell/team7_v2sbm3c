package dev.mvc.category;

import java.util.ArrayList;
import java.util.Map;

public interface CategoryDAOInter {

  /**
   * 카테고리 등록
   * inseart id="cate_create" parameterType="dev.mvc.category.CategoryVO"
   * @param categoryVO
   * @return
   */
  public int cate_create(CategoryVO categoryVO);
  
  /**
   * 카테고리 전체 목록
   * select id="cate_list_all"  resultType="dev.mvc.category.CategoryVO"
   * @return
   */
  public ArrayList<CategoryVO> cate_list_all();
  
  /**
   * 카테고리 조회
   * select id="cate_read" resultType="dev.mvc.category.CategoryVO" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public CategoryVO cate_read(int cate_no);
  
  /**
   * 카테고리 수정
   * update id="cate_update" parameterType="dev.mvc.category.CategoryVO"
   * @param categoryVO
   * @return
   */
  public int cate_update(CategoryVO categoryVO);
  
  /**
   * 카테고리 삭제
   * delete id="cate_delete" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int cate_delete(int cate_no);
  
  /**
   * 우선 순위 높임, 10 등 -> 1 등
   * update id="cate_update_seqno_forward" parameterType="Integer"
   * @param cate_no
   * @return 수정한 레코드 갯수
   * */
  public int cate_update_seqno_forward(int cate_no);
  
  /**
   * 우선 순위 낮춤, 1 등 -> 10 등
   * update id="cate_update_seqno_backward" parameterType="Integer"
   * @param cate_no
   * @return 수정한 레코드 갯수
   * */
  public int cate_update_seqno_backward(int cate_no);
 
  /**
   * 카테고리 공개 설정
   * update id="cate_update_visible_y" parameterType="int"
   * @param cate_no
   * @return
   */
  public int cate_update_visible_y(int cate_no);

  /**
   * 카테고리 비공개 설정
   * update id="cate_update_visible_n" parameterType="int"
   * @param cate_no
   * @return
   */
  public int cate_update_visible_n(int cate_no);
  
  /**
   * 회원/비회원에게 공개할 카테고리 목록
   * select id="list_all_cate_name_y" resultType="dev.mvc.category.CategoryVO"
   * @return
   */
  public ArrayList<CategoryVO> list_all_cate_name_y();
  
  /**
   * 관리자용 검색 목록
   * select id="cate_list_search" resultType="dev.mvc.category.CategoryVO" parameterType="String"
   * @param word
   * @return
   */
  public ArrayList<CategoryVO> cate_list_search(String word);
  
  /**
   * 관리자용 검색 + 페이징 목록
   * select id="cate_list_search_paging" resultType="dev.mvc.category.CategoryVO" parameterType="Map"
   * @param map
   * @return
   */
  public ArrayList<CategoryVO> cate_list_search_paging(Map<String, Object> map);
  
  /**
   * 검색된 레코드 수
   * select id="cate_list_search_count" resultType="int" parameterType="String"
   * @param word
   * @return
   */
  public int cate_list_search_count(String word);
  
}
