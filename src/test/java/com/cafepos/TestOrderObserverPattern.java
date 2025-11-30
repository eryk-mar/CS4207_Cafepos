package com.cafepos;

import com.cafepos.domain.catalog.SimpleProduct;
import com.cafepos.domain.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.payment.CardPayment;
import com.cafepos.domain.payment.WalletPayment;
import org.junit.jupiter.api.Test;
import com.cafepos.domain.Order;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class TestOrderObserverPattern {
    @Test void observers_notified_on_item_add() {
        var p = new SimpleProduct("A", "A", Money.of(2));
        var o = new Order(1);
        o.addItem(new LineItem(p, 1)); // baseline
        List<String> events = new ArrayList<>();
        o.register((order, evt) -> events.add(evt));
        o.addItem(new LineItem(p, 1));
        assertTrue(events.contains("itemAdded"));
    }
    @Test
    void observer_notified_on_itemAdded(){
        var ribeye = new SimpleProduct("R-STEAK", "Ribeye Steak", Money.of(25.60));
        var ribOrder = new Order(51347);

        List<String> captdEvents = new ArrayList<>();

        ribOrder.register((order, event) -> captdEvents.add(event));

        ribOrder.addItem(new LineItem(ribeye, 40));

        assertTrue(captdEvents.contains("itemAdded"));

    }

    @Test
    void multi_observers_notified_ready(){
        var testOrder = new Order(4321);

        List<String> ob1Events = new ArrayList<>();
        List<String> ob2Events = new ArrayList<>();

        testOrder.register((order, event) -> ob1Events.add((event)));
        testOrder.register((order, event) -> ob2Events.add((event)));

        testOrder.markReady();

        assertTrue(ob1Events.contains("ready"));
        assertTrue(ob2Events.contains("ready"));
    }

    @Test
    void observer_notified_on_paid(){
        var popcorn = new SimpleProduct("P-KINO", "popcorn", Money.of(5.50));
        var paymentOrder = new Order(423907);

        List<String> paymentEvents = new ArrayList<>();

        paymentOrder.register((order,event) -> paymentEvents.add(event));

        paymentOrder.addItem(new LineItem(popcorn, 2));
        paymentOrder.pay( new WalletPayment("My-Wally"));

        assertTrue(paymentEvents.contains("paid"));

    }

    @Test
    void observers_notified_in_sequence(){
        var cherry_pie = new SimpleProduct("C-PIE", "Cherry Pie", Money.of(12));
        var sequencedOrder= new Order(940234);

        List<String> eventSequence = new ArrayList<>();

        sequencedOrder.register((order,event) -> eventSequence.add(event));


        sequencedOrder.addItem(new LineItem(cherry_pie, 5));
        sequencedOrder.pay(new CardPayment("2940163898234014"));
        sequencedOrder.markReady();

        assertTrue(eventSequence.contains("itemAdded"));
        assertTrue(eventSequence.contains("paid"));
        assertTrue(eventSequence.contains("ready"));
    }
}
