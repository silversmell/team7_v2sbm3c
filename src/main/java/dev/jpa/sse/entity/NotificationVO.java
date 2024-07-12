package dev.jpa.sse.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "notification")
@NoArgsConstructor
public class NotificationVO {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
    private Long id; //기본키

    private String sender; //발신자
    

    private String createdAt; //보낸 시간
    
    private String contents;  // 채팅 메시지 내용 또는 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scon_no")
    private Share_contentsVO sharecontents; // 외래키: share_contents //수신자 같고오는 VO
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_no")
    private AccountVO account; // 외래키: Use
    
    private String mark = "F"; // 기본값 설정
}

