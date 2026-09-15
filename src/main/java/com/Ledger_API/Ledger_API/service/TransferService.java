package com.Ledger_API.Ledger_API.service;

import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.entity.TransactionStatus;
import com.Ledger_API.Ledger_API.entity.TransactionType;
import com.Ledger_API.Ledger_API.exceptions.InvalidTransferException;
import com.Ledger_API.Ledger_API.exceptions.WalletNotFoundException;
import com.Ledger_API.Ledger_API.repository.TransactionRepository;
import com.Ledger_API.Ledger_API.repository.WalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Ledger_API.Ledger_API.entity.Wallet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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


        //I'm not sure if I would have this to save the changes into the database or not , im assuming that's the main function of this line .
//        walletRepository.save();
        walletRepository.save(sender);
        walletRepository.save(receiver);
        Transaction trans = new Transaction( senderId,  receiverId,  transferAmount , TransactionStatus.SUCCESS , TransactionType.TRANSFER);

        return transactionRepository.save(trans);
    }

    //Here I suppose will be where the transaction service will be initiated with the transaction notice inorder to make sure the process isn'r buggy halfway instead it goes on through until the end of the operation .
    //Pagination will be implemented here
    public Page<Transaction> getTransactionHistory(
            Long Id,
            TransactionType type,
            TransactionStatus status,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDate dateFrom,
            LocalDate dateTo,
            Pageable pageable){

        //So the service would no longer need to call this , instead it constructs a Specification<> which would contain all the
//        WHERE conditions for Transaction and return the query . The returned queries would have the conditions met using WHERE , this is where
//        the type , amount ; (etc) will be used as filters only letting through records that meet the condition set

//        return transactionRepository.findBySenderIdOrReceiverId(Id, Id ,pageable);

        Specification<Transaction> spec =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.or(
                                criteriaBuilder.equal(root.get("senderId"), Id),
                                criteriaBuilder.equal(root.get("receiverId"), Id)
                        );

        //Here the Type criteria is addressed ( DEPOSIT , WITHDRAWAL , TRANSFER )
        if (type != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("transactionType"),
                                    type
                            )
            );
        }
        // Here the Status criteria is addressed ( check if the status was a success or fail )
        if (status != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    root.get("Transaction_status"),
                                    status
                            )
            );
        }

//        Here the minAmount criteria is addressed ( Amount is greater than or equal to )
        if (minAmount != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("transferAmount"),
                                    minAmount
                            )
            );
        }

//      Here the maxAmount criteria is addressed ( Amount is less than or equal to )
        if (maxAmount != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("transferAmount"),
                                    maxAmount
                            )
            );
        }

        if (dateFrom != null) {

            // This is syntax to ideally convert from datetime object to date alone
            LocalDateTime from = dateFrom.atStartOfDay();

            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("transactionTime"),
                                    from
                            )
            );
        }


        if (dateTo != null) {

            //Here on top of converting we add one day to address the issue of including
            // the day until since at startOfDAY would not include results of that day
            LocalDateTime until = dateTo.plusDays(1).atStartOfDay();

            spec = spec.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThan(
                                    root.get("transactionTime"),
                                    until
                            )
            );
        }





        return transactionRepository.findAll(spec, pageable);
    }
}
