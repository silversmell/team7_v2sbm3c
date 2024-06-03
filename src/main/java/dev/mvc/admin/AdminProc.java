package dev.mvc.admin;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
import jakarta.servlet.http.HttpSession;

@Component("dev.mvc.admin.AdminProc")
public class AdminProc implements AdminProcInter {
  
  @Autowired
  private AdminDAOInter adminDAO;
  
  @Autowired
  Security security;
  
  public AdminProc() {
    System.out.println("-> AdimProc created.");
  }

  @Override
  public int adm_create(AdminVO adminVO) {
    int cnt = this.adminDAO.adm_create(adminVO);
    
    return cnt;
  }
  
  @Override
  public AdminVO adm_read(int acc_no) {
    AdminVO adminVO = this.adminDAO.adm_read(acc_no);
    
    return adminVO;
  }
  
  @Override
  public boolean isShareAdmin(HttpSession session) {
    boolean sw = false; // 로그인 여부를 나타내는 변수
    String acc_grade = (String) session.getAttribute("acc_grade");
    
    // 로그인 상태를 확인하여 sw 변수에 저장
    if (acc_grade != null && acc_grade.equals("1")) {
      sw = true; // 로그인 상태
    }
    
    return sw; // 로그인 상태 반환
  }
  
  @Override
  public boolean isQuestionAdmin(HttpSession session) {
    boolean sw = false; // 로그인 여부를 나타내는 변수
    String acc_grade = (String) session.getAttribute("acc_grade");
    Integer acc_no = (Integer)session.getAttribute("acc_no");
    
    // 로그인 상태를 확인하여 sw 변수에 저장
    if (acc_grade != null && acc_no == 2) {
      sw = true; // 로그인 상태
    }
    
    return sw; // 로그인 상태 반환
  }

  @Override
  public boolean isMyPageAdmin(HttpSession session) {
    boolean sw = false; // 로그인 여부를 나타내는 변수
    String acc_grade = (String) session.getAttribute("acc_grade");
    
    // 로그인 상태를 확인하여 sw 변수에 저장
    if (acc_grade != null && acc_grade.equals("3")) {
      sw = true; // 로그인 상태
    }
    
    return sw; // 로그인 상태 반환
  }

  
}
