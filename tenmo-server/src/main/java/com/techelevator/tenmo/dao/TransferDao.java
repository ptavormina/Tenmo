package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    boolean create(Transfer transfer);

    List<Transfer> listTransfersByUserId(int userId);

    Transfer getTransferDetails(int transferId);

    Transfer transfer(int accountFrom, int accountTo, double transferAmount);

}
