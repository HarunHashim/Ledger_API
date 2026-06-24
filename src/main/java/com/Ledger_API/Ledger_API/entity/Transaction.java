package com.Ledger_API.Ledger_API.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table ( name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    private Long senderId;

    private Long receiverId;

    @Positive
    private BigDecimal transferAmount;

    private LocalDateTime transactionTime;

    //Tells JPA to store a string
    @Enumerated(EnumType.STRING)
    private TransactionStatus Transaction_status;

    @Enumerated(EnumType.STRING)
    private  TransactionType transactionType;



    public TransactionStatus getTransaction_status() {
        return Transaction_status;
    }

    public void setTransaction_status(TransactionStatus transaction_status) {
        Transaction_status = transaction_status;
    }

    public Transaction(Long senderId, Long receiverId, BigDecimal transferAmount,  TransactionStatus transaction_status , TransactionType transactionType) {

        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transferAmount = transferAmount;
        this.transactionTime = LocalDateTime.now();
        this.Transaction_status = transaction_status;
        this.transactionType = transactionType;
    }

    public Transaction() {
    }

    //    Getters and setters
    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
