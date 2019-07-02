package com.kkb.ms.mapper;

import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

	void update(@Param("name") String name, @Param("money")double money);

	double queryMoney(String name);
}
