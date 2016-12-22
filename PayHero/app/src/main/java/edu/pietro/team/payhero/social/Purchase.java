package edu.pietro.team.payhero.social;

import java.util.Calendar;

import edu.pietro.team.payhero.entities.AmountOfMoney;


public class Purchase {

    private final User mBuyer;

    private final User mSeller;

    private final Purchasable mPurchasable;

    private final AmountOfMoney mPrice;

    private final Calendar mTimeOfPurchase;

    public Purchase(User buyer, User seller, Purchasable purchasable, AmountOfMoney price,
                    Calendar timeOfPurchase) {
        mBuyer = buyer;
        mSeller = seller;
        mPurchasable = purchasable;
        mPrice = price;
        mTimeOfPurchase = timeOfPurchase;
    }

    public final User getBuyer() {
        return mBuyer;
    }

    public final User getSeller() {
        return mSeller;
    }

    public final Purchasable getPurchasable() {
        return mPurchasable;
    }

    public final AmountOfMoney getPrice() {
        return mPrice;
    }

    public final Calendar getTimeOfPurchase() {
        return mTimeOfPurchase;
    }

}
