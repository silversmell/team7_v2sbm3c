package dev.mvc.textmining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.account.AccountVO;
import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;


@Service("dev.mvc.textmining.Tm_contentsProc")
public class Tm_contentsProc implements Tm_contentsProcInter {
  
  @Autowired
  private Tm_contentsDAOInter tm_contentsDAO;
  
  @Autowired
  Security security;

  @Override
  public int tm_create(Tm_contentsVO tm_contentsVO) {
    int cnt = this.tm_contentsDAO.tm_create(tm_contentsVO);
    
    return cnt;
  }

  @Override
  public ArrayList<Tm_contentsVO> tm_list_all() {
    ArrayList<Tm_contentsVO> list = this.tm_contentsDAO.tm_list_all();
    
    return list;
  }

  @Override
  public ArrayList<Tm_contentsVO> list_by_tcon_no(int tcon_no) {
    ArrayList<Tm_contentsVO> list = this.tm_contentsDAO.list_by_tcon_no(tcon_no);
    
    return list;
  }

  @Override
  public Tm_contentsVO tm_read(int tcon_no) {
    Tm_contentsVO tm_contentsVO = this.tm_contentsDAO.tm_read(tcon_no);
    
    return tm_contentsVO;
  }

  @Override
  public ArrayList<Tm_contentsVO> list_by_tm_search(HashMap<String, Object> map) {
    ArrayList<Tm_contentsVO> list = this.tm_contentsDAO.list_by_tm_search(map);
    
    return list;
  }

  @Override
  public int list_by_tm_search_count(HashMap<String, Object> map) {
    int cnt = this.tm_contentsDAO.list_by_tm_search_count(map);
    
    return cnt;
  }

  @Override
  public ArrayList<Tm_contentsVO> list_by_tm_search_paging(HashMap<String, Object> map) {
    /*
     * 예) 페이지당 10개의 레코드 출력 1 page: WHERE r >= 1 AND r <= 10 2 page: WHERE r >= 11
     * AND r <= 20 3 page: WHERE r >= 21 AND r <= 30
     * 
     * 페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작 1 페이지 시작 rownum: now_page = 1, (1
     * - 1) * 10 --> 0 2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10 3 페이지 시작
     * rownum: now_page = 3, (3 - 1) * 10 --> 20
     */
    int begin_of_page = ((int)map.get("now_page") - 1) * Tcontents.RECORD_PER_PAGE;

    // 시작 rownum 결정
    // 1 페이지 = 0 + 1: 1
    // 2 페이지 = 10 + 1: 11
    // 3 페이지 = 20 + 1: 21
    int start_num = begin_of_page + 1;

    // 종료 rownum
    // 1 페이지 = 0 + 10: 10
    // 2 페이지 = 10 + 10: 20
    // 3 페이지 = 20 + 10: 30
    int end_num = begin_of_page + Tcontents.RECORD_PER_PAGE;
    /*
     * 1 페이지: WHERE r >= 1 AND r <= 10 2 페이지: WHERE r >= 11 AND r <= 20 3 페이지: WHERE
     * r >= 21 AND r <= 30
     */

    // System.out.println("begin_of_page: " + begin_of_page);
    // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);

    map.put("start_num", start_num);
    map.put("end_num", end_num);

    ArrayList<Tm_contentsVO> list = this.tm_contentsDAO.list_by_tm_search_paging(map);

    return list;
  }

  @Override
  public String pagingBox(int cate_no, int now_page, String word, String list_file, int search_count, int record_per_page, int page_per_block) {
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
    str.append("    font-size: 15px;");
    str.append("    font-weight: bold;");
    str.append("    border: none;"); // 테두리 제거
    str.append("    background: none;"); // 배경 제거
    str.append("    padding: 0 13px; /*위, 오른쪽, 아래, 왼쪽*/");
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
    str.append("    display: inline-flex;");
    str.append("    justify-content: center;");
    str.append("    align-items: center;");
    str.append("    height: 40px;");
    str.append("    border-radius: 4px;");
    str.append("    box-sizing: border-box;");
    str.append("  }");
    str.append("  .span_box_2{");
    str.append("    text-align: center;");
    str.append("    background-color: #35C5F0;");
    str.append("    color: #FFFFFF;");
    str.append("    font-size: 15px;");
    str.append("    font-weight: bold;");
    str.append("    border: 1px solid #35C5F0;"); // 테두리 추가
    str.append("    padding: 0 13px; /* 위, 오른쪽, 아래, 왼쪽 */");
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
    str.append("    display: inline-flex;");
    str.append("    justify-content: center;");
    str.append("    align-items: center;");
    str.append("    height: 40px;");
    str.append("    border-radius: 4px;");
    str.append("    box-sizing: border-box;");
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
    int _now_page = (now_grp - 1) * Tcontents.PAGE_PER_BLOCK;
    if (now_grp >= 2) { // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성
      str.append("<span class='span_box_1'><A href='" + list_file + "?cate_no=" + cate_no + "&word=" + word + "&now_page=" + _now_page
          + "'>◀</A></span>");
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
        str.append("<span class='span_box_1'><A href='" + list_file + "?cate_no=" + cate_no + "&word=" + word + "&now_page=" + i + "'>" + i
            + "</A></span>");
      }
    }

    // 10개 다음 페이지로 이동
    // nowGrp: 1 (1 ~ 10 page), nowGrp: 2 (11 ~ 20 page), nowGrp: 3 (21 ~ 30 page)
    // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
    // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
    // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
    _now_page = (now_grp * Tcontents.PAGE_PER_BLOCK) + 1; // 최대 페이지수 + 1
    if (now_grp < total_grp) {
      str.append("<span class='span_box_1'><A href='" + list_file + "?cate_no=" + cate_no +  "&word=" + word + "&now_page=" + _now_page
          + "'>▶</A></span>");
    }
    str.append("</DIV>");

    return str.toString();
  }

  @Override
  public int tm_password_check(HashMap<String, Object> hashMap) {
    String tcon_passwd = (String)hashMap.get("tcon_passwd");
    
    // Null 체크 추가
    if (tcon_passwd == null) {
        return 0; // 패스워드가 null인 경우 불일치로 처리
    }

    tcon_passwd = this.security.aesEncode(tcon_passwd);
    hashMap.put("passwd", tcon_passwd);
    
    int cnt = this.tm_contentsDAO.tm_password_check(hashMap);
    return cnt;
  }
  
  @Override
  public int tm_update_view(int tcon_no) {
    int cnt = this.tm_contentsDAO.tm_update_view(tcon_no);
    
    return cnt;
  }

  @Override
  public int tm_update_text(Tm_contentsVO tm_contentsVO) {
    int cnt = this.tm_contentsDAO.tm_update_text(tm_contentsVO);
    
    return cnt;
  }

  @Override
  public int tm_delete(int tcon_no) {
    int cnt = this.tm_contentsDAO.tm_delete(tcon_no);
    
    return cnt;
  }

  @Override
  public int tm_attach_create(Tm_imageVO tm_imageVO) {
    int cnt = this.tm_contentsDAO.tm_attach_create(tm_imageVO);
    
    return cnt;
  }

  @Override
  public ArrayList<Tm_imageVO> tm_list_all_image() {
    ArrayList<Tm_imageVO> list = this.tm_contentsDAO.tm_list_all_image();
    
    return list;
  }

  @Override
  public ArrayList<Tm_imageVO> tm_read_image(int tcon_no) {
    ArrayList<Tm_imageVO> list = this. tm_contentsDAO.tm_read_image(tcon_no);
    
    return list;
  }
  
  @Override
  public int tm_update_file(Tm_imageVO tm_imageVO) {
    int cnt = this.tm_contentsDAO.tm_update_file(tm_imageVO);
    
    return cnt;
  }

  @Override
  public int tm_delete_image(int tcon_no) {
    int cnt = this.tm_contentsDAO.tm_delete_image(tcon_no);
    
    return cnt;
  }

  @Override
  public int tm_search_count_image(int tcon_no) {
    int cnt = this.tm_contentsDAO.tm_search_count_image(tcon_no);
    
    return cnt;
  }

  @Override
  public int tm_create_comment(Tm_commentVO tm_commentVO) {
    int cnt = this.tm_contentsDAO.tm_create_comment(tm_commentVO);
    
    return cnt;
  }

  @Override
  public ArrayList<Tm_commentVO> tm_list_all_comment() {
    ArrayList<Tm_commentVO> list = this.tm_contentsDAO.tm_list_all_comment();
    
    return list;
  }

  @Override
  public List<Tm_Acc_commentVO> list_by_tcmt_no_join(int tcon_no) {
    List<Tm_Acc_commentVO> list = this.tm_contentsDAO.list_by_tcmt_no_join(tcon_no);
    String tcmt_contents = "";
    
    // 특수문자 변경
    for (Tm_Acc_commentVO tm_acc_commentVO:list) {
      tcmt_contents = tm_acc_commentVO.getTcmt_contents();
      tcmt_contents = Tool.convertChar(tcmt_contents);
      tm_acc_commentVO.setTcmt_contents(tcmt_contents);;
    }
    
    return list;
  }
  
  @Override
  public List<Tm_Acc_commentVO> list_by_tcmt_no_join_500(int tcon_no) {
    List<Tm_Acc_commentVO> list = this.tm_contentsDAO.list_by_tcmt_no_join_500(tcon_no);
    
    return list;
  }
  
  @Override
  public List<Tm_Acc_commentVO> asc_list_by_tcmt_no_join_500(int tcon_no) {
    List<Tm_Acc_commentVO> list = this.tm_contentsDAO.asc_list_by_tcmt_no_join_500(tcon_no);
    
    return list;
  }

  @Override
  public Tm_commentVO tm_read_comment(int tcmt_no) {
    Tm_commentVO tm_commentVO = this.tm_contentsDAO.tm_read_comment(tcmt_no);
    
    return tm_commentVO;
  }
  
  @Override
  public int tm_search_count_comment(int tcon_no) {
    int cnt = this.tm_contentsDAO.tm_search_count_comment(tcon_no);
    
    return cnt;
  }
  
  @Override
  public int tm_update_comment(Tm_commentVO tm_commentVO) {
    int cnt = this.tm_contentsDAO.tm_update_comment(tm_commentVO);
    
    return cnt;
  }

  @Override
  public int tm_delete_comment(int tcmt_no) {
  	int cnt = this.tm_contentsDAO.tm_delete_comment(tcmt_no);
  	
  	return cnt;
  }
  
  @Override
  public int all_tm_delete_comment(int tcon_no) {
    int cnt = this.tm_contentsDAO.all_tm_delete_comment(tcon_no);
    
    return cnt;
  }

  @Override
  public int bookmark_count(int tcon_no) {
    int cnt = this.tm_contentsDAO.bookmark_count(tcon_no);
    
    return cnt;
  }

  @Override
  public ArrayList<Tm_markVO> is_bookmarked(HashMap<String, Object> map) {
    ArrayList<Tm_markVO> list = this.tm_contentsDAO.is_bookmarked(map);
    
    return list;
  }

  @Override
  public int bookmark_create(HashMap<String, Object> map) {
    int cnt = this.tm_contentsDAO.bookmark_create(map);
    
    return cnt;
  }

  @Override
  public int bookmark_delete(HashMap<String, Object> map) {
    int cnt = this.tm_contentsDAO.bookmark_delete(map);
    
    return cnt;
  }

  @Override
  public int all_bookmark_delete(int tcon_no) {
    int cnt = this.tm_contentsDAO.all_bookmark_delete(tcon_no);
    
    return cnt;
  }

  @Override
  public int bookmark_y(HashMap<String, Object> map) {
    int cnt = this.tm_contentsDAO.bookmark_y(map);
    
    return cnt;
  }

  @Override
  public int bookmark_n(HashMap<String, Object> map) {
    int cnt = this.tm_contentsDAO.bookmark_n(map);
    
    return cnt;
  }

  @Override
  public String user_name(HashMap<String, Object> map) {
    String user_name = this.tm_contentsDAO.user_name(map);
    
    return user_name;
  }

  @Override
  public int delete_tconno(List<Integer> tcon_no) {
    int cnt = this.tm_contentsDAO.delete_tconno(tcon_no);
    
    return cnt;
  }

  @Override
  public int delete_tconno_image(List<Integer> tcon_no) {
    int cnt = this.tm_contentsDAO.delete_tconno_image(tcon_no);
    
    return cnt;
  }

  @Override
  public int delete_tconno_bookmark(List<Integer> tcon_no) {
    int cnt = this.tm_contentsDAO.delete_tconno_bookmark(tcon_no);
    
    return cnt;
  }

  @Override
  public int delete_tconno_comment(List<Integer> tcon_no) {
    int cnt = this.tm_contentsDAO.delete_tconno_comment(tcon_no);
    
    return cnt;
  }

  @Override
  public int delete_tconno_recomment(List<Integer> tcon_no) {
    int cnt = this.tm_contentsDAO.delete_tconno_recomment(tcon_no);
    
    return cnt;
  }

  @Override
  public int tm_create_recomment(HashMap<String, Object> map) {
    int cnt = this.tm_contentsDAO.tm_create_recomment(map);
    return cnt;
  }

  @Override
  public ArrayList<Tm_recommentVO> tm_read_recomment_all() {
    ArrayList<Tm_recommentVO> list = this.tm_contentsDAO.tm_read_recomment_all();
    
    return list;
  }

  @Override
  public ArrayList<Tm_recommentVO> tm_read_recomment(HashMap<String, Object> map) {
    ArrayList<Tm_recommentVO> list = this.tm_contentsDAO.tm_read_recomment(map);
    
    return list;
  }

  @Override
  public int delete_tcmtno_recomment(int tcmt_no) {
    int cnt = this.tm_contentsDAO.delete_tcmtno_recomment(tcmt_no);
    
    return cnt;
  }

  @Override
  public int tm_update_recomment(HashMap<String, Object> map) {
    int cnt = this.tm_contentsDAO.tm_update_recomment(map);
    
    return cnt;
  }

  @Override
  public Tm_recommentVO read_recomment(int tcmt_no) {
    Tm_recommentVO tm_recommentVO = this.tm_contentsDAO.read_recomment(tcmt_no);
    
    return tm_recommentVO;
  }

  @Override
  public int tm_delete_recomment(int trecmt_no) {
    int cnt = this.tm_contentsDAO.tm_delete_recomment(trecmt_no);
    
    return cnt;
  }
  
  @Override
  public int all_tm_delete_recomment(int tcon_no) {
    int cnt = this.tm_contentsDAO.all_tm_delete_recomment(tcon_no);
    
    return cnt;
  }
  
  @Override
  public int  tm_search_count_recomment(int tcmt_no) {
    int cnt = this.tm_contentsDAO.tm_search_count_recomment(tcmt_no);
    
    return cnt;
  }

  @Override
  public AccountVO acc_profile_img(int tcon_no) {
    AccountVO accountVO = this.tm_contentsDAO.acc_profile_img(tcon_no);
    
    return accountVO;
  }
  
  @Override
  public AccountVO acc_profile_img_by_tcmt_no(int tcmt_no) {
    AccountVO accountVO = this.tm_contentsDAO.acc_profile_img_by_tcmt_no(tcmt_no);
    
    return accountVO;
  }

  @Override
  public Tm_recommentVO select_acc_id(int trecmt_no) {
    Tm_recommentVO tm_recommentVO = this.tm_contentsDAO.select_acc_id(trecmt_no);
    
    return tm_recommentVO;
  }


  
 
  
}
