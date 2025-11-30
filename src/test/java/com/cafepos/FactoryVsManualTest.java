package com.cafepos;

import com.cafepos.domain.catalog.SimpleProduct;
import com.cafepos.domain.catalog.Product;
import com.cafepos.domain.common.Money;
import com.cafepos.app.factory.ProductFactory;
import com.cafepos.domain.Order;  // Fixed import
import com.cafepos.domain.LineItem;  // Fixed import
import com.cafepos.domain.decorator.ExtraShot;
import com.cafepos.domain.decorator.OatMilk;
import com.cafepos.domain.decorator.Priced;
import com.cafepos.domain.decorator.SizeLarge;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactoryVsManualTest {

    @Test
    public void testFactoryVsManualProduceSameDrink() {

        ProductFactory factory = new ProductFactory();
        Product viaFactory = factory.create("ESP+SHOT+OAT+L");

        Product viaManual = new SizeLarge(
                new OatMilk(
                        new ExtraShot(
                                new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))
                        )
                )
        );


        assertEquals("Espresso + Extra Shot + Oat Milk (Large)",
                viaFactory.name(), viaManual.name());

        Priced pricedFactory = (Priced) viaFactory;
        Priced pricedManual = (Priced) viaManual;
        assertEquals(pricedFactory.price(),
                pricedManual.price(), "Factory and Manual should produce same price");

        Order order1 = new Order(1L);
        order1.addItem(new LineItem(viaFactory, 1));

        Order order2 = new Order(2L);
        order2.addItem(new LineItem(viaManual, 1));

        assertEquals(order1.subtotal(),
                order2.subtotal(), "Factory and manual products orders with equal subtotals");

        assertEquals(order1.totalWithTax(10),
                order2.totalWithTax(10), "Orders with factory and manual products with equal totalWithTax");

        System.out.println("Factory: " + viaFactory.name() + " = " + pricedFactory.price());
        System.out.println("Manual: " + viaManual.name() + " = " + pricedManual.price());
        System.out.println("Order 1 subtotal: " + order1.subtotal());
        System.out.println("Order 2 subtotal: " + order2.subtotal());
        System.out.println("Order 1 total + tax: " + order1.totalWithTax(10));
        System.out.println("Order 2 total + tax: " + order2.totalWithTax(10));
    }
}