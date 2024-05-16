package dev.mvc.qna_contents;

import java.util.ArrayList;

public interface Qna_contentsProcInter {
  
  /**
   * 질문게시글 등록
   * insert id="qna_create" parameterType="dev.mvc.qna_contents"
   * @param qna_contentsVO
   * @return
   */
  public int create(Qna_contentsVO qna_contentsVO);

  /**
   * 질문게시글 전체 목록
   * insert id="qna_list_all" resultTyle="dev.mvc.qna_contents.Qna_contentsVO"
   * @return
   */
  public ArrayList<Qna_contentsVO> list_all();
}
