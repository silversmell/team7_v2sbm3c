package dev.mvc.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.mvc.account.AccountVO;
import jakarta.servlet.http.HttpSession;

public interface AdminProcInter {
	
	/**
	 * 로그인된 관리자 세션 확인
	 * 
	 * @param session
	 * @return
	 */
	public boolean isAdmin(HttpSession session);

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
	 * 관리자 목록
	 * 
	 * @return
	 */
	public ArrayList<AdminVO> list();
	
	/**
	 * 관리자 검색
	 * 
	 * @param map
	 * @return
	 */
	public ArrayList<AdminVO> searchList(Map<String, Object> map);
	
	/**
	 * 관리자 로그 전체 조회
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> logList();
	
	/**
	 * 관리자 로그 검색
	 * 
	 * @param map
	 * @return
	 */
	public ArrayList<Map<String, Object>> searchLogs(Map<String, String> map);
	
	/**
	 * 검색된 레코드 갯수
	 * 
	 * @param hashMap
	 * @return
	 */
	public int searchCount(HashMap<String, Object> hashMap);
	
	/**
	 * 관리자 로그 목록 (검색 + 페이징)
	 * 
	 * @param map
	 * @return
	 */
	public ArrayList<Map<String, Object>> pagingList(Map<String, Object> map);

	/**
	 * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 현재 페이지: 11 / 22 [이전] 11 12 13 14 15 16 17
	 * 18 19 20 [다음]
	 * 
	 * @param now_page			현재 페이
	 * @param word_id			검색어(아이디)
	 * @param word_ip			검색어(아이피)
	 * @param start_date		검색날짜(시작일)
	 * @param end_date			검색날짜(종료일)
	 * @param list_file			목록 파일명
	 * @param search_count		검색 레코드 수
	 * @param record_per_page	페이지당 레코드 수
	 * @param page_per_block	블럭당 레코드 수
	 * @return 페이징 생성 문자열
	 */
	public String pagingBox(int now_page, String word_id, String word_ip, String start_date, String end_date,
			String list_file, int search_count, int record_per_page, int page_per_block);

	/**
	 * 회원 목록
	 * 
	 * @return
	 */
	public ArrayList<AccountVO> accList();
	
	/**
	 * 회원 검색
	 * 
	 * @param map
	 * @return
	 */
	public ArrayList<AccountVO> accSearchList(Map<String, Object> map);

	/**
	 * 회원 로그 전체 조회
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> accLogList();

	/**
	 * 회원 로그 검색
	 * 
	 * @param words
	 * @return
	 */
	public ArrayList<Map<String, Object>> accSearchLogs(Map<String, String> words);
	
	/**
	 * 검색된 레코드 갯수
	 * 
	 * @param hashMap
	 * @return
	 */
	public int accSearchCount(HashMap<String, Object> hashMap);

	/**
	 * 회원 로그 목록 (검색 + 페이징)
	 * 
	 * @param map
	 * @return
	 */
	public ArrayList<Map<String, Object>> accPagingList(Map<String, Object> map);

}
