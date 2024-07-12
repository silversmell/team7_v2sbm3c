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
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    
    public List<AccountVO> getAllAccounts() {
        return accountRepository.findAll();
    }
//    public List<AccountVO> getAccountsByAccId(String acc_id) {
//        return accountRepository.findByAcc_id(acc_id);
//    }

    public void saveAccount(AccountVO account) {
        accountRepository.save(account);
    }
    
    public Optional<AccountVO> findByAccno(int accno){
    	return accountRepository.findByAccno(accno);
    }
}