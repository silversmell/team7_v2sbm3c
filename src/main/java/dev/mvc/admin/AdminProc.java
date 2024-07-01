package dev.mvc.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.account.AccountVO;
import dev.mvc.tool.Security;

@Component("dev.mvc.admin.AdminProc")
public class AdminProc implements AdminProcInter {
	@Autowired
	private AdminDAOInter adminDAO;

	@Autowired
	Security security;
	
    /** 페이지당 출력할 레코드 갯수 */
    public static int RECORD_PER_PAGE = 10;

    /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
    public static int PAGE_PER_BLOCK = 10;

	public AdminProc() {
		System.out.println("-> AdminProc created.");
	}

	@Override
	public int checkID(String adm_id) {
		int cnt = this.adminDAO.checkID(adm_id);
		return cnt;
	}

	@Override
	public int checkName(String adm_name) {
		int cnt = this.adminDAO.checkName(adm_name);
		return cnt;
	}

	@Override
	public int checkEmail(String adm_email) {
		int cnt = this.adminDAO.checkEmail(adm_email);
		return cnt;
	}

	@Override
	public int create(AdminVO adminVO) {
		/* 비밀번호 암호화 */
		adminVO.setAdm_pw(new Security().aesEncode(adminVO.getAdm_pw())); // 단축형

		int cnt = this.adminDAO.create(adminVO);
		return cnt;
	}

	@Override
	public AdminVO readById(String adm_id) {
		AdminVO adminVO = this.adminDAO.readById(adm_id);
		return adminVO;
	}

	@Override
	public int login(HashMap<String, Object> map) {
		int cnt = this.adminDAO.login(map);
		return cnt;
	}

	@Override
	public int recordLog(AdminLogVO adminLogVO) {
		int cnt = this.adminDAO.recordLog(adminLogVO);
		return cnt;
	}

	@Override
	public ArrayList<Map<String, Object>> logList() {
		 ArrayList<Map<String, Object>> list = this.adminDAO.logList();
		return list;
	}

	@Override
	public ArrayList<Map<String, Object>> searchLogs(Map<String, String> map) {
		ArrayList<Map<String, Object>> list = this.adminDAO.searchLogs(map);
		return list;
	}

	@Override
	public int searchCount(HashMap<String, Object> hashMap) {
	    int cnt = this.adminDAO.searchCount(hashMap);
	    return cnt;
	}

	@Override
	public ArrayList<Map<String, Object>> pagingList(Map<String, Object> map) {
		/*
		 * 예) 페이지당 10개의 레코드 출력 1 page: WHERE r >= 1 AND r <= 10 2 page: WHERE r >= 11
		 * AND r <= 20 3 page: WHERE r >= 21 AND r <= 30
		 * 
		 * 페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작 1 페이지 시작 rownum: now_page = 1, (1
		 * - 1) * 10 --> 0 2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10 3 페이지 시작
		 * rownum: now_page = 3, (3 - 1) * 10 --> 20
		 */
		int begin_of_page = ((int) map.get("now_page") - 1) * this.RECORD_PER_PAGE;

		// 시작 rownum 결정
		// 1 페이지 = 0 + 1: 1
		// 2 페이지 = 10 + 1: 11
		// 3 페이지 = 20 + 1: 21
		int start_num = begin_of_page + 1;

		// 종료 rownum
		// 1 페이지 = 0 + 10: 10
		// 2 페이지 = 10 + 10: 20
		// 3 페이지 = 20 + 10: 30
		int end_num = begin_of_page + this.RECORD_PER_PAGE;
		/*
		 * 1 페이지: WHERE r >= 1 AND r <= 10 2 페이지: WHERE r >= 11 AND r <= 20 3 페이지: WHERE
		 * r >= 21 AND r <= 30
		 */

		// System.out.println("begin_of_page: " + begin_of_page);
		// System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);

		map.put("start_num", start_num);
		map.put("end_num", end_num);

		ArrayList<Map<String, Object>> list = this.adminDAO.pagingList(map);
		return list; 
	}
	
	/**
	 * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작
	 * 
	 * @param now_page        현재 페이지
	 * @param word            검색어
	 * @param list_file       목록 파일명
	 * @param search_count    검색 레코드수
	 * @param record_per_page 페이지당 레코드 수
	 * @param page_per_block  블럭당 페이지 수
	 * @return 페이징 생성 문자열
	 */
	@Override
	public String pagingBox(int now_page, String word_id, String word_ip, String start_date, String end_date,
			String list_file, int search_count, int record_per_page, int page_per_block) {
		// 전체 페이지 수: (double)1/10 -> 0.1 -> 1 페이지, (double)12/10 -> 1.2 페이지 -> 2 페이지
		int total_page = (int) (Math.ceil((double) search_count / record_per_page));
		// 전체 그룹 수: (double)1/10 -> 0.1 -> 1 그룹, (double)12/10 -> 1.2 그룹-> 2 그룹
		int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
		// 현재 그룹 번호: (double)13/10 -> 1.3 -> 2 그룹
		int now_grp = (int) (Math.ceil((double) now_page / page_per_block));

		// 1 group: 1, 2, 3 ... 9, 10
		// 2 group: 11, 12 ... 19, 20
		// 3 group: 21, 22 ... 29, 30
		int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지
		int end_page = (now_grp * page_per_block); // 특정 그룹의 마지막 페이지

		StringBuffer str = new StringBuffer(); // String class 보다 문자열 추가등의 편집시 속도가 빠름

		// style이 java 파일에 명시되는 경우는 로직에 따라 css가 영향을 많이 받는 경우에 사용하는 방법
		str.append("<style type='text/css'>");
		str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}");
		str.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}");
		str.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}");
		str.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}");
		str.append("  .span_box_1{");
		str.append("    text-align: center;");
		str.append("    font-size: 1em;");
		str.append("    border: 1px;");
		str.append("    border-style: solid;");
		str.append("    border-color: #cccccc;");
		str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/");
		str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
		str.append("  }");
		str.append("  .span_box_2{");
		str.append("    text-align: center;");
		str.append("    background-color: #668db4;");
		str.append("    color: #FFFFFF;");
		str.append("    font-size: 1em;");
		str.append("    border: 1px;");
		str.append("    border-style: solid;");
		str.append("    border-color: #cccccc;");
		str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/");
		str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/");
		str.append("  }");
		str.append("</style>");
		str.append("<div id='paging'>");
		// str.append("현재 페이지: " + nowPage + " / " + totalPage + "  "); 

		// 이전 10개 페이지로 이동
		// now_grp: 1 (1 ~ 10 page)
		// now_grp: 2 (11 ~ 20 page)
		// now_grp: 3 (21 ~ 30 page)
		// 현재 2그룹일 경우: (2 - 1) * 10 = 1그룹의 마지막 페이지 10
		// 현재 3그룹일 경우: (3 - 1) * 10 = 2그룹의 마지막 페이지 20
		int _now_page = (now_grp - 1) * page_per_block;
		if (now_grp >= 2) { // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성
			str.append("<span class='span_box_1'><A href='" + list_file 
					+ "?word_id=" + word_id + "&word_ip=" + word_ip
					+ "&start_date=" + start_date + "&end_date=" + end_date
					+ "&now_page=" + _now_page + "'>이전</A></span>");
		}
		

		// 중앙의 페이지 목록
		for (int i = start_page; i <= end_page; i++) {
			if (i > total_page) { // 마지막 페이지를 넘어갔다면 페이 출력 종료
				break;
			}

			if (now_page == i) { // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
				str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지, 강조
			} else {
				// 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
				str.append("<span class='span_box_1'><A href='" + list_file
						+ "?word_id=" + word_id + "&word_ip=" + word_ip
						+ "&start_date=" + start_date + "&end_date=" + end_date
						+ "&now_page=" + i + "'>" + i + "</A></span>");
			}
		}

		// 10개 다음 페이지로 이동
		// nowGrp: 1 (1 ~ 10 page), nowGrp: 2 (11 ~ 20 page), nowGrp: 3 (21 ~ 30 page)
		// 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
		// 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
		// 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
		_now_page = (now_grp * page_per_block) + 1; // 최대 페이지수 + 1
		if (now_grp < total_grp) {
			str.append("<span class='span_box_1'><A href='" + list_file
					+ "?word_id=" + word_id + "&word_ip=" + word_ip
					+ "&start_date=" + start_date + "&end_date=" + end_date
					+ "&now_page=" + _now_page + "'>다음</A></span>");
		}
		str.append("</div>");

		return str.toString();
	}
	
	@Override
	public ArrayList<AccountVO> accList() {
		ArrayList<AccountVO> list = this.adminDAO.accList();
		return list;
	}
	
	@Override
	public ArrayList<AccountVO> accSearchList(Map<String, Object> map) {
		ArrayList<AccountVO> list = this.adminDAO.accSearchList(map);
		return list;
	}
	
	@Override
	public ArrayList<Map<String, Object>> accLogList() {
		ArrayList<Map<String, Object>> list = this.adminDAO.accLogList();
		return list;
	}
	
	@Override
	public ArrayList<Map<String, Object>> accSearchLogs(Map<String, String> map) {
		ArrayList<Map<String, Object>> list = this.adminDAO.accSearchLogs(map);
		return list;
	}
	
	@Override
	public int accSearchCount(HashMap<String, Object> hashMap) {
	    int cnt = this.adminDAO.accSearchCount(hashMap);
	    return cnt;
	}

	@Override
	public ArrayList<Map<String, Object>> accPagingList(Map<String, Object> map) {
		/*
		 * 예) 페이지당 10개의 레코드 출력 1 page: WHERE r >= 1 AND r <= 10 2 page: WHERE r >= 11
		 * AND r <= 20 3 page: WHERE r >= 21 AND r <= 30
		 * 
		 * 페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작 1 페이지 시작 rownum: now_page = 1, (1
		 * - 1) * 10 --> 0 2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10 3 페이지 시작
		 * rownum: now_page = 3, (3 - 1) * 10 --> 20
		 */
		int begin_of_page = ((int) map.get("now_page") - 1) * this.RECORD_PER_PAGE;

		// 시작 rownum 결정
		// 1 페이지 = 0 + 1: 1
		// 2 페이지 = 10 + 1: 11
		// 3 페이지 = 20 + 1: 21
		int start_num = begin_of_page + 1;

		// 종료 rownum
		// 1 페이지 = 0 + 10: 10
		// 2 페이지 = 10 + 10: 20
		// 3 페이지 = 20 + 10: 30
		int end_num = begin_of_page + this.RECORD_PER_PAGE;
		/*
		 * 1 페이지: WHERE r >= 1 AND r <= 10 2 페이지: WHERE r >= 11 AND r <= 20 3 페이지: WHERE
		 * r >= 21 AND r <= 30
		 */

		// System.out.println("begin_of_page: " + begin_of_page);
		// System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);

		map.put("start_num", start_num);
		map.put("end_num", end_num);

		ArrayList<Map<String, Object>> list = this.adminDAO.accPagingList(map);
		return list;
	}
	
}
