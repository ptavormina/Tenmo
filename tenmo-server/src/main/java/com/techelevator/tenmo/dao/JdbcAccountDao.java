package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Account> list() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;

    }

    @Override
    public Account find(int accountId) throws AccountNotFoundException {
        String sql = "SELECT * FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        if (results.next()) {
            return mapRowToAccount(results);
        } throw new AccountNotFoundException("Account " + accountId + " was not found.");

    }

    @Override
    public Account updateBalance(Account accountToUpdate, BigDecimal balanceAmount) throws AccountNotFoundException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, accountToUpdate.getBalance(), accountToUpdate.getAccountId());
        return accountToUpdate;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) throws AccountNotFoundException {
        String sql = "SELECT balance FROM accounts " +
                     "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        if (results.next()) {
            Account account = mapRowToAccount(results);
            return account.getBalance();
        } throw new AccountNotFoundException("Unable to find account associated with User ID " + userId);
    }

    @Override
    public void delete(int accountId) {
        String sql = "DELETE FROM accounts WHERE account_id = ?;";
        jdbcTemplate.update(sql, accountId);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
