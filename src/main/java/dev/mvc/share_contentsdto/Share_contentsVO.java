package dev.mvc.share_contentsdto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class Share_contentsVO {
	private int scon_no;
	
	private int cate_no;
	
	private int acc_no;
	
	private String scon_title="";
	
	private String scon_contents="";
	
	private int scon_views;
	
	private String scon_date;

	private int scon_priority;
	
	private String word="";
	
	private String scon_bookmark="N";

	public String scon_bookmark() {
		return scon_bookmark;
	}

	public void setscon_bookmark(String scon_bookmark) {
		this.scon_bookmark = scon_bookmark;
	}

	public int getScon_no() {
		return scon_no;
	}

	public void setScon_no(int scon_no) {
		this.scon_no = scon_no;
	}

	public int getacc_no() {
		return acc_no;
	}

	public void setacc_no(int acc_no) {
		this.acc_no = acc_no;
	}

	public String getScon_title() {
		return scon_title;
	}

	public void setScon_title(String scon_title) {
		this.scon_title = scon_title;
	}

	public String getScon_contents() {
		return scon_contents;
	}

	public void setScon_contents(String scon_contents) {
		this.scon_contents = scon_contents;
	}

	public int getScon_views() {
		return scon_views;
	}

	public void setScon_views(int scon_views) {
		this.scon_views = scon_views;
	}

	public String getScon_date() {
		return scon_date;
	}

	public void setScon_date(String scon_date) {
		this.scon_date = scon_date;
	}

	public int getScon_priority() {
		return scon_priority;
	}

	public void setScon_priority(int scon_priority) {
		this.scon_priority = scon_priority;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}


	
}
