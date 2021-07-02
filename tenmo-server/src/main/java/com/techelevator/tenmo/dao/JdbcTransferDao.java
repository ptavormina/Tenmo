package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbctemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Transfer> listTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfers.*. f.username AS userFrom, t.username AS userTo FROM transfers " +
                "JOIN accounts a ON transfers.account_from = a.account_id " +
                "JOIN accounts b ON transfers.account_to = b.account_id " +
                "JOIN users f ON a.user_id = f.user_id " +
                "JOIN users t ON b.user_id = t.user_id " +
                "WHERE a.userId = ? AND b.userId = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }
    @Override
    public Transfer getTransferDetails(int transferId) {
        Transfer transfer = null;
        String sql = "Select transfers.*, f.username AS userFrom, t.username AS userTo, ts.transfer_status_desc, tt.transfer_type_desc FROM transfers " +
                "JOIN accounts a ON transfers.account_from = a.account_id " +
                "JOIN accounts b ON transfers.account_to = b.account_id " +
                "JOIN users f ON a.user_id = f.user_id " +
                "JOIN users t ON b.user_id = t.user_id " +
                "JOIN transfer_statuses ts ON transfers.transfer_status_id = ts.transfer_status_id " +
                "JOIN transfer_types tt ON transfers.transfer_type_id = tt.transfer_type_id " +
                "WHERE transfers.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if(results.next()){
            transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }
    @Override
    public Transfer sendTransfer(int accountFrom, int accountTo, BigDecimal transferAmount) throws AccountNotFoundException {
        Transfer transfer = null;
        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(2, 2, ?, ?, ?) RETURNING transfer_id;";
        int newTransferId = jdbcTemplate.queryForObject(sql, int.class, accountFrom, accountTo, transferAmount);
        accountDao.addFunds(transferAmount, accountTo);
        accountDao.subtractFunds(transferAmount, accountFrom);

        return getTransferDetails(newTransferId);
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer t = new Transfer();
        t.setTransferId(rowSet.getInt("transfer_id"));
        t.setTypeId(rowSet.getInt("transfer_type_id"));
        t.setStatusId(rowSet.getInt("transfer_status_id"));
        t.setAccountFrom(rowSet.getInt("account_from"));
        t.setAccountTo(rowSet.getInt("account_to"));
        t.setTransferAmount(rowSet.getBigDecimal("amount"));

        t.setTransferStatus(rowSet.getString("transfer_status_desc"));
        t.setTransferType(rowSet.getString("transfer_type_desc"));
        t.setUserFrom(rowSet.getString("userFrom"));
        t.setUserTo(rowSet.getString("userTo"));
        return t;
    }
}
