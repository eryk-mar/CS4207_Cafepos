package com.cafepos.observer;

import com.cafepos.domain.Order;
import com.cafepos.observer.OrderObserver;

public final class CustomerNotifier implements OrderObserver {
    @Override
    public void updated(Order order, String eventType) {
        System.out.println("[Customer] Dear customer, your Order #" + order.id() + " has been updated: " + eventType + ".");
    }
}
