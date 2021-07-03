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
import javax.security.auth.login.AccountNotFoundException;
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
            // TODO Should path be changed to user/{userId}/transfers??
            transfers = restTemplate.exchange(baseUrl + "users/" + user.getUser().getId() + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("---------------------------------------------------\n " +
                    "Transfers\n" +
                    "ID         From/To         Amount\n" +
                    "---------------------------------------------------");
            String fOrT = "";
            String name = "";

            for(Transfer one : transfers){

                if(user.getUser().getId() + 1000 == one.getAccountFrom()){
                    fOrT = "From: ";
                    name = one.getUserTo();
                }else{
                    fOrT = "To: ";
                    name = one.getUserFrom();
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


    public void sendTransfer() {
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
            if (recipientId == 0) {
                return;
            }

            transfer.setAccountFrom(user.getUser().getId() + 1000);
            transfer.setAccountTo(recipientId + 1000);
            System.out.println("Enter amount: ");

                try {
                    String amountResponse = scanner.nextLine();
                    double transferAmount = Double.parseDouble(amountResponse);
                    if (transferAmount <= 0) {
                        System.out.println("Transfers must be a positive, nonzero amount. Try again...");
                        System.out.println();
                        sendTransfer();
                    }
                if (transferAmount > 0) {
                        BigDecimal transferAmount1 = new BigDecimal(transferAmount);
                        transfer.setTransferAmount(transferAmount1);
                    }
                } catch (Exception e) {
                    System.out.println("IDK dude");
                }

                restTemplate.exchange(baseUrl + "transfers", HttpMethod.POST, transferHttpEntity(transfer), String.class).getBody();
                System.out.println("Transfer successful.");
                System.out.println("-------------------------------------------");

        } catch (Exception e) {
            System.out.println("Invalid user ID");
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

