package edu.pietro.team.payhero.entities;


import java.util.Currency;

public class AmountOfMoney {

    private Double mAmount;

    private Currency mCurrency;

    public AmountOfMoney() {
    }

    public AmountOfMoney(Double amount, Currency currency) {
        mAmount = amount;
        mCurrency = currency;
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        this.mAmount = amount;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public void setCurrency(Currency currency) {
        this.mCurrency = currency;
    }
}
