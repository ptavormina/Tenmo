package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;
    private Account account;

    public TransferService(String baseUrl, AuthenticatedUser user, Account account){
        this.baseUrl = baseUrl;
        this.user = user;
        this.account = account;
    }

    public Transfer sendTransfer(){
        return null;
    }


    public Transfer[] listTransfers(){
        Transfer[] transfers = null;
        try{
            transfers = restTemplate.exchange(baseUrl + "transfers/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        }catch (RestClientResponseException e){
            System.out.println("Could not find list of Transactions");
        }
        return transfers;
    }

    public User[] listAllUsers(){
        User[] users = null;
        try{
            users = restTemplate.exchange(baseUrl + "users" , HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        }catch (RestClientResponseException e){
            System.out.println("Your request could not be completed.");
        }
        return users;
    }

    private HttpEntity<Transfer> transferHttpEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }


}

