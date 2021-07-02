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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public TransferService(String baseUrl, AuthenticatedUser user){
        this.baseUrl = baseUrl;
        this.user = user;
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


    public void sendTransfer(){
        Transfer transfer = new Transfer();
        User[] users = null;
        Scanner scanner = new Scanner(System.in);

        try {
            users = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

            System.out.println("-------------------------------------------");
            System.out.println("Users:");
            System.out.println("Id\t\tName");
            System.out.println("-------------------------------------------");

            for (User recipient : users) {
                if (!recipient.getId().equals(user.getUser().getId())) {
                    System.out.println(recipient.getId() + "\t" + recipient.getUsername());
                }
            }

            System.out.println("Enter the ID of the User you're sending to, or enter 0 to cancel:");
            String response = scanner.nextLine();
            int recipientId = Integer.parseInt(response);
            if (recipientId != 0) {
                transfer.setAccountFrom(user.getUser().getId());
                transfer.setAccountTo(recipientId);
                System.out.println("Enter amount: ");
                try {
                    String amountResponse = scanner.nextLine();
                    double transferAmount = Double.parseDouble(amountResponse);
                    if (transferAmount > 0) {
                        BigDecimal transferAmount1 = new BigDecimal(transferAmount);
                        transfer.setTransferAmount(transferAmount1);
                    }
                } catch (Exception e) {
                    System.out.println("IDK dude");
                }
                restTemplate.exchange(baseUrl + "transfers", HttpMethod.POST, transferHttpEntity(transfer), String.class).getBody();
            }
        } catch (Exception e) {
            System.out.println("No good");
        }
    }

//    public User[] listAllUsers(){
//        User[] users = null;
//        try{
//            users = restTemplate.getForObject(baseUrl + "users", User[].class);
//            for (User recipient : users) {
//                if (recipient.getId() != user.getUser().getId()) {
//                    System.out.println(recipient.getId() + "\t" + recipient.getUsername());
//                }
//            }
//        }catch (RestClientResponseException e){
//            System.out.println("Your request could not be completed.");
//        }
//        return users;
//    }




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

