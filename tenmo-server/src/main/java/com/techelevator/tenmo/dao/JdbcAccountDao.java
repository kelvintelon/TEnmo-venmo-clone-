package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Account> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM accounts";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
        while (result.next()) {
            allAccounts.add(mapRowToAccount(result));
        }
        return allAccounts;
    }

    @Override
    public Account findAccountByAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT account_id FROM accounts WHERE account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        if (result.next()) {
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public String getUsernameByAccountId(int accountId) throws UsernameNotFoundException {
        String sql = "SELECT username FROM accounts " +
                "JOIN tenmo_user ON accounts.user_id = tenmo_user.user_id " +
                "WHERE account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        if (result.next()) {
            return result.getString("username");
        }
        throw new UsernameNotFoundException("No users were found here");
    }

    @Override
    public Account create(Account newAccount) {
        String sql = "INSERT INTO account (user_id, balance) " +
                "values(?,?,?) RETURNING account_id;";
        int newId = jdbcTemplate.queryForObject(sql, Integer.class, newAccount.getUserId(), newAccount.getBalance());

        return findAccountByAccountId(newId);
    }

    @Override
    public void addBalance(Transfer transfer) {
        String sql = "UPDATE accounts SET balance = (balance + ?) WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccount_to());
    }

    @Override
    public void subtractBalance(Transfer transfer) {
        String sql = "UPDATE accounts SET balance = (balance - ?) WHERE account_id = ?;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccount_from());
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }

}
