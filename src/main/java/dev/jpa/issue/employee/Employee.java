package dev.jpa.issue.employee;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity @Getter @Setter @ToString
public class Employee {
  /**
   * 사원 번호, 식별자, sequence 자동 생성됨.
   * @Id: Primary Key
   */
  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="employee_seq")
  @SequenceGenerator(name="employee_seq", sequenceName="EMPLOYEE_SEQ", allocationSize=1)
  private int employeeno;
  
  /** 사원명 */
  private String id;
  
  /** 패스워드 */
  private String passwd;
  
  /** 사원명 */
  private String mname;
  
  /** 입사 날짜 */
  private String rdate;
  
  /** 사원 직책 */
  private int grade;
  
  public Employee() {
    
  }

  /**
   * 사용자로부터 입력받는 필드만 명시
   */
  public Employee(String id, String passwd, String mname, String rdate, int grade) {
    this.id = id;
    this.passwd = passwd;
    this.mname = mname;
    this.rdate = rdate;
    this.grade = grade;
  }
    
}



