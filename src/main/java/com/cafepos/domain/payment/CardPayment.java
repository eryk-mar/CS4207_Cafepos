package com.cafepos.domain.payment;
import com.cafepos.domain.Order;
public final class CardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        //implemented with a fail-quick mechanism to take into account the card number length and the presence of non-digit chars.

        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Please provide a 16 digit card number.");
        }else {
            this.cardNumber = cardNumber;
        }
    }


    @Override
    public void pay(Order order) {
        System.out.println("[Card] Customer paid " +
                order.totalWithTax(10) + " EUR with card ****" + cardNumber.substring(cardNumber.length() - 4));
    }
}
