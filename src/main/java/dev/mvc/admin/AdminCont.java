package dev.mvc.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.category.CategoryProcInter;
import dev.mvc.tool.Security;

@RequestMapping("/admin")
@Controller
public class AdminCont {
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;
  
  @Autowired
  @Qualifier("dev.mvc.category.CagegoryProc")
  private CategoryProcInter categoryProc;
  
  @Autowired
  Security security;
  
  public AdminCont() {
    System.out.println("-> AdminCont created.");
  }
  
}
