package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.view.ConsoleService;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public BigDecimal getAccountBalance(int userId) {
        Account account = null;
        try {
           account = restTemplate.getForObject(baseUrl + "accounts/" + userId + "/balance", Account.class);
           return account.getBalance();
        } catch (RestClientResponseException e) {
            System.out.println("");
        }
        return null;
    }
}
