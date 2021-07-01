package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    List<Transfer> listTransfersByUserId(int userId);

    Transfer getTransferDetails(int transferId);

    Transfer sendTransfer(Long accountFrom, Long accountTo, BigDecimal transferAmount);

}
