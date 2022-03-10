package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path="/account")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) throws AccountNotFoundException {
        int userId = userDao.findIdByUsername(principal.getName());
        return accountDao.findAccountByUserId(userId).getBalance();
    }

//    @RequestMapping(path = "", method = RequestMethod.GET)
//    public List<Account> getAllAccounts() {
//        return accountDao.getAllAccounts();
//    }

//    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
//    public Account get(@Valid @PathVariable int id) throws AccountNotFoundException {
//        return accountDao.findAccountByAccountId(id);
//    }

//    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
//    public String getByUsername(@Valid @PathVariable String username) throws AccountNotFoundException {
//        return accountDao.getUsernameByAccountId();
//    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(path = "", method = RequestMethod.POST)
//    public Account create(@Valid @RequestBody Account account) {
//        return accountDao.create(account);
//    }


}
