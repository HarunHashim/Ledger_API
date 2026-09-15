package com.Ledger_API.Ledger_API.repository;
import com.Ledger_API.Ledger_API.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends
        JpaRepository<Transaction , Long>,
        JpaSpecificationExecutor < Transaction> {
    // This is used by Spring to instantiate or know that it's a repository . ( I suppose)

    //Springboot runs this as if it is a query
    //Also here is where instead of asking for a list we ask for a page . Hence first step of pagination .
    Page<Transaction> findBySenderIdOrReceiverId(Long senderId , Long receiverId , Pageable pageable);

}
