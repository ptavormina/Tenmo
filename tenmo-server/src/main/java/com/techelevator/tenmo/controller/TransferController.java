package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import javax.validation.Path;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class TransferController {
    private TransferDao transferDao;


    @RequestMapping(path = "transfers/{userId}", method = RequestMethod.GET)
    public List<Transfer> transfers(@PathVariable int userId){
        return transferDao.listTransfersByUserId(userId);
    }

    @RequestMapping(path = "transfers/{transferId}", method = RequestMethod.GET)
    public Transfer transfer(@PathVariable int transferId){
        return transferDao.getTransferDetails(transferId);
    }

    //TODO figure out the proper formatting and if we need balance etc.
    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public Transfer sendTransfer(@RequestBody @Valid Transfer transfer){
        return transferDao.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount())
    }
}
