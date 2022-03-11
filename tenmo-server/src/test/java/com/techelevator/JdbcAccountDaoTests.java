package com.techelevator;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests{

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private UserDao userDao;

    @Test
    public void getBalance_Returns_Correct_Balance () {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        accountDao = new JdbcAccountDao(jdbcTemplate);
        userDao = new JdbcUserDao(jdbcTemplate);

        userDao.create("testusername", "testpassword");
        int id = userDao.findIdByUsername("testusername");
        Account actual = accountDao
    }


}
