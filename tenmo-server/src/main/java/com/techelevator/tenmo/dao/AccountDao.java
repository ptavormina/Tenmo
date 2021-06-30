package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> list();

    Account find(int accountId) throws AccountNotFoundException;

    Account updateBalance(Account account, BigDecimal balanceAmount) throws AccountNotFoundException;

    BigDecimal getBalanceByUserId(int userId) throws AccountNotFoundException;

    void delete(int accountId);



}
