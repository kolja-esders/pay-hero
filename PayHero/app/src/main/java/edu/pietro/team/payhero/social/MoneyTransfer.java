package edu.pietro.team.payhero.social;

import java.util.Calendar;

import edu.pietro.team.payhero.entities.AmountOfMoney;


public class MoneyTransfer {

    private final User mSender;

    private final User mRecipient;

    private final Item mItem;

    private final AmountOfMoney mAmount;

    private final Calendar mTimeOfPurchase;

    public MoneyTransfer(User recipient, Item item, AmountOfMoney price) {
        // The app-user is the sender
        mSender = null;
        mRecipient = recipient;
        mItem = item;
        mAmount = price;
        mTimeOfPurchase = Calendar.getInstance();
    }

    public MoneyTransfer(User sender, User recipient, Item item, AmountOfMoney price,
                         Calendar timeOfPurchase) {
        mSender = sender;
        mRecipient = recipient;
        mItem = item;
        mTimeOfPurchase = timeOfPurchase;
        mAmount = price;
    }

    public final User getSender() {
        return mSender;
    }

    public final User getRecipient() {
        return mRecipient;
    }

    public final Item getItem() {
        return mItem;
    }

    public final Calendar getTimeOfPurchase() {
        return mTimeOfPurchase;
    }

    public final AmountOfMoney getAmount() {
        return mAmount;
    }

    public boolean isValid() {
        return mRecipient != null && mRecipient.getName() != null && mRecipient.getIban() != null
                && mAmount != null;
    }

}
