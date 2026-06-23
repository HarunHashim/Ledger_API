package com.Ledger_API.Ledger_API;

import com.Ledger_API.Ledger_API.entity.Transaction;
import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.repository.TransactionRepository;
import com.Ledger_API.Ledger_API.repository.WalletRepository;
import com.Ledger_API.Ledger_API.service.TransferService;
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
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
//    Checks if transfer logic still works using methods built in
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
}
