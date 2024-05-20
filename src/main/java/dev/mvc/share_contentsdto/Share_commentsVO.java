package dev.mvc.share_contentsdto;

import lombok.Getter;
import lombok.Setter;


public class Share_commentsVO {
	private int scmt_no;
	
	private int acc_no;
	
	private int scon_no;
	
	private String scmt_comment;
	
	private String scmt_date;
	

	


	public int getScmt_no() {
		return scmt_no;
	}

	public void setScmt_no(int scmt_no) {
		this.scmt_no = scmt_no;
	}

	public int getacc_no() {
		return acc_no;
	}

	public void setacc_no(int acc_no) {
		this.acc_no = acc_no;
	}

	public int getScon_no() {
		return scon_no;
	}

	public void setScon_no(int scon_no) {
		this.scon_no = scon_no;
	}

	public String getScmt_comment() {
		return scmt_comment;
	}

	public void setScmt_comment(String scmt_comment) {
		this.scmt_comment = scmt_comment;
	}

	public String getScmt_date() {
		return scmt_date;
	}

	public void setScmt_date(String scmt_date) {
		this.scmt_date = scmt_date;
	}
	

	
}
