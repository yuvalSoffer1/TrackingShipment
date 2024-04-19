package com.example.trackingshipment;

public class Shipment {
    private String orderNumber;
    private String shipmentNumber;
    private String shipmentDate;
    private String shipmentTime;
    private String shipmentStatus;

    // No-argument constructor for the firebase
    public Shipment() {
    }

    // Constructor
    public Shipment(String orderNumber, String shipmentNumber, String shipmentDate, String shipmentTime, String shipmentStatus) {
        this.orderNumber = orderNumber;
        this.shipmentNumber = shipmentNumber;
        this.shipmentDate = shipmentDate;
        this.shipmentTime = shipmentTime;
        this.shipmentStatus = shipmentStatus;
    }

    // Getters
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    public String getShipmentTime() {
        return shipmentTime;
    }

    public String getShipmentStatus() {
        return shipmentStatus;
    }

    // Setters
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }
}
