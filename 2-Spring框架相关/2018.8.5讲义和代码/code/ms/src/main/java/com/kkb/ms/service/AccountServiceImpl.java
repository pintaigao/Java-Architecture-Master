package com.kkb.ms.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kkb.ms.mapper.AccountMapper;

@Service
public class AccountServiceImpl implements AccountService {

	@Resource
	private AccountMapper mapper;

	@Override
	public void transfer(String from, String to, double money) {
		// 先查询from账户的钱
		double fromMoney = mapper.queryMoney(from);
		// 对from账户进行扣钱操作
		mapper.update(from, fromMoney - money);

		// 手动制造异常
		// System.out.println(1/0);
		// 先查询from账户的钱
		double toMoney = mapper.queryMoney(to);
		// 对to账户进行加钱操作
		mapper.update(to, toMoney + money);
	}

}
