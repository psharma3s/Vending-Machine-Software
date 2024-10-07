package com.techelevator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class VendingMachine {

    private Map<String, VendingMachineItems> inventory = new HashMap<>();
    private double balance = 0.00;

    public VendingMachine() {
        loadItemsFromFile("vendingmachine.csv");
    }

    private void loadItemsFromFile(String fileName) {

        File inputFile = new File(fileName);
        List<Map.Entry<String, VendingMachineItems>> tempList = new ArrayList<>();

        try (Scanner scanner = new Scanner(inputFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] items = line.split("\\|");

                String slotLocation = items[0];
                String name = items[1];
                double price = Double.parseDouble(items[2]);
                String type = items[3];

                VendingMachineItems item = new VendingMachineItems(slotLocation, name, price, type);
                tempList.add(Map.entry(slotLocation, item));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        Collections.sort(tempList, Map.Entry.comparingByKey());

        inventory.clear();
        for (Map.Entry<String, VendingMachineItems> entry : tempList) {

            inventory.put(entry.getKey(), entry.getValue());
        }
    }

    public void displayItems() {

        System.out.println("|Slot|      |Item|          |Price($)|   |Quantity Available|");
        System.out.println("");

        List<String> sortedSlotLocations = new ArrayList<>(inventory.keySet());
        Collections.sort(sortedSlotLocations);

        for (String slotLocation : sortedSlotLocations) {

            VendingMachineItems item = inventory.get(slotLocation);

            String soldOutText = item.isSoldOut() ? "SOLD OUT" : String.valueOf(item.getQuantity());
            System.out.printf("%-8s %-20s %4.2f            (%s)\n", item.getSlotLocation(), item.getName(), item.getPrice(), soldOutText);
        }
    }

    public VendingMachineItems getItem(String slotLocation) {

        return inventory.get(slotLocation);
    }

    public double getBalance() {

        return balance;
    }

    public void feedMoney(int amount) {

        if (amount > 0) {

            balance += amount;

            Logger.log("FEED MONEY:", amount, balance);

        } else {
            System.out.println("Please enter a valid amount.");
        }
    }

    public void selectProduct(String slotLocation) {
        VendingMachineItems item = inventory.get(slotLocation);

        if (item == null) {

            System.out.println("Invalid slot. Please try again.");

        } else if (item.isSoldOut()) {

            System.out.println("The product is SOLD OUT.");

        } else if (balance < item.getPrice()) {

            System.out.printf("Insufficient funds. The item costs $%.2f, but your balance is $%.2f.\n", item.getPrice(), balance);
        } else {

            item.decreaseQuantity();
            balance -= item.getPrice();
            System.out.printf("Dispensing: %s - $%.2f\n", item.getName(), item.getPrice());


            if (item.getType().equals("Chip")) {

                System.out.println("\nCrunch, Crunch, Yum! Enjoy your Chips.");

            } else if (item.getType().equals("Candy")) {

                System.out.println("\nMunch, Munch, Yum! Enjoy your Candy.");

            } else if (item.getType().equals("Drink")) {

                System.out.println("\nGlug, Glug, Yum! Enjoy your Drink.");

            } else if (item.getType().equals("Gum")) {

                System.out.println("\nChew, Chew, Yum! Enjoy your Gum.");
            }

            Logger.log(item.getName() + " " + slotLocation, item.getPrice(), balance);
        }
    }

    public void finishTransaction() {

        int cents = (int) Math.round(balance * 100);
        int quarters = cents / 25;
        cents %= 25;
        int dimes = cents / 10;
        cents %= 10;
        int nickels = cents / 5;

        System.out.println(" ");
        System.out.println("Don't forget to grab your change:");


        StringBuilder changeOutput = new StringBuilder();

        if (quarters > 0) {
            changeOutput.append(quarters).append(quarters == 1 ? " Quarter" : " Quarters");
        }

        if (dimes > 0) {
            if (changeOutput.length() > 0) {
                changeOutput.append(", ");
            }
            changeOutput.append(dimes).append(dimes == 1 ? " Dime" : " Dimes");
        }

        if (nickels > 0) {
            if (changeOutput.length() > 0) {
                changeOutput.append(", ");
            }
            changeOutput.append(nickels).append(nickels == 1 ? " Nickel" : " Nickels");
        }


        if (changeOutput.length() > 0) {

            String result = changeOutput.toString();
            result += ".";
            System.out.print(result);
        } else {
            System.out.print("No change to return.");
        }


        Logger.log("GIVE CHANGE:", balance, 0.00);

        balance = 0.00;
        System.out.println(" ");
        System.out.println("\nTransaction complete. Thank you for your purchase.");
    }

    public void generateSalesReport() {

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        String folderPath = "Sales Report";
        String fileName = "SalesReport_" + timestamp + ".txt";

        File folder = new File(folderPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = folderPath + File.separator + fileName;

        try (FileWriter writer = new FileWriter(filePath)) {

            double totalSales = 0;

            writer.write("|Item|                       |Quantity Sold|     |Total Item Sales($)|");
            writer.write("\n----------------------------------------------------------------------\n");
            writer.write("\n                                                                      \n");

            List<Map.Entry<String, VendingMachineItems>> sortedItems = new ArrayList<>(inventory.entrySet());

            Collections.sort(sortedItems, Map.Entry.comparingByKey());

            for (Map.Entry<String, VendingMachineItems> entry : sortedItems) {

                VendingMachineItems item = entry.getValue();


                String line = String.format("%-20s %15d %20s%.2f\n",

                        item.getName(), item.getTotalSold(), "$", item.getTotalSold() * item.getPrice());

                writer.write(line);

                totalSales += item.getTotalSold() * item.getPrice();
            }

            writer.write("\n**TOTAL SALES**: $" + String.format("%.2f", totalSales));

            System.out.println("Sales report generated: " + fileName);

        } catch (IOException e) {

            System.out.println("Error writing sales report: " + e.getMessage());
        }

    }
}

