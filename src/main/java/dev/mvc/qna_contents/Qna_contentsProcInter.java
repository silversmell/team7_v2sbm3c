package dev.mvc.qna_contents;

import java.util.ArrayList;
import java.util.HashMap;


public interface Qna_contentsProcInter {
 
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<Qna_contentsVO> qna_list_all();
  
  /**
   * 파일 전체 목록
   * @return
   */
  public ArrayList<Qna_imageVO> qna_list_all_image();
  
  /**
   * qcon_no에 따른 목록
   * @param qcon_no
   * @return
   */
  public ArrayList<Qna_contentsVO> list_by_qcon_no(int qcon_no);
  
  /**
   * 등록
   * @param qna_contentsVO
   * @return
   */
  public int qna_create(Qna_contentsVO qna_contentsVO);
  
  /**
   * 댓글 등록
   * @param map
   * @return
   */
  public int qna_create_comment(HashMap<String, Object> map);
  
  /** 
   * 파일 등록
   * @param qna_imageVO
   * @return
   */
  public int qna_attach_create(Qna_imageVO qna_imageVO);
  
  /**
   * 일반적인 조회
   * @param qcon_no
   * @return
   */
  public Qna_contentsVO qna_read(int qcon_no);
  
  
}
