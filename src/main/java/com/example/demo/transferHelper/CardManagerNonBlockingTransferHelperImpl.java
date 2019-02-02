package com.example.demo.transferHelper;

import com.example.demo.model.TransactionResponse;
import com.example.demo.model.TransferTransaction;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CardManagerNonBlockingTransferHelperImpl implements CardManagerNonBlockingTransferHelper {


    ExecutorService executor = Executors.newFixedThreadPool(100);
    @Override
    public  void doTransfer(List<TransferTransaction> transferTransactions, TransferTransactionListener listener) {


        for (TransferTransaction transaction : transferTransactions) {
            Runnable worker = new WorkerThread(transaction,  listener);
            executor.execute(worker);
        }
    }

    @Override
    public void shutDown() {
        executor.shutdown();
    }

    public void shutDownNow(){
        executor.shutdownNow();
    }

    @Override
    public void awaitTermination() {

        try {
            executor.awaitTermination(1000, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();

        }
    }


    public class WorkerThread implements Runnable {

        private TransferTransaction transaction;
        TransferTransactionListener listener;

        public WorkerThread(TransferTransaction transaction, TransferTransactionListener listener){
            this.transaction = transaction;
            this.listener = listener;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+" Start. Command = "+transaction);
            processCommand();
            System.out.println(Thread.currentThread().getName()+" End.");
        }

        private void processCommand() {
            try {
                Thread.sleep(5000);
                Random random = new Random();
                if(random.nextInt(50) % 2 == 0)
                    listener.onSuccess(new TransactionResponse(new Date(), transaction));
                else
                    listener.onFailed((new TransactionResponse(new Date(), transaction)));
            } catch (InterruptedException e) {
                e.printStackTrace();
                listener.onInterrupt((new TransactionResponse(new Date(), transaction)));
            }
        }

        @Override
        public String toString(){
            return this.transaction.toString();
        }
    }
}
