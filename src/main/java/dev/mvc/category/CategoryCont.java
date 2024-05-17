package dev.mvc.category;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@RequestMapping("/category")
@Controller
public class CategoryCont {

  @Autowired
  @Qualifier("dev.mvc.category.CategoryProc")
  private CategoryProcInter categoryProc;
  
  // @@@@회원 자리@@@@
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  public CategoryCont() {
    System.out.println("-> CategoryCont created.");
  }
  
  /**
   * 카테고리 등록 처리
   * http://localhost:9093/category/cate_list_search
   * @param model
   * @param categoryVO
   * @param bindingResult
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value="/cate_create")
  public String cate_create(Model model, @Valid CategoryVO categoryVO, BindingResult bindingResult,
                                  @RequestParam(name="word", defaultValue = "") String word,
                                  @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // 카테고리 메뉴 목록
    ArrayList<CategoryVOMenu> menu = this.categoryProc.menu();
    model.addAttribute("menu", menu);
    
    if (bindingResult.hasErrors()) {
      
      // 페이징 목록
      ArrayList<CategoryVO> list = this.categoryProc.cate_list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      // 페이징 버튼 목록
      int search_count = this.categoryProc.cate_list_search_count(word);
      
      // 일련번호 생성: 레코드 갯수 - ((현재 페이지 수 - 1) * 페이지당 레코드 수)
      int no = search_count - ((now_page -1) * this.record_per_page);
      model.addAttribute("no", no);
      
      String paging = this.categoryProc.pagingBox(now_page, 
                                                              word, 
                                                              "category/cate_list_search", 
                                                              search_count, 
                                                              this.record_per_page, 
                                                              this.page_per_block);
      
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      return "category/cate_list_search"; // /templates/category/cate_list_search.html
    }
    
    int cnt = this.categoryProc.cate_create(categoryVO);
    System.out.println("-> cnt: " + cnt);
    model.addAttribute("cnt", cnt);
    
    if (cnt == 1) { // 등록 성공
      return "redirect:/category/cate_list_search"; // /category/cate_list_search.html
    } else { // 등록 실패
      model.addAttribute("code", "cate_create_fail");
      return "category/msg"; // /templates/category/msg.html
    }  
  }
  
  /**
   * 조회 _ 목록 폼
   * http://localhost:9093/category/cate_read/1
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/cate_read/{cate_no}")
  public String cate_read(Model model,
                                @PathVariable("cate_no") Integer cate_no,
                                @RequestParam(name="word", defaultValue = "") String word,
                                @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // 카테고리 메뉴 목록
    ArrayList<CategoryVOMenu> menu = this.categoryProc.menu();
    model.addAttribute("menu", menu);
    
    // 카테고리 가져오기
    CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
    model.addAttribute("categoryVO", categoryVO);
    
    // 페이징 목록
    ArrayList<CategoryVO> list = this.categoryProc.cate_list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);
    
    // 페이징 버튼 목록
    int search_count = this.categoryProc.cate_list_search_count(word);
    
    String paging = this.categoryProc.pagingBox(now_page, 
                                                            word, 
                                                            "category/cate_list_search", 
                                                            search_count, 
                                                            this.record_per_page, 
                                                            this.page_per_block);

    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    
    model.addAttribute("word", word);
    
    // 일련번호 생성: 레코드 갯수 - ((현재 페이지 수 - 1) * 페이지당 레코드 수)
    int no = search_count - ((now_page -1) * this.record_per_page);
    model.addAttribute("no", no);
    
    return "category/cate_read"; // /templates/category/read.html
  }
  
}
