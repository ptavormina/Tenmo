package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    boolean create(Transfer transfer);

    List<Transfer> listTransfersByUserId(int userId);

    Transfer getTransferDetails(int transferId);

    List<Transfer> listAllTransfers();

    Transfer transfer(int accountFrom, int accountTo, double transferAmount);

    void delete (int transferId);

    Transfer update (Transfer transfer, int transferId);
}
