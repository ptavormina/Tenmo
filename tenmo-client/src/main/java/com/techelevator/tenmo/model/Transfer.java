package com.techelevator.tenmo.model;

public class Transfer {

    private Long transferId;
    private int statusId;
    private int typeId;
    private Long accountFrom;
    private Long accountTo;

    public Transfer(Long transferId, int statusId, int typeId, Long accountFrom, Long accountTo) {
        this.transferId = transferId;
        this.statusId = statusId;
        this.typeId = typeId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }
    public Transfer(){
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
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

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }
}
