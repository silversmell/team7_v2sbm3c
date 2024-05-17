package dev.mvc.team7_v2sbm3c;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.account.AccountDAOInter;
import dev.mvc.account.AccountProcInter;
import dev.mvc.account.AccountVO;

@SpringBootTest
class Team7V2sbm3cApplicationTests {
	@Autowired
	private AccountDAOInter accountDAO;
	
	@Autowired
	@Qualifier("dev.mvc.account.AccountProc")
	private AccountProcInter accountProc;
	
	@Test
	void contextLoads() {
	}


}
