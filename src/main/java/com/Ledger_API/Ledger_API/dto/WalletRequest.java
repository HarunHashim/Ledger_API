package com.Ledger_API.Ledger_API.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WalletRequest {
    @NotNull
    private BigDecimal balance;

    public @NotNull String getUserName() {
        return userName;
    }

    public void setUserName(@NotNull String userName) {
        this.userName = userName;
    }

    public @NotNull BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(@NotNull BigDecimal balance) {
        this.balance = balance;
    }

    @NotNull
    private String userName;
}
