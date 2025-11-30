package com.cafepos.app.observer;

import com.cafepos.domain.Order;

public interface OrderObserver {
    void updated(Order order, String eventType);
}
