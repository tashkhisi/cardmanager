package com.example.demo.repository.transaction;

import com.example.demo.model.TransferTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface TransactionRepository extends CrudRepository<TransferTransaction, Long> {

}
