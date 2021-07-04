package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
//import org.junit.Assert;


import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountsDaoTests extends Tests{
/*
BigDecimal accountTwo = new BigDecimal(5000);
BigDecimal accountThree = new BigDecimal(1500);
BigDecimal accountFour = new BigDecimal(500);
    private static final Account ACCOUNT_1 = new Account(2000, 1000, new BigDecimal(1000));
    private static final Account ACCOUNT_2 = new Account(2001, 1001, new BigDecimal(5000));
    private static final Account ACCOUNT_3 = new Account(2002, 1002, new BigDecimal(500));

    private JdbcAccountDao sut;
    private Account testAccount;

    @Before
    public void setup(){
        sut = new JdbcAccountDao(dataSource);
    }

    public void listAccounts_list_all_accounts(){
        List<Account> accounts = sut.list();
        Assert.assertEquals(3, accounts.size());
    }
    public void findAccount_returns_correct_account() throws AccountNotFoundException {
        Account accountOne = sut.findByUserId(2000);
        assertAccountsMatch(ACCOUNT_1, accountOne);

        Account accountTwo = sut.findByUserId(2001);
        assertAccountsMatch(ACCOUNT_2, accountTwo);

        Account accountThree = sut.findByUserId(2002);
        assertAccountsMatch(ACCOUNT_3, accountThree);
    }
    public void findAccount_returns_null_when_account_does_not_exist() throws AccountNotFoundException {
        Account account = sut.findByUserId(8000);
        Assert.assertNull(account);
    }

    public void getBalanceByUserId_returns_correct_balance() throws AccountNotFoundException {
        BigDecimal accountOne = sut.getBalanceByUserId(2000);
        Assert.assertEquals(1000, accountOne);

        BigDecimal accountTwo = sut.getBalanceByUserId(2001);
        Assert.assertEquals(5000, accountOne);

        BigDecimal accountThree = sut.getBalanceByUserId(2002);
        Assert.assertEquals(500, accountThree);

    }

    public void subtractFunds_subtracts_funds() throws AccountNotFoundException {
        BigDecimal accountOne = sut.subtractFunds(new BigDecimal(20), 2000);
        Assert.assertEquals(980, accountOne);

        BigDecimal accountTwo = sut.subtractFunds(new BigDecimal(100), 2001);
        Assert.assertEquals(4900, accountOne);

        BigDecimal accountThree = sut.subtractFunds(new BigDecimal(200), 2002);
        Assert.assertEquals(300, accountOne);


    }

    public void addFunds_adds_funds() throws AccountNotFoundException {
        BigDecimal accountOne = sut.addFunds(new BigDecimal(50), 2000);
        Assert.assertEquals(1050, accountOne);

        BigDecimal accountTwo = sut.addFunds(new BigDecimal(500), 2001);
        Assert.assertEquals(5500, accountTwo);

        BigDecimal accountThree = sut.addFunds(new BigDecimal(100), 2002);
        Assert.assertEquals(600, accountThree);
    }

    private void assertAccountsMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
        Asset.assertEquals(expected.getUserId(), actual.getUserId());
    }

*/


}
