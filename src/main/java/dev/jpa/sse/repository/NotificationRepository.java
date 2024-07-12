package dev.jpa.sse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dev.jpa.sse.entity.NotificationVO;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationVO, Long> {
    Optional<NotificationVO> findById(Long id);

    Optional<NotificationVO> findByAccount_AccnoAndCreatedAt(int accNo, String createdAt);
   
    void deleteById(Long id);
    
    List<NotificationVO> findBySharecontents_Sconno(int sconno);
    
    
    @Transactional
    @Query(value = "SELECT nt.contents, nt.sender, nt.created_at, nt.scon_no,nt.id " +
            "FROM notification nt " +
            "INNER JOIN share_contents sc ON sc.scon_no = nt.scon_no " +
            "INNER JOIN account ac ON ac.acc_no = sc.acc_no " +
            "WHERE ac.acc_no = :acc_no order by nt.id desc ", nativeQuery = true)
    	List<Object[]> findNotificationDetailsByAccNo(@Param("acc_no") int acc_no);

	}
