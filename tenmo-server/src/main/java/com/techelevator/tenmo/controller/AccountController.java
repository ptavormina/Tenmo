package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "balance/{userId}", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@PathVariable int userId) throws AccountNotFoundException {
        BigDecimal balance = accountDao.getBalanceByUserId(userId);
        return balance;
    }

    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<User> userList() {
        List<User> users = userDao.findAll();
        return users;
    }
}
