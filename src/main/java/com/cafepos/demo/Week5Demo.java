package com.cafepos.demo;

import com.cafepos.catalog.*;
import com.cafepos.domain.*;
import com.cafepos.factory.ProductFactory;
import java.util.Scanner;

public final class Week5Demo {
    public static void main(String[] args) {
        Scanner keyboard_input = new Scanner(System.in);
        System.out.println("Please make your first order e.g ESP+SHOT+OAT");
        String o1 = keyboard_input.nextLine();
        System.out.println("Quantity of the first order (in natural numbers).");
        int q1 = Integer.parseInt(keyboard_input.nextLine());
        System.out.println("Please make your second order e.g LAT+L");
        String o2 = keyboard_input.nextLine();
        System.out.println("Quantity of the second order (in natural numbers).");
        int q2 = Integer.parseInt(keyboard_input.nextLine());
        ProductFactory factory = new ProductFactory();
        Product p1 = factory.create(o1);
        Product p2 = factory.create(o2);
        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(p1, q1));
        order.addItem(new LineItem(p2, q2));
        System.out.println("Order #" + order.id());
        for (LineItem li : order.items()) {
            System.out.println(" - " + li.product().name() + " x"
                    + li.quantity() + " = " + li.lineTotal());
        }
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (10%): " +
                order.taxAtPercent(10));
        System.out.println("Total: " + order.totalWithTax(10));
    }
}