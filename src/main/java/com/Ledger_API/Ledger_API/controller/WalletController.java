package com.Ledger_API.Ledger_API.controller;

import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

//    So this mapping is responsible for creating wallet , syntax used is from the
    @PostMapping
    public Wallet createWallet(@RequestParam String username,
                               @RequestParam BigDecimal balance) {
        return walletService.createWallet(username, balance);
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

}