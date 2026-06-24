package com.Ledger_API.Ledger_API;

import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.entity.TransactionStatus;
import com.Ledger_API.Ledger_API.entity.TransactionType;
import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.exceptions.InvalidTransferException;
import com.Ledger_API.Ledger_API.repository.TransactionRepository;
import com.Ledger_API.Ledger_API.repository.WalletRepository;
import com.Ledger_API.Ledger_API.service.TransferService;
import com.Ledger_API.Ledger_API.service.WalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

//This class is responsible for testing the transfer process between different services
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TransferServiceTest {
    @Autowired
    private TransferService transferService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
//    Checks if transfer logic still works using methods built in
//    TEST :1
    @DisplayName("Test 1 : Successful transfer updates between wallets")
    void successfulTransferUpdatesBothWalletBalances() {
        Wallet sender = walletRepository.save(new Wallet("harun", new BigDecimal("500")));
        Wallet receiver = walletRepository.save(new Wallet("alex", new BigDecimal("200")));

        Transaction transaction = transferService.transferMoney(
                sender.getId(),
                receiver.getId(),
                new BigDecimal("100")
        );

        Wallet updatedSender = walletRepository.findById(sender.getId()).orElseThrow();
        Wallet updatedReceiver = walletRepository.findById(receiver.getId()).orElseThrow();

        assertNotNull(transaction);
        assertEquals(new BigDecimal("400"), updatedSender.getBalance());
        assertEquals(new BigDecimal("300"), updatedReceiver.getBalance());
        assertEquals(1, transactionRepository.findAll().size());
    }

    @Test
    @DisplayName("Test 2: Transfer fails due to insufficient funds")
    void transferFailsWhenSenderHasInsufficientFunds() {
        // create sender with $50
        // attempt transfer of $100
        // assert exception thrown
        Wallet sender = walletRepository.save(new Wallet("harun", new BigDecimal("50")));
        Wallet receiver = walletRepository.save(new Wallet("alex", new BigDecimal("200")));


//        Exception should be thrown in place of having a
       assertThrows(InvalidTransferException.class, ()-> transferService.transferMoney(
               sender.getId(),
               receiver.getId(),
               new BigDecimal("100")));
    }

    @Test
    @DisplayName("Test 3: Transfer fails when sender and receiver are the same")
    void transferFailsWhenSenderAndReceiverAreSame() {

        Wallet sender = walletRepository.save(
                new Wallet("harun", new BigDecimal("500"))
        );

        assertThrows(InvalidTransferException.class, () ->
                transferService.transferMoney(
                        sender.getId(),
                        sender.getId(),
                        new BigDecimal("100")
                ));
    }

    @Test
    @DisplayName("Test 4: Deposit increases wallet balance and records transaction")
    void depositIncreasesBalanceAndRecordsTransaction() {
        Wallet wallet = walletRepository.save(new Wallet("harun", new BigDecimal("500")));

        Wallet updatedWallet = walletService.deposit(
                wallet.getId(),
                new BigDecimal("100")
        );

        assertEquals(new BigDecimal("600"), updatedWallet.getBalance());
        assertEquals(1, transactionRepository.findAll().size());

        Transaction transaction = transactionRepository.findAll().get(0);
        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
        assertNull(transaction.getSenderId());
        assertEquals(wallet.getId(), transaction.getReceiverId());
    }

    @Test
    @DisplayName("Test 5: Withdrawal decreases wallet balance and records transaction")
    void withdrawalDecreasesBalanceAndRecordsTransaction() {
        Wallet wallet = walletRepository.save(
                new Wallet("harun", new BigDecimal("500"))
        );

        Wallet updatedWallet = walletService.withdraw(
                wallet.getId(),
                new BigDecimal("100")
        );

        assertEquals(new BigDecimal("400"), updatedWallet.getBalance());
        assertEquals(1, transactionRepository.findAll().size());

        Transaction transaction = transactionRepository.findAll().get(0);

        assertEquals(TransactionType.WITHDRAWAL, transaction.getTransactionType());
        assertEquals(TransactionStatus.SUCCESS, transaction.getTransaction_status());
        assertEquals(wallet.getId(), transaction.getSenderId());
        assertNull(transaction.getReceiverId());
        assertEquals(new BigDecimal("100"), transaction.getTransferAmount());
    }
}
