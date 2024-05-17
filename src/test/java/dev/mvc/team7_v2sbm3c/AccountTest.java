package dev.mvc.team7_v2sbm3c;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.account.AccountProcInter;
import dev.mvc.account.AccountVO;

@SpringBootTest
public class AccountTest {

	@Autowired
	@Qualifier("dev.mvc.account.AccountProc") // @Service("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;

	@Test
	public void checkId() {
		String acc_id = "user1";
		int cnt = this.accountProc.checkID(acc_id);
		System.out.println("-> cnt: " + cnt);
	}

//	@Test
//	public void testCreate() {
//		AccountVO accountVO = new AccountVO();
//		accountVO.setAcc_id("test@test.com");
//		accountVO.setAcc_pw("1234");
//		accountVO.setAcc_name("TESTACCT");
//		
//		JSONObject obj = new JSONObject();
//		obj.put("acc_id", accountVO.getAcc_id());
//		obj.put("acc_pw", accountVO.getAcc_pw());
//		obj.put("acc_name", accountVO.getAcc_name());
//
//		int cnt = this.accountProc.create(accountVO);
//		System.out.println();
//		System.out.println("-> cnt: " + cnt);
//	}

}
