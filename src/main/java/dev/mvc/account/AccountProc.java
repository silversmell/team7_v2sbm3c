package dev.mvc.account;

import java.util.ArrayList;
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

	@Override
	public ArrayList<AccountVO> list() {
		ArrayList<AccountVO> list = this.accountDAO.list();
		return list;
	}

	@Override
	public AccountVO read(int acc_no) {
		AccountVO accountVO = this.accountDAO.read(acc_no);
		return accountVO;
	}

	@Override
	public String selectedTags(int acc_no) {
		String tag_names  = this.accountDAO.selectedTags(acc_no);
		return tag_names;
	}

	@Override
	public int update(AccountVO accountVO) {
		int cnt = this.accountDAO.update(accountVO);
		return cnt;
	}

	@Override
	public int deleteRecommend(int acc_no) {
		int cnt = this.accountDAO.deleteRecommend(acc_no);
		return cnt;
	}
	
	@Override
	public int delete(int acc_no) {
		int cnt = this.accountDAO.delete(acc_no);
		return cnt;
	}


	
}
