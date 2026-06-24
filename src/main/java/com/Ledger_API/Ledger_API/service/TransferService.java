package com.Ledger_API.Ledger_API.service;

import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.entity.TransactionStatus;
import com.Ledger_API.Ledger_API.entity.TransactionType;
import com.Ledger_API.Ledger_API.exceptions.InvalidTransferException;
import com.Ledger_API.Ledger_API.exceptions.WalletNotFoundException;
import com.Ledger_API.Ledger_API.repository.TransactionRepository;
import com.Ledger_API.Ledger_API.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.service.WalletService;

import java.math.BigDecimal;
import java.util.List;

@Service
//@Transactional
public class TransferService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransactionRepository transactionRepository , WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }



    //Responsible for creating a transfer instance
    @Transactional
    public Transaction transferMoney(Long senderId, Long receiverId, BigDecimal transferAmount){



        //Find wallets using id's
        Wallet sender = walletRepository.findById(senderId).orElse(null);
        Wallet receiver = walletRepository.findById(receiverId).orElse(null);

        //check sender != recvr
        if( sender==null || receiver==null){
            //How to print an error in ths case
            throw new WalletNotFoundException("Sender or receiver wallet not found");

        }

        if(sender==receiver){
            throw new InvalidTransferException("Sender and receiver cannot be the same");
        }

        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Transfer amount must be greater than 0.00");
        }

        //Transfer money
        BigDecimal senderCB = sender.getBalance();
        BigDecimal receiverCB = receiver.getBalance();


        if(senderCB.compareTo(transferAmount) < 0 ){
            //insufficient funds message should be alerted
            throw new InvalidTransferException("Insufficient funds");
        }

        //Update balance
        sender.setBalance(senderCB.subtract(transferAmount)) ;
        receiver.setBalance(receiverCB.add(transferAmount)) ;

        //By this point new balances in the account should be updated


        //Im not sure if i would have this to save the changes into the database or not , im assuming thats the main function of this line .
//        walletRepository.save();
        walletRepository.save(sender);
        walletRepository.save(receiver);
        Transaction trans = new Transaction( senderId,  receiverId,  transferAmount , TransactionStatus.SUCCESS , TransactionType.TRANSFER);

        return transactionRepository.save(trans);
    }

    //Here I suppose will be where the transaction service will be initiated with the transaction notice inorder to make sure the process isn'r buggy halfway instead it goes on through until the end of hte operation .
    public List<Transaction> getTransactionHistory(Long Id){
        return transactionRepository.findBySenderIdOrReceiverId(Id, Id);
    }
}
