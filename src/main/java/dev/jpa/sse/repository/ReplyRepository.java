package dev.jpa.sse.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Repository;

import dev.jpa.sse.entity.AccountVO;
import dev.jpa.sse.entity.NotificationVO;
import dev.jpa.sse.entity.ReplyVO;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyVO, Integer> {
	Optional<ReplyVO> findByScmtno(int scmtno);
	
	List<ReplyVO>findByAccno(int accno);
	
} 
