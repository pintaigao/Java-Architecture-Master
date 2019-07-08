package com.hptg.springdemo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * JdbcDaoSupport内部封装了JdbcTemplate
 *
 * @author think
 */
//@Repository
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {
    // JDBC操作
    // Mybatis操作
    // JdbcTemplate操作

    /**
     * Attention:
     * 传统的方法是用Autowired一个JdbcTemplate来实现数据库交互
     * 但是，现在可以 extends 一个JdbcDaoSupport来实现数据库交互，这样的作用就是，下面这个@Autowired就可以删掉了，
     * 然后jdbcTemplate.update改成this.getJdbcTemplate().update...
     * 但是，extends JdbcDaoSupport 后，xml就要手动装配这个Bean，还有@Repository和XML中的Bean是相互矛盾的，在XML中定义了，头顶的Repository就要去掉
     * 或者，附带下面这两个dataSource和@PostConstruct
     */

    /*@Autowired
    private DataSource dataSource;
    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }*/

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void update(String name, double money) {
        Object[] args = {money, name};
        //jdbcTemplate.update("UPDATE account SET money = ? WHERE name = ? ", args);
        this.getJdbcTemplate().update("UPDATE account SET money = ? WHERE name = ? ", args);
    }

    @Override
    public double queryMoney(String name) {
        // Double money = jdbcTemplate.queryForObject("SELECT money FROM account WHERE name = ?", new DoubleMapper(), name);
        Double money = this.getJdbcTemplate().queryForObject("SELECT money FROM account WHERE name = ?", new DoubleMapper(), name);
        return money;
    }

}

class DoubleMapper implements RowMapper<Double> {
    @Override
    public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getDouble("money");
    }

}
