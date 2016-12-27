package edu.pietro.team.payhero.social;

import java.util.Calendar;

import edu.pietro.team.payhero.entities.AmountOfMoney;


public class Purchase {

    private final User mBuyer;

    private final User mSeller;

    private final Item mItem;

    private final Calendar mTimeOfPurchase;

    public Purchase(User buyer, User seller, Item item, AmountOfMoney price,
                    Calendar timeOfPurchase) {
        mBuyer = buyer;
        mSeller = seller;
        mItem = item;
        mTimeOfPurchase = timeOfPurchase;
    }

    public final User getBuyer() {
        return mBuyer;
    }

    public final User getSeller() {
        return mSeller;
    }

    public final Item getItem() {
        return mItem;
    }

    public final Calendar getTimeOfPurchase() {
        return mTimeOfPurchase;
    }

}
