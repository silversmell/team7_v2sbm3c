package dev.mvc.qna_contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("dev.mvc.qna_contents.Qna_contentsProc")
public class Qna_contentsProc implements Qna_contentsProcInter {
  
  @Autowired
  private Qna_contentsDAOInter qna_contentsDAO;
  
  public Qna_contentsProc() {
    System.out.println("-> qna_contentsProc create");
  }
  
  
  
}
