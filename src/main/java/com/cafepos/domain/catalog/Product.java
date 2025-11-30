package com.cafepos.domain.catalog;

import com.cafepos.domain.common.Money;

public interface Product {
    String id();
    String name();
    Money basePrice();


}