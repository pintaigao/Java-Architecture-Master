package com.hptg.springdemo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hptg.springdemo.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-transfer.xml")
public class AccountServiceTest {

	@Autowired
	private AccountService service;
	
	@Test
	public void testTransfer() {
		service.transfer("老公", "老婆", 100);
	}
}
