package dev.mvc.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface AdminProcInter {

	/**
	 * 중복 아이디 검사
	 * 
	 * @param adm_id
	 * @return
	 */
	public int checkID(String adm_id);

	/**
	 * 중복 이름 검사
	 * 
	 * @param adm_name
	 * @return
	 */
	public int checkName(String adm_name);
	
	/**
	 * 중복 이메일 검사
	 * @param adm_email
	 * @return
	 */
	public int checkEmail(String adm_email);

	/**
	 * 관리자 생성(회원 가입)
	 * 
	 * @param adminVO
	 * @return
	 */
	public int create(AdminVO adminVO);
	
	/**
	 * 아이디로 관리자 정보 조회(로그인, 비밀번호 찾기)
	 * 
	 * @param adm_id
	 * @return
	 */
	public AdminVO readById(String adm_id);
	
	/**
	 * 로그인
	 * 
	 * @param map
	 * @return
	 */
	public int login(HashMap<String, Object> map);
	
	/**
	 * 관리자 로그 기록
	 * 
	 * @param AdminLogVO
	 * @return 추가된 레코드 갯수
	 */
	public int recordLog(AdminLogVO adminLogVO);
	
	/**
	 * 관리자 로그 전체 조회
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> logList();

}
