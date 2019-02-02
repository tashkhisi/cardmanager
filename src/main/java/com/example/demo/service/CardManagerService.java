package com.example.demo.service;

import com.example.demo.CardManager;
import com.example.demo.Utils;
import com.example.demo.model.TransactionResponse;
import com.example.demo.model.TransferTransaction;
import com.example.demo.repository.transaction.TransactionRepository;
import com.example.demo.transferHelper.CardManagerNonBlockingTransferHelper;
import com.example.demo.repository.CardManagerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * the main service that is called by controller
 */
@Service()
public class CardManagerService {

    @Autowired
    private CardManagerRepo cardManagerRepo;

    @Autowired
    CardManagerNonBlockingTransferHelper cardManagerNonBlockingTransferHelper;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CardManager cardManager;

    private  Date currentDate;

    AtomicBoolean blockedTime = new AtomicBoolean();

    public List<TransferTransaction> getTransactions(Long totalAmount){
        if(!blockedTime.get()){
            List<TransferTransaction> transactions = cardManager.getTransferTransactions(totalAmount);
            cardManagerNonBlockingTransferHelper.doTransfer(transactions, new ServiceTransactionListener());
            return transactions;
        }
        else{
            throw new RuntimeException();
        }
    }

    class ServiceTransactionListener implements CardManagerNonBlockingTransferHelper.TransferTransactionListener{

        @Override
        public void onFailed(TransactionResponse response) {
            if(!blockedTime.get()) {
                cardManager.addAmountToCard(response.getTransaction().getCard().getCardId(),  response.getTransaction().getAmount());
            }
            //should update status of transaction in database
        }

        @Override
        public void onSuccess(TransactionResponse response) {

        }

        public void onInterrupt(TransactionResponse response){
            // update status of transaction to termination at
            return;
        }


    }

    @Scheduled(cron = "0 30 23 * * *")
    public void scheduleTaskForBlockingComingRequest() {
        blockedTime.set(false);
        cardManagerNonBlockingTransferHelper.shutDown();
    }

    @Scheduled(cron = "0 50 23 * * *")
    public void scheduleTaskForKillAllThreads() {

        cardManagerNonBlockingTransferHelper.shutDownNow();
    }



    @Scheduled(cron = "0 05 0 * * *")
    public void scheduleForResetConstraints() {
        cardManagerNonBlockingTransferHelper.awaitTermination();
        cardManager.initialize(cardManagerRepo.getAllCards());
        blockedTime.set(true);


    }



}
