package com.cafepos;
import com.cafepos.domain.payment.PaymentStrategy;
import com.cafepos.domain.Order;

public class FakePaymentStrategy implements PaymentStrategy {
    private boolean wasCalled = false;

    @Override
    public void pay(Order order) {
        this.wasCalled = true;
    }

    public boolean wasCalled() {
        return wasCalled;
    }
}