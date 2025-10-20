package com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
public final class Money implements Comparable<Money> {
    private final BigDecimal amount;
    public static Money of(double value) {
        return new Money(new BigDecimal(value));
    }
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
    private Money(BigDecimal a) {
        if (a == null) throw new IllegalArgumentException("amount required");
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }
    public Money multiply(int qty) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(qty)));
    }

    public Money multiply(double factor) {
        //overloading which will allow use to work with percentages elsewhere
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
    }
    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return amount.toString();
    }

    @Override
    public int compareTo(Money other) {
        return this.amount.compareTo(other.amount);
    }


}
