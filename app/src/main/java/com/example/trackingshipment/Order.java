package com.example.trackingshipment;


public class Order {
    private String orderNumber;
    private String orderDate;
    private String itemNumber;
    private String itemDescription;
    private String originCountry;
    private String departureDate;
    private String destinationCountry;
    private String estimatedArrivalDate;
    private String deliveryDate;
    private String orderStatus;
    private String shipmentNumber;

    // No-argument constructor for the firebase
    public Order() {
    }

    // Constructor
    public Order(String orderDate, String itemNumber,
                 String itemDescription, String originCountry, String departureDate,
                 String destinationCountry, String estimatedArrivalDate, String deliveryDate,
                 String orderStatus) {
        this.orderNumber = MainActivity.generateTenDigitNumber();
        this.orderDate = orderDate;
        this.itemNumber = itemNumber;
        this.itemDescription = itemDescription;
        this.originCountry = originCountry;
        this.departureDate = departureDate;
        this.destinationCountry = destinationCountry;
        this.estimatedArrivalDate = estimatedArrivalDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
        this.shipmentNumber = MainActivity.generateTenDigitNumber();
    }

    // Getters
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public String getEstimatedArrivalDate() {
        return estimatedArrivalDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    // Setters
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }
}
