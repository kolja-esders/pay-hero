package edu.pietro.team.payhero.social;


import edu.pietro.team.payhero.entities.AmountOfMoney;

/**
 * Represents a product or service that can be purchased.
 */
public class Purchasable {

    /**
     * Name used to identify the purchasable.
     */
    private String mName;

    /**
     * Producer or provider of the purchasable.
     */
    private User mProducer;

    // private Image mImage;

    public Purchasable(String name, User producer) {
        mName = name;
        mProducer = producer;
    }

    public String getName() {
        return mName;
    }

    public User getProducer() {
        return mProducer;
    }

}
