package com.techelevator;

public class VendingMachineItems {

    private String slotLocation;
    private String name;
    private double price;
    private String type;
    private int quantity;
    private int totalSold;

    public String getSlotLocation() {
        return slotLocation;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public VendingMachineItems(String slotLocation, String name, double price, String type) {
        this.slotLocation = slotLocation;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = 5;
        this.totalSold = 0;
    }

    public void decreaseQuantity() {

        if (quantity > 0) {

            quantity--;
            totalSold++;
        }
    }

    public boolean isSoldOut() {
        return quantity == 0;
    }
}
