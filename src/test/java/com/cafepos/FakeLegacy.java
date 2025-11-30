import com.cafepos.infra.printing.LegacyPrinterAdapter;
import com.cafepos.infra.printing.Printer;
import org.junit.jupiter.api.Test;
import com.cafepos.infra.vendor.legacy.LegacyThermalPrinter;

public class FakeLegacy extends LegacyThermalPrinter {
    int lastLen = -1;
    @Override
    public void legacyPrint(byte[] payload) {
        lastLen = payload.length;
    }
    @Test
    void adapter_converts_text_to_bytes() {
        var fake = new FakeLegacy();
        Printer p = new LegacyPrinterAdapter(fake);
        p.print("ABC");
        org.junit.jupiter.api.Assertions.assertTrue(fake.lastLen >= 3);
    }
}
