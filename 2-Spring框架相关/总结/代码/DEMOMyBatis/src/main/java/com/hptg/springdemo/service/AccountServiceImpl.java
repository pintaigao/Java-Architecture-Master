package com.hptg.springdemo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hptg.springdemo.mapper.AccountMapper;

@Service
public class AccountServiceImpl implements AccountService {

	@Resource
	private AccountMapper mapper;

	@Override
	public void transfer(String from, String to, double money) {
		// 先查询from账户的钱
		int fromMoney = mapper.queryMoney2(from);
		// 对from账户进行扣钱操作
		mapper.update(from, fromMoney - money);

		// 手动制造异常
		// System.out.println(1/0);
		// 先查询from账户的钱
		int toMoney = mapper.queryMoney2(to);
		// 对to账户进行加钱操作
		mapper.update(to, toMoney + money);
	}

}
