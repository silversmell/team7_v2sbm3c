package dev.mvc.category;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.category.CategoryVO;

public interface CategoryDAOInter {

  /**
   * 등록
   * insert id="create" parameterType="dev.mvc.category.CategoryVO"
   * @param categoryVO
   * @return
   */
  public int create(CategoryVO categoryVO);
  
  /**
   * 전체 목록
   * select id="list_all", resultType="dev.mvc.category.CategoryVO"
   * @return 레코드 목록
   */
  public ArrayList<CategoryVO> list_all();
  
  /**
   * 조회
   * select id="read" resultType="dev.mvc.category.CategoryVO" paparemterType="int"
   * @param cate_no
   * @return
   */
  public CategoryVO read(int cate_no);
  
  /**
   * 수정
   * update id="update" parameterType="dev.mvc.category.CategoryVO"
   * @param categoryVO
   * @return
   */
  public int update(CategoryVO categoryVO);
  
  /**
   * 삭제
   * select id="delete" parameterType="Integer"
   * @param categoryVO
   * @return
   */
  public int delete(int cate_no);
  
  /**
   * 우선순위 높임, 10 -> 1
   * update id="update_seqno_forward" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int update_seqno_forward(int cate_no);
  
  /**
   * 우선순위 낮춤, 1 -> 10
   * update id="update_seqno_backward" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int update_seqno_backward(int cate_no);
  
  /**
   * 카테고리 공개
   * update id="update_visible_y" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int update_visibe_y(int cate_no);
  
  /**
   * 카테고리 비공개
   * update id="update_visible_n" parameterType="Integer"
   * @param cate_no
   * @return
   */
  public int update_visible_n(int cate_no);
  
  /**
   * 회원/비회원에게 공개할 카테고리 목록
   * select id="list_all_y" resultType="dev.mvc.category.CategoryVO" parameterType="String"
   * @param cate_name
   * @return
   */
  public ArrayList<CategoryVO> list_all_cate_name_y(String cate_name);
  
  /**
   * 관리자용 검색 목록
   * select id="list_search" resultType="dev.mvc.category.CategoryVO" parameterType="String"
   * @param map
   * @return 조회한 레코드 목록
   */
  public ArrayList<CategoryVO> list_search(String word);    
      
  /**
   * 검색목록 페이징
   * select id="list_search_paging" resultType="dev.mvc.category.CategoryVO" parameterType="Map"
   * @param map
   * @return 조회한 레코드 목록
   */
  public ArrayList<CategoryVO> list_search_paging(Map<String, Object> map);
  
  /**
   * 검색된 레코드 수
   * @param word
   * @return
   */
  public int list_search_count(String word);
}
