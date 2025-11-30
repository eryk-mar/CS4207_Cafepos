package com.cafepos.smells;
import com.cafepos.domain.common.Money;
import com.cafepos.app.factory.ProductFactory;
import com.cafepos.domain.catalog.Product;
import com.cafepos.domain.decorator.Priced;

public class OrderManagerGod { // God Class:
    public static int TAX_PERCENT = 10; // Global/Static State
    public static String LAST_DISCOUNT_CODE = null; // Global/Static State:
    public static String process(String recipe, int qty, String
            paymentType, String discountCode, boolean printReceipt) { // Long Method - the method  holds way too much responsibility which includes product creation and pricing, discount/tax calculation, payment processing and console output
        ProductFactory factory = new ProductFactory(); // Feature Envy
        Product product = factory.create(recipe); // Feature Envy
        Money unitPrice;
        try {
            var priced = product instanceof Priced
                    p ? p.price() : product.basePrice(); // Feature Envy
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice(); // Feature Envy
        }
        if (qty <= 0) qty = 1;
        Money subtotal = unitPrice.multiply(qty);
        Money discount = Money.zero();
        if (discountCode != null) { //Primitive Obsession
            if (discountCode.equalsIgnoreCase("LOYAL5")) { // Primitive Obsession
                discount = Money.of(subtotal.asBigDecimal() //Shotgun Surgery risk
                        .multiply(java.math.BigDecimal.valueOf(5))
                        .divide(java.math.BigDecimal.valueOf(100))); // Duplicated Logic: follow the same formula
            } else if (discountCode.equalsIgnoreCase("COUPON1")) { // Primitive Obsession
                discount = Money.of(1.00); // Primitive Obsession
            } else if (discountCode.equalsIgnoreCase("NONE")) { // Primitive Obsession
                discount = Money.zero();
            } else {
                discount = Money.zero();
            }
            LAST_DISCOUNT_CODE = discountCode; //Global/Static State
        }
        Money discounted =
                Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));
        if (discounted.asBigDecimal().signum() < 0) discounted =
                Money.zero();
        var tax = Money.of(discounted.asBigDecimal()
                .multiply(java.math.BigDecimal.valueOf(TAX_PERCENT)) // Shotgun Surgery
                .divide(java.math.BigDecimal.valueOf(100))); // Duplicated Logic: follow the same formula
        var total = discounted.add(tax);

        if (paymentType != null) {
            if (paymentType.equalsIgnoreCase("CASH")) { // Primitive Obsession
                System.out.println("[Cash] Customer paid " + total + " EUR");
            } else if (paymentType.equalsIgnoreCase("CARD")) {  // Primitive Obsession
                System.out.println("[Card] Customer paid " + total + " EUR with card ****1234");
            } else if (paymentType.equalsIgnoreCase("WALLET")) { // Primitive Obsession
                System.out.println("[Wallet] Customer paid " + total + " EUR via wallet user-wallet-789");
            } else {
                System.out.println("[UnknownPayment] " + total);
            }
        }
        StringBuilder receipt = new StringBuilder(); // Feature Envy
        receipt.append("Order (").append(recipe).append(") x").append(qty).append("\n");
                receipt.append("Subtotal: ").append(subtotal).append("\n");// Feature Envy
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");// Feature Envy
        }
        receipt.append("Tax (").append(TAX_PERCENT).append("%): ").append(tax).append("\n"); // Primitive Obsession
                receipt.append("Total: ").append(total);
        String out = receipt.toString(); // Primitive Obsession
        if (printReceipt) {
            System.out.println(out);
        }
        return out;
    }
}