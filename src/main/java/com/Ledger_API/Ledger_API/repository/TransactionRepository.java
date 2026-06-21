package com.Ledger_API.Ledger_API.repository;
import com.Ledger_API.Ledger_API.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction , Long> {
    // This is used by Spring to instantiate or know that it's a repository . ( I suppose)

    //Springboot runs this as if it is a query
    List<Transaction> findBySenderIdOrReceiverId(Long senderId , Long receiverId);

}
