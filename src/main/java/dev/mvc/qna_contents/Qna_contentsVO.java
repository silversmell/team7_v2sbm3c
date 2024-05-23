package dev.mvc.qna_contents;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import groovy.transform.ToString;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @ToString
public class Qna_contentsVO {

  /** 질문 게시글 번호 */
  private Integer qcon_no;
  
  /** 카테고리 번호 */
  private Integer cate_no;
  
  /** 회원 번호 */
  private Integer acc_no;
  
  /** 질문 게시글 제목 */
  @NotEmpty(message="질문게시글 제목은 필수입력 항목입니다.")
  @Size(min=1, max=30, message="질문게시글 제목의 입력글자 수는 최소 1자에서 최대 30자(한글 10자) 가능합니다.")
  private String qcon_name="";
  
  /** 질문 게시글 내용 */
  @NotEmpty(message = "질문게시글 내용은 필수입력 항목입니다.")
  private String qcon_contents="";
  
  /** 질문 게시글 조회수 */
  @Min(value=0)
  @Max(value=1000000)
  private Integer qcon_views=0;
  
  /** 질문 게시글 북마크 수 */
  @Min(value=0)
  @Max(value=1000000)
  private Integer qcon_bookmark=0;
  
  /** 질문 게시글 댓글 수 */
  @Min(value=0)
  @Max(value=1000000)
  private Integer qcon_comment=0;
  
  /** 질문 게시글 등록일 */
  private String qcon_date = "";
  
  /** 질문게시글 검색어 */
  private String word="";
  
  /** 비밀번호 */
  private String qcon_passwd="";
  
  /** Form의 파일을 MultipartFile로 변환하여 List에 저장 */
  private List<MultipartFile> fnamesMF;

  /** 파일 단위 출력 */
  private String flabel;
  
  private MultipartFile file1MF = null;
}
