package com.cafepos;

import com.cafepos.pricing.*;
import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountPolicyTests {

    @Test void no_discount_returns_zero() {
        assertTrue(new NoDiscount().discountOf(Money.of(10)).equals(Money.zero()));
    }

    @Test void loyalty_discount_calculates_5_percent() {
        assertTrue(new LoyaltyPercentDiscount(5).discountOf(Money.of(7.80)).equals(Money.of(0.39)));
    }

    @Test void coupon_discount_applies_fixed_amount() {
        assertTrue(new FixedCouponDiscount(Money.of(1)).discountOf(Money.of(3.30)).equals(Money.of(1)));
    }

    @Test void coupon_discount_caps_at_subtotal() {
        assertTrue(new FixedCouponDiscount(Money.of(5)).discountOf(Money.of(3)).equals(Money.of(3)));
    }
}