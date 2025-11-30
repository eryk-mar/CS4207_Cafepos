package com.cafepos.domain.decorator;
import com.cafepos.domain.common.Money;

// Introduce a new interface for pricing
public interface Priced {
    Money price();
}
// Make SimpleProduct implement Priced (price() == basePrice())
// Make all decorators implement Priced (price() == basePrice() +

