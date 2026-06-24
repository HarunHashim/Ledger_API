package com.Ledger_API.Ledger_API.controller;

import com.Ledger_API.Ledger_API.dto.DepositRequest;
import com.Ledger_API.Ledger_API.dto.WalletRequest;
import com.Ledger_API.Ledger_API.dto.WithdrawalRequest;
import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

//    So this mapping is responsible for creating wallet , syntax used is from the
    @PostMapping
    public Wallet createWallet(@Valid @RequestBody WalletRequest walletInfo) {
        return walletService.createWallet(walletInfo.getUserName(), walletInfo.getBalance());
    }
//    //This could work but query becomes long
//    @GetMapping
//    public Wallet getWallet(@RequestParam Long id){
//        return walletService.getWalletById(id);
//    }

    @GetMapping("/{id}")
    public Wallet getWallet(@PathVariable Long id){
        return walletService.getWalletById(id);
    }

    @PostMapping("/{id}/deposit")
    public Wallet deposit(@PathVariable Long id,  @Valid @RequestBody DepositRequest depositInfo){
        return walletService.deposit(id, depositInfo.getAmount());
    }

    @PostMapping("/{id}/withdraw")
    public Wallet withdrawal(@PathVariable Long id , @Valid @RequestBody WithdrawalRequest withdrawalInfo){
        return walletService.withdraw(id ,withdrawalInfo.getAmount());
    }


}