package dev.mvc.admin;

import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

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
    System.out.println("-> AdminProc created.");
  }
  
  @Override
  public int checkId(String adm_id) {
    int cnt = this.adminDAO.checkId(adm_id);
    return cnt;
  }
  
  @Override
  public int create(AdminVO adminVO) {
    String adm_passwd = adminVO.getAdm_pw();
    
    int cnt = this.adminDAO.create(adminVO);
    return cnt;
  }
  
  @Override
  public ArrayList<AdminVO> list() {
    ArrayList<AdminVO> list = this.adminDAO.list();
    return list;
  }
  
  @Override
  public AdminVO read(int adm_no) {
    AdminVO adminVO = this.adminDAO.read(adm_no);
    return adminVO;
  }
  
  @Override
  public AdminVO readById(String adm_id) {
    AdminVO adminVO = this.adminDAO.readById(adm_id);
    return adminVO;
  }
  
  @Override
  public boolean isAdmin(HttpSession session) {
    boolean sw = false;
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if(grade.equals("admin")) {
        sw = true; // 로그인한 경우
      }
    }
    
    return sw;
  }
  
  @Override
  public int update(AdminVO adminVO) {
    int cnt = this.adminDAO.update(adminVO);
    return cnt;
  }
  
  @Override
  public int delete(int adm_no) {
    int cnt = this.adminDAO.delete(adm_no);
    return cnt;
  }
  
  @Override
  public int passwd_check(HashMap<String, Object> map) {
    // 패스워드 암호화
    String adm_pw = (String)map.get("adm_pw");
    adm_pw = this.security.aesEncode(adm_pw);
    map.put("adm_pw", adm_pw);
    
    int cnt = this.adminDAO.passwd_check(map);
    return cnt;
  }
  
  @Override
  public int passwd_update(HashMap<String, Object> map) {
    int cnt = this.adminDAO.passwd_update(map);
    return cnt;
  }
  
  @Override
  public int login(HashMap<String, Object> map) {
    int cnt = this.adminDAO.login(map);
    return cnt;
  }
  
  
}
