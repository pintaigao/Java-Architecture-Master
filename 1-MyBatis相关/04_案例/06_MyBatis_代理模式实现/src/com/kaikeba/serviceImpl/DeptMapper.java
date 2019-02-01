package com.kaikeba.serviceImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.kaikeba.service.SqlSession;

public class DeptMapper implements SqlSession {
    PreparedStatement ps;
	@Override
	public int save(String sql) throws SQLException {//JDBC主要业务 输送sql
		   int num= ps.executeUpdate(sql);
		return num;
	}

}
