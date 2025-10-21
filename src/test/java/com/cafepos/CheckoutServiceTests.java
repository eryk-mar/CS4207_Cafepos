package com.cafepos;

import com.cafepos.pricing.*;
import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.domain.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutServiceTests {

    @Test void checkout_computes_totals() {
        var checkout = new CheckoutService(new ProductFactory(),
                new PricingService(new NoDiscount(), new FixedRateTaxPolicy(10)),
                new ReceiptPrinter(), 10);
        String receipt = checkout.checkout("ESP", 2);
        assertTrue(receipt.contains("Total: 5.50"));
    }

    @Test void checkout_clamps_zero_qty_to_one() {
        var checkout = new CheckoutService(new ProductFactory(),
                new PricingService(new NoDiscount(), new FixedRateTaxPolicy(10)),
                new ReceiptPrinter(), 10);
        String receipt = checkout.checkout("ESP", 0);
        assertTrue(receipt.contains("x1"));
    }

    @Test void checkout_calls_payment_strategy() {
        var testPayment = new TestPaymentStrategy();
        var checkout = new CheckoutService(new ProductFactory(),
                new PricingService(new NoDiscount(), new FixedRateTaxPolicy(10)),
                new ReceiptPrinter(), 10);
        checkout.checkout("ESP", 1, testPayment);
        assertTrue(testPayment.wasCalled);
    }

    static class TestPaymentStrategy implements PaymentStrategy {
        boolean wasCalled = false;
        @Override public void pay(Order order) { wasCalled = true; }
    }
}