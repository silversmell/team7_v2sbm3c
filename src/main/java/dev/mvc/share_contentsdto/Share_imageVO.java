package dev.mvc.share_contentsdto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Share_imageVO {
  
  private MultipartFile file1MF = null;
  
	private int file_no;
	
	private int scon_no;
	
	private String file_origin_name;
	
	private String file_upload_name;
	
	private String file_thumb_name;
	
	private int file_size;
	
	private String file_date;
	

}
