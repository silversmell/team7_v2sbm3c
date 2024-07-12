package dev.jpa.sse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.jpa.sse.entity.AccountVO;
import dev.jpa.sse.entity.ReplyVO;
import dev.jpa.sse.entity.Share_contentsVO;
import dev.jpa.sse.repository.AccountRepository;
import dev.jpa.sse.repository.ReplyRepository;
import dev.jpa.sse.repository.SconRepository;

@Service
public class SconService {
	
    @Autowired
    private SconRepository sconRepository;
    
    public Optional<Share_contentsVO> findbySconno(int sconno){
    	return sconRepository.findBySconno(sconno);
    }


}