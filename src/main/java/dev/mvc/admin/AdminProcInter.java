package dev.mvc.admin;

import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

public interface AdminProcInter {

  /**
   * 관리자 생성
   * @param adminVO
   * @return
   */
  public int adm_create(AdminVO adminVO);
  
  /**
   * 관리자 조회
   * @param acc_no
   * @return
   */
  public AdminVO adm_read(int acc_no);
  
  /**
   * 로그인된 공유글 관리자 계정인지 검사
   * @param session
   * @return
   */
  public boolean isShareAdmin(HttpSession session);
  
  /**
   * 로그인된 질문글 관리자 계정인지 검사
   * @param session
   * @return
   */
  public boolean isQuestionAdmin(HttpSession session);
  
  /**
   * 로그인된 마이페이지 관리자 계정인지 검사
   * @param session
   * @return
   */
  public boolean isMyPageAdmin(HttpSession session);
  
}
