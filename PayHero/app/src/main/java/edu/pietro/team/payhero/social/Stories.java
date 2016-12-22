package edu.pietro.team.payhero.social;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import edu.pietro.team.payhero.entities.AmountOfMoney;

public class Stories {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Story> ITEMS = new ArrayList<>();

    private static final int COUNT = 25;

    static {
        User[] users = {new User("Kolja", "XXX"), new User("David", "YYY"),
                new User("Maxim", "ZZZ"), new User("Random Dude", "ABC")};
        String[] messages = {"Fuck yeah!", "Dabbing all the way O_o", "#teampietro",
                "Du bist die Schlampe, ich bin normaler Mensch!!!", "AIAIAIAIIII"};
        Purchasable[] stuff = {new Purchasable("Dopen shit", users[0]),
                new Purchasable("Fitz Backpacks", users[2])};

        for (int i = 1; i <= COUNT; i++) {
            Calendar c = GregorianCalendar.getInstance();
            c.set(2016, 10, 23, 16, 37);
            AmountOfMoney amount = new AmountOfMoney(ThreadLocalRandom.current().nextDouble(500.f));
            int randUserIndex = ThreadLocalRandom.current().nextInt(users.length);
            User buyer = users[randUserIndex];
            User seller = users[(randUserIndex + 1) % users.length];
            Purchasable purchasable = stuff[ThreadLocalRandom.current().nextInt(stuff.length)];
            Purchase p = new Purchase(buyer, seller, purchasable, amount, c);
            addItem(createDummyItem(p, messages[ThreadLocalRandom.current().nextInt(messages.length)]));
        }
    }

    private static void addItem(Story item) {
        ITEMS.add(item);
    }

    private static Story createDummyItem(Purchase purchase, String message) {
        return new Story(purchase, message);
    }

    public static class Story {
        private final Purchase mPurchase;

        private final String mMessage;

        public Story(Purchase purchase, String message) {
            mPurchase = purchase;
            mMessage = message;
        }

        public String getMessage() {
            return mMessage;
        }

        public String getBuyerName() {
            return mPurchase.getBuyer().getName();
        }

        public String getSellerName() {
            return mPurchase.getSeller().getName();
        }

        public String getPurchasableName() {
            return mPurchase.getPurchasable().getName();
        }

        public String getFormattedDate() {
            return mPurchase.getTimeOfPurchase().toString();
        }

        @Override
        public String toString() {
            return mMessage;
        }
    }
}