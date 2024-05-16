package dev.mvc.admin;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

public interface AdminProcInter {

  /**
   * 관리자 계정 생성
   * @param adminVO
   * @return
   */
  public int create(AdminVO adminVO);
  
  /**
   * 중복 adm_id 검사
   * @param adm_id
   * @return 중복 아이디 갯수
   */
  public int checkId(String adm_id);
  
  /**
   * 관리자 전체 목록
   * @return
   */
  public ArrayList<AdminVO> list();
  
  /**
   * adm_no로 관리자 정보 조회
   * @param adm_no
   * @return
   */
  public AdminVO read(int adm_no);
  
  /**
   * adm_id로 관리자 정보 조회
   * @param adm_id
   * @return
   */
  public AdminVO readById(String adm_id);
  
  /**
   * 로그인된 관리자 계정인지 검사
   * @param session
   * @return true: 관리자
   */
  public boolean isAdmin(HttpSession session);
  
  /**
   * 수정
   * @param adminVO
   * @return
   */
  public int update(AdminVO adminVO);
  
  /**
   * 삭제
   * @param adm_no
   * @return
   */
  public int delete(int adm_no);
  
  /**
   * 현재 패스워드 검사
   * @param map
   * @return
   */
  public int passwd_check(HashMap<String, Object> map);
  
  /**
   * 현재 패스워드 변경
   * @param map
   * @return
   */
  public int passwd_update(HashMap<String, Object> map);
  
  /**
   * 로그인
   * @param map
   * @return
   */
  public int login(HashMap<String, Object> map);
}
