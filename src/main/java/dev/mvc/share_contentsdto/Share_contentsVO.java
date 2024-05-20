package dev.mvc.share_contentsdto;

import lombok.Getter;
import lombok.Setter;


public class Share_contentsVO {
	private int scon_no;
	
	private int catno;
	
	private int acc_no;
	
	private String scon_title="";
	
	private String scon_contents="";
	
	private int scon_views;
	
	private String scon_date;
	
	private int scon_bookmark;
	
	private int scon_comment;
	
	private String scon_commnet;
	
	private int scon_priority;
	
	private String word="";
	
	private String mark="N";

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public int getScon_no() {
		return scon_no;
	}

	public void setScon_no(int scon_no) {
		this.scon_no = scon_no;
	}

	public int getCatno() {
		return catno;
	}

	public void setCatno(int catno) {
		this.catno = catno;
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

	public int getScon_bookmark() {
		return scon_bookmark;
	}

	public void setScon_bookmark(int scon_bookmark) {
		this.scon_bookmark = scon_bookmark;
	}

	public int getScon_comment() {
		return scon_comment;
	}

	public void setScon_comment(int scon_comment) {
		this.scon_comment = scon_comment;
	}

	public String getScon_commnet() {
		return scon_commnet;
	}

	public void setScon_commnet(String scon_commnet) {
		this.scon_commnet = scon_commnet;
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
