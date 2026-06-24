package com.Ledger_API.Ledger_API.dto;

import java.math.BigDecimal;

public class DepositRequest {
    private Long id;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private BigDecimal amount;
}
