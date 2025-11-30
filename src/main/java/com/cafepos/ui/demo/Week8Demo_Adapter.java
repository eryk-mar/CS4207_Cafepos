package com.cafepos.ui.demo;

import com.cafepos.infra.printing.LegacyPrinterAdapter;
import com.cafepos.infra.printing.Printer;
import com.cafepos.infra.vendor.legacy.LegacyThermalPrinter;

public class Week8Demo_Adapter {
    public static void main(String[] args) {
        String receipt = "Order (LAT+L) x2\nSubtotal: 7.80\nTax (10%): 0.78\nTotal: 8.58";
        Printer printer = new LegacyPrinterAdapter(new
                LegacyThermalPrinter());
        printer.print(receipt);
        System.out.println("[Demo] Sent receipt via adapter.");
    }
}
