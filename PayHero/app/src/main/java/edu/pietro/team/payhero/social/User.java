package edu.pietro.team.payhero.social;

import edu.pietro.team.payhero.R;
import edu.pietro.team.payhero.helper.AddressBook;

public final class User {

    public static final User AMAZON = new User("Amazon", "DE64700700100203477500", R.drawable.amazon_logo);

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
