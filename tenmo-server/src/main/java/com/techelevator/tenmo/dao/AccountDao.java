package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountDao {

    List<Account> list();

    Account find(int accountId) throws AccountNotFoundException;

    Account updateBalance(Account account, double balanceAmount) throws AccountNotFoundException;

    double getBalanceByUserId(int userId) throws AccountNotFoundException;

    void delete(int accountId);



}
