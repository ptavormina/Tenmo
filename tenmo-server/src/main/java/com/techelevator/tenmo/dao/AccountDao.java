package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountDao {

    List<Account> list();

    Account find(int accountId) throws AccountNotFoundException;

    Account update(Account account, int accountId);

    Account updateBalance(int accountId, double transferAmount);

    double getBalanceByUserId(int userId);

    void delete(int accountId);



}
