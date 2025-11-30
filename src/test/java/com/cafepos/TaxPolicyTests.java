package com.cafepos;

import com.cafepos.domain.pricing.FixedRateTaxPolicy;
import com.cafepos.domain.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaxPolicyTests {

    @Test void fixed_rate_tax_10_percent() {
        assertEquals(new FixedRateTaxPolicy(10).taxOn(Money.of(7.41)), Money.of(0.74));
    }

    @Test void zero_tax_returns_zero() {
        assertEquals(new FixedRateTaxPolicy(0).taxOn(Money.of(10)), Money.zero());
    }
}