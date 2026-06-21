package com.Ledger_API.Ledger_API;

import com.Ledger_API.Ledger_API.entity.Wallet;
import com.Ledger_API.Ledger_API.service.WalletService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

// SO this shows how a RestController annotation works , in other words it makes the whole use of rest api easier , only seems harder because it's new still.
// should have the same idea for get , post , put , delete , update etc. ; all CRUD operations really

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health(){

        return "The ledger API is running successfully";
    }

}
