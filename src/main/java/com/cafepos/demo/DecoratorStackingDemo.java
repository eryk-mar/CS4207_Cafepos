package com.cafepos.demo;

import com.cafepos.catalog.Product;
import com.cafepos.catalog.SimpleProduct;
import com.cafepos.common.Money;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.Priced;
import com.cafepos.decorator.SizeLarge;

public class DecoratorStackingDemo {
    public static void main(String[] args){

        SimpleProduct espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));

        Priced priced = (Priced) decorated;  // Explicit "I need pricing now"

        System.out.println("Product: " + decorated.name());
        System.out.println("Price: " + priced.price());

    }

}
