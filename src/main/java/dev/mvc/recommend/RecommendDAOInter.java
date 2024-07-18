package dev.mvc.recommend;

import java.util.ArrayList;

import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;

public interface RecommendDAOInter {

	/**
	 * 추천글 목록
	 * 
	 * @param acc_no
	 * @return
	 */
	public ArrayList<Share_contentsVO> list(int acc_no);
	
	/**
	 * 게시글 사진 가져오기
	 * 
	 * @return
	 */
	public ArrayList<Share_imageVO> contentImages(int scon_no);
	
	/**
	 * 선택된 해시태그 목록
	 * 
	 * @param acc_no
	 * @return
	 */
	public ArrayList<HashtagVO> selectedTags(int acc_no);

	/**
	 * 랜덤으로 게시글 가져오기
	 * 
	 * @return
	 */
	public ArrayList<Share_contentsVO> random_list();
}
