package dev.mvc.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//CREATE TABLE CATEGORY(
//    CATE_NO NUMBER(10) NOT NULL PRIMARY KEY,
//    CATE_NAME VARCHAR2(30) NOT NULL,
//    CATE_CNT NUMBER(7) DEFAULT 0 NOT NULL,
//    CATE_SEQNO NUMBER(5) DEFAULT 0 NOT NULL,
//    CATE_VISIBLE CHAR(1) DEFAULT 'Y' NOT NULL
//);

@Getter @Setter
public class CategoryVO {
  
  /** 카테고리 번호 */
  private Integer cate_no;
  
  /** 카테고리명 */
  @NotEmpty(message="카테고리명은 필수입력 항목입니다.")
  @Size(min=1, max=30, message="카테고리명의 입력글자 수는 최소 1자에서 30자(한글 10자)이어야 합니다.")
  private String cate_name = "";
  
  /** 관련 자료 수 */
  @NotNull(message="관련 자료 수는 필수입력 항목입니다.")
  @Min(value=0)
  @Max(value=1000000)
  private Integer cnt = 0;
  
  /** 카테고리 출력 순서 */
  @NotNull(message="출력 순서는 필수입력 항목입니다.")
  @Min(value=0)
  @Max(value=1000000)
  private Integer seqno = 0;
  
  /** 카테고리 출력 모드 */
  @NotNull(message="출력 모드는 필수입력 항목입니다.")
  @Pattern(regexp="^[YN]$", message="Y 또는 N만 입력 가능합니다.")
  private String visible = "Y";
  
}
