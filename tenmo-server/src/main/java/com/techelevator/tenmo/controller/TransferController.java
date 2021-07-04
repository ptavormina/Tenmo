package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Path;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {
    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }


    @RequestMapping(path = "users/{userId}/transfers", method = RequestMethod.GET)
    public List<Transfer> transfers(@PathVariable int userId){
        return transferDao.listTransfersByUserId(userId);
    }

    @RequestMapping(path = "transfers/{transferId}", method = RequestMethod.GET)
    public Transfer transfer(@PathVariable int transferId){
        return transferDao.getTransferDetails(transferId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public String sendTransfer(@RequestBody @Valid Transfer transfer) throws AccountNotFoundException {
      return transferDao.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());
    }
}
