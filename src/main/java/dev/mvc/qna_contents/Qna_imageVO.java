package dev.mvc.qna_contents;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE QNA_IMAGE(
//    FILE_NO NUMBER(10) NOT NULL PRIMARY KEY,
//    QCON_NO NUMBER(10), -- FK
//    FILE_ORIGIN_NAME VARCHAR2(100) NOT NULL,
//    FILE_UPLOAD_NAME VARCHAR2(100) NOT NULL,
//    FILE_THUMB_NAME VARCHAR2(100) NOT NULL,
//    FILE_SIZE INT DEFAULT 0 NOT NULL,
//    FILE_DATE DATE NOT NULL,
//  FOREIGN KEY (QCON_NO) REFERENCES QNA_CONTENTS (QCON_NO)
//);

@Getter @Setter
public class Qna_imageVO {
 
  /** 첨부파일 번호 */
  private Integer file_no;
  
  /** 질문게시글 번호 */
  private Integer qcon_no;
  
  /** 원본 파일명 */
  private String file_origin_name="";
  
  /** 업로드 파일명 */
  private String file_upload_name="";
  
  /** Thumb 파일명 */
  private String file_thumb_name="";
  
  /** 파일 크기 */
  private long file_size;
  
  /** 파일 등록일 */
  private String file_date ="";
  
  
  /** Form의 파일을 MultipartFile로 변환하여 List에 저장  */
  private List<MultipartFile> fnamesMF;
  
  // private MultipartFile fnamesMF;  // 하나의 파일 처리
  /** 파일 단위 출력 */
  private String flabel;   
  
  private MultipartFile file1MF = null;
}
