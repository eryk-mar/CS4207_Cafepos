package com.cafepos;

import com.cafepos.domain.*;
import com.cafepos.domain.catalog.SimpleProduct;
import com.cafepos.domain.common.Money;
import com.cafepos.domain.payment.CardPayment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardPaymentTest {

    @Test
    void cardPayment_throws_for_short_card_number() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CardPayment("1234567") // less than 16 digits
        );
        assertTrue(exception.getMessage().contains("Please provide a 16 digit card number."));
    }

    @Test
    void cardPayment_throws_for_long_card_number() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CardPayment("1234567265324234234") // more than 16 digits
        );
        assertTrue(exception.getMessage().contains("Please provide a 16 digit card number."));
    }

    @Test
    void cardPayment_throws_for_non_digit_characters() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CardPayment("123-567-ABC-EFGH") // 16 chars but contains non digits
        );
        assertTrue(exception.getMessage().contains("Please provide a 16 digit card number."));
    }

    @Test
    void cardPayment_works_with_valid_number() {
        assertDoesNotThrow(() -> {
            CardPayment cardPayment = new CardPayment("1234567812345678");
            Order order = createTestOrder();
            cardPayment.pay(order);
        });
    }

    private Order createTestOrder() {
        SimpleProduct product = new SimpleProduct("TEST", "Test Product", Money.of(10.0));
        Order order = new Order(1);
        order.addItem(new LineItem(product, 1));
        return order;
    }
}
