package dev.jpa.issue.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
  private final EmployeeRepository repository;

  /** 생성자 */
  @Autowired
  public EmployeeService(EmployeeRepository repository) {
    this.repository = repository;
  }

  /**
   * 직원 로그인
   * 
   * @param id
   * @param passwd
   * @return
   */
  public Optional<Employee> find_by_id_and_passwd(String id, String passwd) {
    return repository.findByIdAndPasswd(id, passwd);
  }

}
