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

import dev.mvc.account.AccountProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RequestMapping("/category")
@Controller
public class CategoryCont {

  @Autowired
  @Qualifier("dev.mvc.category.CategoryProc")
  private CategoryProcInter categoryProc;
  
  @Autowired
  @Qualifier("dev.mvc.account.AccountProc")
  private AccountProcInter accountProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  public CategoryCont() {
    System.out.println("-> CategoryCont created.");
  }
  
  
  /**
   * 카테고리 등록 + 검색 폼
   * http://localhost:9093/category/list_search?word=${word}  ← GET Form
   * @param session
   * @param model
   * @param categoeyVO
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/cate_list_search")
  public String cate_list_search(HttpSession session, Model model, CategoryVO categoeyVO, String word, 
                                      @RequestParam(name="cate_no", defaultValue = "0") int cate_no,
                                      @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
//      // 카테고리 가져오기
//      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
//      model.addAttribute("categoryVO", categoryVO);
    
    
      word = Tool.checkNull(word).trim();
      System.out.println("--> word: " + word);
      
      // 페이징 목록
      ArrayList<CategoryVO> list = this.categoryProc.cate_list_search_paging(word, now_page, this.record_per_page);    
      model.addAttribute("list", list);
      
      // 페이징 버튼 목록
      int search_count = this.categoryProc.cate_list_search_count(word);
      String paging = this.categoryProc.pagingBox(now_page, 
          word, "/category/cate_list_search", search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      model.addAttribute("word", word);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      return "category/cate_list_search"; // /category/cate_list_search.html
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
   * 카테고리 조회 + 카테고리 목록 폼
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
    
    // cate_no도 함께 전달
    model.addAttribute("cate_no", cate_no);
    
    return "category/cate_read"; // /templates/category/read.html
  }
  
  /**
   * 카테고리 수정 폼
   * @param session
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/cate_update/{cate_no}")
  public String cate_update(HttpSession session,
                               Model model,
                               @PathVariable("cate_no") Integer cate_no,
                               @RequestParam(name="word", defaultValue = "") String word,
                               @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
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
    
    return "category/cate_update"; // /templates/category/cate_update.html
  }
  
  /**
   * 카테고리 수정 처리
   * @param session
   * @param model
   * @param bindingResult
   * @param categoryVO
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value="/cate_update")
  public String cate_update(HttpSession session,
                                    Model model,
                                    @Valid CategoryVO categoryVO, BindingResult bindingResult,
                                    @RequestParam(name="word", defaultValue = "") String word,
                                    @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    if (bindingResult.hasErrors()) {
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
      
      return "category/cate_update"; // /templates/category/cate_update.html
    }
    
    int cnt = this.categoryProc.cate_update(categoryVO);
    model.addAttribute("cnt", cnt);
    
    if (cnt == 1) { // 수정 성공
      return "redirect:/category/cate_update/" + categoryVO.getCate_no() + "?word=" + Tool.encode(word) + "&now_page=" + now_page;
    } else { // 수정 실패
      model.addAttribute("code", "update_fail");
      return "category/msg"; // /templates/category/msg.html
    }  
  }
  
  /**
   * 카테고리 삭제 폼
   * @param session
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/cate_delete/{cate_no}")
  public String cate_delete(HttpSession session,
                                   Model model,
                                   @PathVariable("cate_no") Integer cate_no,
                                   @RequestParam(name="word", defaultValue = "") String word,
                                   @RequestParam(name="now_page", defaultValue = "1") int now_page) {

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
    
    return "category/cate_delete"; // /templates/category/cate_delete.html
  }
  
  /**
   * 카테고리 삭제 처리
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value="/cate_delete")
  public String cate_delete(Model model,
                                  Integer cate_no,
                                  @RequestParam(name="word", defaultValue = "") String word,
                                  @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    int cnt = this.categoryProc.cate_delete(now_page);
    model.addAttribute("cnt", cnt);
    
 // ----------------------------------------------------------------------------------------------------------
    // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
    int search_cnt = this.categoryProc.cate_list_search_count(word);
    if (search_cnt % this.record_per_page == 0) {
      now_page = now_page - 1;
      if (now_page < 1) {
        now_page = 1; // 최소 시작 페이지
      }
    }
    // ----------------------------------------------------------------------------------------------------------
    
    if (cnt == 1) { // 삭제 성공
      return "redirect:/category/cate_list_search?word=" + Tool.encode(word) + "&now_page=" + now_page;
    } else { // 삭제 실패
      model.addAttribute("code", "delete_fail");
      return "category/msg"; // /templates/category/msg.html
    }
  }                                                                               
   
  /**
   * 카테고리 출력 순위 높임: cate_seqno 10 -> 1
   * http://localhost:9093/category/cate_update_seqno_forward/1
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/cate_update_seqno_forward/{cate_no}")
  public String cate_update_seqno_forward(Model model,
                                                    @PathVariable("cate_no") Integer cate_no,
                                                    @RequestParam(name="word", defaultValue = "") String word,
                                                    @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    this.categoryProc.cate_update_seqno_forward(cate_no);
    
    return "redirect:/category/cate_list_search?word=" + Tool.encode(word) + "&now_page=" + now_page;
  }
  
  /**
   * 카테고리 출력 순위 낮춤: cate_seqno 1 -> 10
   * http://localhost:9093/category/cate_update_seqno_backward/1
   * @param model
   * @param cate_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value="/cate_update_seqno_backward/{cate_no}")
  public String cate_update_seqno_backward(Model model,
                                                    @PathVariable("cate_no") Integer cate_no,
                                                    @RequestParam(name="word", defaultValue = "") String word,
                                                    @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    this.categoryProc.cate_update_seqno_backward(cate_no);
    
    return "redirect:/category/cate_list_search?word=" + Tool.encode(word) + "&now_page=" + now_page;
  }
  
  /**
   * 카테고리 공개 설정
   * http:///localhost:9093/cateogry/cate_update_visible_y/1
   * @param model1
   * @param cate_no
   * @param word
   * @return
   */
  @GetMapping(value="/cate_update_visible_y/{cate_no}")
  public String cate_update_visible_y(Model model,
                                              @PathVariable("cate_no") Integer cate_no,
                                              @RequestParam(name="word", defaultValue = "") String word) {
    
    this.categoryProc.cate_update_visible_y(cate_no);
    
    return "redirect:/category/cate_list_search?word=" + Tool.encode(word); // /templates/category/cate_list_search
  }
  
  /**
   * 카테고리 비공개 설정
   * http:///localhost:9093/cateogry/cate_update_visible_n/1
   * @param model1
   * @param cate_no
   * @param word
   * @return
   */
  @GetMapping(value="/cate_update_visible_n/{cate_no}")
  public String cate_update_visible_n(Model model,
                                              @PathVariable("cate_no") Integer cate_no,
                                              @RequestParam(name="word", defaultValue = "") String word) {
    
    this.categoryProc.cate_update_visible_n(cate_no);
    
    return "redirect:/category/cate_list_search?word=" + Tool.encode(word); // /templates/category/cate_list_search
  }


}
