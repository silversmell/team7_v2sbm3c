package dev.jpa.issue.employee;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController // REST 컨트롤러 선언
@RequestMapping("/employee")
public class EmployeeController {
  @Autowired
  private EmployeeService service;

  public EmployeeController() {
    System.out.println("-> EmployeeController created.");
  }
  
  /**
   * 로그인 처리
   * http://localhost:9100/employee/login_proc
   * @param employee
   * @return
   */
  @PostMapping(value = "/login_proc")
  public String login_proc(@RequestBody Employee employee) {
    System.out.println("-> id: " + employee.getId());
    System.out.println("-> passwd: " + employee.getPasswd());
    
    Optional<Employee> optional = service.find_by_id_and_passwd(employee.getId(), employee.getPasswd());
    
    JSONObject json = new JSONObject();
    if (optional.isPresent()) {
      json.put("sw", true);
      json.put("employeeno", optional.get().getEmployeeno()); // optional -> Employee -> getEmployeeno()
    } else {
      json.put("res", "NOT_FOUND");
    }
    
    return json.toString();
  }
  
  /**
   * 로그아웃
   * @return 회원 정보
   */
  @GetMapping(value="/logout")
  public String logout(HttpSession session) {
    session.invalidate();  // 모든 세션 변수 삭제
    
    JSONObject json = new JSONObject();
    json.put("logout", true); // 로그아웃 성공 여부
    json.put("sw", false);  // 로그인 여부를 저장하는 React  전역 변수와 대응
    json.put("employeeno", 0); 
    json.put("res", "LOGOUT_OK");
    
    return json.toString();
  }
  

}

