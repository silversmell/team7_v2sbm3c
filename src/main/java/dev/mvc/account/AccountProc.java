package dev.mvc.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;

@Component("dev.mvc.account.AccountProc")
public class AccountProc implements AccountProcInter {
	@Autowired
	private AccountDAOInter accountDAO;

	public AccountProc() {
		System.out.println("-> AccountProc created.");
	}

	@Override
	public List<HashtagVO> hashtagList() {
		List<HashtagVO> hashtags = this.accountDAO.hashtagList();
		return hashtags;
	}

	@Override
	public String tagCodeList() {
		String tag_codes  = this.accountDAO.tagCodeList();
		return tag_codes;
	}

	@Override
	public int checkID(String acc_id) {
		int cnt = this.accountDAO.checkID(acc_id);
		return cnt;
	}

	@Override
	public int checkName(String acc_name) {
		int cnt = this.accountDAO.checkName(acc_name);
		return cnt;
	}

	@Override
	public int create(AccountVO accountVO) {
		int cnt = this.accountDAO.create(accountVO);
		return cnt;
	}

	@Override
	public int insertRecommend(RecommendVO recommendVO) {
		int cnt = this.accountDAO.insertRecommend(recommendVO);
		return cnt;
	}

	
	
}
