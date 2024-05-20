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

  @Override
  public ArrayList<Qna_contentsVO> qna_list_all() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int qna_create(Qna_contentsVO qna_contentsVO) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public ArrayList<Qna_imageVO> qna_list_all_image() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<Qna_contentsVO> list_by_qcon_no(int qcon_no) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int qna_create_comment(HashMap<String, Object> map) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int qna_attach_create(Qna_imageVO qna_imageVO) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Qna_contentsVO qna_read(int qcon_no) {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  
}
