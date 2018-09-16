package model.tracker;

public class ShippingMainData {
    private final String shippingNumber;
    private final String postingTerminal;
    private final String postingCountry;
    private final String postingData;
    private final String deliveryTerminal;
    private final String deliveryCountry;
    private final String deliveryType;
    private final String amountOfPackages;
    private final String deliveryStatus;
    private final String deliveryPlace;

    public ShippingMainData(String shippingNumber, String postingTerminal, String postingCountry, String postingData, String deliveryTerminal, String deliveryCountry, String deliveryType, String amountOfPackages, String deliveryStatus, String deliveryPlace) {
        this.shippingNumber = shippingNumber;
        this.postingTerminal = postingTerminal;
        this.postingCountry = postingCountry;
        this.postingData = postingData;
        this.deliveryTerminal = deliveryTerminal;
        this.deliveryCountry = deliveryCountry;
        this.deliveryType = deliveryType;
        this.amountOfPackages = amountOfPackages;
        this.deliveryStatus = deliveryStatus;
        this.deliveryPlace = deliveryPlace;
    }

    public String getShippingNumber() {
        return shippingNumber;
    }

    public String getPostingTerminal() {
        return postingTerminal;
    }

    public String getPostingCountry() {
        return postingCountry;
    }

    public String getPostingData() {
        return postingData;
    }

    public String getDeliveryTerminal() {
        return deliveryTerminal;
    }

    public String getDeliveryCountry() {
        return deliveryCountry;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getAmountOfPackages() {
        return amountOfPackages;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    @Override
    public String toString() {
        return
                "Package num.: " + shippingNumber + System.lineSeparator() +
                "Posting terminal: " + postingTerminal + System.lineSeparator() +
                "Posting country: " + postingCountry + System.lineSeparator() +
                "Posting data: " + postingData + System.lineSeparator() +
                "Delivery terminal: " + deliveryTerminal + System.lineSeparator() +
                "Delivery country: " + deliveryCountry + System.lineSeparator() +
                "Delivery type: " + deliveryType + System.lineSeparator() +
                "Amount of packages: " + amountOfPackages + System.lineSeparator() +
                "Delivery status: " + deliveryStatus + System.lineSeparator() +
                "Delivery place: " + deliveryPlace;
    }
}
