package dev.mvc.textmining;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.MediaType;

import dev.mvc.account.AccountProc;
import dev.mvc.account.AccountVO;
import dev.mvc.admin.AdminProc;
import dev.mvc.category.CategoryProcInter;
import dev.mvc.category.CategoryVO;
import dev.mvc.category.CategoryVOMenu;
import dev.mvc.qna_contents.Qna_contentsVO;
import dev.mvc.qna_contents.Qna_imageVO;
import dev.mvc.share_contentsdto.NotificationVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import oracle.jdbc.proxy.annotation.Post;


@RequestMapping("/textmining")
@Controller
public class Tm_contentsCont {

  @Autowired
  @Qualifier("dev.mvc.category.CategoryProc")
  private CategoryProcInter categoryProc;
  
  @Autowired
  @Qualifier("dev.mvc.textmining.Tm_contentsProc")
  private Tm_contentsProc tm_contentsProc;

  @Autowired
  @Qualifier("dev.mvc.account.AccountProc")
  private AccountProc accountProc;
  
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProc adminProc;

  
//  public Qna_contentsCont() {
//    System.out.println("-> Qna_contentsCont created.");
//  }
  
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리완료 -> redirect -> url -> GET -> forward -> html
   * POST → url → GET → 데이터 전송
   * @return
   */
  @GetMapping(value="/msg")
  public String msg(Model model, String url){
    
    // 카테고리 전체 메뉴
    ArrayList<CategoryVOMenu> menu = new ArrayList<CategoryVOMenu>();
    model.addAttribute("menu", menu);

    return url; // /forward, /templates/...
  }

  /**
   * 전체 목록, 관리자만 사용 가능
   * @param model
   * @return
   */
  @GetMapping(value="/list_all")
  public String list_all(HttpSession session, Model model) { 
    // System.out.println("list_all 생성");
    
    if (this.accountProc.isMember(session)) {
      ArrayList<Tm_contentsVO> list = this.tm_contentsProc.tm_list_all();
      model.addAttribute("list", list);
      
      return "textmining/tm_list_all";
    } else { // 관리자 아닐 경우
      return "redirect:/account/login";
    }

  }
  
  /**
   * 질문글 등록 폼
   * http://localhost:9093/textmining/tm_create?cate_no=5
   * @param model
   * @param cate_no
   * @return
   */
  @GetMapping(value="/tm_create")
  public String tm_create(Model model, int cate_no, HttpSession session, Tm_contentsVO tm_contentsVO) {
    
    if (this.accountProc.isMember(session)) {
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
      model.addAttribute("categoryVO", categoryVO);
      
      model.addAttribute("tm_contentsVO", tm_contentsVO);
      model.addAttribute("acc_no", session.getAttribute("acc_no")); 
      
      return "textmining/tm_create"; // /templates/textmining/tm_create.html
    } else {
      return "redirect:/account/login";  // /account/login.html
    }
    
  
  }
  
  /**
   * 질문글 등록 처리
   * @param model
   * @param request
   * @param session
   * @param ra
   * @return
   */
  @PostMapping(value = "/tm_create")
  public String tm_create(Model model, HttpServletRequest request, HttpSession session, RedirectAttributes ra,
      Tm_imageVO tm_imageVO, Tm_contentsVO tm_contentsVO) {

    // 질문글 등록 전 출력
    System.out.println("-> [레코드 등록 전] tcon_no: " + tm_contentsVO.getTcon_no());
    System.out.println("-> [레코드 등록 전] file_no: " + tm_imageVO.getFile_no());

    // 카테고리 번호 가져오기
    int cate_no = tm_contentsVO.getCate_no(); // 부모글 번호

    int acc_no = (int) session.getAttribute("acc_no"); // memberno FK
    tm_contentsVO.setAcc_no(acc_no);

    // 질문글 등록 처리
    int cnt = this.tm_contentsProc.tm_create(tm_contentsVO);

    // 질문글 등록 성공 여부 확인
    if (cnt == 1) { // 질문글 등록 성공
      System.out.println("등록 성공");
      System.out.println("-> cate_no: " + tm_contentsVO.getCate_no());
      this.categoryProc.cnt_plus(tm_contentsVO.getCate_no()); // 관련 글 수 증가

      // 새로 등록된 질문글 번호 가져오기
      int tcon_no = tm_contentsVO.getTcon_no();
      System.out.println("-> [레코드 등록 후] qcon_no: " + tcon_no);

      // ---------------------------------------------------------------
      // 파일 전송 코드 시작
      // ---------------------------------------------------------------
      String upDir = Tcontents.getUploadDir(); // 파일을 업로드할 폴더 준비

      // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
      List<MultipartFile> fnamesMF = tm_imageVO.getFnamesMF();

      int count = fnamesMF.size(); // 전송 파일 갯수

      if (count > 0) {
        for (MultipartFile multipartFile : fnamesMF) { // 파일 추출, 1개이상 파일 처리
          long file_size = multipartFile.getSize(); // 파일 크기
          if (file_size > 0) { // 파일 크기 체크
            String file_origin_name = multipartFile.getOriginalFilename(); // 원본 파일명
            String file_upload_name = Upload.saveFileSpring(multipartFile, upDir); // 파일 저장, 업로드된 파일명
            String file_thumb_name = ""; // Preview 이미지

            if (Tool.isImage(file_origin_name)) { // 이미지인지 검사
              file_thumb_name = Tool.preview(upDir, file_upload_name, 200, 150); // thumb 이미지 생성
            }

            // 개별 파일에 대한 Qna_imageVO 객체 생성
            Tm_imageVO imageVO = new Tm_imageVO();
            imageVO.setTcon_no(tcon_no);
            imageVO.setFile_origin_name(file_origin_name);
            imageVO.setFile_upload_name(file_upload_name);
            imageVO.setFile_thumb_name(file_thumb_name);
            imageVO.setFile_size(file_size);

            // 이미지 파일 등록 처리
            this.tm_contentsProc.tm_attach_create(imageVO);
          }
        }
      }
      // -----------------------------------------------------
      // 파일 전송 코드 종료
      // -----------------------------------------------------

      // 질문글 등록 성공했을 때
      ra.addAttribute("cate_no", cate_no);
      ra.addAttribute("tcon_no", tcon_no);
      ra.addAttribute("acc_no", acc_no);

      return "redirect:/textmining/tm_list_all";
    } else { // 질문글 등록 실패
      System.out.println("텍스트마이닝 등록 실패");

      ra.addFlashAttribute("code", "tm_create_fail"); // 등록 실패
      ra.addFlashAttribute("cnt", 0); // cnt: 0, 질문글 등록 실패
      ra.addFlashAttribute("url", "/textmining/msg"); // /templates/qcontents/msg.html

      return "redirect:/account/login";
    }
  }

  /**
   * 질문글 목록 + 검색 + 페이징
   *  http://localhost:9093/qcontents/qna_list_all?cate_no=2
   * @param model
   * @param session
   * @param cate_no
   * @param file_no
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value = "/tm_list_all")
  public String list_by_tm_search_paging(Model model, HttpSession session, int cate_no,
                                                    @RequestParam(name = "word", defaultValue = "") String word,
                                                    @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // 카테고리 가져오기
    CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
    model.addAttribute("categoryVO", categoryVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("cate_no", cate_no);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<Tm_contentsVO> list = this.tm_contentsProc.list_by_tm_search_paging(map);
    model.addAttribute("list", list);
    model.addAttribute("word", word);

    // 이미지 리스트 가져오기
    ArrayList<Tm_imageVO> allImages = this.tm_contentsProc.tm_list_all_image();

    // 각 질문글에 대한 첫 번째 이미지를 매핑
    HashMap<Integer, Tm_imageVO> imageMap = new HashMap<>();
    for (Tm_imageVO image : allImages) {
      if (!imageMap.containsKey(image.getTcon_no())) {
        imageMap.put(image.getTcon_no(), image); // 첫 번째 이미지를 저장
      }
    }

    // 필터링된 이미지 리스트
    ArrayList<Tm_imageVO> filteredImages = new ArrayList<>();
    for (Tm_contentsVO tmContents : list) {
      if (imageMap.containsKey(tmContents.getTcon_no())) {
        filteredImages.add(imageMap.get(tmContents.getTcon_no()));
      } else {
        filteredImages.add(null); // 이미지가 없는 경우
      }
    }

    model.addAttribute("tm_imageVO", filteredImages);

    // 페이징
    int search_count = this.tm_contentsProc.list_by_tm_search_count(map);
    String paging = this.tm_contentsProc.pagingBox(cate_no, now_page, word, "/textmining/tm_list_all", search_count,
        Tcontents.RECORD_PER_PAGE, Tcontents.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Tcontents.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // 댓글 수 조회 및 저장
    for (Tm_contentsVO tm_contentsVO : list) {
      int tcon_no = tm_contentsVO.getTcon_no();
      int comment_cnt = this.tm_contentsProc.tm_search_count_comment(tcon_no);
      tm_contentsVO.setTcon_comment(comment_cnt);
    }
    
    return "textmining/list_by_tm_search_paging"; // /templates/qcontents/list_by_qna_search_paging.html
  }
  
  /**
   * 질문글 조회
   * http://localhost:9093/qcontents/qna_read?cate_no=2&qcon_no=31&now_page=1
   * @param model
   * @param cate_no
   * @param qcon_no
   * @return
   */
  @GetMapping(value = "/tm_read")
  public String tm_read(Model model, HttpSession session,
                         @RequestParam(name = "cate_no", defaultValue = "5") int cate_no, 
                         @RequestParam(name = "tcon_no") int tcon_no,
                         @RequestParam(name = "now_page") int now_page) {

	  
      if (this.accountProc.isMember(session)) {
        Integer acc_no = (Integer) session.getAttribute("acc_no");
        
        // 카테고리 가져오기
        CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
        model.addAttribute("categoryVO", categoryVO);

        // 질문 내용 가져오기
        Tm_contentsVO tm_contentsVO = this.tm_contentsProc.tm_read(tcon_no);
        model.addAttribute("tm_contentsVO", tm_contentsVO);
        model.addAttribute("memberno" , tm_contentsVO.getAcc_no()); //질문글 작성자
        model.addAttribute("acc_no",acc_no);
       
        // 질문 이미지 가져오기
        List<Tm_imageVO> tm_imageVO = this.tm_contentsProc.tm_read_image(tcon_no);
        model.addAttribute("tm_imageVO", tm_imageVO);
        
        // 댓글 수 가져오기
        int comment_cnt = this.tm_contentsProc.tm_search_count_comment(tcon_no);
        model.addAttribute("comment_cnt", comment_cnt);
        
        // 북마크 수 가져오기
        int mark_cnt = this.tm_contentsProc.bookmark_count(tcon_no);
        model.addAttribute("mark_cnt", mark_cnt);

        HashMap<String, Object> map = new HashMap<>();
        map.put("tcon_no", tcon_no);
        map.put("acc_no", acc_no);

        // 북마크 상태 확인
        if (this.tm_contentsProc.is_bookmarked(map).size() > 0) {
            tm_contentsVO.setTcon_bookmark("Y");
        } else {
            tm_contentsVO.setTcon_bookmark("N");
        }
        
        // 질문글 작성자
        String user_name = this.tm_contentsProc.user_name(map);
        
        // 질문글 작성자 프로필 이미지
        AccountVO acc_profile_img = this.tm_contentsProc.acc_profile_img(tcon_no);
        if (acc_profile_img != null) {
          model.addAttribute("acc_profile_img", acc_profile_img);
        }

        // 모델에 필요한 정보 추가
        model.addAttribute("acc_id", session.getAttribute("acc_id"));
        model.addAttribute("acc_no", acc_no);
        model.addAttribute("cate_no", cate_no);
        model.addAttribute("tcon_no", tcon_no);
        model.addAttribute("now_page", now_page);
        model.addAttribute("user_name", user_name);
        model.addAttribute("acc_profile_img", acc_profile_img);
        model.addAttribute("tm_imageVO", tm_imageVO);

        // 조회수 업데이트 old ver.
        // this.qnacontentsProc.qna_update_view(qcon_no);

        // 현재 시간을 기준으로 조회 여부 확인
        Long current_session = System.currentTimeMillis();
        Long last_session = (Long) session.getAttribute("tm_last_view_time" + tcon_no);

        // 만약 세션에 기록된 시간이 없거나, 마지막 조회 시간이 현재 시간보다 오래된 경우에만 조회수 증가
        if (last_session == null || (current_session - last_session > 300000)) { // 300000 밀리초 = 5분
          // 조회수 업데이트
          this.tm_contentsProc.tm_update_view(tcon_no);

          // 세션에 마지막 조회 시간 기록
          session.setAttribute("tm_last_view_time" + tcon_no, current_session);
        }

        return "textmining/tm_read";
      } else {
        return "redirect:/account/login";
      }
  }
  
  /**
   * 질문글 글 수정 폼
   * @param model
   * @param cate_no
   * @param qcon_no
   * @return
   */
  @GetMapping(value="/tm_update_text/{memberno}")
  public String tm_update_text(HttpSession session, Model model, RedirectAttributes ra,
		  						              @PathVariable("memberno") int memberno,
                                @RequestParam(name="tcon_no") int tcon_no,
                                @RequestParam(name="cate_no") int cate_no,
                                @RequestParam(name="now_page") int now_page) {

      int acc_no = (int) session.getAttribute("acc_no");
      
      if (accountProc.isMemberAdmin(session) || memberno == (int)session.getAttribute("acc_no")) { // 관리자 또는 작성자인 경우
          // 카테고리 가져오기
          CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
          model.addAttribute("categoryVO", categoryVO);

          // 질문글 가져오기
          Tm_contentsVO tm_contentsVO = this.tm_contentsProc.tm_read(tcon_no);
          model.addAttribute("tm_contentsVO", tm_contentsVO);

          // 필요한 데이터 모델에 추가
          model.addAttribute("tcon_no", tcon_no);
          model.addAttribute("word", "word"); 
          model.addAttribute("now_page", now_page);
          model.addAttribute("acc_no", acc_no);

          return "textmining/tm_update_text"; // 슬래시로 시작하도록
      } else { // 권한이 없는 경우
        ra.addAttribute("url", "/account/login_cookie_need");
        return "redirect:/account/login"; // 로그인 페이지로 이동
      }
  }
  
  /**
   * 질문글 글 수정 처리
   * @param model
   * @param ra
   * @param qna_contentsVO
   * @param cate_no
   * @param qcon_no
   * @return
   */
  @PostMapping(value="/tm_update_text")
  public String tm_update_text(HttpSession session, Model model, 
                                        RedirectAttributes ra, 
                                        Tm_contentsVO tm_contentsVO,
                                        String search_word, int now_page,
                                        int cate_no, int tcon_no) {
    
    

      int cnt = this.tm_contentsProc.tm_update_text(tm_contentsVO);
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("qcon_no", tm_contentsVO.getTcon_no());
      
      this.tm_contentsProc.tm_update_text(tm_contentsVO);
      
      ra.addFlashAttribute("cnt", 1);
      ra.addAttribute("cate_no", cate_no);
      ra.addAttribute("tcon_no", tcon_no);
      ra.addAttribute("now_page", now_page);
      ra.addAttribute("word", search_word);
      ra.addAttribute("acc_no", session.getAttribute("acc_no"));
      
      return "redirect:/textmining/tm_read";
  }
  
  /**
   * 질문글 파일
   * @param session
   * @param model
   * @param cate_no
   * @param qcon_no
   * @param now_page
   * @return
   */
  @GetMapping(value="/tm_update_file/{memberno}")
  public String tm_update_file(HttpSession session, RedirectAttributes ra, Model model,
		  							                  @PathVariable("memberno") int memberno,
                                      @RequestParam(name="cate_no", defaultValue = "5") int cate_no, 
                                      int tcon_no, int now_page) {
    
    System.out.println("-> acc_no: " + session.getAttribute("acc_no"));

    if (accountProc.isMemberAdmin(session) || memberno == (int)session.getAttribute("acc_no")) { // 관리자, 회원으로 로그인한 경우
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no); // 카테고리 읽어옴
      model.addAttribute("categoryVO", categoryVO);
      
      // 질문글 가져오기
      Tm_contentsVO tm_contentsVO = this.tm_contentsProc.tm_read(tcon_no);
      model.addAttribute("tm_contentsVO", tm_contentsVO);
      
      ArrayList<Tm_imageVO> tm_imageVO  = this.tm_contentsProc.tm_read_image(tcon_no);
      for (int i = 1; i < tm_imageVO .size(); i++) {
        long size = tm_imageVO .get(i).getFile_size();
        String silze_label = Tool.unit(size);
        tm_imageVO .get(i).setFlabel(silze_label);
      }
      model.addAttribute("tm_imageVO", tm_imageVO);
      
      model.addAttribute("now_page", now_page);
      model.addAttribute("cate_no", cate_no);
      model.addAttribute("tcon_no", tcon_no);
      
      return "textmining/tm_update_file";
    } else {  // 로그인 실패 한 경우      
      ra.addAttribute("url", "/account/login_cookie_need"); // /templates/account/login_cookie_need.html
      return "redirect:/account/login";  // /account/login.html
    }
    
  }
  
  /**
   * 질문글 파일 수정 처리
   * @param model
   * @param ra
   * @param fnamesMF
   * @param cate_no
   * @param qcon_no
   * @return
   */
  @PostMapping(value="tm_update_file")
  public String tm_update_file(HttpSession session, Model model, RedirectAttributes ra,
                                        List<MultipartFile> fnamesMF,
                                        Tm_contentsVO tm_contentsVO,
                                        int cate_no, int tcon_no, int now_page) {
    
    model.addAttribute("cate_no", cate_no);
    model.addAttribute("tcon_no", tcon_no);
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("tcon_no", tm_contentsVO.getTcon_no());
    
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      ArrayList<Tm_imageVO> timage_old = this.tm_contentsProc.tm_read_image(tcon_no);
      
      for (Tm_imageVO tm_imageVO: timage_old) {
        // -------------------------------------------------------------------
        // 파일 삭제 시작
        // -------------------------------------------------------------------
        String file_upload_name = tm_imageVO.getFile_upload_name();
        String file_thumb_name = tm_imageVO.getFile_thumb_name();
        
        String upDir = Tcontents.getUploadDir();
        Tool.deleteFile(upDir, file_upload_name);
        Tool.deleteFile(upDir, file_thumb_name);
        // -------------------------------------------------------------------
        // 파일 삭제 종료
        // -------------------------------------------------------------------
      }
      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      Tm_imageVO tm_imageVO = new Tm_imageVO();
      String upDir = Tcontents.getUploadDir(); // 업로드할 폴더
      String file_origin_name = "";
      String file_upload_name = "";
      String file_thumb_name = "";
      
      long file_size = 0;
      tm_imageVO.setFnamesMF(fnamesMF);
      int count = fnamesMF.size();
      System.out.println("-> count: " + count);

      if (count > 0) {
        int cnt1 = 0;
        for (MultipartFile multipartFile : fnamesMF) {
          file_size = multipartFile.getSize();
          if (file_size > 0) {
            file_origin_name = multipartFile.getOriginalFilename();
            file_upload_name = Upload.saveFileSpring(multipartFile, upDir);

            if (Tool.isImage(file_origin_name)) {
              file_thumb_name = Tool.preview(upDir, file_upload_name, 200, 150);
            }
          } 
          
          // System.out.println("-> cnt1: " + cnt1 + ", image_list_old.size(): " +
          // image_list_old.size());
          if (timage_old.size() <= cnt1) { // 수정할 이미지 갯수가 원래 이미지 갯수보다 많을 경우
            tm_imageVO.setTcon_no(tcon_no);
            tm_imageVO.setFile_origin_name(file_origin_name);
            tm_imageVO.setFile_thumb_name(file_thumb_name);
            tm_imageVO.setFile_upload_name(file_upload_name);
            tm_imageVO.setFile_size(count);

            int image_cnt = this.tm_contentsProc.tm_attach_create(tm_imageVO);
            // System.out.println("image 수정 중 create 완료");
          } else {
            tm_imageVO.setFile_no(timage_old.get(cnt1).getFile_no());
            tm_imageVO.setFile_origin_name(file_origin_name);
            tm_imageVO.setFile_thumb_name(file_thumb_name);
            tm_imageVO.setFile_upload_name(file_upload_name);
            tm_imageVO.setFile_size(count);
            int image_cnt = this.tm_contentsProc.tm_update_file(tm_imageVO);
            System.out.println("-> image_cnt: " + image_cnt);
          }
          cnt1++;
        }
    
      }
      
      ra.addAttribute("cate_no", cate_no);
      ra.addAttribute("tcon_no", tcon_no);
      ra.addAttribute("now_page", now_page);
      ra.addAttribute("tm_imageVO", tm_imageVO);
      
      return "redirect:/textmining/tm_read";
    
  }
  
  /**
   * 질문글 삭제
   * @param model
   * @param qcon_no
   * @return
   */
  @GetMapping(value="/tm_delete/{memberno}")
  public String tm_delete(HttpSession session, 
                                  Model model, 
                                  RedirectAttributes ra,
                                  @PathVariable("memberno") int memberno,
                                  @RequestParam(name="cate_no", defaultValue = "5") int cate_no, 
                                  int tcon_no, int now_page) {
    
    System.out.println("-> acc_no: " + session.getAttribute("acc_no"));

    if (accountProc.isMemberAdmin(session)|| memberno==(int)session.getAttribute("acc_no")) {
      model.addAttribute("cate_no", cate_no);
      model.addAttribute("now_page", now_page);
      
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no); // 카테고리 읽어옴
      model.addAttribute("categoryVO", categoryVO);
      
      // 질문글 가져오기
      Tm_contentsVO tm_contentsVO = this.tm_contentsProc.tm_read(tcon_no);
      model.addAttribute("tm_contentsVO", tm_contentsVO);
      
      return "textmining/tm_delete";
    } else {
      ra.addAttribute("url", "/account/login_cookie_need"); // /templates/account/login_cookie_need.html
      return "redirect:/account/login";  // /account/login.html
    }
   
  }
  
  /**
   * 질문글 삭제 처리
   * @param qcon_no
   * @param cate_no
   * @param ra
   * @return
   */
  @PostMapping(value="/tm_delete")
  public String tm_delete(RedirectAttributes ra, 
                                  int tcon_no, int cate_no, int now_page) {
    
//	  System.out.println("-> qcon_no:" + qcon_no);
	  ArrayList<Tm_contentsVO> list = this.tm_contentsProc.list_by_tcon_no(tcon_no); //회원정보 불러오기 위함.
	  
	  Tm_contentsVO tm_contentsVO = this.tm_contentsProc.tm_read(tcon_no);
	  
	  int acc_no = list.get(0).getAcc_no(); //댓글 삭제 parameter 값에 넣을 회원번호
	  
	  HashMap<String,Object> map = new HashMap<String,Object>();
	  map.put("tcon_no", tcon_no);
	  map.put("acc_no", acc_no);
	  
	  int all_bookmark_delete = this.tm_contentsProc.all_bookmark_delete(tcon_no); // 북마크 삭제
	  if (all_bookmark_delete > 0) {
	    System.out.println("북마크 삭제 성공");
	  }
	  
	  int cnt_image = this.tm_contentsProc.tm_delete_image(tcon_no); //이미지 삭제
	  if(cnt_image>0) {
		  System.out.println("이미지 삭제 성공");
	  }
	  
    int cnt_recomments = this.tm_contentsProc.all_tm_delete_recomment(tcon_no);
    if (cnt_recomments > 0) {
      System.out.println("답글 삭제 성공");
    }
	  
    int cnt_comments = this.tm_contentsProc.all_tm_delete_comment(tcon_no);
    if (cnt_comments > 0) {
      System.out.println("댓글 삭제 성공");
    }
    
	  int cnt_contents = this.tm_contentsProc.tm_delete(tcon_no); //글삭제
	  if(cnt_contents>0) {
		  System.out.println("글 삭제 성공");
		  this.categoryProc.cnt_minus(tm_contentsVO.getCate_no()); // 관련 글 수 감소
	  }
	  
	  ra.addAttribute("cate_no",cate_no);
	  ra.addAttribute("now_page", now_page);
	  
	  return "redirect:/textmining/tm_list_all";
  }
  
  /**
   * 댓글 등록은 부모글(질문글) 조회에서 진행함
   * @param qna_commentVO
   * @param session
   * @return
   */
  @PostMapping(value="/tm_create_comment")
  @ResponseBody
  public String tm_create_comment(@RequestBody Tm_commentVO tm_commentVO, HttpSession session) {
    
    System.out.println("-> 수신 데이터:" + tm_commentVO.toString());
    
    int acc_no = (int)session.getAttribute("acc_no");
    tm_commentVO.setAcc_no(acc_no);
    
    System.out.println("-> acc_no: " + acc_no);
    
    int cnt = this.tm_contentsProc.tm_create_comment(tm_commentVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
  
    return json.toString();
  }
  
  /**
   * 질문글 댓글 목록 최신순
   * @param qconno
   * @return
   */
  @GetMapping(value="/list_by_tcmt_no_join")
  @ResponseBody
  public String list_by_tcmt_no_join(int tcon_no) {
    List<Tm_Acc_commentVO> list = tm_contentsProc.list_by_tcmt_no_join_500(tcon_no);
    
    JSONObject obj = new JSONObject();
    obj.put("res", list);

    return obj.toString();
  }
  
  /**
   * 질문글 댓글 목록 작성순
   * @param qconno
   * @return
   */
  @GetMapping(value="/asc_list_by_tcmt_no_join")
  @ResponseBody
  public String asc_list_by_tcmt_no_join(int tcon_no) {
    List<Tm_Acc_commentVO> list = tm_contentsProc.asc_list_by_tcmt_no_join_500(tcon_no);
    
    JSONObject obj = new JSONObject();
    obj.put("res", list);

    return obj.toString();
  }
  
  /**
   * 댓글 조회
   * @param qcmt_no
   * @return
   */
  @GetMapping(value="/tm_read_comment", produces ="application/json")
  @ResponseBody
  public String tm_read_comment(int tcmt_no) {
    Tm_commentVO tm_commentVO = this.tm_contentsProc.tm_read_comment(tcmt_no);
    
    JSONObject row = new JSONObject();
    row.put("qcmt_no", tm_commentVO.getTcmt_no());
    row.put("acc_no", tm_commentVO.getAcc_no());
    row.put("qcon_no", tm_commentVO.getTcon_no());
    row.put("qcmt_contents", tm_commentVO.getTcmt_contents());
    row.put("qcmt_date", tm_commentVO.getTcmt_date());
    
    JSONObject obj = new JSONObject();
    obj.put("res", row);
    
    return obj.toString();
  }
  
  /**
   * 댓글 수정 처리
   * @param session
   * @param qna_commentVO
   * @return
   */
  @PostMapping(value="/tm_update_comment")
  @ResponseBody
  public String tm_update_comment(HttpSession session, @RequestBody Tm_commentVO tm_commentVO) {
    System.out.println("-> 수정할 수신 댓글: " + tm_commentVO.toString());
    
    int acc_no = (int)session.getAttribute("acc_no");
    
    int cnt = 0;
    if (acc_no == tm_commentVO.getAcc_no()) { // 회원 자신이 쓴 댓글만 수정 가능
      cnt = this.tm_contentsProc.tm_update_comment(tm_commentVO);
    }
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);  // 1: 성공, 0: 실패

    return json.toString();
  }
  
  /**
   * 댓글 삭제 처리
   * @param session
   * @param qna_commentVO
   * @return
   */
  @PostMapping(value="/tm_delete_comment")
  @ResponseBody
  public String tm_delete_comment(HttpSession session, @RequestBody Tm_commentVO tm_commentVO) {
    int acc_no = (int)session.getAttribute("acc_no");
    
    if (acc_no == tm_commentVO.getAcc_no()) { // 회원 자신이 쓴 댓글만 삭제 가능
      JSONObject json = new JSONObject();
      
      int comment = this.tm_contentsProc.delete_tcmtno_recomment(tm_commentVO.getTcmt_no()); // 전체 답글 삭제
      int cnt = this.tm_contentsProc.tm_delete_comment(tm_commentVO.getTcmt_no());
      
      json.put("res", cnt);
      
      return json.toString();
    }
    
    JSONObject json = new JSONObject();
    json.put("res", 0);  // 1: 성공, 0: 실패
    
    return json.toString();
  }
  
  /**
   * 북마크 등록
   * @param session
   * @param ra
   * @param qcon_no
   * @return
   */
  @GetMapping(value="/bookmark_create/{tcon_no}", produces = "application/json")
  @ResponseBody
  public String bookmark_create(HttpSession session, 
                                          RedirectAttributes ra,
                                          @PathVariable("tcon_no") Integer tcon_no,int acc_no,int cate_no) {

    JSONObject obj = new JSONObject();

    if (this.accountProc.isMember(session)) {
      //Integer acc_no = (Integer) session.getAttribute("acc_no");

      HashMap<String, Object> map = new HashMap<>();
      map.put("tcon_no", tcon_no);
      map.put("acc_no", acc_no);

      
      List<Tm_markVO> list = tm_contentsProc.is_bookmarked(map);
      obj.put("res", list);
      
      int cnt = this.tm_contentsProc.bookmark_create(map);

      System.out.println("북마크 등록 성공");
      int cnt1 = this.tm_contentsProc.bookmark_y(map); // 북마크 공개
      if(cnt1>0) {
    	  System.out.println("북마크 공개 성공");
      }
      obj.put("cnt", cnt);
    } else {
      ra.addAttribute("url", "/account/login_cookie_need"); // /templates/account/login_cookie_need.html
      return "redirect:/account/login"; // /account/login.html
    }
    return obj.toString();
  }
  
  /**
   * 질문글 북마크 삭제
   * @param session
   * @param ra
   * @param qna_contentsVO
   * @param cate_no
   * @param qcon_no
   * @return
   */
  @GetMapping(value="/bookmark_delete/{tcon_no}", produces = "application/json")
  @ResponseBody
  public String bookmark_delete(HttpSession session,
                                          RedirectAttributes ra,
                                          @PathVariable("tcon_no") Integer tcon_no,int acc_no,int cate_no) {
    System.out.println("북마크 등록 들어옴");
    JSONObject obj = new JSONObject();
    
    if (this.accountProc.isMember(session)) {
      //Integer acc_no = (Integer) session.getAttribute("acc_no");

      HashMap<String, Object> map = new HashMap<>();
      map.put("tcon_no", tcon_no);
      map.put("acc_no", acc_no);
      
      System.out.println(tcon_no);
      System.out.println(acc_no);
      
      List<Tm_markVO> list = tm_contentsProc.is_bookmarked(map);
      obj.put("res", list);
      
      int cnt = this.tm_contentsProc.bookmark_delete(map);
      
      System.out.println("북마크 등록 성공");

      this.tm_contentsProc.bookmark_y(map);// 북마크 공개

      obj.put("cnt", cnt);
    } else {
      ra.addAttribute("url", "/account/login_cookie_need"); // /templates/account/login_cookie_need.html
      return "redirect:/account/login"; // /account/login.html
    }
    return obj.toString();
  }
  
  /**
   * 질문글 체크박스 처리
   * @param map
   * @param session
   * @return
   */
  @PostMapping(value = "/tm_select_delete")
  @ResponseBody
  public String select_delete(@RequestBody Map<String, Object> map, HttpSession session) {
    JSONObject obj = new JSONObject();
    int cnt = 0;
    if (this.accountProc.isMemberAdmin(session)) {
      List<Integer> tconno_List = (List<Integer>) map.get("tcon_nos");
      int cate_no = (int) map.get("cate_no");

      int recomment = this.tm_contentsProc.delete_tconno_recomment(tconno_List);
      if(recomment > 0) {
        System.out.println("답글 삭제 성공");
      }
      
      // 댓글 삭제
      int comment = this.tm_contentsProc.delete_tconno_comment(tconno_List);

      // 이미지 삭제
      int image = this.tm_contentsProc.delete_tconno_image(tconno_List);

      // 북마크 삭제
      int bookmark = this.tm_contentsProc.delete_tconno_bookmark(tconno_List);
      if (bookmark > 0) {
        System.out.println("북마크 삭제 성공");
      }

      // 선택 삭제
      cnt = this.tm_contentsProc.delete_tconno(tconno_List);
      for (int i = 0; i < tconno_List.size(); i++) {
        this.categoryProc.cnt_minus(cate_no);
      }

      obj.put("cnt", cnt);
    } else {
      obj.put("cnt", cnt);
    }

    return obj.toString();
  }
  
  /**
   * 답글 작성 처리
   * @param qna_recommentVO
   * @return
   */
  @PostMapping(value="/tm_create_recomment")
  @ResponseBody
  public String tm_create_recomment(@RequestBody Tm_recommentVO tm_recommentVO) {
    
    HashMap<String, Object> map = new HashMap<>();
    map.put("trecmt_contents", tm_recommentVO.getTrecmt_contents());
    map.put("tcon_no", tm_recommentVO.getTcon_no());
    map.put("tcmt_no", tm_recommentVO.getTcmt_no());
    map.put("acc_no", tm_recommentVO.getAcc_no());
    
    int cnt = this.tm_contentsProc.tm_create_recomment(map);
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  
   /**
    * 회원 답글 조회
    * @param qcmt_no
    * @return
    */
  @GetMapping(value="/tm_read_recomment")
  @ResponseBody
  public String tm_read_recomment(int tcmt_no) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("tcmt_no", tcmt_no);
    
    ArrayList<Tm_recommentVO> list = this.tm_contentsProc.tm_read_recomment(map);
    
    if(list.size() > 0) {
      System.out.println("답글 조회 성공");
    }
    for(Tm_recommentVO vo : list) {
      System.out.println("-> 답글 :" + vo.getTrecmt_contents());
      System.out.println("-> acc_no:" + vo.getAcc_no());
      System.out.println("-> acc_id:" + vo.getAcc_id());
    }
    
    JSONObject obj = new JSONObject();
    obj.put("res", list);
    
    return obj.toString();
  }
  
  /**
   * 로그인 시 acc_id
   * @param acc_no
   * @return
   */
  @GetMapping(value="/account_read")
  @ResponseBody
  public String account_read(int acc_no) {
    System.out.println("-> acc_no: " + acc_no);
    
    AccountVO accountVO = this.accountProc.read(acc_no);
    
    JSONObject obj = new JSONObject();
    obj.put("acc_id", accountVO.getAcc_id());
    System.out.println("-> acc_id: " + accountVO.getAcc_id());
    
    return obj.toString();
  }
  
  /**
   * 답글 조회
   * @param qrecmt_no
   * @return
   */
  @GetMapping(value="/read_recomment")
  @ResponseBody
  public String read_recomment(int trecmt_no) {
    System.out.println("-> trecmt_no: " + trecmt_no);
    
    Tm_recommentVO tm_recomment = this.tm_contentsProc.read_recomment(trecmt_no);
    
    JSONObject row = new JSONObject();
    row.put("qrecomt_no", tm_recomment.getTrecmt_no());
    row.put("qcmt_no", tm_recomment.getTcmt_no());
    row.put("qcon_no", tm_recomment.getTcon_no());
    row.put("qrecmt_contents", tm_recomment.getTrecmt_contents());
    row.put("qrecmt_date", tm_recomment.getTrecmt_date());

    JSONObject obj = new JSONObject();
    obj.put("res", row);
    
    return obj.toString();
  }
  
  /**
   * 답글 수정 처리
   * @param qna_recommentVO
   * @param session
   * @return
   */
  @PostMapping(value="/tm_update_recomment")
  @ResponseBody
  public String tm_update_recomment(@RequestBody Tm_recommentVO tm_recommentVO, HttpSession session) {
    System.out.println("답글 수정 회원번호: " + tm_recommentVO.getAcc_no());

    if (tm_recommentVO.getAcc_no() == (int)session.getAttribute("acc_no")) {
      System.out.println("답글 작성한 회원과 동일합니다.");

      HashMap<String,Object> map = new HashMap<>();
      map.put("trecmt_no", tm_recommentVO.getTrecmt_no());
      map.put("trecmt_contents", tm_recommentVO.getTrecmt_contents());
      int cnt = this.tm_contentsProc.tm_update_recomment(map);

      JSONObject obj = new JSONObject();
      obj.put("res", cnt);

      return obj.toString();
    } else {
      JSONObject obj = new JSONObject();
      obj.put("res", 0);

      return obj.toString();
    }
  }
  
  /**
   * 답글 삭제 처리
   * @param qna_recommentVO
   * @param session
   * @return
   */
  @PostMapping(value="/tm_delete_recomment")
  @ResponseBody
  public String tm_delete_recomment(@RequestBody Tm_recommentVO tm_recommentVO, HttpSession session) {
    JSONObject obj = new JSONObject();
    if (tm_recommentVO.getAcc_no() == (int)session.getAttribute("acc_no")) {
        System.out.println("답글 작성한 회원과 동일합니다.");
        
        Tm_recommentVO vo = this.tm_contentsProc.read_recomment(tm_recommentVO.getTrecmt_no());
        obj.put("tcmt_no", vo.getTcmt_no());
        // System.out.println("-> qcmt_no: " + vo.getQcmt_no());
        
        int cnt = this.tm_contentsProc.tm_delete_recomment(tm_recommentVO.getTrecmt_no());
        obj.put("res", cnt);
    } else {
        obj.put("res", 0);
    }
    
    return obj.toString();
  }
  
  
}