package edu.pietro.team.payhero.event;


import edu.pietro.team.payhero.MainActivity;
import edu.pietro.team.payhero.helper.ProcessingState;
import edu.pietro.team.payhero.social.MoneyTransfer;

public class OnPaymentInit {

    private final MoneyTransfer mPurchase;
    private final ProcessingState mAssumedProcessingState;

    public OnPaymentInit(MoneyTransfer purchase, ProcessingState assumedPS) {
        mPurchase = purchase;
        mAssumedProcessingState = assumedPS;

        MainActivity.getCurrentActivity().setCurrentTransfer(mPurchase);

    }

    public MoneyTransfer getPurchase() {
        return mPurchase;
    }
    public ProcessingState getAssumedProcessingState() { return mAssumedProcessingState; }

}
