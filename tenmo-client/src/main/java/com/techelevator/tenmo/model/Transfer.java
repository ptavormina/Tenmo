package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int statusId;
    private int typeId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal transferAmount;
    private String transferType;
    private String transferStatus;
    private String userFrom;
    private String userTo;

    public Transfer(int transferId, int statusId, int typeId, int accountFrom, int accountTo, BigDecimal transferAmount, String transferType, String transferStatus, String userFrom, String userTo) {

    public Transfer(int transferId, int statusId, int typeId, int accountFrom, int accountTo, BigDecimal transferAmount) {
        this.transferId = transferId;
        this.statusId = statusId;
        this.typeId = typeId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    public Transfer(){
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
