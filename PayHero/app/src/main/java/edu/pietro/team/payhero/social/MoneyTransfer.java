package edu.pietro.team.payhero.social;

import java.util.Calendar;

import edu.pietro.team.payhero.entities.AmountOfMoney;


public class MoneyTransfer {

    private final User mSender;

    private final User mRecipient;

    private final Item mItem;

    private final Calendar mTimeOfPurchase;

    public MoneyTransfer(User recipient, Item item, AmountOfMoney price) {
        // The app-user is the sender
        mSender = null;
        mRecipient = recipient;
        mItem = item;
        mTimeOfPurchase = Calendar.getInstance();
    }

    public MoneyTransfer(User sender, User recipient, Item item, AmountOfMoney price,
                         Calendar timeOfPurchase) {
        mSender = sender;
        mRecipient = recipient;
        mItem = item;
        mTimeOfPurchase = timeOfPurchase;
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

}
