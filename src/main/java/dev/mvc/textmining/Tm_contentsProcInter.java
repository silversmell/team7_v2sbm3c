package dev.mvc.textmining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mvc.account.AccountVO;


public interface Tm_contentsProcInter {
  
  /**
   * 질문글 등록
   * @param tm_contentsVO
   * @return
   */
  public int tm_create(Tm_contentsVO tm_contentsVO);
  
  /**
   * 질문글 전체 목록
   * @return
   */
  public ArrayList<Tm_contentsVO> tm_list_all();

  /**
   * 질문글별 등록된 글 목록
   * @param qcon_no
   * @return
   */
  public ArrayList<Tm_contentsVO> list_by_tcon_no(int tcon_no);
  
  /**
   * 질문글 조회
   * @param qcon_no
   * @return
   */
  public Tm_contentsVO tm_read(int tcon_no);
  
  /**
   * 질문글별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<Tm_contentsVO> list_by_tm_search(HashMap<String, Object> hashMap);
  
  /**
   * 질문글별 검색된 레코드 개수
   * @param hashMap
   * @return
   */
  public int list_by_tm_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 질문글별 검색 목록 + 페이징
   * @param hashMap
   * @return
   */
  public ArrayList<Tm_contentsVO> list_by_tm_search_paging(HashMap<String, Object> hashMap);
  
  /**
   * 질문글 비밀번호 검사
   * @param hashMap
   * @return
   */
  public int tm_password_check(HashMap<String, Object> hashMap);
  
  /**
   * 조회수 증가
   * @param qcon_no
   * @return
   */
  public int tm_update_view(int qcon_no);
  
  /**
   * 질문글 텍스트 수정
   * @param tm_contentsVO
   * @return
   */
  public int tm_update_text(Tm_contentsVO tm_contentsVO);
  
  /**
   * 질문글 삭제
   * @param qcon_no
   * @return
   */
  public int tm_delete(int tcon_no);
  
  // 카테고리 통합이었을 때
//  /**
//   * FK cate_no 값이 같은 레코드 갯수 산출
//   * @param cate_no
//   * @return
//   */
//  public int count_by_cate_no(int cate_no);
//  
//  /**
//   * FK cate_no 값이 같은 특정 카테고리에 속한 모든 레코드 삭제
//   * @param cate_no
//   * @return
//   */
//  public int delete_by_cate_no(int cate_no);
  
  
  /**
   * 질문글 이미지 등록
   * @param tm_imageVO
   * @return
   */
  public int tm_attach_create(Tm_imageVO tm_imageVO);
  
  /**
   * 질문글 이미지 전체 목록
   * @return
   */
  public ArrayList<Tm_imageVO> tm_list_all_image();
  
  /**
   * 질문글 이미지 조회
   * @param qcon_no
   * @return
   */
  public ArrayList<Tm_imageVO> tm_read_image(int tcon_no);
  
  /**
   * 질문글 이미지 수정
   * @param tm_imageVO
   * @return
   */
  public int tm_update_file(Tm_imageVO tm_imageVO);
  
  /**
   * 질문글 이미지 삭제
   * @param tcon_no
   * @return
   */
  public int tm_delete_image(int tcon_no);
  
  /**
   * 질문글 이미지 수
   * @param tcon_no
   * @return
   */
  public int tm_search_count_image(int tcon_no);
  
  /**
   * 질문글 댓글 등록
   * @param tm_commentVO
   * @return
   */
  public int tm_create_comment(Tm_commentVO tm_commentVO);
  
  /**
   * 질문글 댓글 전체 목록
   * @return
   */
  public ArrayList<Tm_commentVO> tm_list_all_comment();
  
  /**
   * 질문글에 따른 댓글 목록
   * @param tcon_no
   * @return
   */
  public List<Tm_Acc_commentVO> list_by_tcmt_no_join(int tcon_no);
  
  /**
   * 최신 댓글 500건
   * @param tcon_no
   * @return
   */
  public List<Tm_Acc_commentVO> list_by_tcmt_no_join_500(int tcon_no);
  
  /**
   * 작성순 댓글 500건
   * @param tcon_no
   * @return
   */
  public List<Tm_Acc_commentVO> asc_list_by_tcmt_no_join_500(int tcon_no);
  
  /**
   * 질문글에 따른 댓글 조회
   * @param tcmt_no
   * @return
   */
  public Tm_commentVO tm_read_comment(int tcmt_no);
  
  /**
   * 질문글 댓글 수정
   * @param tm_commentVO
   * @return
   */
  public int tm_update_comment(Tm_commentVO tm_commentVO);
  
  /**
   * 질문글 댓글 삭제
   * @param tcmt_no
   * @return
   */
  public int tm_delete_comment(int tcmt_no);
  
  /**
   * 질문글 전체 댓글 삭제
   * @param tcmt_no
   * @return
   */
  public int all_tm_delete_comment(int tcon_no);
  
  /**
   * 질문글 댓글 수
   * @param tcon_no
   * @return
   */
  public int tm_search_count_comment(int tcon_no);
   
  /**
   * 특정 질문글 북마크 수
   * @param tcon_no
   * @return
   */
  public int bookmark_count(int tcon_no);
  
  /**
   * 특정 질문글 북마크하였는지 확인
   * @param map
   * @return
   */
  public ArrayList<Tm_markVO> is_bookmarked(HashMap<String, Object> map);
  
  /**
   * 질문글 북마크 등록
   * @param map
   * @return
   */
  public int bookmark_create(HashMap<String, Object> map);
  
  /**
   * 특정 질문글 북마크 삭제
   * @param map
   * @return
   */
  public int bookmark_delete(HashMap<String, Object> map);
  
  /**
   * 특정 질문글 북마크 전체 삭제
   * @param map
   * @return
   */
  public int all_bookmark_delete(int tcon_no);
  
  /**
   * 특정 질문글 북마크 공개 모드
   * @param tcon_no
   * @param acc_no
   * @return
   */
  public int bookmark_y(HashMap<String, Object> map);
  
  /**
   * 질문글 북마크 비공개 모드
   * @param tcon_no
   * @param acc_no
   * @return
   */
  public int bookmark_n(HashMap<String, Object> map);
  
  /**
   * 작성자
   * @param map
   * @return
   */
  public String user_name(HashMap<String, Object> map);
  
  
  /**
   * tcon_no에 따른 선택 삭제
   * @param tcon_no
   * @return
   */
  public int delete_tconno(List<Integer> tcon_no);
  
  /**
   * tcon_no에 따른 선택 이미지 삭제
   * @param tcon_no
   * @return
   */
  public int delete_tconno_image(List<Integer> tcon_no);
  
  /**
   * tcon_no에 따른 선택 북마크 삭제
   * @param tcon_no
   * @return
   */
  public int delete_tconno_bookmark(List<Integer> tcon_no);
   
  /**
   * tcon_no에 따른 선택 댓글 삭제
   * @param tcon_no
   * @return
   */
  public int delete_tconno_comment(List<Integer> tcon_no);
  
  /**
   * tcon_no에 따른 선택 대댓글 삭제
   * @param tcon_no
   * @return
   */
  public int delete_tconno_recomment(List<Integer> tcon_no);
  
  /**
   * 질문글 대댓글 작성
   * @param map
   * @return
   */
  public int tm_create_recomment(HashMap<String, Object> map);
  
  /**
   * 질문글 대댓글 전체 조회
   * @return
   */
  public ArrayList<Tm_recommentVO> tm_read_recomment_all();
  
  /**
   * 해당 대댓글에 대한 질문글 보기
   * @param map
   * @return
   */
  public ArrayList<Tm_recommentVO> tm_read_recomment(HashMap<String, Object> map);
  
  /**
   * 질문글 특정 대댓글 삭제
   * @param tcmt_no
   * @return
   */
  public int delete_tcmtno_recomment(int tcmt_no);
  
  /**
   * 질문글 특정 대댓글 수정
   * @param map
   * @return
   */
  public int tm_update_recomment(HashMap<String, Object> map);
  
  /**
   * 질문글 특정 대댓글 조회
   * @param tcmt_no
   * @return
   */
  public Tm_recommentVO read_recomment(int tcmt_no);
  
  /**
   * 질문글 특정 대댓글 삭제
   * @param trecmt_no
   * @return
   */
  public int tm_delete_recomment(int trecmt_no);
  
  /**
   * 질문글 전체 대댓글 삭제
   * @param tcmt_no
   * @return
   */
  public int all_tm_delete_recomment(int tcon_no);
  
  /**
   * 선택한 답글의 회원 아이디 조회
   * @param trecmt_no
   * @return
   */
  public Tm_recommentVO select_acc_id(int trecmt_no);
  
  /**
   * 질문글 대댓글 수
   * @param tcon_no
   * @return
   */
  public int tm_search_count_recomment(int tcmt_no);
  
  /**
   * 글 작성한 회원의 프로필 사진
   * @param tcon_no
   * @return
   */
  public AccountVO acc_profile_img(int tcon_no);
  
  /**
   * 댓글 작성한 회원의 프로필 사진
   * @param tcmt_no
   * @return
   */
  public AccountVO acc_profile_img_by_tcmt_no(int qcmt_no);

  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   * @param cate_no  카테고리 번호
   * @param now_page  현재 페이지
   * @param now_page 
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int cate_no, int now_page, String word, String list_file, int search_count, 
                                      int record_per_page, int page_per_block);   

  
}
