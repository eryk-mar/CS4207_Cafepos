package com.cafepos.catalog;

import com.cafepos.common.Money;
import com.cafepos.decorator.Priced;

public class SimpleProduct implements Product, Priced {
    private final String id;
    private final String name;
    private final Money basePrice;

    public SimpleProduct(String id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.basePrice = price;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Money basePrice() {
        return basePrice;
    }

    @Override
    public Money price() { return basePrice; }



}