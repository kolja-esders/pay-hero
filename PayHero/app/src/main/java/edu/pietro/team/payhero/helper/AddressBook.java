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
            new Contact("Kolja Esders", "DE32266500011101040705", null),
            new Contact("Maxim Köhler", "DE32266500011101040705", null),
            new Contact("David Zimmerer", "DE32266500011101040705", null)
    };

}
