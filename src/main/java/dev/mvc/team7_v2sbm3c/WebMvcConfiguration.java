package dev.mvc.team7_v2sbm3c;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.mvc.account.PicUpload;
import dev.mvc.qna_contents.Qcontents;
import dev.mvc.share_contents.Contents;
import dev.mvc.tool.Tool;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Windows: path = "C:/kd/deploy/resort_v2sbm3c_blog/contents/storage";
        // ▶ file:///C:/kd/deploy/resort_v2sbm3c_blog/contents/storage
    	
        // Mac: path = "/Users/{username}/deploy/team7/contents/storage/";
        // ▶ file:////Users/{username}/deploy/team7/contents/storage/
      
        // Ubuntu: path = "/home/ubuntu/deploy/resort_v2sbm3c_blog/contents/storage";
        // ▶ file:////home/ubuntu/deploy/resort_v2sbm3c_blog/contents/storage
      
        // JSP 인식되는 경로: http://localhost:9091/attachfile/storage";
        // registry.addResourceHandler("/contents/storage/**").addResourceLocations("file:///" +  Tool.getOSPath() + "/attachfile/storage/");
      
        // 인식되는 경로: http://localhost:9093/contents/storage;
        registry.addResourceHandler("/contents/storage/**").addResourceLocations("file:///" +  Contents.getUploadDir());
          
        // 인식되는 경로: http://localhost:9093/contents/storage";
        registry.addResourceHandler("/scontents/storage/**").addResourceLocations("file:///" +  PicUpload.getUploadDir());
        
        // 인식되는 가상경로: http://localhost:9093/qcontents/storage"; 
        registry.addResourceHandler("/qcontents/storage/**").addResourceLocations("file:///" +  Qcontents.getUploadDir());
        registry.addResourceHandler("/openai/member/storage/**").addResourceLocations("file:///" +  Qcontents.getUploadDirOpenAI());
    }
 
}
