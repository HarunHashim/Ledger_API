package com.Ledger_API.Ledger_API.controller;

import com.Ledger_API.Ledger_API.dto.TransferRequest;
import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/{id}")
    public List<Transaction> getTransHistory(@PathVariable  Long id){
        return transferService.getTransactionHistory(id);
    }

}
