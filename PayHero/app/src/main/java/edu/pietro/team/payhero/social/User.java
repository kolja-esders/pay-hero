package edu.pietro.team.payhero.social;

import edu.pietro.team.payhero.R;

public final class User {

    public static final User AMAZON = new User("Amazon", "DE64700700100203477500", R.drawable.amazon_logo);

    private String mName;

    private String mIban;

    // Some kind of image

    private int mImageResourceId = -1;

    public User(String name, String iban) {
        mName = name;
        mIban = iban;
    }

    public User(String name, String iban, int imageResourceId) {
        mName = name;
        mIban = iban;
        mImageResourceId = imageResourceId;
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

}
