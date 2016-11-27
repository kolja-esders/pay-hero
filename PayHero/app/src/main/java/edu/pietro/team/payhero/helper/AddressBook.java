package edu.pietro.team.payhero.helper;

/**
 * Created by kolja on 26/11/2016.
 */

public class AddressBook {

    public static class Contact {
        private String mName;

        private String mIban;

        private String mFaceId;

        public Contact(String name, String iban, String faceId) {
            mName = name;
            mIban = iban;
            mFaceId = faceId;
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
    }

    public static final Contact[] CONTACTS = {
            new Contact("Kolja Esders", "DE32266500011101040705", "2f154220-39bf-4b64-8b36-b2cce3a68492"),
            new Contact("Maxim Köhler", "DE32266500011101040705", "829e847e-29d4-45aa-aba2-4704981d27f1"),
            new Contact("David Zimmerer", "DE32266500011101040705", "0704a7f3-dda2-4001-b064-e082f9ee036c")
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


}
