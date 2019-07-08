package com.hptg.springdemo.mapper;

import org.apache.ibatis.annotations.Param;

public interface AccountMapper {

    int queryMoney2(@Param("name") String name);
    void update(@Param("name") String name, @Param("money") double money);

}
