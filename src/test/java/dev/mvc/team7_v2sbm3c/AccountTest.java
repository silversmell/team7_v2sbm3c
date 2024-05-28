package dev.mvc.team7_v2sbm3c;

import java.util.HashMap;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import dev.mvc.account.AccountProcInter;
import dev.mvc.account.AccountVO;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
public class AccountTest {

	@Autowired
	@Qualifier("dev.mvc.account.AccountProc") // @Service("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;

	@Test
	public void checkPasswd() {
		String json_src = "{\"current_passwd\":\"1234\"}";
		String acc_no = "2";
		String acc_pw = "1111";
		
		JSONObject src = new JSONObject(json_src); // String -> JSON
		String current_passwd = (String) src.getString(""); // 값 가져오기
		System.out.println("---> current_passwd:  " + current_passwd);

		try {
			Thread.sleep(3000);
		} catch (Exception e) {

		}

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("acc_no", acc_no);
		map.put("acc_pw", current_passwd);

		int cnt = this.accountProc.checkPasswd(map);

		JSONObject json = new JSONObject();
		json.put("cnt", cnt);
		System.out.println("---> json.toString() cnt: " + json.toString());
	}

//	@Test
//	public void checkId() {
//		String acc_id = "user1";
//		int cnt = this.accountProc.checkID(acc_id);
//		System.out.println("-> cnt: " + cnt);
//	}

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
