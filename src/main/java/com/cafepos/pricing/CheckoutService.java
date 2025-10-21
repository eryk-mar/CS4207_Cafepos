package com.cafepos.pricing;

import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;
import com.cafepos.payment.PaymentStrategy;

public final class CheckoutService {
    private final ProductFactory factory;
    private final PricingService pricing;
    private final ReceiptPrinter printer;
    private final int taxPercent;

    public CheckoutService(ProductFactory factory, PricingService pricing,
                           ReceiptPrinter printer, int taxPercent) {
        this.factory = factory;
        this.pricing = pricing;
        this.printer = printer;
        this.taxPercent = taxPercent;
    }

    public String checkout(String recipe, int qty, PaymentStrategy paymentStrategy) {
        Product product = factory.create(recipe);
        if (qty <= 0) qty = 1;
        Money unit = (product instanceof com.cafepos.decorator.Priced p)
                ? p.price() : product.basePrice();

        Money subtotal = unit.multiply(qty);
        var result = pricing.price(subtotal);


        if (paymentStrategy != null) {
            Order order = new Order(OrderIds.next());
            order.addItem(new LineItem(product, qty));

            paymentStrategy.pay(order);
        }


        return printer.format(recipe, qty, result, taxPercent);


    }

    public String checkout(String recipe, int qty) {
        return checkout(recipe, qty, null);
    }

}
