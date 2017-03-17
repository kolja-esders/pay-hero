package edu.pietro.team.payhero.social;

import edu.pietro.team.payhero.R;
import edu.pietro.team.payhero.helper.AddressBook;

public final class User {

    public static final User AMAZON = new User("Amazon", "DE64700700100203477500", R.drawable.amazon_logo);
    public static final User ZALANDO = new User("Zalando", "DE86210700200123010101", R.drawable.zalando_logo);
    //public static final User ZKM = new User("ZKM Karlsruhe", "DE11660501010009116419", R.drawable.zkm);
    public static final User DISNEY = new User("Disneyland Paris", "DE11660501010009116419", R.drawable.disney);
    public static final User ELON = new User("Elon Msuk", "DE11660501010009116419", R.drawable.elon);
    public static final User PIETRO = new User("Pietro Lombardi", "DE11660501010009116419", R.drawable.pietro);
    public static final User MAXIM = new User("Maxim Köhler", "DE11660501010009116419", R.drawable.maxim);
    public static final User KOLJA = new User("Kolja Esders", "DE11660501010009116419", R.drawable.kolja);
    public static final User DAVID = new User("David Zimmerer", "DE11660501010009116419", R.drawable.david);

    private String mName;

    private String mIban;

    // Some kind of image

    private int mImageResourceId = -1;

    private String mFaceId = null;

    public User(String name, String iban) {
        mName = name;
        mIban = iban;
    }

    public User(String name, String iban, int imageResourceId) {
        mName = name;
        mIban = iban;
        mImageResourceId = imageResourceId;
    }

    public User(String name, String iban, int imageResourceId, String faceId) {
        mName = name;
        mIban = iban;
        mImageResourceId = imageResourceId;
        mFaceId = faceId;
    }

    public String getName() {
        return mName;
    }

    public String getIban() {
        return mIban;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public String getFaceId() {
        return mFaceId;
    }

}
