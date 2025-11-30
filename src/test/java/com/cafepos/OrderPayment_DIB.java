package com.cafepos;

import com.cafepos.domain.catalog.SimpleProduct;
import com.cafepos.domain.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.payment.PaymentStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class OrderPayment_DIB {
    @Test
    void payment_strat_call(){
        var p1 = new SimpleProduct("F32024", "Pizza", Money.of(12.75));
        var p2 = new SimpleProduct("F5350", "Chips", Money.of(2.85));
        var p3 = new SimpleProduct("D9302", "Cola", Money.of(1.65));
        var o = new Order(42);
        o.addItem(new LineItem(p1, 1));
        o.addItem(new LineItem(p2, 3));
        o.addItem(new LineItem(p3, 3));

        final boolean[] called = {false};
        PaymentStrategy fake = x -> called[0] = true;
        o.pay(fake);
        assertTrue(called[0], "Payment strategy call");

    }

    @Test
    void fakeclass_payment_strat_call(){
        var p1 = new SimpleProduct("E32024", "Petrol", Money.of(7));
        var p2 = new SimpleProduct("F5350", "Beef Jerky", Money.of(2.85));
        var p3 = new SimpleProduct("D9302", "Fanta", Money.of(1.65));
        var o = new Order(74);
        o.addItem(new LineItem(p1, 4));
        o.addItem(new LineItem(p2, 1));
        o.addItem(new LineItem(p3, 2));

        FakePaymentStrategy fakePaymentStrategy = new FakePaymentStrategy();

        o.pay(fakePaymentStrategy);

        assertTrue(fakePaymentStrategy.wasCalled(), "Fake Payment strategy call");
    }
}
