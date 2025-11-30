package com.cafepos;

import com.cafepos.domain.pricing.PricingService;
import com.cafepos.domain.pricing.ReceiptPrinter;
import com.cafepos.domain.common.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReceiptPrinterTests {

    @Test void receipt_includes_all_lines_with_discount() {
        var result = new PricingService.PricingResult(Money.of(7.80), Money.of(0.39), Money.of(0.74), Money.of(8.15));
        String receipt = new ReceiptPrinter().format("LAT+L", 2, result, 10);
        assertTrue(receipt.contains("Subtotal: 7.80") && receipt.contains("Discount: -0.39") && receipt.contains("Total: 8.15"));
    }

    @Test void receipt_excludes_discount_line_when_zero() {
        var result = new PricingService.PricingResult(Money.of(5.00), Money.zero(), Money.of(0.50), Money.of(5.50));
        String receipt = new ReceiptPrinter().format("ESP", 1, result, 10);
        assertFalse(receipt.contains("Discount: -"));
    }
}