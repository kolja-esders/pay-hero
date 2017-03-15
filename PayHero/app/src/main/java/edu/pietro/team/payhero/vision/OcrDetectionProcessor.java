package edu.pietro.team.payhero.vision;


import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import edu.pietro.team.payhero.entities.AmountOfMoney;
import edu.pietro.team.payhero.event.OnPaymentInit;
import edu.pietro.team.payhero.helper.AddressBook;
import edu.pietro.team.payhero.helper.LangAnalytics;
import edu.pietro.team.payhero.social.Item;
import edu.pietro.team.payhero.social.MoneyTransfer;
import edu.pietro.team.payhero.social.User;

public class OcrDetectionProcessor implements Detector.Processor<TextBlock> {

    private final static String TAG = "OcrDetectionProcessor";

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        StringBuilder sb = new StringBuilder();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            List components = item.getComponents();
            for (int j = 0; j < components.size(); ++j) {
                if (components.get(j) instanceof Line) {
                    Line l = (Line) components.get(j);
                    sb.append(l.getValue());
                    sb.append(" ");
                    //Log.d(TAG, l.getValue());
                }
            }
        }
        String ocrText = sb.toString();
        Log.d(TAG, ocrText);
        String iban = LangAnalytics.getIBAN(ocrText);
        String amount = LangAnalytics.getAmount(ocrText);
        String contact = LangAnalytics.findFamiliarFriends(ocrText);

        if (!amount.equals("")) {
            if (!contact.equals("")) {
                iban = AddressBook.getIBANforName(contact);
            } else if (!iban.equals("")) {
                contact = AddressBook.getNameforIBAN(iban);
            }
            if (!contact.equals("") && !iban.equals("")) {
                User recipient = new User(contact, iban);
                Item moneyTransferItem = new Item("Money Transfer", null, null, null);
                EventBus.getDefault().post(new OnPaymentInit(
                        new MoneyTransfer(recipient, moneyTransferItem,
                                new AmountOfMoney(Double.valueOf(amount)))));
            }
        }
    }

    @Override
    public void release() {
    }

}
