package com.cafepos.ui.demo;

import com.cafepos.domain.common.Money;
import com.cafepos.app.factory.ProductFactory;
import com.cafepos.domain.pricing.*;
import com.cafepos.domain.payment.CardPayment;
import com.cafepos.domain.payment.CashPayment;
import com.cafepos.domain.payment.PaymentStrategy;
import com.cafepos.domain.payment.WalletPayment;
import com.cafepos.smells.OrderManagerGod;

import java.util.Scanner;

public class Week6DemoCLI {

    public static void main(String[] args) {
        displayPriceMenu();

        while (true) {
            Scanner in = new Scanner(System.in);

            String recipe;
            while (true) {
                System.out.print("\nPlease enter a recipe for your order (or 'EXIT' to quit): ");
                recipe = in.nextLine().toUpperCase();
                if (recipe.equals("EXIT")) return;

                if (!recipe.isBlank()) {
                    break;
                } else {
                    System.out.println("Invalid recipe. Please try again.");
                }
            }

            int qty;
            while (true) {
                System.out.print("Please enter quantity: ");
                try {
                    qty = Integer.parseInt(in.nextLine());
                    if (qty >= 0) break;
                    System.out.println("Quantity cannot be negative. Please try again.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a valid quantity.");
                }
            }

            String paymentType;
            while (true) {
                System.out.print("Please enter payment type (CARD/CASH/WALLET): ");
                paymentType = in.nextLine().toUpperCase();

                if (paymentType.equals("CARD") || paymentType.equals("CASH") || paymentType.equals("WALLET")) {
                    break;
                } else {
                    System.out.println("Unknown payment type. Please enter CARD, CASH, or WALLET.");
                }
            }

            PaymentStrategy paymentStrategy;
            switch (paymentType) {
                case "CARD":
                    String cardNumber;
                    while (true) {
                        System.out.print("Please enter card number: ");
                        cardNumber = in.nextLine();
                        if (!cardNumber.isBlank()) break;
                        System.out.println("Card number cannot be empty. Please try again.");
                    }
                    paymentStrategy = new CardPayment(cardNumber);
                    break;
                case "WALLET":
                    String walletId;
                    while (true) {
                        System.out.print("Please enter wallet ID: ");
                        walletId = in.nextLine();
                        if (!walletId.isBlank()) break;
                        System.out.println("Wallet ID cannot be empty. Please try again.");
                    }
                    paymentStrategy = new WalletPayment(walletId);
                    break;
                case "CASH":
                    paymentStrategy = new CashPayment();
                    break;
                default:
                    paymentStrategy = new CashPayment();
            }

            String discountCode;
            while (true) {
                System.out.print("Please enter discount code (LOYAL5/COUPON1/NONE): ");
                discountCode = in.nextLine().toUpperCase();

                if (discountCode.equals("LOYAL5") || discountCode.equals("COUPON1") || discountCode.equals("NONE")) {
                    break;
                } else {
                    System.out.println("Unknown discount code. Please enter LOYAL5, COUPON1, or NONE.");
                }
            }

            try {
                //Proof of God class object creation
                String oldReceipt = OrderManagerGod.process(recipe, qty, paymentType, discountCode, false);
                //Proof of new refactoring that follows SOLID principles
                DiscountPolicy discountPolicy;
                if (discountCode.equals("LOYAL5")) {
                    discountPolicy = new LoyaltyPercentDiscount(5); //5 percent reduction
                } else if (discountCode.equals("COUPON1")) {
                    discountPolicy = new FixedCouponDiscount(Money.of(1.00)); //subtracts a euro
                } else {
                    discountPolicy = new NoDiscount();
                }

                var pricing = new PricingService(discountPolicy, new FixedRateTaxPolicy(10));
                var printer = new ReceiptPrinter();
                var checkout = new CheckoutService(new ProductFactory(), pricing, printer, 10);

                String newReceipt = checkout.checkout(recipe, qty, paymentStrategy);

                System.out.println("\n=== OLD vs NEW COMPARISON ===");
                System.out.println("Old Receipt:\n" + oldReceipt);
                System.out.println("\nNew Receipt:\n" + newReceipt);
                System.out.println("\nMatch: " + oldReceipt.equals(newReceipt));
                System.out.println("==============================");

            } catch (Exception e) {
                System.out.println("Error processing order: " + e.getMessage());
                System.out.println("Please check your recipe format and try again.");
            }
        }
    }

    private static void displayPriceMenu() {
        System.out.println("=== CAFE POS PRICE MENU ===");
        System.out.println("BASE DRINKS:");
        System.out.println("  ESP - Espresso: €2.50");
        System.out.println("  LAT - Latte: €3.20");
        System.out.println("  CAP - Cappuccino: €3.00");
        System.out.println("\nEXTRAS (add with + symbol):");
        System.out.println("  SHOT - Extra Shot: +€0.80");
        System.out.println("  OAT  - Oat Milk: +€0.50");
        System.out.println("  SYP  - Syrup: +€0.30");
        System.out.println("  L    - Large Size: +€0.70");
        System.out.println("\nEXAMPLES:");
        System.out.println("  LAT+OAT+L - Large Oat Milk Latte");
        System.out.println("  ESP+SHOT  - Espresso with extra shot");
        System.out.println("  CAP+SYP   - Cappuccino with syrup");
        System.out.println("============================\n");
    }
}