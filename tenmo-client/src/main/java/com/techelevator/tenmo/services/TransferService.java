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
import java.util.zip.DataFormatException;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;
    private Account account;

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

        if (transferId != 0) {

            try {
                details = restTemplate.exchange(baseUrl + "transfers/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
            } catch (Exception e) {
                System.out.println("Invalid transfer ID, try again");
                listTransfers();
            }

            if (details != null) {
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
        }
    }

    public void sendTransfer() {
        Transfer transfer = new Transfer();
        listOtherUsers();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the User you're sending to, or enter 0 to cancel:");
        String response = scanner.nextLine();
        int recipientId = Integer.parseInt(response);

        if (recipientId != 0) {
            transfer.setAccountFrom(user.getUser().getId() + 1000);
            transfer.setAccountTo(recipientId + 1000);

            System.out.println("Enter amount:");
            try {
                String amountResponse = scanner.nextLine();
                double transferAmount = Double.parseDouble(amountResponse);

                if (transferAmount <= 0) {
                    System.out.println("Transfers must be a positive, nonzero amount. Try again...");
                    System.out.println();
                    sendTransfer();
                }
                if (transferAmount > getCurrentBalanceAsDouble()) {
                    System.out.println("Unlike real banks, you're not allowed to spend more than you have here.");
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
                restTemplate.exchange(baseUrl + "transfers", HttpMethod.POST, transferHttpEntity(transfer), String.class).getBody();
                System.out.println("Transfer successful.");
                System.out.println("-------------------------------------------");
            } catch (Exception e) {
                System.out.println("Invalid user ID, please try again.");
                sendTransfer();
            }
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
        Transfer transferRequest = new Transfer();
        listOtherUsers();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the User you're requesting, or enter 0 to cancel:");
        String idResponse = scanner.nextLine();
        int recipientId = Integer.parseInt(idResponse);

        if (recipientId != 0) {

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
                    giveMeMoney();
                }
                if (requestAmount > 0) {
                    BigDecimal requestAmount1 = new BigDecimal(requestAmount);
                    transferRequest.setTransferAmount(requestAmount1);
                }
            } catch (Exception e) {
                System.out.println("Invalid input, please try again.");
                giveMeMoney();
            }
            try {
                restTemplate.exchange(baseUrl + "transfers/requests", HttpMethod.POST, transferHttpEntity(transferRequest), String.class).getBody();
                System.out.println("Request sent successfully.");
                System.out.println("-------------------------------------------");
            } catch (Exception e) {
                System.out.println("Invalid user ID, please try again.");
                giveMeMoney();
            }
        }
    }

    public List<Transfer> viewRequests() {
        Transfer[] requests = null;
        List<Transfer> pendingRequests = new ArrayList<>();
        try {
            requests = restTemplate.exchange(baseUrl + "users/" + user.getUser().getId() + "/transfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
            boolean userHasPendingRequests = false;
            for (Transfer request : requests) {
                if (request.getStatusId() == 1 && request.getAccountTo() == user.getUser().getId() + 1000) {
                    pendingRequests.add(request);
                    userHasPendingRequests = true;
                }
            }
            //if the user has pending requests, display this menu
            if (userHasPendingRequests) {
                System.out.println("---------------------------------------------------\n " +
                        "Pending Transfers\n" +
                        " ID              To                 Amount\n" +
                        "---------------------------------------------------");
                for (Transfer request : pendingRequests) {
                    System.out.println(request.getTransferId() + "\t\t\t" + request.getUserFrom() + "\t\t\t" + request.getTransferAmount());
                }
                processRequests(pendingRequests);
            } else {
                System.out.println("No pending requests.");
            }

        } catch (Exception e) {
            System.out.println("Unable to find any pending requests.");
        }
        return pendingRequests;
    }

    public void processRequests(List<Transfer> pendingRequests) {
        System.out.println("---------");
        System.out.println("Please enter request ID to approve or reject (0 to cancel)");
        int requestId = 0;
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        try {
            requestId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input type.");
            viewRequests();
        }

        if (requestId != 0) {

            Transfer chosenRequest = null;
            for (Transfer pendingRequest : pendingRequests) {
                if (requestId == pendingRequest.getTransferId()) {
                    chosenRequest = pendingRequest;
                }
            }

            if (chosenRequest == null) {
                System.out.println("Could not find transfer with that ID. Please try again.");
                viewRequests();
            }

            System.out.println("----------------------------------");
            System.out.println("1: Approve");
            System.out.println("2: Reject");
            System.out.println("0: Exit Menu");
            System.out.println("-----------");
            System.out.println("Please choose an option:");
            String choice = scanner.nextLine();
            int menuChoice = -1;

            try {
                menuChoice = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input type.");
                viewRequests();
            }

            if (menuChoice == 0) {
                System.out.println("Okay, but don't forget to pay your friend later!");

            } else if (menuChoice == 1) {
                approveRequest(chosenRequest);
            } else if (menuChoice == 2) {
                declineRequest(chosenRequest);
            } else {
                System.out.println("Invalid choice.");
                processRequests(pendingRequests);
            }
        }
    }

    public void approveRequest(Transfer request) {
        double currentBalance = getCurrentBalanceAsDouble();
        if (Double.parseDouble(String.valueOf(request.getTransferAmount())) > currentBalance) {
            System.out.println("Insufficient funds to complete request.");
            viewRequests();
        } else {
            restTemplate.exchange(baseUrl + "transfers/requests/2", HttpMethod.PUT, transferHttpEntity(request), String.class).getBody();
            System.out.println("Request approved!");
        }
    }

    public void declineRequest(Transfer request) {
        restTemplate.exchange(baseUrl + "transfers/requests/3", HttpMethod.PUT, transferHttpEntity(request), String.class).getBody();
        System.out.println("Request declined.");
        viewRequests();
    }

    public double getCurrentBalanceAsDouble() {
        return Double.parseDouble(String.valueOf(restTemplate.exchange(baseUrl + "balance/" + user.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody()));
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

