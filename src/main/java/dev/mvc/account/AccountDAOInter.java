package dev.mvc.account;

import java.util.List;

import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;

public interface AccountDAOInter {
	
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

}
