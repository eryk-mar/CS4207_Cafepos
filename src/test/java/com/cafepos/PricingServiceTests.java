package com.cafepos;

import com.cafepos.domain.pricing.*;
import com.cafepos.domain.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PricingServiceTests {

    @Test void pricing_with_loyalty_and_tax() {
        var result = new PricingService(new LoyaltyPercentDiscount(5), new FixedRateTaxPolicy(10))
                .price(Money.of(7.80));
        assertEquals(result.total(), Money.of(8.15));
    }

    @Test void pricing_with_no_discount() {
        var result = new PricingService(new NoDiscount(), new FixedRateTaxPolicy(10))
                .price(Money.of(5.00));
        assertEquals(result.total(), Money.of(5.50));
    }

    @Test void pricing_prevents_negative_total() {
        var result = new PricingService(new FixedCouponDiscount(Money.of(10)), new FixedRateTaxPolicy(10))
                .price(Money.of(5.00));
        assertEquals(result.total(), Money.zero());
    }
}