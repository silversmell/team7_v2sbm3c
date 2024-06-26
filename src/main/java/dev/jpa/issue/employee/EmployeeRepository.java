package dev.jpa.issue.employee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// Long: 식별자(PK) 필드의 타입, DAO 역활
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  
  /**
   * 직원 로그인
   * @param id
   * @param passwd
   * @return
   */
  Optional<Employee> findByIdAndPasswd(String id, String passwd);

}

