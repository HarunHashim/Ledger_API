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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;
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
//    Ensure before you test you have switched into the test database and not your main database in  the config file .

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

    @Test
    @DisplayName("Test 6: Transaction history filters by transaction type")
    void transactionHistoryFiltersByType() {

        Wallet wallet = walletRepository.save(
                new Wallet("harun", new BigDecimal("500"))
        );

        walletService.deposit(wallet.getId(), new BigDecimal("100"));
        walletService.withdraw(wallet.getId(), new BigDecimal("50"));

        Page<Transaction> result =
                transferService.getTransactionHistory(
                        wallet.getId(),
                        TransactionType.DEPOSIT,
                        null,
                        null,
                        null,
                        null,
                        null,
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(
                TransactionType.DEPOSIT,
                result.getContent().get(0).getTransactionType()
        );
    }

    @Test
    @DisplayName("Test 7: Transaction history filters by status")
    void transactionHistoryFiltersByStatus() {

        Wallet wallet = walletRepository.save(
                new Wallet("harun", new BigDecimal("500"))
        );

        walletService.deposit(wallet.getId(), new BigDecimal("100"));
        walletService.withdraw(wallet.getId(), new BigDecimal("50"));

        Page<Transaction> result =
                transferService.getTransactionHistory(
                        wallet.getId(),
                        null,
                        TransactionStatus.SUCCESS,
                        null,
                        null,
                        null,
                        null,
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());

        assertTrue(
                result.getContent().stream()
                        .allMatch(transaction ->
                                transaction.getTransaction_status()
                                        == TransactionStatus.SUCCESS)
        );
    }

    @Test
    @DisplayName("Test 8: Transaction history filters by amount range")
    void transactionHistoryFiltersByAmountRange() {

        Wallet wallet = walletRepository.save(
                new Wallet("harun", new BigDecimal("1000"))
        );

        walletService.deposit(wallet.getId(), new BigDecimal("25"));
        walletService.deposit(wallet.getId(), new BigDecimal("100"));
        walletService.deposit(wallet.getId(), new BigDecimal("250"));

        Page<Transaction> result =
                transferService.getTransactionHistory(
                        wallet.getId(),
                        null,
                        null,
                        new BigDecimal("50"),
                        new BigDecimal("200"),
                        null,
                        null,
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(
                0,
                new BigDecimal("100").compareTo(
                        result.getContent().get(0).getTransferAmount()
                )
        );
    }

    @Test
    @DisplayName("Test 9: Transaction history filters by date range")
    void transactionHistoryFiltersByDateRange() {

        Wallet wallet = walletRepository.save(
                new Wallet("harun", new BigDecimal("500"))
        );

        walletService.deposit(
                wallet.getId(),
                new BigDecimal("100")
        );

        LocalDate today = LocalDate.now();

        Page<Transaction> result =
                transferService.getTransactionHistory(
                        wallet.getId(),
                        null,
                        null,
                        null,
                        null,
                        today,
                        today,
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());

        Transaction transaction = result.getContent().get(0);

        assertEquals(
                today,
                transaction.getTransactionTime().toLocalDate()
        );
    }

    @Test
    @DisplayName("Test 10: Transaction history combines multiple filters")
    void transactionHistoryCombinesMultipleFilters() {

        Wallet wallet = walletRepository.save(
                new Wallet("harun", new BigDecimal("1000"))
        );

        walletService.deposit(wallet.getId(), new BigDecimal("25"));
        walletService.deposit(wallet.getId(), new BigDecimal("100"));
        walletService.withdraw(wallet.getId(), new BigDecimal("100"));

        LocalDate today = LocalDate.now();

        Page<Transaction> result =
                transferService.getTransactionHistory(
                        wallet.getId(),
                        TransactionType.DEPOSIT,
                        TransactionStatus.SUCCESS,
                        new BigDecimal("50"),
                        new BigDecimal("150"),
                        today,
                        today,
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());

        Transaction transaction = result.getContent().get(0);

        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());

        assertEquals(
                0,
                new BigDecimal("100")
                        .compareTo(transaction.getTransferAmount())
        );
    }


}
