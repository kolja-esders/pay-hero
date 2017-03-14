package edu.pietro.team.payhero.social;


import edu.pietro.team.payhero.entities.AmountOfMoney;

/**
 * Represents a product or service that can be purchased.
 */
public class Item {

    /**
     * Name used to identify the purchasable.
     */
    private String name;

    /**
     * Producer or provider of the purchasable.
     */
    private String producer;

    /**
     * URL of an image of the product / service.
     */
    private String imageUrl;

    /**
     * Retail price.
     */
    private AmountOfMoney retailPrice;

    public Item() {
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(String name, String producer, String imageUrl, AmountOfMoney price) {
        this.name = name;
        this.producer = producer;
        this.imageUrl = imageUrl;
        this.retailPrice = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRetailPrice(AmountOfMoney price) {
        this.retailPrice = price;
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public AmountOfMoney getRetailPrice() {
        return retailPrice;
    }
}
