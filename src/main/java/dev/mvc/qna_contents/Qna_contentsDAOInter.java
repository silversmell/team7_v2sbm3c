package dev.mvc.qna_contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.mvc.account.AccountVO;

public interface Qna_contentsDAOInter {

  /**
   * 질문글 등록
   * @param qna_contentsVO
   * @return
   */
  public int qna_create(Qna_contentsVO qna_contentsVO);
  
  /**
   * 질문글 전체 목록
   * @return
   */
  public ArrayList<Qna_contentsVO> qna_list_all();

  /**
   * 질문글별 등록된 글 목록
   * @param qcon_no
   * @return
   */
  public ArrayList<Qna_contentsVO> list_by_qcon_no(int qcon_no);
  
  /**
   * 질문글 조회
   * @param qcon_no
   * @return
   */
  public Qna_contentsVO qna_read(int qcon_no);
  
  /**
   * 질문글별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<Qna_contentsVO> list_by_qna_search(HashMap<String, Object> hashMap);
  
  /**
   * 질문글별 검색된 레코드 개수
   * @param hashMap
   * @return
   */
  public int list_by_qna_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 질문글별 검색 목록 + 페이징
   * @param hashMap
   * @return
   */
  public ArrayList<Qna_contentsVO> list_by_qna_search_paging(HashMap<String, Object> hashMap);
  
  /**
   * 질문글 비밀번호 검사
   * @param hashMap
   * @return
   */
  public int qna_password_check(HashMap<String, Object> hashMap);
  
  /**
   * 조회수 증가
   * @param qcon_no
   * @return
   */
  public int qna_update_view(int qcon_no);
  
  /**
   * 질문글 텍스트 수정
   * @param qna_contentsVO
   * @return
   */
  public int qna_update_text(Qna_contentsVO qna_contentsVO);
  
  /**
   * 질문글 삭제
   * @param qcon_no
   * @return
   */
  public int qna_delete(int qcon_no);
  
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
   * @param qna_imageVO
   * @return
   */
  public int qna_attach_create(Qna_imageVO qna_imageVO);
  
  /**
   * 질문글 이미지 전체 목록
   * @return
   */
  public ArrayList<Qna_imageVO> qna_list_all_image();
  
  /**
   * 질문글 이미지 조회
   * @param qcon_no
   * @return
   */
  public ArrayList<Qna_imageVO> qna_read_image(int qcon_no);
  
  /**
   * 질문글 이미지 수정
   * @param qna_imageVO
   * @return
   */
  public int qna_update_file(Qna_imageVO qna_imageVO);
  
  /**
   * 질문글 이미지 삭제
   * @param qcon_no
   * @return
   */
  public int qna_delete_image(int qcon_no);
  
  /**
   * 질문글 이미지 수
   * @param qcon_no
   * @return
   */
  public int qna_search_count_image(int qcon_no);
  
  /**
   * 질문글 댓글 등록
   * @param qna_commentVO
   * @return
   */
  public int qna_create_comment(Qna_commentVO qna_commentVO);
  
  /**
   * 질문글 댓글 전체 목록
   * @return
   */
  public ArrayList<Qna_commentVO> qna_list_all_comment();
  
  /**
   * 질문글에 따른 댓글 목록
   * @param qcon_no
   * @return
   */
  public List<Qna_Acc_commentVO> list_by_qcmt_no_join(int qcon_no);
  
  /**
   * 최신 댓글 500건
   * @param qcon_no
   * @return
   */
  public List<Qna_Acc_commentVO> list_by_qcmt_no_join_500(int qcon_no);
  
  /**
   * 작성순 댓글 500건
   * @param qcon_no
   * @return
   */
  public List<Qna_Acc_commentVO> asc_list_by_qcmt_no_join_500(int qcon_no);
  
  /**
   * 질문글에 따른 댓글 조회
   * @param qcmt_no
   * @return
   */
  public Qna_commentVO qna_read_comment(int qcmt_no);
  
  /**
   * 질문글 댓글 수정
   * @param qna_commentVO
   * @return
   */
  public int qna_update_comment(Qna_commentVO qna_commentVO);
  
  /**
   * 질문글 댓글 삭제
   * @param qcmt_no
   * @return
   */
  public int qna_delete_comment(int qcmt_no);
  
  /**
   * 질문글 전체 댓글 삭제
   * @param qcmt_no
   * @return
   */
  public int all_qna_delete_comment(int qcon_no);
  
  /**
   * 질문글 댓글 수
   * @param qcon_no
   * @return
   */
  public int qna_search_count_comment(int qcon_no);
   
  /**
   * 특정 질문글 북마크 수
   * @param qcon_no
   * @return
   */
  public int bookmark_count(int qcon_no);
  
  /**
   * 특정 질문글 북마크하였는지 확인
   * @param map
   * @return
   */
  public ArrayList<Qna_markVO> is_bookmarked(HashMap<String, Object> map);
  
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
  public int all_bookmark_delete(int qcon_no);
  
  /**
   * 특정 질문글 북마크 공개 모드
   * @param qcon_no
   * @param acc_no
   * @return
   */
  public int bookmark_y(HashMap<String, Object> map);
  
  /**
   * 질문글 북마크 비공개 모드
   * @param qcon_no
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
   * qcon_no에 따른 선택 삭제
   * @param qcon_no
   * @return
   */
  public int delete_qconno(List<Integer> qcon_no);
  
  /**
   * qcon_no에 따른 선택 이미지 삭제
   * @param qcon_no
   * @return
   */
  public int delete_qconno_image(List<Integer> qcon_no);
  
  /**
   * qcon_no에 따른 선택 북마크 삭제
   * @param qcon_no
   * @return
   */
  public int delete_qconno_bookmark(List<Integer> qcon_no);
   
  /**
   * qcon_no에 따른 선택 댓글 삭제
   * @param qcon_no
   * @return
   */
  public int delete_qconno_comment(List<Integer> qcon_no);
  
  /**
   * qcon_no에 따른 선택 대댓글 삭제
   * @param qcon_no
   * @return
   */
  public int delete_qconno_recomment(List<Integer> qcon_no);
  
  /**
   * 질문글 대댓글 작성
   * @param map
   * @return
   */
  public int qna_create_recomment(HashMap<String, Object> map);
  
  /**
   * 질문글 대댓글 전체 조회
   * @return
   */
  public ArrayList<Qna_recommentVO> qna_read_recomment_all();
  
  /**
   * 해당 대댓글에 대한 질문글 보기
   * @param map
   * @return
   */
  public ArrayList<Qna_recommentVO> qna_read_recomment(HashMap<String, Object> map);
  
  /**
   * 질문글 특정 대댓글 삭제
   * @param qcmt_no
   * @return
   */
  public int delete_qcmtno_recomment(int qcmt_no);
  
  /**
   * 질문글 특정 대댓글 수정
   * @param map
   * @return
   */
  public int qna_update_recomment(HashMap<String, Object> map);
  
  /**
   * 질문글 특정 대댓글 조회
   * @param qcmt_no
   * @return
   */
  public Qna_recommentVO read_recomment(int qcmt_no);
  
  /**
   * 질문글 특정 대댓글 삭제
   * @param qrecmt_no
   * @return
   */
  public int qna_delete_recomment(int qrecmt_no);
  
  /**
   * 질문글 전체 대댓글 삭제
   * @param qcmt_no
   * @return
   */
  public int all_qna_delete_recomment(int qcon_no);
  
  /**
   * 선택한 답글의 회원 아이디 조회
   * @param qrecmt_no
   * @return
   */
  public Qna_recommentVO select_acc_id(int qrecmt_no);
  
  /**
   * 질문글 대댓글 수
   * @param qcon_no
   * @return
   */
  public int qna_search_count_recomment(int qcmt_no);
  
  /**
   * 글 작성한 회원의 프로필 사진
   * @param qcon_no
   * @return
   */
  public AccountVO acc_profile_img(int qcon_no);
  
  /**
   * 댓글 작성한 회원의 프로필 사진
   * @param qcmt_no
   * @return
   */
  public AccountVO acc_profile_img_by_qcmt_no(int qcmt_no);
  
  /**
   * 프롬포트 실시간 생성어 
   * @param map
   * @return
   */
  public ArrayList<Qna_dalleVO> get_prompt(HashMap <String, Object> map);
  
  /**
   * 전체 요약 로그 실시간 조회
   * @param map
   * @return
   */
  public ArrayList<Qna_summaryVO> get_summary_logs(HashMap <String, Object> map);
  
  /**
   * 본인 요약 로그 실시간 조회
   * @param map
   * @return
   */
  public ArrayList<Qna_summaryVO> get_summary_log(HashMap <String, Object> map);
  
  /**
   * 전체 이미지 로그 실시간 조회
   * @param map
   * @return
   */
  public ArrayList<Qna_dalleVO> get_img_logs(HashMap <String, Object> map);
  
  /**
   * 본인 이미지 로그 실시간 조회
   * @param map
   * @return
   */
  public ArrayList<Qna_dalleVO> get_img_log(HashMap <String, Object> map);
  
}
