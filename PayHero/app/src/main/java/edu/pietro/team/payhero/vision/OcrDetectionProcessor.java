package edu.pietro.team.payhero.vision;


import android.util.Log;
import android.util.SparseArray;
import android.util.StringBuilderPrinter;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;

import edu.pietro.team.payhero.MainActivity;
import edu.pietro.team.payhero.entities.AmountOfMoney;
import edu.pietro.team.payhero.helper.AddressBook;
import edu.pietro.team.payhero.helper.LangAnalytics;

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

        if (amount != "") {
            if (contact != "") {
                MainActivity.getCurrentActivity().showPaymentInit(
                        new AmountOfMoney(Double.valueOf(amount)),
                        AddressBook.getIBANforName(contact),
                        contact);
            } else if (iban != "") {
                MainActivity.getCurrentActivity().showPaymentInit(
                        new AmountOfMoney(Double.valueOf(amount)),
                        iban,
                        AddressBook.getNameforIBAN(iban));
            }
        }
    }

    @Override
    public void release() {
    }

}
