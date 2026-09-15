package com.Ledger_API.Ledger_API.controller;

import com.Ledger_API.Ledger_API.dto.TransferRequest;
import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.entity.TransactionStatus;
import com.Ledger_API.Ledger_API.entity.TransactionType;
import com.Ledger_API.Ledger_API.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    //Used to perform transfer between a receiver and sender
    //Use of DTO here proving very useful for accepting JSON body message .
    @PostMapping
    public Transaction transfer(@Valid @RequestBody TransferRequest request
                                ){
        return transferService.transferMoney(
                request.getSenderId(),
                request.getReceiverId(),
                request.getTransferAmount()
        );

    }
//    At the moment the code is returning a list but in the event you have a lot of data ,best thing is to use pageable
//    ( pagination ) in order to have better performance .

//   Adding extra optional @parameters -> Implementing filters inorder to be able to find transactions falling under various criteria making it easier to search

    @GetMapping("/{id}")
    public Page<Transaction> getTransHistory(
            @PathVariable  Long id ,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false)

            //Here have to use localdate due to nature of how time is represented in the database
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateTo,
            Pageable pageable){
        return transferService.getTransactionHistory(
                id ,
                type,
                status ,
                minAmount,
                maxAmount,
                dateFrom,
                dateTo,
                pageable );
    }



}
