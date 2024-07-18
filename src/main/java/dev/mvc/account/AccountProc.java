package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.qna_contents.Qna_contentsVO;
import dev.mvc.qna_contents.Qna_imageVO;
import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;
import dev.mvc.tool.Security;
import jakarta.servlet.http.HttpSession;

@Component("dev.mvc.account.AccountProc")
public class AccountProc implements AccountProcInter {
	@Autowired
	private AccountDAOInter accountDAO;
	
	@Autowired
	Security security;
	
    /** 페이지당 출력할 레코드 갯수 */
    public static int RECORD_PER_PAGE = 10;

    /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
    public static int PAGE_PER_BLOCK = 10;

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
		// accountVO.setAcc_pw(new Security().aesEncode(accountVO.getAcc_pw())); // 단축형
		String acc_pw = accountVO.getAcc_pw();
		String pw_encoded = this.security.aesEncode(acc_pw);
		accountVO.setAcc_pw(pw_encoded);
		
		int cnt = this.accountDAO.create(accountVO);
		return cnt;
	}

	@Override
	public int insertRecommend(RecommendVO recommendVO) {
		int cnt = this.accountDAO.insertRecommend(recommendVO);
		return cnt;
	}

	@Override
	public AccountVO read(int acc_no) {
		AccountVO accountVO = this.accountDAO.read(acc_no);
		return accountVO;
	}
	
	@Override
	public AccountVO mypage(int acc_no) {
		AccountVO accountVO = this.accountDAO.read(acc_no);
		return accountVO;
	}
	
	@Override
	public AccountVO readById(String acc_id) {
		AccountVO accountVO = this.accountDAO.readById(acc_id);
		return accountVO;
	}

	@Override
	public int login(HashMap<String, Object> map) {
		int cnt = this.accountDAO.login(map);
		return cnt;
	}
	
	@Override
	public int recordLog(AccLogVO AccLogVO) {
		int cnt = this.accountDAO.recordLog(AccLogVO);
		return cnt;
	}
	
	@Override
	public boolean isMember(HttpSession session) {
		boolean sw = false;	// 로그아웃 상태로 초기화
		String acc_grade = (String)session.getAttribute("acc_grade");
		
		if(acc_grade != null) {
			if(acc_grade.equals("admin") || acc_grade.equals("member")) {
				sw = true;	// 로그인 상태로 전환
			}
		}
		return sw;
	}

	@Override
	public boolean isMemberAdmin(HttpSession session) {
	  boolean sw = false; // 로그아웃 상태로 초기화
    String acc_grade = (String)session.getAttribute("acc_grade");
    
    if(acc_grade != null) {
      if(acc_grade.equals("admin")) {
        sw = true;  // 로그인 상태로 전환
      }
    }
    return sw;
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
	public int updatePic(AccountVO accountVO) {
		int cnt = this.accountDAO.updatePic(accountVO);
		return cnt;
	}

	@Override
	public int deleteRecommend(int acc_no) {
		int cnt = this.accountDAO.deleteRecommend(acc_no);
		return cnt;
	}
	
	@Override
	public int checkPasswd(HashMap<String, Object> map) {
		int cnt = this.accountDAO.checkPasswd(map);
		return cnt;
	}

	@Override
	public int updatePasswd(HashMap<String, Object> map) {
		int cnt = this.accountDAO.updatePasswd(map);
		return cnt;
	}
	
	@Override
	public int resetPasswd(HashMap<String, Object> map) {
		int cnt = this.accountDAO.resetPasswd(map);
		return cnt;
	}

	
	@Override
	public int delete(int acc_no) {
		int cnt = this.accountDAO.delete(acc_no);
		return cnt;
	}

	@Override
	public ArrayList<Share_contentsVO> myScontents(int acc_no) {
		ArrayList<Share_contentsVO> list = this.accountDAO.myScontents(acc_no);
		return list;
	}
	
	@Override
	public ArrayList<Qna_contentsVO> myQcontents(int acc_no) {
		ArrayList<Qna_contentsVO> list = this.accountDAO.myQcontents(acc_no);
		return list;
	}

	@Override
	public ArrayList<Share_imageVO> shareImages(int scon_no) {
		ArrayList<Share_imageVO> images = this.accountDAO.shareImages(scon_no);
		return images;
	}
	
	@Override
	public ArrayList<Qna_imageVO> qnaImages(int qcon_no) {
		ArrayList<Qna_imageVO> images = this.accountDAO.qnaImages(qcon_no);
		return images;
	}

	@Override
	public ArrayList<Share_contentsVO> shareMarks(int acc_no) {
		ArrayList<Share_contentsVO> list = this.accountDAO.shareMarks(acc_no);
		return list;
	}

	@Override
	public ArrayList<Qna_contentsVO> qnaMarks(int acc_no) {
		ArrayList<Qna_contentsVO> list = this.accountDAO.qnaMarks(acc_no);
		return list;
	}

	@Override
	public int deleteShareMark(Map<String, Object> map) {
		int cnt = this.accountDAO.deleteShareMark(map);
		return cnt;
	}

	@Override
	public int deleteQnaMark(Map<String, Object> map) {
		int cnt = this.accountDAO.deleteQnaMark(map);
		return cnt;
	}

	@Override
	public int insertShareMark(Map<String, Object> map) {
		int cnt = this.accountDAO.insertShareMark(map);
		return cnt;
	}

	@Override
	public int insertQnaMark(Map<String, Object> map) {
		int cnt = this.accountDAO.insertQnaMark(map);
		return cnt;
	}

	@Override
	public int sconCmtCnt(int scon_no) {
	    int cnt = this.accountDAO.sconCmtCnt(scon_no);
	    return cnt;
	}

	@Override
	public int qconCmtCnt(int qcon_no) {
	    int cnt = this.accountDAO.qconCmtCnt(qcon_no);
	    return cnt;
	}



}
