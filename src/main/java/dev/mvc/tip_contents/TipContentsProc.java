package dev.mvc.tip_contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.Security;

@Component("dev.mvc.tip_contents.TipContentsProc")
public class TipContentsProc implements TipContentsProcInter {
	@Autowired
	Security security;
	
	@Autowired
	private TipContentsDAOInter tcontentsDAO;

	@Override
	public int create(TipContentsVO tcontentsVO) {
		String passwd = tcontentsVO.getTcon_passwd();
		String passwd_encoded = this.security.aesEncode(passwd);
		tcontentsVO.setTcon_passwd(passwd_encoded);
		
		int cnt = this.tcontentsDAO.create(tcontentsVO);
		return cnt;
	}
	
	@Override
	public int youtube(HashMap<String, Object> map) {
		int cnt = this.tcontentsDAO.youtube(map);
		return cnt;
	}

	@Override
	public int list_count(HashMap<String, Object> map) {
		int cnt = this.tcontentsDAO.list_count(map);
		return cnt;
	}

	@Override
	public ArrayList<TipContentsVO> list(HashMap<String, Object> map) {
		ArrayList<TipContentsVO> list = this.tcontentsDAO.list(map);
		return list;
	}
	
	@Override
	public ArrayList<TipContentsVO> tconImages(int tcon_no) {
		ArrayList<TipContentsVO> images = this.tcontentsDAO.tconImages(tcon_no);
		return images;
	}

	@Override
	public TipContentsVO read(int tcon_no) {
		TipContentsVO tcontentsVO = this.tcontentsDAO.read(tcon_no);
		return tcontentsVO;
	}

	@Override
	public boolean isLiked(Map<String, Object> map) {
	    boolean check = this.tcontentsDAO.isLiked(map);
		return check;
	}
	
	@Override
	public int insertLike(Map<String, Object> map) {
		int cnt = this.tcontentsDAO.insertLike(map);
		return cnt;
	}

	@Override
	public int deleteLike(Map<String, Object> map) {
		int cnt = this.tcontentsDAO.deleteLike(map);
		return cnt;
	}

}
