package dev.mvc.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.category.CategoryProc")
public class CategoryProc implements CategoryProcInter {

  @Autowired
  private CategoryDAOInter categoryDAO;
  
  public CategoryProc() {
    System.out.println("-> CategoryProc created.");
  }
  
  @Override
  public int cate_create(CategoryVO categoryVO) {
    int cnt = this.categoryDAO.cate_create(categoryVO);
    
    return cnt;
  }
  
  @Override
  public ArrayList<CategoryVO> cate_list_all() {
    ArrayList<CategoryVO> list = this.categoryDAO.cate_list_all();
    
    return list;
  }
  
  @Override
  public CategoryVO cate_read(int cate_no) {
    CategoryVO categoryVO = this.categoryDAO.cate_read(cate_no);
    
    return categoryVO;
  }
  
  @Override
  public int cate_update(CategoryVO categoryVO) {
    int cnt = this.categoryDAO.cate_update(categoryVO);
    
    return cnt;
  }
  
  @Override
  public int cate_delete(int cate_no) {
    int cnt = this.categoryDAO.cate_delete(cate_no);
    
    return cnt;
  }
  
  @Override
  public int cate_update_seqno_forward(int cate_no) {
    int cnt =this.categoryDAO.cate_update_seqno_forward(cate_no);
    
    return cnt;
  }
  
  @Override
  public int cate_update_seqno_backward(int cate_no) {
    int cnt =this.categoryDAO.cate_update_seqno_backward(cate_no);
    
    return cnt;
  }
  
  @Override
  public int cate_update_visible_y(int cate_no) {
    int cnt = this.categoryDAO.cate_update_visible_y(cate_no);
    
    return cnt;
  }
  
  @Override
  public int cate_update_visible_n(int cate_no) {
    int cnt = this.categoryDAO.cate_update_visible_n(cate_no);
    
    return cnt;
  }
  
  @Override
  public ArrayList<CategoryVO> list_all_cate_name_y() {
    ArrayList<CategoryVO> list = this.list_all_cate_name_y();
    
    return list;
  }
  
  @Override
  public ArrayList<CategoryVOMenu> menu() {
    // 카테고리명 저장할 객체 선언
    ArrayList<CategoryVOMenu> menu = new ArrayList<CategoryVOMenu> ();
    
    // 카테고리 목록 로딩
    ArrayList<CategoryVO> list = this.categoryDAO.list_all_cate_name_y();
    
    // 카테고리 이름 결합
    for (CategoryVO categoryVO: list) {
      
      // 하나의 카테고리명 메뉴 그룹
      CategoryVOMenu categoryVOMenu = new CategoryVOMenu();
      
      // 카테고리명 가져옴
      categoryVOMenu.setCate_name(categoryVO.getCate_name());
      
      // 메뉴 등록
      menu.add(categoryVOMenu);
    }
    
    return menu;
  }
  
  @Override
  public ArrayList<CategoryVO> cate_list_search(String word) {
    ArrayList<CategoryVO> list = this.categoryDAO.cate_list_search(word);
    
    return list;
  }
  
  @Override
  public ArrayList<CategoryVO> cate_list_search_paging(String word, int now_page, int record_per_page) {
    /*
    예) 페이지당 10개의 레코드 출력
    1 page: WHERE r >= 1 AND r <= 10
    2 page: WHERE r >= 11 AND r <= 20
    3 page: WHERE r >= 21 AND r <= 30

    페이지당 3개의 레코드 출력
    1 page: WHERE r >= 1 AND r <= 3
    2 page: WHERE r >= 4 AND r <= 6
    3 page: WHERE r >= 7 AND r <= 9
    
    페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작
    1 페이지 시작 rownum: now_page = 1, (1 - 1) * 10 --> 0 
    2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10
    3 페이지 시작 rownum: now_page = 3, (3 - 1) * 10 --> 20
    */
    int begin_of_page = (now_page - 1) * record_per_page;
   
    // 시작 rownum 결정
    // 1 페이지 = 0 + 1: 1
    // 2 페이지 = 10 + 1: 11
    // 3 페이지 = 20 + 1: 21 
    int start_num = begin_of_page + 1; 
    
    //  종료 rownum
    // 1 페이지 = 0 + 10: 10
    // 2 페이지 = 10 + 10: 20
    // 3 페이지 = 20 + 10: 30
    int end_num = begin_of_page + record_per_page;   
    /*
    1 페이지: WHERE r >= 1 AND r <= 10
    2 페이지: WHERE r >= 11 AND r <= 20
    3 페이지: WHERE r >= 21 AND r <= 30
    */
    
    // System.out.println("begin_of_page: " + begin_of_page);
    // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("word", word);
    map.put("start_num", start_num);
    map.put("end_num", end_num);
    
    ArrayList<CategoryVO> list = this.categoryDAO.cate_list_search_paging(map);
    
    return list;
  }
  
  @Override
  public int cate_list_search_count(String word) {
    int cnt = this.categoryDAO.cate_list_search_count(word);
    
    return cnt;
  }
  
  @Override
  public int cnt_plus(int cate_no) {
    int cnt = this.categoryDAO.cnt_plus(cate_no);
    
    return cnt;
  }

  @Override
  public int cnt_minus(int cate_no) {
    int cnt = this.categoryDAO.cnt_minus(cate_no);
    
    return cnt;
  }
  
  @Override
  public String cate_name(int cate_no) {
    String cate_name = this.categoryDAO.cate_name(cate_no);
    
    return cate_name;
  }
  

  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  @Override
  public String pagingBox(int now_page, String word, String list_file, int search_count, 
      int record_per_page, int page_per_block) {
    
    // 전체 페이지 수: (double)1/10 -> 0.1 -> 1 페이지, (double)12/10 -> 1.2 페이지 -> 2 페이지
    int total_page = (int)(Math.ceil((double)search_count / record_per_page));
    // 전체 그룹  수: (double)1/10 -> 0.1 -> 1 그룹, (double)12/10 -> 1.2 그룹-> 2 그룹
    int total_grp = (int)(Math.ceil((double)total_page / page_per_block)); 
    // 현재 그룹 번호: (double)13/10 -> 1.3 -> 2 그룹
    int now_grp = (int)(Math.ceil((double)now_page / page_per_block));  
    
    // 1 group: 1, 2, 3 ... 9, 10
    // 2 group: 11, 12 ... 19, 20
    // 3 group: 21, 22 ... 29, 30
    int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지  
    int end_page = (now_grp * page_per_block);               // 특정 그룹의 마지막 페이지   
     
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
    // str.append("현재 페이지: " + nowPage + " / " + totalPage + "  "); 
 
    // 이전 10개 페이지로 이동
    // now_grp: 1 (1 ~ 10 page)
    // now_grp: 2 (11 ~ 20 page)
    // now_grp: 3 (21 ~ 30 page) 
    // 현재 2그룹일 경우: (2 - 1) * 10 = 1그룹의 마지막 페이지 10
    // 현재 3그룹일 경우: (3 - 1) * 10 = 2그룹의 마지막 페이지 20
    int _now_page = (now_grp - 1) * page_per_block;  
    if (now_grp >= 2){ // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성 
      str.append("<span class='span_box_1'><A href='"+list_file+"?&word="+word+"&now_page="+_now_page+"'>◀</A></span>"); 
    } 
 
    // 중앙의 페이지 목록
    for(int i=start_page; i<=end_page; i++){ 
      if (i > total_page){ // 마지막 페이지를 넘어갔다면 페이 출력 종료
        break; 
      } 
  
      if (now_page == i){ // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
        str.append("<span class='span_box_2'>"+i+"</span>"); // 현재 페이지, 강조 
      }else{
        // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
        str.append("<span class='span_box_1'><A href='"+list_file+"?word="+word+"&now_page="+i+"'>"+i+"</A></span>");   
      } 
    } 
 
    // 10개 다음 페이지로 이동
    // nowGrp: 1 (1 ~ 10 page),  nowGrp: 2 (11 ~ 20 page),  nowGrp: 3 (21 ~ 30 page) 
    // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
    // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
    // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
    _now_page = (now_grp * page_per_block)+1; //  최대 페이지수 + 1 
    if (now_grp < total_grp){ 
      str.append("<span class='span_box_1'><A href='"+list_file+"?&word="+word+"&now_page="+_now_page+"'>▶</A></span>"); 
    } 
    str.append("</DIV>"); 
     
    return str.toString(); 
  }


  
}
