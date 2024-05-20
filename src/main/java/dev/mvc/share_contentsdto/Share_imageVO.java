package dev.mvc.share_contentsdto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;


public class Share_imageVO {
  

	public int getFile_no() {
		return file_no;
	}

	public void setFile_no(int file_no) {
		this.file_no = file_no;
	}

	public int getScon_no() {
		return scon_no;
	}

	public void setScon_no(int scon_no) {
		this.scon_no = scon_no;
	}

	public String getFile_origin_name() {
		return file_origin_name;
	}

	public void setFile_origin_name(String file_origin_name) {
		this.file_origin_name = file_origin_name;
	}

	public String getFile_upload_name() {
		return file_upload_name;
	}

	public void setFile_upload_name(String file_upload_name) {
		this.file_upload_name = file_upload_name;
	}

	public String getFile_thumb_name() {
		return file_thumb_name;
	}

	public void setFile_thumb_name(String file_thumb_name) {
		this.file_thumb_name = file_thumb_name;
	}

	public int getFile_size() {
		return file_size;
	}

	public void setFile_size(int file_size) {
		this.file_size = file_size;
	}

	public String getFile_date() {
		return file_date;
	}

	public void setFile_date(String file_date) {
		this.file_date = file_date;
	}
	public List<MultipartFile> getFnamesMF() {
		return fnamesMF;
	}

	public void setFnamesMF(List<MultipartFile> fnamesMF) {
		this.fnamesMF = fnamesMF;
	}

	public String getFlabel() {
		return flabel;
	}

	public void setFlabel(String flabel) {
		this.flabel = flabel;
	}
	
	/** Form의 파일을 MultipartFile로 변환하여 List에 저장  */
	private List<MultipartFile> fnamesMF;
	
	private String flabel;

	private int file_no;
	
	private int scon_no;
	
	private String file_origin_name;
	
	private String file_upload_name;
	
	private String file_thumb_name;
	
	private int file_size;
	
	private String file_date;
	

}
