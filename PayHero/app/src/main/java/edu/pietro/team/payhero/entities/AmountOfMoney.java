package edu.pietro.team.payhero.entities;


import java.util.Currency;

public class AmountOfMoney {

    private final Double mAmount;

    private final Currency mCurrency;

    /**
     * Defaulting to EUR as currency.
     * @param amount of money
     */
    public AmountOfMoney(Double amount) {
        mAmount = amount;
        mCurrency = Currency.getInstance("EUR");
    }

    public AmountOfMoney(Double amount, Currency currency) {
        mAmount = amount;
        mCurrency = currency;
    }

}
