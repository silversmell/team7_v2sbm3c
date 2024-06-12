package dev.mvc.rank;

import java.util.ArrayList;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import dev.mvc.share_contentsdto.Share_contentsVO;
@Service("dev.mvc.rank.RankProc")
public class RankProc implements RankProcInter {
	
	@Autowired
	private RankDAOInter rankDAO;
	
	public RankProc() {
		System.out.println("->RankProc.create .");
	}


	@Override
	public ArrayList<Share_contentsVO> ranking() {
		ArrayList<Share_contentsVO> list = this.rankDAO.ranking();
		return list;
	}


	@Override
	public ArrayList ranking_tag(int tag_no) {
		ArrayList<Share_contentsVO> list = this.rankDAO.ranking_tag(tag_no);
		return list;
	}


	@Override
	public int ranking_tag_count(int tag_no) {
		int cnt = this.rankDAO.ranking_tag_count(tag_no);
		return cnt;
	}


	@Override
	public ArrayList ranking_tag_list(int tag_no) {
		ArrayList<Share_contentsVO> list = this.rankDAO.ranking_tag_list(tag_no);
		return null;
	}


}
