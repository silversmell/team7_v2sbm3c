package dev.mvc.fotorama;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/fotorama")
@Controller
public class FotoramaCont {
  public FotoramaCont() {
    System.out.println("-> FotoramaCont created.");
  }

  @GetMapping(value="/data") // http://localhost:9091/fotorama/data
  public String data(Model model) {
    // System.out.println("-> pay");
    return "fotorama/data"; // /templates/fotorama/data.html
  }
  // 기본 이미지 지원 안됨
  // http://localhost:9091/fotorama/fotorama-4.6.4/example.html   // X
  // http://localhost:9091/templates/fotorama/fotorama-4.6.4/example.html   // X
  @GetMapping(value="/example") // http://localhost:9091/fotorama/example
  public String example(Model model) {
    return "fotorama/fotorama-4.6.4/example"; // /templates/fotorama/fotorama-4.6.4/example.html
  }

  @GetMapping(value="/exam1") // http://localhost:9091/fotorama/exam1
  public String exam1(Model model) {
    return "fotorama/exam1"; // /templates/fotorama/exam1.html
  }
  
  @GetMapping(value="/exam2") // http://localhost:9091/fotorama/exam2
  public String exam2(Model model) {
    return "fotorama/exam2"; // /templates/fotorama/exam2.html
  }

  @GetMapping(value="/exam3") // http://localhost:9091/fotorama/exam3
  public String exam3(Model model) {
    return "fotorama/exam3"; // /templates/fotorama/exam3.html
  }
  
}