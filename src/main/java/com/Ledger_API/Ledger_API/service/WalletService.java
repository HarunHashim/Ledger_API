package com.Ledger_API.Ledger_API.service;

import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.entity.TransactionStatus;
import com.Ledger_API.Ledger_API.entity.TransactionType;
import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.exceptions.InvalidDepositException;
import com.Ledger_API.Ledger_API.exceptions.InvalidWithdrawalException;
import com.Ledger_API.Ledger_API.exceptions.WalletNotFoundException;
import com.Ledger_API.Ledger_API.repository.TransactionRepository;
import com.Ledger_API.Ledger_API.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;



@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository , TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(String username, BigDecimal balance) {
        Wallet wallet = new Wallet(username, balance);

        return walletRepository.save(wallet);
    }

    public Wallet getWalletById(Long id){
        return walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException("Wallet not found")) ;
    }

    public BigDecimal getWalletBalance(Long id){
        Wallet wallet = walletRepository.findById(id).orElse(null);
        if(wallet == null){
            throw new WalletNotFoundException("Wallet not found");
        }

        return wallet.getBalance();
    }
    @Transactional
    public Wallet deposit(Long id, BigDecimal amount){

        //check if wallet exists
        Wallet wallet = walletRepository.findById(id).orElse(null);

        if(wallet == null){
            throw new WalletNotFoundException("Wallet not found");
        }

        //check if amount is valid
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDepositException("Deposit amount must be greater than 0.00");
        }

        wallet.setBalance(wallet.getBalance().add(amount));

        //This function should probably return a transaction but how would I record sender and receiver id ???

        Transaction trans = new Transaction( null,  id,  amount , TransactionStatus.SUCCESS , TransactionType.DEPOSIT);

        //Save transaction but return wallet account details for proof of funds transfer
        transactionRepository.save(trans);

        return walletRepository.save(wallet);
    }
    @Transactional
    public Wallet withdraw(Long id ,BigDecimal amount){
        //check if wallet exists
        Wallet wallet = walletRepository.findById(id).orElse(null);

        if(wallet == null){
            throw new WalletNotFoundException("Wallet not found");
        }

        //check if amount is valid
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWithdrawalException("Withdrawal amount must be greater than 0.00");
        }

        //check if wallet balance is greater than balance
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InvalidWithdrawalException("Sorry you have insufficient funds for that withdrawal amount");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        Transaction trans = new Transaction( id,  null,  amount , TransactionStatus.SUCCESS , TransactionType.WITHDRAWAL);

        transactionRepository.save(trans);
        return walletRepository.save(wallet);
    }
}
