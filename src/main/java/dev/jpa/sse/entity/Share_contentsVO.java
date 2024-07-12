package dev.jpa.sse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="share_contents")
public class Share_contentsVO {
    
    @Id
    @Column(name="scon_no", nullable=false, columnDefinition="NUMBER(10,0) default 0")
    private int sconno;
    
    @Column(name="acc_no")
    private int accno;
    
    @Column(name="scon_title")
	private String scontitle="";
    
    @Column(name="scon_contents")
	private String sconcontents="";
	
}

