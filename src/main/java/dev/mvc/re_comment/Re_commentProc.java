package dev.mvc.re_comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.account.AccountVO;

@Service("dev.mvc.re_comment.Re_commentProc")
public class Re_commentProc implements Re_commentProcInter {
	
	public Re_commentProc() {
		System.out.println("->Re_commentProcInter created.");
	}

	
	@Autowired
	private Re_commentDAOInter re_commentDAO;

	@Override
	public int create(HashMap<String, Object> map) {
		int cnt = this.re_commentDAO.create(map);
		return cnt;
	}

	@Override
	public ArrayList<Share_recommentVO> read() {
		ArrayList<Share_recommentVO> list = this.re_commentDAO.read();
		return list;
	}

	@Override
	public ArrayList<Share_recommentVO> read_recomment(HashMap<String, Object> map) {
		ArrayList<Share_recommentVO> list = this.re_commentDAO.read_recomment(map);
		return list;
	}

	@Override
	public int scmtno_delete(int scmt_no) {
		int cnt = this.re_commentDAO.scmtno_delete(scmt_no);
		return cnt;
	}

	@Override
	public int sconno_delete(List<Integer> scon_no) {
		int cnt = this.re_commentDAO.sconno_delete(scon_no);
		return cnt;
	}

	@Override
	public int update(HashMap<String, Object> map) {
		int cnt = this.re_commentDAO.update(map);
		return cnt;
	}

	@Override
	public Share_recommentVO read_comment(int srecmt_no) {
		Share_recommentVO re_commentVO = this.re_commentDAO.read_comment(srecmt_no);
		return re_commentVO;
	}

	@Override
	public int delete(int srecmt_no) {
		int cnt = this.re_commentDAO.delete(srecmt_no);
		return cnt;
	}

	@Override
	public AccountVO recomment_acc(int srecmt_no) {
		AccountVO acc = this.re_commentDAO.recomment_acc(srecmt_no);
		return acc;
	}

}
