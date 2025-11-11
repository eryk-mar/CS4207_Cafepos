package com.cafepos;

import com.cafepos.catalog.Product;
import com.cafepos.decorator.Priced;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.factory.ProductFactory;
import com.cafepos.menu.*;
import com.cafepos.common.Money;
import com.cafepos.state.OrderFSM;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class W9_Integration_TEST {
    @Test
    void compositeMenu_Factory_State_Integration() {
        // our tree like menu structure
        Menu cafeMenu = new Menu("Cafe Menu");
        Menu coffeeMenu = new Menu("Coffee");
        Menu specialsMenu = new Menu("Specials");

        coffeeMenu.add(new MenuItem("Espresso", Money.of(2.50), true));
        coffeeMenu.add(new MenuItem("Latte", Money.of(3.20), true));
        coffeeMenu.add(new MenuItem("Cappuccino", Money.of(3.00), true));

        specialsMenu.add(new MenuItem("Large Oat Latte", Money.of(4.40), true));
        specialsMenu.add(new MenuItem("Espresso with Shot", Money.of(3.30), true));

        cafeMenu.add(coffeeMenu);
        cafeMenu.add(specialsMenu);

        // composite iterator to find menu items
        List<MenuItem> foundItems = new ArrayList<>();
        Iterator<MenuComponent> iterator = cafeMenu.iterator();

        while (iterator.hasNext()) {
            MenuComponent component = iterator.next();
            if (component instanceof MenuItem menuItem) {
                foundItems.add(menuItem);
            }
        }

        //order and state machine
        OrderFSM order = new OrderFSM();
        assertEquals("NEW", order.status());

        ProductFactory factory = new ProductFactory();
        Order fullOrder = new Order(OrderIds.next());

        // creating menu items in the factory
        for (MenuItem menuItem : foundItems) {
            Product product = factory.create(getRecipeFromMenuItem(menuItem));

            // check for menu.price == factory.price
            Money menuPrice = menuItem.price();
            Money productPrice = getProductPrice(product);
            assertEquals(menuPrice, productPrice);

            // simulation of the act of ordering an item
            fullOrder.addItem(new LineItem(product, 1)); // 1 quantity each
        }

        // acting out the process
        order.pay(); // NEW → PREPARING
        assertEquals("PREPARING", order.status());

        order.markReady(); // PREPARING → READY
        assertEquals("READY", order.status());

        order.deliver(); // READY → DELIVERED
        assertEquals("DELIVERED", order.status());

        // final verification of the work flow
        assertEquals("DELIVERED", order.status());
        assertEquals(5, fullOrder.items().size()); // should have 5 line items
        assertNotEquals(fullOrder.subtotal(), Money.zero()); // should have non-zero total
    }

    @Test
    void stateMachine_InvalidTransitions_FromCompositeMenuSelection() {
        // Test error case: trying to prepare order before payment
        Menu cafeMenu = new Menu("Cafe Menu");
        cafeMenu.add(new MenuItem("Espresso", Money.of(2.50), true));

        OrderFSM order = new OrderFSM();
        assertEquals("NEW", order.status());

        // Try invalid transition (should not change state)
        order.prepare(); // Should fail - cannot prepare before payment
        assertEquals("NEW", order.status()); // State should remain NEW

        order.markReady(); // Should also fail
        assertEquals("NEW", order.status());

        order.deliver(); // Should also fail
        assertEquals("NEW", order.status());

        // Now do valid payment
        order.pay();
        assertEquals("PREPARING", order.status()); // Now state changes
    }

    // helper for the recipes
    private String getRecipeFromMenuItem(MenuItem menuItem) {
        return switch (menuItem.name()) {
            case "Espresso" -> "ESP";
            case "Latte" -> "LAT";
            case "Cappuccino" -> "CAP";
            case "Large Oat Latte" -> "LAT+OAT+L";
            case "Espresso with Shot" -> "ESP+SHOT";
            default -> throw new IllegalArgumentException("Unknown menu item: " + menuItem.name());
        };
    }
    // access to our pricing
    private Money getProductPrice(Product product) {
        if (product instanceof Priced priced) {
            return priced.price();
        }
        return product.basePrice();
    }
}
