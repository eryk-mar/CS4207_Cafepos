package com.cafepos.demo;


import com.cafepos.catalog.*;
import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.payment.*;
public final class Week3CustomDemo {
    public static void main(String[] args) {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-SCR", "Sugar", Money.of(2.42)));
        catalog.add(new SimpleProduct("P-TMP", "Tea Multi-Pack", Money.of(3.99)));

        Order order3 = new Order(OrderIds.next());
        order3.addItem(new LineItem(catalog.findById("P-SCR").orElseThrow(), 2));
        order3.addItem(new LineItem(catalog.findById("P-TMP").orElseThrow(),3 ));
        System.out.println("Order #" + order3.id() + " Total: " + order3.totalWithTax(10)); // when calculated value comes up to 9.713 but our class ensures it is to the second decimal thus 9.71
        order3.pay(new WalletPayment("UL-student-wallet-26"));
        //when calculated the value would be 18.491 however the Money class ensures that currency operations will be applied thus we get 18.49!
    }
}