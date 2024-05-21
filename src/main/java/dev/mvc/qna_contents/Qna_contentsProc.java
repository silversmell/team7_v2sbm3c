package dev.mvc.qna_contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.tool.Security;


@Service("dev.mvc.qna_contents.Qna_contentsProc")
public class Qna_contentsProc implements Qna_contentsProcInter {
  
  @Autowired
  private Qna_contentsDAOInter qna_contentsDAO;
  
  @Autowired
  Security security;

  @Override
  public int qna_create(Qna_contentsVO qna_contentsVO) {
    int cnt = this.qna_contentsDAO.qna_create(qna_contentsVO);
    
    return cnt;
  }

  @Override
  public ArrayList<Qna_contentsVO> qna_list_all() {
    ArrayList<Qna_contentsVO> list = this.qna_contentsDAO.qna_list_all();
    
    return list;
  }

  @Override
  public ArrayList<Qna_contentsVO> list_by_qcon_no(int qcon_no) {
    ArrayList<Qna_contentsVO> list = this.qna_contentsDAO.list_by_qcon_no(qcon_no);
    
    return list;
  }

  @Override
  public Qna_contentsVO qna_read(int qcon_no) {
    Qna_contentsVO qna_contentsVO = this.qna_contentsDAO.qna_read(qcon_no);
    
    return qna_contentsVO;
  }

  @Override
  public ArrayList<Qna_contentsVO> list_by_qna_search(HashMap<String, Object> hashMap) {
    ArrayList<Qna_contentsVO> list = this.qna_contentsDAO.list_by_qna_search(hashMap);
    
    return list;
  }

  @Override
  public int list_by_qna_search_count(HashMap<String, Object> hashMap) {
    int cnt = this.qna_contentsDAO.list_by_qna_search_count(hashMap);
    
    return cnt;
  }

  @Override
  public ArrayList<Qna_contentsVO> list_by_qna_search_paging(HashMap<String, Object> hashMap) {
    /*
     * 예) 페이지당 10개의 레코드 출력 1 page: WHERE r >= 1 AND r <= 10 2 page: WHERE r >= 11
     * AND r <= 20 3 page: WHERE r >= 21 AND r <= 30
     * 
     * 페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작 1 페이지 시작 rownum: now_page = 1, (1
     * - 1) * 10 --> 0 2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10 3 페이지 시작
     * rownum: now_page = 3, (3 - 1) * 10 --> 20
     */
    int begin_of_page = ((int)hashMap.get("now_page") - 1) * Contents.RECORD_PER_PAGE;

    // 시작 rownum 결정
    // 1 페이지 = 0 + 1: 1
    // 2 페이지 = 10 + 1: 11
    // 3 페이지 = 20 + 1: 21
    int start_num = begin_of_page + 1;

    // 종료 rownum
    // 1 페이지 = 0 + 10: 10
    // 2 페이지 = 10 + 10: 20
    // 3 페이지 = 20 + 10: 30
    int end_num = begin_of_page + Contents.RECORD_PER_PAGE;
    /*
     * 1 페이지: WHERE r >= 1 AND r <= 10 2 페이지: WHERE r >= 11 AND r <= 20 3 페이지: WHERE
     * r >= 21 AND r <= 30
     */

    // System.out.println("begin_of_page: " + begin_of_page);
    // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);

    hashMap.put("start_num", start_num);
    hashMap.put("end_num", end_num);

    ArrayList<Qna_contentsVO> list = this.qna_contentsDAO.list_by_qna_search_paging(hashMap);

    return list;
  }

  @Override
  public String pagingBox(int cateno, int now_page, String word, String list_file, int search_count, int record_per_page, int page_per_block) {
 // 전체 페이지 수: (double)1/10 -> 0.1 -> 1 페이지, (double)12/10 -> 1.2 페이지 -> 2 페이지
    int total_page = (int) (Math.ceil((double) search_count / record_per_page));
    // 전체 그룹 수: (double)1/10 -> 0.1 -> 1 그룹, (double)12/10 -> 1.2 그룹-> 2 그룹
    int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
    // 현재 그룹 번호: (double)13/10 -> 1.3 -> 2 그룹
    int now_grp = (int) (Math.ceil((double) now_page / page_per_block));

    // 1 group: 1, 2, 3 ... 9, 10
    // 2 group: 11, 12 ... 19, 20
    // 3 group: 21, 22 ... 29, 30
    int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지
    int end_page = (now_grp * page_per_block); // 특정 그룹의 마지막 페이지

    StringBuffer str = new StringBuffer(); // String class 보다 문자열 추가등의 편집시 속도가 빠름

    // style이 java 파일에 명시되는 경우는 로직에 따라 css가 영향을 많이 받는 경우에 사용하는 방법
    str.append("<style type='text/css'>");
    str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}");
    str.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}");
    str.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}");
    str.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}");
    str.append("  .span_box_1{");
    str.append("    text-align: center;");
    str.append("    font-size: 1em;");
    str.append("    border: 1px;");
    str.append("    border-style: solid;");
    str.append("    border-color: #cccccc;");
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/");
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
    str.append("  }");
    str.append("  .span_box_2{");
    str.append("    text-align: center;");
    str.append("    background-color: #668db4;");
    str.append("    color: #FFFFFF;");
    str.append("    font-size: 1em;");
    str.append("    border: 1px;");
    str.append("    border-style: solid;");
    str.append("    border-color: #cccccc;");
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/");
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
    str.append("  }");
    str.append("</style>");
    str.append("<DIV id='paging'>");
//    str.append("현재 페이지: " + nowPage + " / " + totalPage + "  "); 

    // 이전 10개 페이지로 이동
    // now_grp: 1 (1 ~ 10 page)
    // now_grp: 2 (11 ~ 20 page)
    // now_grp: 3 (21 ~ 30 page)
    // 현재 2그룹일 경우: (2 - 1) * 10 = 1그룹의 마지막 페이지 10
    // 현재 3그룹일 경우: (3 - 1) * 10 = 2그룹의 마지막 페이지 20
    int _now_page = (now_grp - 1) * Contents.PAGE_PER_BLOCK;
    if (now_grp >= 2) { // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성
      str.append("<span class='span_box_1'><A href='" + list_file + "?cateno=" + cateno + "&word=" + word + "&now_page=" + _now_page
          + "'>이전</A></span>");
    }

    // 중앙의 페이지 목록
    for (int i = start_page; i <= end_page; i++) {
      if (i > total_page) { // 마지막 페이지를 넘어갔다면 페이 출력 종료
        break;
      }

      if (now_page == i) { // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
        str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지, 강조
      } else {
        // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
        str.append("<span class='span_box_1'><A href='" + list_file + "?cateno=" + cateno + "&word=" + word + "&now_page=" + i + "'>" + i
            + "</A></span>");
      }
    }

    // 10개 다음 페이지로 이동
    // nowGrp: 1 (1 ~ 10 page), nowGrp: 2 (11 ~ 20 page), nowGrp: 3 (21 ~ 30 page)
    // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
    // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
    // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
    _now_page = (now_grp * Contents.PAGE_PER_BLOCK) + 1; // 최대 페이지수 + 1
    if (now_grp < total_grp) {
      str.append("<span class='span_box_1'><A href='" + list_file +  "?word=" + word + "&now_page=" + _now_page
          + "'>다음</A></span>");
    }
    str.append("</DIV>");

    return str.toString();
  }

  @Override
  public int qna_password_check(HashMap<String, Object> hashMap) {
    String passwd = (String)hashMap.get("passwd");
    passwd = this.security.aesEncode(passwd);
    hashMap.put("passwd", passwd);
    
    int cnt = this.qna_contentsDAO.qna_password_check(hashMap);
    return cnt;
  }

  @Override
  public int qna_update_text(Qna_contentsVO qna_contentsVO) {
    int cnt = this.qna_contentsDAO.qna_update_text(qna_contentsVO);
    
    return cnt;
  }

  @Override
  public int qna_delete(int qcon_no) {
    int cnt = this.qna_contentsDAO.qna_delete(qcon_no);
    
    return cnt;
  }

  @Override
  public int qna_attach_create(Qna_imageVO qna_imageVO) {
    int cnt = this.qna_contentsDAO.qna_attach_create(qna_imageVO);
    
    return cnt;
  }

  @Override
  public ArrayList<Qna_imageVO> qna_list_all_image() {
    ArrayList<Qna_imageVO> list = this.qna_contentsDAO.qna_list_all_image();
    
    return list;
  }

  @Override
  public ArrayList<Qna_imageVO> qna_read_image(int qcon_no) {
    ArrayList<Qna_imageVO> list = this.qna_read_image(qcon_no);
    
    return list;
  }
  
  @Override
  public int qna_update_file(Qna_imageVO qna_imageVO) {
    int cnt = this.qna_contentsDAO.qna_update_file(qna_imageVO);
    
    return cnt;
  }

  @Override
  public int qna_delete_image(int qcon_no) {
    int cnt = this.qna_contentsDAO.qna_delete_image(qcon_no);
    
    return cnt;
  }

  @Override
  public int qna_search_count_image(int qcon_no) {
    int cnt = this.qna_contentsDAO.qna_search_count_image(qcon_no);
    
    return cnt;
  }

  @Override
  public int qna_create_comment(HashMap<String, Object> hashMap) {
    int cnt = this.qna_contentsDAO.qna_create_comment(hashMap);
    
    return cnt;
  }

  @Override
  public ArrayList<Qna_commentVO> qna_list_all_comment() {
    ArrayList<Qna_commentVO> list = this.qna_contentsDAO.qna_list_all_comment();
    
    return list;
  }

  @Override
  public ArrayList<Qna_commentVO> list_by_qcmt_no_join(int qcon_no) {
    ArrayList<Qna_commentVO> list = this.qna_contentsDAO.list_by_qcmt_no_join(qcon_no);
    
    return list;
  }

  @Override
  public int qna_search_count_comment(int qcon_no) {
    int cnt = this.qna_contentsDAO.qna_search_count_comment(qcon_no);
    
    return cnt;
  }

  @Override
  public int qna_delete_comment(int qcon_no, int acc_no) {
    int cnt = this.qna_contentsDAO.qna_delete_comment(qcon_no, acc_no);
    
    return cnt;
  }
  
  
  
}
