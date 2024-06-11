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


}
