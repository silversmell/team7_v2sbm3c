package dev.mvc.category;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping("/category")
@Controller
public class CategoryCont {
  
  // 카테고리 참조
  @Autowired
  @Qualifier("dev.mvc.category.CategoryProc")
  private CategoryProcInter categoryProc;
  
  // 회원 참조
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  public CategoryCont() {
    System.out.println("-> CategoryCont created.");
  }
  
  /**
   * 등록 처리
   * http://localhost:9093/category/list_search
   * @param model
   * @param categoryVO
   * @param bindingResult
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value="/create") // http://localhost:9093/category/create
  public String create(Model model, 
                            @Valid CategoryVO categoryVO, 
                            BindingResult bindingResult,
                            @RequestParam(name="word", defaultValue = "") String word,
                            @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // 카테고리 메뉴 목록
    ArrayList<CategoryVOMenu> menu =this.categoryProc.menu();
    model.addAttribute("menu", menu);
    
    if (bindingResult.hasErrors()) {
      // 페이징 목록
      ArrayList<CategoryVO> list = this.categoryProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      // 페이징 버튼 목록
      int search_count = this.categoryProc.list_search_count(word);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      String paging = this.categoryProc.pagingBox(now_page, 
          word, "category/list_search", search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      return "category/list_search";  // /templates/category/list_search.html
    }
    
    int cnt = this.categoryProc.create(categoryVO);
    System.out.println("-> cnt: " + cnt);
    
    model.addAttribute("cnt", cnt);
    
    if (cnt == 1) { // 성공
      // model.addAttribute("code", "create_success");
      return "redirect:/category/list_search";
    } else { // 실패
      model.addAttribute("code", "create_fail");
      return "category/msg"; // /templates/category/msg.html
    }
  }
  
  /**
   * 조회 + 목록
   * @param model
   * @param cate_no 조회할 카테고리 번호
   * @param word 검색어
   * @param now_page 현제 페이지
   * @return
   */
  @GetMapping(value="/read/{cate_no}")
  public String read(Model model,
                          @PathVariable("cate_no") Integer cate_no,
                          @RequestParam(name="word", defaultValue = "") String word,
                          @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // 카테고리 메뉴 목록
    ArrayList<CategoryVOMenu> menu =this.categoryProc.menu();
    model.addAttribute("menu", menu);
    
    CategoryVO categoryVO = this.categoryProc.read(cate_no);
    model.addAttribute("categoryVO", categoryVO);
    
    // 페이징 목록
    ArrayList<CategoryVO> list = this.categoryProc.list_search_paging(word, now_page, this.record_per_page);    
    model.addAttribute("list", list);
    
    // 페이징 버튼 목록
    int search_count = this.categoryProc.list_search_count(word);
    String paging = this.categoryProc.pagingBox(now_page, 
        word, "/category/list_search", search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    model.addAttribute("word", word);
    
    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    
    return "category/read";  // /templates/category/read.html
  }
  
  
  
  /**
   * 우선순위 높임(10->1)
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/update_seqno_forward/{cate_no}")
  public String update_seqno_forward(Model model,
                                                @PathVariable("cate_no") Integer cate_no,
                                                @RequestParam(name="word", defaultValue = "") String word,
                                                @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    this.categoryProc.update_seqno_forward(now_page);
    
    return "redirect:/category/list_search?word=" + Tool.encode(word) + "&now_page=" + now_page;
  }
  
  /**
   * 우선순위 낮춤(1->10)
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/update_seqno_backward/{cate_no}")
  public String update_seqno_backward(Model model,
                                                @PathVariable("cate_no") Integer cate_no,
                                                @RequestParam(name="word", defaultValue = "") String word,
                                                @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    this.categoryProc.update_seqno_backward(now_page);
    
    return "redirect:/category/list_search?word=" + Tool.encode(word) + "&now_page=" + now_page;
  }
  
  /**
   * 카테고리 공개 설정
   * @param model
   * @param cate_no
   * @param word
   * @return
   */
  @GetMapping(value="/update_visible_y/{cate_no}")
  public String update_visible_y(Model model,
                                        @PathVariable("cate_no") Integer cate_no,
                                        @RequestParam(name="word", defaultValue = "") String word) {
    
    this.categoryProc.update_visible_y(cate_no);
    
    return "redirect:/category/list_search?word=" + Tool.encode(word);
  }
  
  /**
   * 카테고리 비공개 설정
   * @param model
   * @param cate_no
   * @param word
   * @return
   */
  @GetMapping(value="/update_visible_n/{cate_no}")
  public String update_visible_n(Model model,
                                        @PathVariable("cate_no") Integer cate_no,
                                        @RequestParam(name="word", defaultValue = "") String word) {
    
    this.categoryProc.update_visible_n(cate_no);
    
    return "redirect:/category/list_search?word=" + Tool.encode(word);
  }
  
  
  
}