package com.cafepos.app.factory;
import com.cafepos.domain.catalog.Product;
import com.cafepos.domain.catalog.SimpleProduct;
import com.cafepos.domain.common.Money;
import com.cafepos.domain.decorator.ExtraShot;
import com.cafepos.domain.decorator.OatMilk;
import com.cafepos.domain.decorator.SizeLarge;
import com.cafepos.domain.decorator.Syrup;

public final class ProductFactory {
    public Product create(String recipe) {
        String[] parts = normalize(recipe);
        Product base = createBase(parts[0]);
        return applyAddons(base, parts);
    }

    private String[] normalize(String recipe) {
        if (recipe == null || recipe.isBlank()) throw new
                IllegalArgumentException("recipe required");
        String[] raw = recipe.split("\\+");
        return java.util.Arrays.stream(raw)
                .map(String::trim)
                .map(String::toUpperCase)
                .toArray(String[]::new);
    }

    private Product createBase(String code) {
        return switch (code) {
            case "ESP" -> new SimpleProduct("P-ESP", "Espresso",
                    Money.of(2.50));
            case "LAT" -> new SimpleProduct("P-LAT", "Latte",
                    Money.of(3.20));
            case "CAP" -> new SimpleProduct("P-CAP",
                    "Cappuccino", Money.of(3.00));
            default -> throw new
                    IllegalArgumentException("Unknown base: " + code);
        };
    }

    private Product applyAddons(Product base, String[] parts) {
        Product p = base;
        for (int i = 1; i < parts.length; i++) {
            p = switch (parts[i]) {
                case "SHOT" -> new ExtraShot(p); // 0.80
                case "OAT" -> new OatMilk(p); // 0.50
                case "SYP" -> new Syrup(p);  //0.40
                case "L" -> new SizeLarge(p); // 0.70
                default -> throw new
                        IllegalArgumentException("Unknown addon: " + parts[i]);
            };
        }
        return p;
    }
}