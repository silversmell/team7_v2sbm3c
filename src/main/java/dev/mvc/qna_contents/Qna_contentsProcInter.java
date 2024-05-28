package dev.mvc.qna_contents;

import java.util.ArrayList;
import java.util.HashMap;


public interface Qna_contentsProcInter {
  
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
   * qcon_no에 따른 질문글 이미지 조회
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
   * qcon_no에 따른 질문글 이미지 삭제
   * @param qcon_no
   * @return
   */
  public int qna_delete_image(int qcon_no);
  
  /**
   * qcon_no에 따른 질문글 이미지 수
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
  public ArrayList<Qna_commentVO> list_by_qcmt_no_join(int qcon_no);
  
  /**
   * 질문글 댓글 삭제
   * @param qcon_no
   * @param acc_no
   * @return
   */
  public int qna_delete_comment(HashMap<String, Object> hashMap);
  
  /**
   * 질문글 당 댓글 수
   * @param qcon_no
   * @return
   */
  public int qna_search_count_comment(int qcon_no);
  
}
