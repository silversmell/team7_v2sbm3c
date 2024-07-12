package dev.jpa.sse.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account")
public class AccountVO {
    @Id
    @Column(name = "acc_no")
    private Integer accno;

    @Column(name = "acc_id")
    private String accId = "";

    @Column(name = "acc_pw")
    private String accPw = "";

    @Column(name = "acc_name")
    private String accName = "";

    @Column(name = "acc_tel")
    private String accTel = "";

    @Column(name = "acc_age")
    private String accAge = "";

    @Column(name = "acc_grade")
    private int accGrade;
}
	
