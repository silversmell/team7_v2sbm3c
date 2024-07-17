package dev.mvc.textmining;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE TM_RECOMMENT(
//    TRECMT_NO                         NUMBER(10)     NOT NULL PRIMARY KEY,
//    TCMT_NO                           NUMBER(10)     NULL ,
//    TCON_NO                           NUMBER(10)     NULL ,
//    ACC_NO                            NUMBER(10)     NULL ,
//    TRECMT_CONTENTS                   VARCHAR2(300)    NOT NULL,
//    TRECMT_DATE                       DATE     NOT NULL,
//    FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT (ACC_NO),
//    FOREIGN KEY (TCON_NO) REFERENCES TM_CONTENTS (TCON_NO),
//    FOREIGN KEY (TCMT_NO) REFERENCES TM_COMMENT (TCMT_NO)
//);

@Getter @Setter
public class Tm_recommentVO {
  
  private int trecmt_no;
  
  private int tcmt_no;
  
  private int tcon_no;
  
  private int acc_no;
  
  private String trecmt_contents;
  
  private String trecmt_date;

  private String acc_id;
}
