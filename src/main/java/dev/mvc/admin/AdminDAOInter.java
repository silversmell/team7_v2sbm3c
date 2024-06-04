package dev.mvc.admin;

import java.util.HashMap;

public interface AdminDAOInter {
 
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
}
