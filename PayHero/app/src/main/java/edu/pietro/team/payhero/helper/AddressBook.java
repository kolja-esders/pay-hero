package edu.pietro.team.payhero.helper;

import edu.pietro.team.payhero.R;
import edu.pietro.team.payhero.social.User;

/**
 * Created by kolja on 26/11/2016.
 */

public class AddressBook {

    /*public static class Contact {
        private String mName;

        private String mIban;

        private String mFaceId;

        private int mImgRes;

        public Contact(String name, String iban, String faceId, int imgRes) {
            mName = name;
            mIban = iban;
            mFaceId = faceId;
            mImgRes = imgRes;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = mName;
        }

        public String getIban() {
            return mIban;
        }

        public void setIban(String iban) {
            this.mIban = mIban;
        }

        public String getFaceId() {
            return mFaceId;
        }

        public void setFaceId(String faceId) {
            this.mFaceId = mFaceId;
        }

        public int getImgRes() { return mImgRes;}
    }

    /*public static final Contact[] CONTACTS = {
            new Contact("Kolja Esders", "DE07660000000066001535", "2f154220-39bf-4b64-8b36-b2cce3a68492", R.drawable.kolja),
            new Contact("Maxim Köhler", "DE45700500003901190315", "829e847e-29d4-45aa-aba2-4704981d27f1", R.drawable.maxim),
            new Contact("David Zimmerer", "DE34390500000000014522", "0704a7f3-dda2-4001-b064-e082f9ee036c", R.drawable.david),
            new Contact("Polizei Stuttgart", "DE81100100100005578146", "aslkdklsjda", -1)
    };


    public static String getIBANforName(String name){
        for(Contact c : CONTACTS){
            if (c.getName().equals(name)){
                return c.getIban();
            }
        }
        return "";
    }


    public static String getNameforIBAN(String iban){
        for(Contact c : CONTACTS){
            if (c.getIban().equals(iban)){
                return c.getName();
            }
        }
        return "";
    }

    public static int getImgForName(String name) {
        for(Contact c : CONTACTS){
            if (c.getName().equals(name)){
                return c.getImgRes();
            }
        }
        return -1;
    }

    public static Contact getByFace(String fid) {
        for(Contact c : CONTACTS){
            if (c.getFaceId().equals(fid)){
                return c;
            }
        }
        return null;
    }*/

    public static final User[] CONTACTS = {
            new User("Kolja Esders", "DE07660000000066001535", R.drawable.kolja, "2f154220-39bf-4b64-8b36-b2cce3a68492"),
            new User("Maxim Köhler", "DE45700500003901190315", R.drawable.maxim, "829e847e-29d4-45aa-aba2-4704981d27f1"),
            new User("David Zimmerer", "DE34390500000000014522", R.drawable.david, "0704a7f3-dda2-4001-b064-e082f9ee036c"),
            new User("Parkverwaltung", "DE81100100100005578146")
    };


    public static String getIBANforName(String name){
        for(User c : CONTACTS){
            if (c.getName().equals(name)){
                return c.getIban();
            }
        }
        return "";
    }


    public static String getNameforIBAN(String iban){
        for(User c : CONTACTS){
            if (c.getIban().equals(iban)){
                return c.getName();
            }
        }
        return "";
    }

    public static int getImgForName(String name) {
        for(User c : CONTACTS){
            if (c.getName().equals(name)){
                return c.getImageResourceId();
            }
        }
        return -1;
    }

    public static User getByFace(String fid) {
        for(User c : CONTACTS){
            if (c.getFaceId() != null && c.getFaceId().equals(fid)){
                return c;
            }
        }
        return null;
    }


}
