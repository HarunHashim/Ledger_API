package com.Ledger_API.Ledger_API.service;

import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(String username, BigDecimal balance) {
        Wallet wallet = new Wallet(username, balance);

        return walletRepository.save(wallet);
    }

    public Wallet getWalletById(Long id){
        return walletRepository.findById(id).orElse(null) ;
    }

    public BigDecimal getWalletBalance(Long id){
        return walletRepository.findById(id).orElse(null).getBalance() ;
    }
}
