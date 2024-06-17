package dev.mvc.category;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE CATEGORY(
//CATE_NO NUMBER(10) NOT NULL PRIMARY KEY,
//CATE_NAME VARCHAR2(30) NOT NULL,
//CATE_CNT NUMBER(7) DEFAULT 0 NOT NULL,
//CATE_SEQNO NUMBER(5) DEFAULT 0 NOT NULL,
//CATE_VISIBLE CHAR(1) DEFAULT 'Y' NOT NULL
//);

@Getter @Setter
public class CategoryVOMenu {

  /** 카테고리명 */
  private String cate_name;
}
