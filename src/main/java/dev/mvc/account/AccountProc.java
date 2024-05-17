package dev.mvc.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.account.AccountProc")
public class AccountProc implements AccountProcInter {
	@Autowired
	private AccountDAOInter accountDAO;
	
	public AccountProc() {
		System.out.println("-> AccountProc created.");
	}

	@Override
	public int checkID(String acc_id) {
		int cnt = this.accountDAO.checkID(acc_id);
		return cnt;
	}

	@Override
	public int create(AccountVO accountVO) {
		int cnt = this.accountDAO.create(accountVO);
		return cnt;
	}
}
