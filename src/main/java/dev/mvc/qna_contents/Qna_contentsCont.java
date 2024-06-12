package dev.mvc.qna_contents;

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
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@RequestMapping("/qcontents")
@Controller
public class Qna_contentsCont {

  @Autowired
  @Qualifier("dev.mvc.category.CategoryProc")
  private CategoryProcInter categoryProc;
  
  @Autowired
  @Qualifier("dev.mvc.qna_contents.Qna_contentsProc")
  private Qna_contentsProc qna_contentsProc;

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
  @GetMapping("/list_all")
  public String list_all(HttpSession session, Model model) { 
    // System.out.println("list_all 생성");
    
    if (this.accountProc.isMemberAdmin(session)) {
      ArrayList<Qna_contentsVO> list = this.qna_contentsProc.qna_list_all();
      model.addAttribute("list", list);
      
      return "qcontents/qna_list_all";
    } else { // 관리자 아닐 경우
      return "redirect:/account/login";
    }

  }
  
  /**
   * 질문글 등록 폼
   * http://localhost:9093/qcontents/qna_create?cate_no=2
   * @param model
   * @param cate_no
   * @return
   */
  @GetMapping(value="/qna_create")
  public String qna_create(Model model, int cate_no, HttpSession session, Qna_contentsVO qna_contentsVO) {
    
    if (this.accountProc.isMember(session)) {
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
      model.addAttribute("categoryVO", categoryVO);
      
      model.addAttribute("qna_contentsVO", qna_contentsVO);
      model.addAttribute("acc_no", session.getAttribute("acc_no")); 
      
      return "qcontents/qna_create"; // /templates/qcontents/create.html
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
  @PostMapping(value = "/qna_create")
  public String qna_create(Model model, HttpServletRequest request, HttpSession session, RedirectAttributes ra,
      Qna_imageVO qna_imageVO, Qna_contentsVO qna_contentsVO) {

    // 질문글 등록 전 출력
    System.out.println("-> [레코드 등록 전] qcon_no: " + qna_contentsVO.getQcon_no());
    System.out.println("-> [레코드 등록 전] file_no: " + qna_imageVO.getFile_no());

    // 카테고리 번호 가져오기
    int cate_no = qna_contentsVO.getCate_no(); // 부모글 번호

    int acc_no = (int) session.getAttribute("acc_no"); // memberno FK
    qna_contentsVO.setAcc_no(acc_no);

    // 질문글 등록 처리
    int cnt = this.qna_contentsProc.qna_create(qna_contentsVO);

    // 질문글 등록 성공 여부 확인
    if (cnt == 1) { // 질문글 등록 성공
      System.out.println("등록 성공");
      System.out.println("-> cate_no: " + qna_contentsVO.getCate_no());
      this.categoryProc.cnt_plus(qna_contentsVO.getCate_no()); // 관련 글 수 증가

      // 새로 등록된 질문글 번호 가져오기
      int qcon_no = qna_contentsVO.getQcon_no();
      System.out.println("-> [레코드 등록 후] qcon_no: " + qcon_no);

      // ---------------------------------------------------------------
      // 파일 전송 코드 시작
      // ---------------------------------------------------------------
      String upDir = Qcontents.getUploadDir(); // 파일을 업로드할 폴더 준비

      // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
      List<MultipartFile> fnamesMF = qna_imageVO.getFnamesMF();

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
            Qna_imageVO imageVO = new Qna_imageVO();
            imageVO.setQcon_no(qcon_no);
            imageVO.setFile_origin_name(file_origin_name);
            imageVO.setFile_upload_name(file_upload_name);
            imageVO.setFile_thumb_name(file_thumb_name);
            imageVO.setFile_size(file_size);

            // 이미지 파일 등록 처리
            this.qna_contentsProc.qna_attach_create(imageVO);
          }
        }
      }
      // -----------------------------------------------------
      // 파일 전송 코드 종료
      // -----------------------------------------------------

      // 질문글 등록 성공했을 때
      ra.addAttribute("cate_no", cate_no);
      ra.addAttribute("qcon_no", qcon_no);
      ra.addAttribute("acc_no", acc_no);

      return "redirect:/qcontents/qna_list_all";
    } else { // 질문글 등록 실패
      System.out.println("질문글 등록 실패");

      ra.addFlashAttribute("code", "qna_create_fail"); // 등록 실패
      ra.addFlashAttribute("cnt", 0); // cnt: 0, 질문글 등록 실패
      ra.addFlashAttribute("url", "/qcontents/msg"); // /templates/qcontents/msg.html

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
  @GetMapping(value="/qna_list_all")
  public String list_by_qna_search_paging(Model model, HttpSession session, int cate_no, 
                                                    @RequestParam(name="word", defaultValue = "") String word,
                                                    @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    // 카테고리 가져오기
    CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
    model.addAttribute("categoryVO", categoryVO);
    
    word = Tool.checkNull(word).trim();
    
    HashMap<String, Object> map = new HashMap<>();
    map.put("cate_no", cate_no);
    map.put("word", word);
    map.put("now_page", now_page);
    
    ArrayList<Qna_contentsVO> list = this.qna_contentsProc.list_by_qna_search_paging(map);
    model.addAttribute("list", list);
    
    ArrayList<Qna_imageVO> qna_imageVO = this.qna_contentsProc.qna_list_all_image();
    // 각 질문글에 대한 이미지 중 첫 번째 이미지만 선택
    ArrayList<Qna_imageVO> filterImage = new ArrayList<>();
    for (Qna_contentsVO qnaContents : list) {
        int qconNo = qnaContents.getQcon_no();
        for (Qna_imageVO image : qna_imageVO) {
            if (image.getQcon_no() == qconNo) {
                filterImage.add(image);
                break; // 하나의 이미지만 추가하기 위해 반복문 탈출
            }
        }
    }   
    model.addAttribute("qna_imageVO", filterImage);
    
    model.addAttribute("word", word);
    
    // 페이징
    int search_count = this.qna_contentsProc.list_by_qna_search_count(map);
    String paging = this.qna_contentsProc.pagingBox(cate_no, now_page, word, "/qcontents/qna_list_all", 
        search_count, Qcontents.RECORD_PER_PAGE, Qcontents.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    model.addAttribute("search_count", search_count);
    model.addAttribute("cate_no", cate_no);
    model.addAttribute("qna_imageVO", qna_imageVO);
    
    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Qcontents.RECORD_PER_PAGE);
    model.addAttribute("no", no);
    
    // 댓글 수 조회 및 저장
    for (Qna_contentsVO qna_contentsVO : list) {
        int qcon_no = qna_contentsVO.getQcon_no();
        int comment_cnt = this.qna_contentsProc.qna_search_count_comment(qcon_no);
        qna_contentsVO.setQcon_comment(comment_cnt);
    }
    
    return "qcontents/list_by_qna_search_paging"; // /templates/qcontents/list_by_qna_search_paging.html
  }
  
  /**
   * 질문글 조회
   * http://localhost:9093/qcontents/qna_read?cate_no=2
   * @param model
   * @param cate_no
   * @param qcon_no
   * @return
   */
  @GetMapping(value="/qna_read")
  public String qna_read(Model model, 
                         HttpSession session,
                         @RequestParam(name="cate_no", defaultValue = "2") int cate_no, 
                         int qcon_no, 
                         int now_page) {
      
      model.addAttribute("acc_id", session.getAttribute("acc_id"));
      model.addAttribute("acc_no", session.getAttribute("acc_no"));
      model.addAttribute("cate_no", cate_no);
      model.addAttribute("qcon_no", qcon_no);
      model.addAttribute("now_page", now_page);
      
      Integer acc_no = (Integer) session.getAttribute("acc_no");
      
      // 비회원인 경우 (acc_no가 null인 경우) 기본값 설정
      if (acc_no == null) {
          // 비회원일 경우 처리
          // 필요에 따라 다르게 처리할 수 있습니다.
      }
      
      // 계정 번호 출력
      System.out.println("-> acc_no: " + acc_no);
      
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no);
      model.addAttribute("categoryVO", categoryVO);
      
      // 조회수 업데이트
      this.qna_contentsProc.qna_update_view(qcon_no);
      
      // 댓글 수 가져오기
      int comment_cnt = this.qna_contentsProc.qna_search_count_comment(qcon_no);
      model.addAttribute("comment_cnt", comment_cnt);
      
      // 질문 내용 가져오기
      Qna_contentsVO qna_contentsVO = this.qna_contentsProc.qna_read(qcon_no);
      model.addAttribute("qna_contentsVO", qna_contentsVO);
      
      // 질문 이미지 가져오기
      ArrayList<Qna_imageVO> qna_imageVO = this.qna_contentsProc.qna_read_image(qcon_no);
      model.addAttribute("qna_imageVO", qna_imageVO);
      
      // 질문이 북마크되어 있는지 확인
      if (acc_no != null) {
          HashMap<String, Object> map = new HashMap<String, Object>();
          map.put("qcon_no", qna_contentsVO.getQcon_no());
          map.put("acc_no", acc_no);
          
          int qcon_bookcnt = this.qna_contentsProc.is_bookmarked(map);
          if(qcon_bookcnt > 0) {
              qna_contentsVO.setQcon_bookmark("Y");
          } else {
              qna_contentsVO.setQcon_bookmark("N");
          }
      }
      
      return "qcontents/qna_read";
  }


  
  /**
   * 질문글 글 수정 폼
   * @param model
   * @param cate_no
   * @param qcon_no
   * @return
   */
  @GetMapping(value="/qna_update_text")
  public String qna_update_text(HttpSession session, Model model, 
                                            RedirectAttributes ra,
                                            String word, int now_page,
                                            @RequestParam(name="cate_no", defaultValue = "2") int cate_no, int qcon_no) {


    
    System.out.println("-> acc_no: " + session.getAttribute("acc_no"));

    if (accountProc.isMember(session)) { // 관리자, 회원으로 로그인 한 경우
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no); // 카테고리 읽어옴
      model.addAttribute("categoryVO", categoryVO);
      
      // 질문글 가져오기
      Qna_contentsVO qna_contentsVO = this.qna_contentsProc.qna_read(qcon_no);
      model.addAttribute("qna_contentsVO", qna_contentsVO);
      
      model.addAttribute("cate_no", cate_no);
      model.addAttribute("qcon_no", qcon_no);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      model.addAttribute("acc_no",session.getAttribute("acc_no"));
      
      return "qcontents/qna_update_text";
    } else {  // 로그인 실패 한 경우      
      ra.addAttribute("url", "/account/login_cookie_need"); // /templates/account/login_cookie_need.html
      return "redirect:/account/login";  // /account/login.html
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
  @PostMapping(value="/qna_update_text")
  public String qna_update_text(HttpSession session, Model model, 
                                        RedirectAttributes ra, 
                                        Qna_contentsVO qna_contentsVO,
                                        String search_word, int now_page,
                                        int cate_no, int qcon_no) {
    
    

      int cnt = this.qna_contentsProc.qna_update_text(qna_contentsVO);
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("qcon_no", qna_contentsVO.getQcon_no());
      map.put("qcon_passwd", qna_contentsVO.getQcon_passwd());
      
      if (this.qna_contentsProc.qna_password_check(map) == 1) { // 패스워드 일치
        this.qna_contentsProc.qna_update_text(qna_contentsVO);
        
        ra.addFlashAttribute("cnt", 1);
        ra.addAttribute("cate_no", cate_no);
        ra.addAttribute("qcon_no", qcon_no);
        ra.addAttribute("now_page", now_page);
        ra.addAttribute("word", search_word);
        ra.addAttribute("acc_no", session.getAttribute("acc_no"));
        
        return "redirect:/qcontents/qna_read";
      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail");
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/qcontents/msg"); // msg.html, redirect parameter 적용
        return "redirect:/qcontents/msg";  // @GetMapping(value="/msg")
      }

  
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
  @GetMapping(value="/qna_update_file")
  public String qna_update_file(HttpSession session, RedirectAttributes ra, Model model,
                                      @RequestParam(name="cate_no", defaultValue = "2") int cate_no, 
                                      int qcon_no, int now_page) {
    
    System.out.println("-> acc_no: " + session.getAttribute("acc_no"));

    if (accountProc.isMember(session)) { // 관리자, 회원으로 로그인한 경우
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no); // 카테고리 읽어옴
      model.addAttribute("categoryVO", categoryVO);
      
      // 질문글 가져오기
      Qna_contentsVO qna_contentsVO = this.qna_contentsProc.qna_read(qcon_no);
      model.addAttribute("qna_contentsVO", qna_contentsVO);
      
      ArrayList<Qna_imageVO> qimage = this.qna_contentsProc.qna_read_image(qcon_no);
      for (int i = 1; i < qimage.size(); i++) {
        long size = qimage.get(i).getFile_size();
        String silze_label = Tool.unit(size);
        qimage.get(i).setFlabel(silze_label);
      }
      model.addAttribute("qimage", qimage);
      
      model.addAttribute("now_page", now_page);
      model.addAttribute("cate_no", cate_no);
      model.addAttribute("qcon_no", qcon_no);
      
      return "qcontents/qna_update_file";
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
  @PostMapping(value="qna_update_file")
  public String qna_update_file(HttpSession session, Model model, RedirectAttributes ra,
                                        List<MultipartFile> fnamesMF,
                                        int cate_no, int qcon_no, int now_page) {
    
      model.addAttribute("cate_no", cate_no);
      model.addAttribute("qcon_no", qcon_no);
      
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      ArrayList<Qna_imageVO> qimage_old = this.qna_contentsProc.qna_read_image(qcon_no);
      
      for (Qna_imageVO qimage: qimage_old) {
        // -------------------------------------------------------------------
        // 파일 삭제 시작
        // -------------------------------------------------------------------
        String file1saved = qimage.getFile_upload_name();
        String thumb = qimage.getFile_thumb_name();
        
        String upDir = Qcontents.getUploadDir();
        Tool.deleteFile(upDir, file1saved);
        Tool.deleteFile(upDir, thumb);
        // -------------------------------------------------------------------
        // 파일 삭제 종료
        // -------------------------------------------------------------------
      }
      long size1 = 0;
      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      Qna_imageVO qna_imageVO = new Qna_imageVO();
      String upDir = Qcontents.getUploadDir(); // 업로드할 폴더
      String file_origin_name = "";
      String file_upload_name = "";
      String file_thumb_name = "";
      
      long file_size = 0;
      qna_imageVO.setFnamesMF(fnamesMF);
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
          if (qimage_old.size() <= cnt1) { // 수정할 이미지 갯수가 원래 이미지 갯수보다 많을 경우
            qna_imageVO.setQcon_no(qcon_no);
            qna_imageVO.setFile_origin_name(file_origin_name);
            qna_imageVO.setFile_thumb_name(file_thumb_name);
            qna_imageVO.setFile_upload_name(file_upload_name);
            qna_imageVO.setFile_size(count);

            int image_cnt = this.qna_contentsProc.qna_attach_create(qna_imageVO);
            // System.out.println("image 수정 중 create 완료");
          } else {
            qna_imageVO.setFile_no(qimage_old.get(cnt1).getFile_no());
            qna_imageVO.setFile_origin_name(file_origin_name);
            qna_imageVO.setFile_thumb_name(file_thumb_name);
            qna_imageVO.setFile_upload_name(file_upload_name);
            qna_imageVO.setFile_size(count);
            int image_cnt = this.qna_contentsProc.qna_update_file(qna_imageVO);
            System.out.println("-> image_cnt: " + image_cnt);
          }
          cnt1++;
        }
    
      }
      
      ra.addAttribute("cate_no", cate_no);
      ra.addAttribute("qcon_no", qcon_no);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/qcontents/qna_read";
  }
  
  /**
   * 질문글 삭제
   * @param model
   * @param qcon_no
   * @return
   */
  @GetMapping(value="/qna_delete")
  public String qna_delete(HttpSession session, 
                                  Model model, 
                                  RedirectAttributes ra,
                                  @RequestParam(name="cate_no", defaultValue = "2") int cate_no, 
                                  int qcon_no, int now_page) {
    
    System.out.println("-> acc_no: " + session.getAttribute("acc_no"));

    if (accountProc.isMemberAdmin(session)) {
      model.addAttribute("cate_no", cate_no);
      model.addAttribute("now_page", now_page);
      
      // 카테고리 가져오기
      CategoryVO categoryVO = this.categoryProc.cate_read(cate_no); // 카테고리 읽어옴
      model.addAttribute("categoryVO", categoryVO);
      
      // 질문글 가져오기
      Qna_contentsVO qna_contentsVO = this.qna_contentsProc.qna_read(qcon_no);
      model.addAttribute("qna_contentsVO", qna_contentsVO);
      
      return "qcontents/qna_delete";
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
  @PostMapping(value="/qna_delete")
  public String qna_delete(RedirectAttributes ra, 
                                  int qcon_no, int cate_no, int now_page) {
    
//	  System.out.println("-> qcon_no:" + qcon_no);
	  ArrayList<Qna_contentsVO> list = this.qna_contentsProc.list_by_qcon_no(qcon_no); //회원정보 불러오기 위함.
	  
	  Qna_contentsVO qna_contentsVO = this.qna_contentsProc.qna_read(qcon_no);
	  
	  int acc_no = list.get(0).getAcc_no(); //댓글 삭제 parameter 값에 넣을 회원번호
	  
	  HashMap<String,Object> map = new HashMap<String,Object>();
	  map.put("qcon_no", qcon_no);
	  map.put("acc_no", acc_no);
	  
	  int cnt_image = this.qna_contentsProc.qna_delete_image(qcon_no); //이미지 삭제
	  if(cnt_image>0) {
		  System.out.println("이미지 삭제 성공");
	  }

	  int cnt_contents = this.qna_contentsProc.qna_delete(qcon_no); //글삭제
	  if(cnt_contents>0) {
		  System.out.println("글 삭제 성공");
		  this.categoryProc.cnt_minus(qna_contentsVO.getCate_no()); // 관련 글 수 감소
	  }
	  
	  int cnt_comments = this.qna_contentsProc.all_qna_delete_comment(qcon_no);
	  if(cnt_comments>0) {
	    System.out.println("댓글 삭제 성공");
	  }
	  
	  ra.addAttribute("cate_no",cate_no);
	  ra.addAttribute("now_page", now_page);
	  
	  return "redirect:/qcontents/qna_list_all";
  }
  
  /**
   * 댓글 등록은 부모글(질문글) 조회에서 진행함
   * @param qna_commentVO
   * @param session
   * @return
   */
  @PostMapping(value="/qna_create_comment")
  @ResponseBody
  public String qna_create_comment(@RequestBody Qna_commentVO qna_commentVO, HttpSession session) {
    
    System.out.println("-> 수신 데이터:" + qna_commentVO.toString());
    
    int acc_no = (int)session.getAttribute("acc_no");
    qna_commentVO.setAcc_no(acc_no);
    
    System.out.println("-> acc_no: " + acc_no);
    
    int cnt = this.qna_contentsProc.qna_create_comment(qna_commentVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
  
    return json.toString();
  }
  
  /**
   * 질문글 댓글 목록
   * @param qconno
   * @return
   */
  @GetMapping(value="/list_by_qcmt_no_join")
  @ResponseBody
  public String list_by_qcmt_no_join(int qcon_no) {
    List<Qna_Acc_commentVO> list = qna_contentsProc.list_by_qcmt_no_join_500(qcon_no);
    
    JSONObject obj = new JSONObject();
    obj.put("res", list);

    return obj.toString();
  }
  
  /**
   * 댓글 조회
   * @param qcmt_no
   * @return
   */
  @GetMapping(value="/qna_read_comment", produces ="application/json")
  @ResponseBody
  public String qna_read_comment(int qcmt_no) {
    Qna_commentVO qna_commentVO = this.qna_contentsProc.qna_read_comment(qcmt_no);
    
    JSONObject row = new JSONObject();
    row.put("qcmt_no", qna_commentVO.getQcmt_no());
    row.put("acc_no", qna_commentVO.getAcc_no());
    row.put("qcon_no", qna_commentVO.getQcon_no());
    row.put("qcmt_contents", qna_commentVO.getQcmt_contents());
    row.put("qcmt_date", qna_commentVO.getQcmt_date());
    
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
  @PostMapping(value="/qna_update_comment")
  @ResponseBody
  public String qna_update_comment(HttpSession session, @RequestBody Qna_commentVO qna_commentVO) {
    System.out.println("-> 수정할 수신 댓글: " + qna_commentVO.toString());
    
    int acc_no = (int)session.getAttribute("acc_no");
    
    int cnt = 0;
    if (acc_no == qna_commentVO.getAcc_no()) { // 회원 자신이 쓴 댓글만 수정 가능
      cnt = this.qna_contentsProc.qna_update_comment(qna_commentVO);
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
  @PostMapping(value="/qna_delete_comment")
  @ResponseBody
  public String qna_delete_comment(HttpSession session, @RequestBody Qna_commentVO qna_commentVO) {
    int acc_no = (int)session.getAttribute("acc_no");
    
    int cnt = 0;
    if (acc_no == qna_commentVO.getAcc_no()) { // 회원 자신이 쓴 댓글만 삭제 가능
      cnt = this.qna_contentsProc.qna_delete_comment(qna_commentVO.getQcmt_no());
    }
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);  // 1: 성공, 0: 실패
    return json.toString();
  }
  
//  @PostMapping(value="/qcon_bookmark")
//  @ResponseBody
//  public String bookmark_add(HttpSession session, 
//                                      @RequestBody Qna_commentVO qna_commentVO, 
//                                      HashMap<String, Object> map) {
//    int acc_no= (int)session.getAttribute("acc_no");
//    
//    int cnt = 0;
//    if (acc_no == qna_commentV)
//  }


  /**
   * 이미지 생성 AI
   * http://localhost:9093/qcontents/member_img
   * @param session
   * @return
   */
  @GetMapping(value="/member_img")
  // @ResponseBody: post 메서드에서만 사용.
  public String member_img(HttpSession session) {
    
    return "qcontents/member_img";
  }
  

    
}




///** 강사님 등록처리 성공한 코드
//* 질문글 등록 처리
//* @param model
//* @param request
//* @param session
//* @param ra
//* @return
//*/
//@PostMapping(value="/qna_create")
//public String qna_create(Model model,
//                       HttpServletRequest request,
//                       HttpSession session,
//                       RedirectAttributes ra,
//                       Qna_imageVO qna_imageVO,
//                       Qna_contentsVO qna_contentsVO) {
//
//// 질문글 등록 전 출력
//// System.out.println("-> [레코드 등록 전] qcon_no: " + qna_contentsVO.getQcon_no());
//
//// 카테고리 번호 가져오기
//int cate_no = qna_contentsVO.getCate_no(); // 부모글 번호
//
//// 질문글 등록 처리
//int cnt = this.qna_contentsProc.qna_create(qna_contentsVO);
//
//// 새로 등록된 질문글 번호 가져오기
//int qcon_no = qna_contentsVO.getQcon_no();
//System.out.println("-> [레코드 등록 후] qcon_no: " + qcon_no);
//
// // ---------------------------------------------------------------
// // 파일 전송 코드 시작
// // ---------------------------------------------------------------
// String file_origin_name = ""; // 원본 파일명
// String file_upload_name = ""; // 업로드된 파일명
// long file_size = 0;  // 파일 사이즈
// String file_thumb_name = ""; // Preview 이미지
// 
// String upDir = Contents.getUploadDir(); // 파일을 업로드할 폴더 준비
// 
// // 전송 파일이 없어서도 fnamesMF 객체가 생성됨.
// List<MultipartFile> fnamesMF = qna_imageVO.getFnamesMF();
// 
// int count = fnamesMF.size(); // 전송 파일 갯수
// 
// if (count > 0) {
//   for (MultipartFile multipartFile:fnamesMF) { // 파일 추출, 1개이상 파일 처리
//     file_size = multipartFile.getSize();  // 파일 크기
//     if (file_size > 0) { // 파일 크기 체크
//       file_origin_name = multipartFile.getOriginalFilename(); // 원본 파일명
//       file_upload_name = Upload.saveFileSpring(multipartFile, upDir); // 파일 저장, 업로드된 파일명
//       
//       if (Tool.isImage(file_origin_name)) { // 이미지인지 검사
//         file_thumb_name = Tool.preview(upDir, file_upload_name, 200, 150); // thumb 이미지 생성
//       }
//     }
//     
//     qna_imageVO.setQcon_no(qcon_no);
//     qna_imageVO.setFile_origin_name(file_origin_name);  // 실제 저장된 파일명
//     qna_imageVO.setFile_upload_name(file_upload_name);
//     qna_imageVO.setFile_thumb_name(file_thumb_name);
//     qna_imageVO.setFile_size(file_size);
//     
//     // 이미지 파일 등록 처리
//     int imageCnt = this.qna_contentsProc.qna_attach_create(qna_imageVO);
//
//   }
// }    
// // -----------------------------------------------------
// // 파일 전송 코드 종료
// // -----------------------------------------------------
//   
// ra.addAttribute("cate_no", cate_no);
// 
// return "redirect:/qcontents/qna_list_all";
//}
