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
import java.util.Scanner;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;
    private Account account;

    public TransferService(String baseUrl, AuthenticatedUser user){
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
            // TODO Should path be changed to user/{userId}/transfers??
            transfers = restTemplate.exchange(baseUrl + "transfers/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("---------------------------------------------------\n " +
                    "Transfers\n" +
                    "ID         From/To         Amount\n" +
                    "---------------------------------------------------");
            for(Transfer one : transfers){
                String fOrT = "";
                String name = "";
                if(user.getUser().getId() == one.getAccountFrom()){
                    fOrT = "From:";
                    name = one.getUserFrom();
                }else{
                    fOrT = "To:";
                    name = one.getUserTo();
                }
                System.out.println(one.getTransferId() + "\t\t\t" + fOrT + user.getUser().getUsername() + "\t\t\t$ " + one.getTransferAmount());
            }
        }catch (RestClientResponseException e){
            System.out.println("Could not find list of Transactions");
        }
        return transfers;
    }
    public Transfer transferDetails(){
        Transfer transferDetails = new Transfer();
        try{
            //TODO this path be changed to user/{userId}/transfers/{transferId}
            transferDetails = restTemplate.exchange(baseUrl + "transfers/" + transferDetails.getTransferId(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
            System.out.println("---------------------------------------------------\n" +
                    "Transfer Details\n" +
                    "---------------------------------------------------\n" +
                    "Id:" + transferDetails.getTransferId() + "\n" +
            "From:" + transferDetails.getUserFrom() + "\n" +
            "To:" + transferDetails.getUserTo() + "\n" +
            "Type:" + transferDetails.getTransferType() + "\n" +
            "Status:" + transferDetails.getTransferStatus() + "\n" +
            "Amount:" + transferDetails.getTransferAmount() + "\n");

        }catch (RestClientResponseException e){
            System.out.println("Could not find transaction.");
        }
        return transferDetails;
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

