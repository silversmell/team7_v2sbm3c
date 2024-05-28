package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;
import jakarta.servlet.http.HttpSession;

public interface AccountProcInter {

	/**
	 * 해시태그 목록 불러오기
	 * 
	 * @return
	 */
	public List<HashtagVO> hashtagList();
	
	/**
	 * 해시태그 분류 코드 가져오기
	 * 
	 * @return
	 */
	public String tagCodeList();

	/**
	 * 중복 아이디(이메일) 검사
	 * 
	 * @param acc_id
	 * @return 중복 아이디 갯수, 1: 중복, 0: 중복 없음
	 */
	public int checkID(String acc_id);

	/**
	 * 중복 이름(닉네임) 검사
	 * 
	 * @param acc_name
	 * @return 중복 아이디 갯수, 1: 중복, 0: 중복 없음
	 */
	public int checkName(String acc_name);

	/**
	 * 회원 가입
	 * 
	 * @param accountVO
	 * @return 생성된 PK(acc_no) 값
	 */
	public int create(AccountVO accountVO);

	/**
	 * 추천 해시태그 저장
	 * 
	 * @param recommendVO
	 * @return 추가된 레코드 갯수
	 */
	public int insertRecommend(RecommendVO recommendVO);
	
    /**
     * 회원 목록
     * 
     * @return
     */
    public ArrayList<AccountVO> list();
    
    /**
     * 회원 정보 조회(회원 목록, 마이페이지)
     * @param acc_no
     * @return
     */
    public AccountVO read(int acc_no);
    
    /**
     * 아이디로 회원 정보 조회(로그인)
     * 
     * @param acc_id
     * @return
     */
    public AccountVO readById(String acc_id);
    
    /**
     * 로그인
     * 
     * @param map
     * @return
     */
    public int login(HashMap<String, Object> map);
    
    /**
     * 로그인된 회원 계정인지 검사
     * 
     * @param session
     * @return
     */
    public boolean isMember(HttpSession session);
    
    /**
     * 로그인된 회원이 관리자 계정인지 검사
     * 
     * @param session
     * @return
     */
    public boolean isMemberAdmin(HttpSession session);
    
    
    /**
     * 선택된 해시태그 조회(회원 정보 조회)
     * 
     * @param acc_no
     * @return
     */
    public String selectedTags(int acc_no);
    
    /**
     * 회원 정보 수정(회원 목록, 마이페이지)
     * @param accountVO
     * @return
     */
    public int update(AccountVO accountVO);
    
    /**
     * 프로필 사진 업데이트
     * 
     * @param accountVO
     * @return
     */
    public int updatePic(AccountVO accountVO);
    
    /**
     * 기존 추천 데이터(해시태그) 삭제
     * 
     * @param acc_no
     * @return
     */
    public int deleteRecommend(int acc_no);
    
    /**
     * 현재 비밀번호 확인
     * 
     * @param map
     * @return 0(다름) or 1(일치)
     */
    public int checkPasswd(HashMap<String, Object> map);
    
    /**
     * 비밀번호 변경
     * @param map
     * @return 변경된 비밀번호 갯수
     */
    public int updatePasswd(HashMap<String, Object> map);
    
    /** 
     * 회원 정보 삭제(관리자, 회원 목록)
     * 
     * @param acc_no
     * @return
     */
    public int delete(int acc_no);

}
