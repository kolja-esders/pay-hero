package edu.pietro.team.payhero.event;


import edu.pietro.team.payhero.social.MoneyTransfer;

public class OnPaymentInit {

    private final MoneyTransfer mPurchase;

    public OnPaymentInit(MoneyTransfer purchase) {
        mPurchase = purchase;
    }

    public MoneyTransfer getPurchase() {
        return mPurchase;
    }

}
