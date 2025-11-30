package com.cafepos.ui.demo;

import com.cafepos.domain.catalog.Product;
import com.cafepos.domain.catalog.SimpleProduct;
import com.cafepos.domain.common.Money;
import com.cafepos.domain.decorator.ExtraShot;
import com.cafepos.domain.decorator.OatMilk;
import com.cafepos.domain.decorator.Priced;
import com.cafepos.domain.decorator.SizeLarge;

public class DecoratorStackingDemo {
    public static void main(String[] args){

        SimpleProduct espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));

        Priced priced = (Priced) decorated;  // Explicit "I need pricing now"

        System.out.println("Product: " + decorated.name());
        System.out.println("Price: " + priced.price());

    }

}
