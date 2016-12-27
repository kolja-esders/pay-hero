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
    public static final List<Story> DISPLAYED_ITEMS = new ArrayList<>();

    public static final List<Story> ALL_ITEMS = new ArrayList<>();

    private static final int COUNT = 25;

    static {
        User[] users = {new User("Kolja", "XXX"), new User("David", "YYY"),
                new User("Maxim", "ZZZ"), new User("Random Dude", "ABC")};
        String[] messages = {"Fuck yeah!", "Dabbing all the way O_o", "#teampietro",
                "Du bist die Schlampe, ich bin normaler Mensch!!!", "AIAIAIAIIII"};
        Item[] stuff = {new Item("Dopen shit"),
                new Item("Fitz Backpacks")};

        for (int i = 1; i <= COUNT; i++) {
            Calendar c = GregorianCalendar.getInstance();
            c.set(2016, 10, 23, 16, 37);
            AmountOfMoney amount = new AmountOfMoney(ThreadLocalRandom.current().nextDouble(500.f));
            int randUserIndex = ThreadLocalRandom.current().nextInt(users.length);
            User buyer = users[randUserIndex];
            User seller = users[(randUserIndex + 1) % users.length];
            Item purchasable = stuff[ThreadLocalRandom.current().nextInt(stuff.length)];
            Purchase p = new Purchase(buyer, seller, purchasable, amount, c);
            addItem(createDummyItem(p, messages[ThreadLocalRandom.current().nextInt(messages.length)]));
        }
        DISPLAYED_ITEMS.addAll(ALL_ITEMS);
    }

    // TODO(kolja): Could change later on to void filter(Filter.ALL_STORIES).
    public static void filterReset() {
        DISPLAYED_ITEMS.clear();
        DISPLAYED_ITEMS.addAll(ALL_ITEMS);
    }

    // TODO(kolja): Could change later on to void filter(Filter.PERSONAL_STORIES).
    public static void filterPersonalStories() {
        DISPLAYED_ITEMS.clear();
        for (Story s : ALL_ITEMS) {
            if (s.getBuyerName().equals("Kolja")) {
                DISPLAYED_ITEMS.add(s);
            }
        }
    }

    private static void addItem(Story item) {
        ALL_ITEMS.add(item);
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
            return mPurchase.getItem().name;
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
