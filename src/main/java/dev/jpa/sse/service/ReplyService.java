package dev.jpa.sse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jpa.sse.entity.AccountVO;
import dev.jpa.sse.entity.ReplyVO;
import dev.jpa.sse.repository.AccountRepository;
import dev.jpa.sse.repository.ReplyRepository;

@Service
public class ReplyService {
	
    @Autowired
    private ReplyRepository replyRepository;

    public List<ReplyVO> getAll() {
        return replyRepository.findAll();
    }
    
    public Optional<ReplyVO> findByScmtno(int scmtno) {
    	return replyRepository.findByScmtno(scmtno);
    }

}