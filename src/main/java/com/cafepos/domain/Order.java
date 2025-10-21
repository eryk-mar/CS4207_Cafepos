package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.observer.OrderObserver;
import com.cafepos.payment.PaymentStrategy;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(long id) {
        this.id = id;
    }

    public void addItem(LineItem li) {

        items.add(li);
        notifyObservers("itemAdded");

    }

    public Money subtotal() {
        return
                items.stream().map(LineItem::lineTotal).reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(double percent) {
        double taxRate = percent / 100.0;
        return subtotal().multiply(taxRate);
    }

    public Money totalWithTax(double percent) {
        return subtotal().add(taxAtPercent(percent));
    }
    public long id() { return id; }
    public List<LineItem> items() { return new ArrayList<>(items); }

    public void pay(PaymentStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("strategy required");
        strategy.pay(this);
        notifyObservers("paid");
    }

    public void register(OrderObserver o) {
        observers.add(o);

    }

    public void unregister(OrderObserver o) {
        observers.remove(o);
    }

    private void notifyObservers(String eventType) {
        for (OrderObserver observer: observers) {
            observer.updated(this, eventType);
        }
    }
    public void markReady() {
        notifyObservers("ready");
    }



}