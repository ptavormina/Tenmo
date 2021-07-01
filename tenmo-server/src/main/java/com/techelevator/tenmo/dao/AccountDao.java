package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    boolean create(Account account);

    List<Account> list();

    Account find(int accountId);

    Account update(Account account, int accountId);

    Account updateBalance(int accountId, double transferAmount);

    double getBalanceByUserId(int userId);

    void delete(int accountId);



}
