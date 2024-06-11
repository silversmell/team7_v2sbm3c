package dev.mvc.recommend;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;

@Component("dev.mvc.recommend.RecommendProc")
public class RecommendProc implements RecommendProcInter {
	
	@Autowired
	private RecommendDAOInter recommendDAO;
	
	public RecommendProc() {
		System.out.println("-> RecommendProc created.");
	}

	@Override
	public ArrayList<Share_contentsVO> list(int acc_no) {
		ArrayList<Share_contentsVO> list = this.recommendDAO.list(acc_no);
		return list;
	}
	
	@Override
	public ArrayList<Share_imageVO> contentImages(int scon_no) {
		ArrayList<Share_imageVO> images = this.recommendDAO.contentImages(scon_no);
		return images;
	}

	@Override
	public ArrayList<HashtagVO> selectedTags(int acc_no) {
		ArrayList<HashtagVO> hashtags = this.recommendDAO.selectedTags(acc_no);
		return hashtags;
	}
	
}
