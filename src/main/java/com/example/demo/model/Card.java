package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Card {

    @Override
    public int hashCode() {
        return new Long(getCardId()).intValue();
    }

    @Override
    public boolean equals(Object obj) {
        return (getCardId() == ((Card)obj).getCardId());
    }

    public Long getCardId() {
        return cardId;
    }

    public Card(Long cardId, Long transferableAmount) {
        this.cardId = cardId;
        this.balance = 0L;
        this.transferableAmount = transferableAmount;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getTransferableAmount() {
        return transferableAmount;
    }

    public void setTransferableAmount(Long transferableAmount) {
        this.transferableAmount = transferableAmount;
    }


    @Id
    private Long cardId;
    private Long balance;
    private Long transferableAmount;
}
