package edu.pietro.team.payhero.social;


import edu.pietro.team.payhero.entities.AmountOfMoney;

/**
 * Represents a product or service that can be purchased.
 */
public class Item {

    /**
     * Name used to identify the purchasable.
     */
    public String name;

    /**
     * Producer or provider of the purchasable.
     */
    public String producer;

    /**
     * URL of an image of the product / service.
     */
    public String imageUrl;

    /**
     * For the sake of simplicity we will include the price in the Item.
     */
    public AmountOfMoney price;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

}
