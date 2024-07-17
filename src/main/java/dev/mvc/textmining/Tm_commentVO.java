package dev.mvc.textmining;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//CREATE TABLE TM_COMMENT(
//    TCMT_NO NUMBER(10) NOT NULL PRIMARY KEY,
//    ACC_NO NUMBER(10),  -- FK
//    TCON_NO NUMBER(10), -- FK
//    TCMT_CONTENTS VARCHAR2(300) NOT NULL,
//    TCMT_DATE DATE NOT NULL,
//  FOREIGN KEY (TCON_NO) REFERENCES TM_CONTENTS (TCON_NO),
//  FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO)
//);

@Getter @Setter
public class Tm_commentVO {

  private Integer tcmt_no;
  
  private Integer acc_no;
  
  private Integer tcon_no;
  
  @NotEmpty(message="댓글 내용은 필수입력 항목입니다,")
  @Size(min=1, max=300, message = "최소 1자에서 최대 300자(한글 100)자 가능합니다.")
  private String tcmt_contents="";
  
  private String tcmt_date;
}
