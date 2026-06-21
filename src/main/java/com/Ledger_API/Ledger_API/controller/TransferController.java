package com.Ledger_API.Ledger_API.controller;

import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.service.TransferService;
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
    @PostMapping
    public Transaction transfer(@RequestParam Long senderId,
                                @RequestParam Long receiverId,
                                @RequestParam BigDecimal transferAmount
                                          ){
        return transferService.transferMoney(senderId,receiverId,transferAmount);

    }

    @GetMapping("/{id}")
    public List<Transaction> getTransHistory(@PathVariable  Long id){
        return transferService.getTransactionHistory(id);
    }

}
