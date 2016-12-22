package edu.pietro.team.payhero.social;

public final class User {

    private String mName;

    private String mIban;

    // Some kind of image

    public User(String name, String iban) {
        mName = name;
        mIban = iban;
    }

    public String getName() {
        return mName;
    }

    public String getIban() {
        return mIban;
    }

}
