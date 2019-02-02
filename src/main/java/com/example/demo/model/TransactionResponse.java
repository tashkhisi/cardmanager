package com.example.demo.model;

import java.util.Date;

public class TransactionResponse {
    private Date date;
    private TransferTransaction transaction;

    public TransferTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(TransferTransaction transaction) {
        this.transaction = transaction;
    }




    public TransactionResponse(Date date, TransferTransaction transaction) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
