package com.cafepos;

import com.cafepos.domain.pricing.FixedCouponDiscount;
import com.cafepos.domain.pricing.LoyaltyPercentDiscount;
import com.cafepos.domain.pricing.NoDiscount;
import com.cafepos.domain.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountPolicyTests {

    @Test void no_discount_returns_zero() {
        assertEquals(new NoDiscount().discountOf(Money.of(10)), Money.zero());
    }

    @Test void loyalty_discount_calculates_5_percent() {
        assertEquals(new LoyaltyPercentDiscount(5).discountOf(Money.of(7.80)), Money.of(0.39));
    }

    @Test void coupon_discount_applies_fixed_amount() {
        assertEquals(new FixedCouponDiscount(Money.of(1)).discountOf(Money.of(3.30)), Money.of(1));
    }

    @Test void coupon_discount_caps_at_subtotal() {
        assertEquals(new FixedCouponDiscount(Money.of(5)).discountOf(Money.of(3)), Money.of(3));
    }
}