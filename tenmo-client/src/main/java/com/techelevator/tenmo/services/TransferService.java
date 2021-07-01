package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public Transfer listAllTransfers(Transfer transfer){
        HttpEntity<Transfer> entity = creat
    }
}
