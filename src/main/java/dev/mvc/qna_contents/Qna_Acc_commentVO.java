package dev.mvc.qna_contents;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ToString
public class Qna_Acc_commentVO {
  
  private String id = "";
  
  private int qcmt_no;
  
  private int qcon_no;
  
  private String qcmt_contents;
  
  private String qcmt_date;
}
