package com.techelevator;
import com.techelevator.view.Menu;

import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";

	private static final String MAIN_MENU_OPTION_EXIT = "Exit";


	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};

	private Menu menu;
	private VendingMachine vendingMachine;

	public VendingMachineCLI(Menu menu, VendingMachine vendingMachine) {
		this.menu = menu;
		this.vendingMachine = vendingMachine;
	}

	public void run() {

		System.out.println("*******************************");
		System.out.println("$$     Vendo-Matic 800       $$");
		System.out.println("# A Lean Mean Vending Machine #");
		System.out.println("$$  Umbrella ^ Corporation   $$");
		System.out.println("*******************************");

		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {

				System.out.println("Vendo-Matic 800 Items:");
				System.out.println("");

				vendingMachine.displayItems();


			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {

				managePurchase();

			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {

				System.out.println("Umbrella Corp thanks you for your business. Have a Vend-Tastic day!!!");

				break;
			} else if (choice.equals("4")) {

				vendingMachine.generateSalesReport();
			}
		}
	}

	private void managePurchase() {

		while(true) {

			System.out.printf("\nCurrent Balance: $%.2f\n", vendingMachine.getBalance());
			String purchaseChoice = (String) menu.getChoiceFromOptions(new String[]{"Feed Money", "Select Product", "Finish Transaction"});

			if (purchaseChoice.equals("Feed Money")) {

				feedMoney();
			} else if (purchaseChoice.equals("Select Product")) {

				selectProduct();

			} else if (purchaseChoice.equals("Finish Transaction")) {

				finishTransaction();

				break;
			}
		}
	}

	private void feedMoney() {
		System.out.println("Please provide a whole dollar amount: $1, $5, $10, $20");

		String input = new Scanner(System.in).nextLine();

		try {
			int amount = Integer.parseInt(input);
			vendingMachine.feedMoney(amount);
		} catch (NumberFormatException e) {
			System.out.println("Invalid amount. Please provide a whole dollar amount.");
		}
	}

	private void selectProduct() {

		System.out.println("Please select the item slot: ");
		vendingMachine.displayItems();

		String slotLocation = new Scanner(System.in).nextLine();

		vendingMachine.selectProduct(slotLocation);
	}

	private void finishTransaction() {
		vendingMachine.finishTransaction();
	}


	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachine vendingMachine = new VendingMachine();
		VendingMachineCLI cli = new VendingMachineCLI(menu, vendingMachine);
		cli.run();
	}
}
