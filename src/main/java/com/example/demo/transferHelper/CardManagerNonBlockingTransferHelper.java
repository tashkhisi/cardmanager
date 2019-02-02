package com.example.demo.transferHelper;

import com.example.demo.model.TransactionResponse;
import com.example.demo.model.TransferTransaction;

import java.util.List;

public interface CardManagerNonBlockingTransferHelper {
    void doTransfer(List<TransferTransaction> transferTransactions, TransferTransactionListener listener);

    void shutDown();

    void awaitTermination();

    void shutDownNow();


    public interface TransferTransactionListener{
        void onFailed(TransactionResponse response);
        void onSuccess(TransactionResponse response);
        void onInterrupt(TransactionResponse response);
    }

}
