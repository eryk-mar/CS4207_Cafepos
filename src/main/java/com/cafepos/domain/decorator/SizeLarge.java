package com.cafepos.domain.decorator;
import com.cafepos.domain.catalog.Product;
import com.cafepos.domain.common.Money;

public final class SizeLarge extends ProductDecorator {
    private static final Money SURCHARGE = Money.of(0.70);

    public SizeLarge(Product base) {
        super(base);
    }

    @Override
    public String name() {
        return base.name() + " (Large)"; }

    public Money price () {
            return (base instanceof Priced p ? p.price() : base.basePrice()).add(SURCHARGE);
        }
}