package dev.jpa.sse.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import dev.jpa.sse.controller.NotificationController;
import dev.jpa.sse.entity.AccountVO;
import dev.jpa.sse.entity.NotificationVO;
import dev.jpa.sse.entity.ReplyVO;
import dev.jpa.sse.entity.Share_contentsVO;
import dev.jpa.sse.repository.AccountRepository;
import dev.jpa.sse.repository.NotificationRepository;
import dev.jpa.sse.repository.ReplyRepository;
import dev.jpa.sse.repository.SconRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
	private static Map<String, Integer> notificationCounts = new HashMap<>();
    //private final PostRepository postRepository;
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired 
	private ReplyRepository replyRepository;
	
	@Autowired 
	private SconRepository sconRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;

    // 메시지 알림
    public SseEmitter subscribe(String accId) {
    
        // 1. 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        
        // 2. 연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect").comment("finish"));
        } catch (IOException e) {
            e.printStackTrace();
        }

       // 3. 저장
        NotificationController.sseEmitters.put(accId, sseEmitter);

		 // 4. 연결 종료 처리
        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(accId));	// sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(accId));		// sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(accId));		// sseEmitter 연결에 오류가 발생할 경우

        return sseEmitter;
    }
    
    
    
   public List<NotificationVO>findBySharecontents(int sconno){
	   return notificationRepository.findBySharecontents_Sconno(sconno);
   }
   
   public Optional<NotificationVO>findByAccnoAndCreatedAt(int accno, String createdAt){
	   return notificationRepository.findByAccount_AccnoAndCreatedAt(accno, createdAt);
   }
   
   public void deleteById(Long id) {
	   notificationRepository.deleteById(id);
   }

   public List<Object[]> findNotificationDetailsByAccNo(int acc_no) { 
	return notificationRepository.findNotificationDetailsByAccNo(acc_no);
   } 
//    
    // 댓글 알림 - 게시글 작성자 에게
    public void notifyComment(int scmtno) {
    	System.out.println("notifyComment에 들어옴");
    	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ReplyVO post = replyRepository.findByScmtno(scmtno).orElseThrow(
                () -> new IllegalArgumentException("댓글을 찾을 수 없습니다.")
        );
        
        Optional<AccountVO> account = accountRepository.findByAccno(post.getAccno());
        int scon_no=post.getSconno();
        
        Share_contentsVO share_contents = sconRepository.findBySconno(scon_no).orElseThrow(
        		 () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );
    	Optional<Share_contentsVO>scon=sconRepository.findBySconno(scon_no);
    	int accno=scon.get().getAccno();
        
        AccountVO accountVO = accountRepository.findByAccno(accno).orElseThrow(
       		 () -> new IllegalArgumentException("회원을 찾을 수 없습니다.")
       );


    	Optional<AccountVO> acc = accountRepository.findByAccno(accno);
    	String accid = acc.get().getAccId();
    	//System.out.println(accid);
    	if(!NotificationController.sseEmitters.containsKey(accid)) { // 만약에 accid가 없다면
    		System.out.println("sseEmitters 들어옴");
    		NotificationController.sseEmitters.put(accid, subscribe(accid));
    	}
        if (NotificationController.sseEmitters.containsKey(accid)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(accid);
            //System.out.println("sseEmitters에 있음");
            try {
            	System.out.println("알림 들어옴");
            	Map<String,String> eventData = new HashMap<>();
            	eventData.put("message", "댓글이 달렸습니다.");
            	System.out.println(account.get().getAccId());
            	eventData.put("sender", account.get().getAccId()); 
            	eventData.put("contents", post.getScmtcomment());
            	
                sseEmitter.send(SseEmitter.event().name("addComment").data(eventData));
                
                // DB 저장
                NotificationVO notification = new NotificationVO();
                notification.setSender(account.get().getAccId());
                notification.setContents(post.getScmtcomment());
                notification.setSharecontents(share_contents);
                notification.setCreatedAt(date.toString());
                notification.setAccount(accountVO);
                notificationRepository.save(notification); //데이터 저장
                
                //알림개수 증가
                notificationCounts.put(accid, notificationCounts.getOrDefault(accid, 0)+1);
                
             // 현재 알림 개수 전송
                sseEmitter.send(SseEmitter.event().name("notificationCount").data(notificationCounts.get(accid)));
                System.out.println("알림 확인");
                
            } catch (Exception e) {
                NotificationController.sseEmitters.remove(accid);
            }
        }
    }

}