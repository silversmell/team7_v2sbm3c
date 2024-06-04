package dev.mvc.reply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.reply.ReplyProc")
public class ReplyProc implements ReplyProcInter {
	@Autowired
	private ReplyDAOInter replyDAO;

	@Override
	public int create_comment(HashMap<String, Object> map) {
		int cnt = this.replyDAO.create_comment(map);
		return cnt;
	}

	@Override
	public ArrayList<Share_commentVO> list() {
		ArrayList<Share_commentVO> list = this.replyDAO.list();
		return list;
	}

	@Override
	public ArrayList read_comment(int scon_no) {
		ArrayList<Share_commentVO> list = this.replyDAO.read_comment(scon_no);
		return list;
	}

	@Override
	public int update_comment(HashMap<String, Object> map) {
		int cnt = this.replyDAO.update_comment(map);
		return cnt;
	}

	@Override
	public int sdelete_comment(List<Integer> scon_no) {
		int cnt = this.replyDAO.sdelete_comment(scon_no);
		return cnt;
	}

	@Override
	public int delete_scmtno(int scmt_no) {
		int cnt = this.replyDAO.delete_scmtno(scmt_no);
		return cnt;
	}

	@Override
	public int delete_comments(int scon_no) {
		int cnt = this.replyDAO.delete_comments(scon_no);
		return cnt;
	}

	@Override
	public int comment_search(int scon_no) {
		int cnt = this.replyDAO.comment_search(scon_no);
		return cnt;
	}

	@Override
	public List<ReplyMemberVO> list_by_contentsno_join_500(int scon_no) {
		List<ReplyMemberVO> list = this.replyDAO.list_by_contentsno_join_500(scon_no);
		return list;
	}
	
	@Override
	public Share_commentVO read(int scmt_no) {
		Share_commentVO share_commentVO = this.replyDAO.read(scmt_no);
 		return share_commentVO;
	}


}
