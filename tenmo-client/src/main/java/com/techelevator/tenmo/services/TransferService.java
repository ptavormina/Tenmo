package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.ls.LSOutput;

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

    public TransferService(String baseUrl, AuthenticatedUser user) {
        this.baseUrl = baseUrl;
        this.user = user;
    }

    public Transfer[] listTransfers() {
        Transfer[] transfers = null;
        try {
            transfers = restTemplate.exchange(baseUrl + "users/" + user.getUser().getId() + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            System.out.println("---------------------------------------------------\n " +
                    "Transfers\n" +
                    "  ID         From/To            Amount\n" +
                    "---------------------------------------------------");
            String fOrT = "";
            String name = "";

            for (Transfer one : transfers) {
                if (user.getUser().getId() + 1000 != one.getAccountFrom()) {
                    fOrT = "From: ";
                    name = one.getUserFrom();
                } else {
                    fOrT = "To: ";
                    name = one.getUserTo();
                }
                System.out.println(one.getTransferId() + "\t\t" + fOrT + name + "\t\t\t$ " + one.getTransferAmount());
            }
        } catch (RestClientResponseException e) {
            System.out.println("Could not find list of Transactions");
        }
        listTransferDetails();
        return transfers;
    }

    public void listTransferDetails() {
        Transfer details = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter transfer ID to view details (0 to cancel)");
        String input = scanner.nextLine();
        int transferId = Integer.parseInt(input);

        if (transferId == 0) {
            return;
        }
        try {
            details = restTemplate.exchange(baseUrl + "transfers/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
        } catch (Exception e) {
            System.out.println("Invalid transfer ID you big dumb idiot!!!!!");
        }

        //before printing the transfer details, gonna decode the type id and status id:
        String transferType = "";
        transferType = details.getTypeId() == 1 ? "Request" : "Send";
        String transferStatus = "";
        if (details.getStatusId() == 1) {
            transferStatus = "Pending";
        }
        if (details.getStatusId() == 2) {
            transferStatus = "Accepted";
        }
        if (details.getStatusId() == 3) {
            transferStatus = "Rejected";
        }

        System.out.println("---------------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("---------------------------------------------------");
        System.out.println("Id:      " + details.getTransferId());
        System.out.println("From:    " + details.getUserFrom());
        System.out.println("To:      " + details.getUserTo());
        System.out.println("Type:    " + transferType);
        System.out.println("Status:  " + transferStatus);
        System.out.println("Amount:  $" + details.getTransferAmount());
    }

    public void sendTransfer() {
        Transfer transfer = new Transfer();
        listOtherUsers();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the User you're sending to, or enter 0 to cancel:");
        String response = scanner.nextLine();
        int recipientId = Integer.parseInt(response);

        if (recipientId == 0) {
            return;
        }

        try {
            transfer.setAccountFrom(user.getUser().getId() + 1000);
            transfer.setAccountTo(recipientId + 1000);
        } catch (Exception e) {
            System.out.println("Invalid user");
            return;
        }

        System.out.println("Enter amount:");
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
                    System.out.println("Invalid input, please try again.");
                    sendTransfer();
                }
                try {
                    restTemplate.exchange(baseUrl + "requests", HttpMethod.POST, transferHttpEntity(transfer), String.class).getBody();
                    System.out.println("Transfer successful.");
                    System.out.println("-------------------------------------------");
                } catch (Exception e) {
                    System.out.println("Invalid user ID, please try again.");
                    sendTransfer();
                }
    }

    public User[] listOtherUsers() {
        User[] users = null;
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
        } catch (Exception e){
            System.out.println("Unable to retrieve list of users. Sorry!");
        }
        return users;
    }

    public void giveMeMoney() {
        Transfer transferRequest = null;
        listOtherUsers();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the User you're requesting, or enter 0 to cancel:");
        String idResponse = scanner.nextLine();
        int recipientId = Integer.parseInt(idResponse);

        if (recipientId == 0) {
             return;
        }

        try {
            transferRequest.setAccountFrom(user.getUser().getId() + 1000);
            transferRequest.setAccountTo(recipientId + 1000);
        } catch (Exception e) {
            System.out.println("Invalid user");
            return;
        }

        System.out.println("Enter request amount:");
        try {
            String response = scanner.nextLine();
            double requestAmount = Double.parseDouble(response);
            if (requestAmount <= 0) {
                System.out.println("Requests must be greater than 0");
                System.out.println();
                sendTransfer();
            }
            if (requestAmount > 0) {
                BigDecimal requestAmount1 = new BigDecimal(requestAmount);
                transferRequest.setTransferAmount(requestAmount1);
            }
        } catch (Exception e) {
            System.out.println("Invalid input, please try again.");
            sendTransfer();
        }


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

