package com.cafepos.domain.decorator;
import com.cafepos.domain.catalog.Product;
import com.cafepos.domain.common.Money;
public abstract class ProductDecorator implements Product, Priced {
    protected final Product base;
    protected ProductDecorator(Product base) {
        if (base == null) throw new
                IllegalArgumentException("base product required");
        this.base = base;
    }
    @Override public String id() { return base.id(); } //
   // id may remain the base product id
    @Override public Money basePrice() { return
            base.basePrice(); } // original price (not total)
    // Concrete decorators will override name() and provide
    //a finalPrice() helper if desired.

    public abstract Money price();
}
