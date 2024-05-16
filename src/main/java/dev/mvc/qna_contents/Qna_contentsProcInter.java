package dev.mvc.qna_contents;

public interface Qna_contentsProcInter {
  
  /**
   * 질문게시글 등록
   * insert id="qna_create" parameterType="dev.mvc.
   * @param qna_contentsVO
   * @return
   */
  public int create(Qna_contentsVO qna_contentsVO);

  
  public ArrayList<Qna_contentsVO> read(qcon_no);
}
