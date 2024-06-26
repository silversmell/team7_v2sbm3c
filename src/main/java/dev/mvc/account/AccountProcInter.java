package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.mvc.qna_contents.Qna_contentsVO;
import dev.mvc.qna_contents.Qna_imageVO;
import dev.mvc.recommend.HashtagVO;
import dev.mvc.recommend.RecommendVO;
import dev.mvc.share_contentsdto.Share_contentsVO;
import dev.mvc.share_contentsdto.Share_imageVO;
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
	 * 
	 * @param acc_no
	 * @return
	 */
	public AccountVO read(int acc_no);

	/**
	 * 아이디로 회원 정보 조회(로그인, 비밀번호 찾기)
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
	 * 회원 로그 기록
	 * 
	 * @param AccLogVO
	 * @return 추가된 레코드 갯수
	 */
	public int recordLog(AccLogVO AccLogVO);

	/**
	 * 회원 로그 전체 조회
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> logList();

	/**
	 * 회원 로그 검색
	 * 
	 * @param words
	 * @return
	 */
	public ArrayList<Map<String, Object>> searchLogs(Map<String, String> words);
	
	/**
	 * 검색된 레코드 갯수
	 * 
	 * @param hashMap
	 * @return
	 */
	public int searchCount(HashMap<String, Object> hashMap);

	/**
	 * 회원 로그 목록 (검색 + 페이징)
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
	 * 
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
	 * 
	 * @param map
	 * @return 변경된 비밀번호 갯수
	 */
	public int updatePasswd(HashMap<String, Object> map);

	/**
	 * 비밀번호 재설정
	 * 
	 * @param map
	 * @return
	 */
	public int resetPasswd(HashMap<String, Object> map);

	/**
	 * 회원 정보 삭제(관리자, 회원 목록)
	 * 
	 * @param acc_no
	 * @return
	 */
	public int delete(int acc_no);

	/**
	 * 내가 쓴 게시글
	 * 
	 * @param acc_no
	 * @return
	 */
	public ArrayList<Share_contentsVO> myContents(int acc_no);

	/**
	 * 공유 게시글 사진 가져오기
	 * 
	 * @param scon_no
	 * @return
	 */
	public ArrayList<Share_imageVO> shareImages(int scon_no);
	
	/**
	 * 질문 게시글 사진 가져오기
	 * 
	 * @param qcon_no
	 * @return
	 */
	public ArrayList<Qna_imageVO> qnaImages(int qcon_no);
	
	/**
	 * 나의 북마크 목록(공유 게시글)
	 * 
	 * @param acc_no
	 * @return
	 */
	public ArrayList<Share_contentsVO> shareMarks(int acc_no);
	
	/**
	 * 나의 북마크 목록(질문 게시글)
	 * 
	 * @param acc_no
	 * @return
	 */
	public ArrayList<Qna_contentsVO> qnaMarks(int acc_no);
	
	/** 
	 * 북마크 삭제(공유, 질문)
	 * 
	 * @param map
	 * @return
	 */
	public int deleteMark(Map<String, Object> map);
	
	/**
	 * 북마크 저장(공유, 질문)
	 * 
	 * @param map
	 * @return
	 */
	public int insertMark(Map<String, Object> map);
	
	/**
	 * 공유 게시글 댓글 수
	 * 
	 * @param scon_no
	 * @return
	 */
	public int sconCmtCnt(int scon_no);
	
	/**
	 * 질문 게시글 댓글 수
	 * 
	 * @param qcon_no
	 * @return
	 */
	public int qconCmtCnt(int qcon_no);

}
