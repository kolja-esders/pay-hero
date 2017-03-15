package edu.pietro.team.payhero.entities;


import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class AmountOfMoney {

    private final Double mAmount;

    private final Currency mCurrency;

    public AmountOfMoney() {
        mAmount = null;
        mCurrency = Currency.getInstance("EUR");
    }

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

    public Double getAmount() {
        return mAmount;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    public String getFormattedAmount() {
        if (mAmount == null) {
            return "";
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        nf.setCurrency(mCurrency);
        nf.setMaximumFractionDigits(mCurrency.getDefaultFractionDigits());
        return nf.format(mAmount);
    }
}
