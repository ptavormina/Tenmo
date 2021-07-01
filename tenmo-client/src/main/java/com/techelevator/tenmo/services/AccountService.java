package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.view.ConsoleService;
import io.cucumber.java.bs.A;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public AccountService(String baseUrl, AuthenticatedUser user) {
        this.baseUrl = baseUrl;
        this.user = user;
    }

    public BigDecimal getAccountBalance() {
        BigDecimal accountBalance = new BigDecimal(0);
        try {
            accountBalance = restTemplate.exchange(baseUrl + "balance/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
            System.out.println("Your current balance is $" + accountBalance);
        } catch (RestClientResponseException e) {
            System.out.println("OOOOPS SORRY :(");
        } return accountBalance;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
