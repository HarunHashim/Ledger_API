package com.Ledger_API.Ledger_API.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferRequest {
    @NotNull
    private Long senderId;

    @NotNull
    private Long receiverId;

    @Positive
    private BigDecimal transferAmount;

    public @NotNull Long getSenderId() {
        return senderId;
    }

    public void setSenderId(@NotNull Long senderId) {
        this.senderId = senderId;
    }

    public @NotNull Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(@NotNull Long receiverId) {
        this.receiverId = receiverId;
    }

    public @Positive BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(@Positive BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }
}
