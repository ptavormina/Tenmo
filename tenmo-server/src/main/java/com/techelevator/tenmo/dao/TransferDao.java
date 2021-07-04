package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    List<Transfer> listTransfersByUserId(int userId);

    Transfer getTransferDetails(int transferId);

    String sendTransfer(int accountFrom, int accountTo, BigDecimal transferAmount) throws AccountNotFoundException;

    String requestTransfer(int accountFrom, int accountTo, BigDecimal transferAmount) throws AccountNotFoundException;

}
