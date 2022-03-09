package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import java.util.List;

public interface AccountDao {

    List<Account> getAllAccounts();

    Account findAccountByAccountId (int accountId);

    String getUserByAccountId (int accountId);

    void create(Account account);
}
