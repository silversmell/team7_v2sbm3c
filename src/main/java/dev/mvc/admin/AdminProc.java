package dev.mvc.admin;

import java.util.HashMap;

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
	public int recordLog(AdmLogVO admLogVO) {
		int cnt = this.adminDAO.recordLog(admLogVO);
		return cnt;
	}


}
