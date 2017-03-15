package edu.pietro.team.payhero.helper;

public class Utils {

    public static String formatIBAN(String iban) {
        iban = iban.replace(" ", "");
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < iban.length(); ++i) {
            if (i % 4 == 0 && i > 0) {
                b.append(' ');
            }
            b.append(iban.charAt(i));
        }
        return b.toString();
    }

}
