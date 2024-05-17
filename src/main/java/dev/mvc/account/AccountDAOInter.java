package dev.mvc.account;

public interface AccountDAOInter {
	/**
	 * 중복 아이디 검사
	 * 
	 * @param acc_id
	 * @return 중복 아이디 갯수, 1: 중복, 0: 중복 없음 
	 */
	 public int checkID(String acc_id);
	 
	 /**
	  * 회원 가입 
	  * 
	  * @param accountVO
	  * @return 추가된 레코드 갯수 
	  */
	 public int create(AccountVO accountVO);
	
}
