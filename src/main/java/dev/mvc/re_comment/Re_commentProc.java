package dev.mvc.re_comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public ArrayList<Re_commentVO> read() {
		ArrayList<Re_commentVO> list = this.re_commentDAO.read();
		return list;
	}

	@Override
	public ArrayList<Re_commentVO> read_recomment(HashMap<String, Object> map) {
		ArrayList<Re_commentVO> list = this.re_commentDAO.read_recomment(map);
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
	public Re_commentVO read_comment(int recomment_no) {
		Re_commentVO re_commentVO = this.re_commentDAO.read_comment(recomment_no);
		return re_commentVO;
	}

	@Override
	public int delete(int recomment_no) {
		int cnt = this.re_commentDAO.delete(recomment_no);
		return cnt;
	}

}
