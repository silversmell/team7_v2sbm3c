package dev.mvc.share_contentsdto;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NotificationVO {
	
    private Long id; //기본키

    private String sender; //발신자
    
    private String createdAt; //보낸 시간
    
    private String contents;  // 채팅 메시지 내용 또는 댓글 내용

    private int scon_no;
    
    private int acc_no;
    
    private String mark = "F"; // 기본값 설정
}

