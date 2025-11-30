package com.cafepos.ui.demo;
import com.cafepos.domain.*;
import com.cafepos.domain.catalog.Catalog;
import com.cafepos.domain.catalog.InMemoryCatalog;
import com.cafepos.domain.catalog.SimpleProduct;
import com.cafepos.domain.common.Money;
import com.cafepos.app.observer.CustomerNotifier;
import com.cafepos.app.observer.DeliveryDesk;
import com.cafepos.app.observer.KitchenDisplay;
import com.cafepos.domain.payment.CashPayment;
public final class Week4Demo {
    public static void main(String[] args) {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso",
                Money.of(2.50)));
        Order order = new Order(OrderIds.next());
        order.register(new KitchenDisplay());
        order.register(new DeliveryDesk());
        order.register(new CustomerNotifier());
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 1));
                order.pay(new CashPayment());
        order.markReady();
    }
}
