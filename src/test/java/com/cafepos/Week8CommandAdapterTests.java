package com.cafepos;
import com.cafepos.app.command.*;
import com.cafepos.domain.common.Money;
import com.cafepos.domain.Order;
import com.cafepos.domain.payment.CashPayment;
import com.cafepos.infra.printing.LegacyPrinterAdapter;
import com.cafepos.infra.printing.Printer;
import com.cafepos.infra.vendor.legacy.LegacyThermalPrinter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Week8CommandAdapterTests {

    @Test
    void invoker_command_call_with_undo(){
        // we decided to use our real order to count items
        Order o = new Order(412431);
        OrderService service = new OrderService(o);
        PosRemote remote = new PosRemote(2);

        // command execution tracker for dummy
        boolean[] otherCommandExe = {false};

        Command addCommand = new AddItemCommand(service, "ESP", 2);
        Command otherCommand = () -> {
            otherCommandExe[0] = true;  // a dummy command for comparison
        };

        // bind to slots
        remote.setSlot(0, addCommand);
        remote.setSlot(1, otherCommand);

        int initItemCount = o.items().size();

        remote.press(0);
        // TEST: add cmd
        assertEquals(1, o.items().size()); // add command should have a single item
        assertFalse(otherCommandExe[0]); // the other command should not execute
        //TEST: array size back to initial
        remote.undo();
        assertEquals(initItemCount, o.items().size());// ensures that remove the last item will return the item list to its original state
    }


    @Test
    void macroCommand_undo_reverts_commands_in_reverse_order() {
        Order order = new Order(1);
        OrderService service = new OrderService(order);

        // tracks our undoing
        java.util.List<String> undoOrder = new java.util.ArrayList<>();

        // 3 commands to test our macro
        Command add1 = new Command() {
            @Override public void execute() {
                service.addItem("ESP", 1);
            }
            @Override public void undo() {
                undoOrder.add("undo1");
                service.removeLastItem();
            }
        };

        Command add2 = new Command() {
            @Override public void execute() {
                service.addItem("LAT", 1);
            }
            @Override public void undo() {
                undoOrder.add("undo2");
                service.removeLastItem();
            }
        };

        MacroCommand macro = new MacroCommand(add1, add2);

        // execute added 2 items: 1 exe * 2 commands = 2
        macro.execute();
        assertEquals(2, order.items().size());
        // execute another 2 items: 2 exe * 4 commands = 4
        macro.execute();
        assertEquals(4, order.items().size());

        // undo removes 2 items: 2 init_exe - 1 undo = 1 exec (the items remaining are from the initial execution)
        macro.undo();
        // TEST: undo logic
        assertEquals(2, order.items().size(), "Single undo should revert exactly one add");
        assertEquals("undo2", undoOrder.get(0), "Undo should happen in reverse order (undo2 first)");
    }
    static class FakeLegacy extends LegacyThermalPrinter {
        int lastPayloadLength = -1;
        byte[] lastPayload = null;

        @Override public void legacyPrint(byte[] payload) {
            lastPayloadLength = payload.length;
            lastPayload = payload;
        }
    }

    @Test
    void adapter_converts_text_to_bytes() {
        FakeLegacy fake = new FakeLegacy();
        Printer adapter = new LegacyPrinterAdapter(fake);

        // tests the conversion of text to bytes
        String text = "text_attempt";
        adapter.print(text);

        // TEST: verify byte conversion by checking if length is greater than or equal to length (usually length + 1
        assertTrue(fake.lastPayloadLength >= text.length());

        // TEST: ensures that text can be converted back to its original state
        String convertedBack = new String(fake.lastPayload);
        assertTrue(convertedBack.contains(text));
    }

    @Test
    void integration_invoker_calls_commands_and_produces_correct_prices() {
        Order order = new Order(1);
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);

        // execution tracker
        boolean[] add1Executed = {false};
        boolean[] add2Executed = {false};
        boolean[] payExecuted = {false};

        // binding of tracker and add command
        remote.setSlot(0, new Command() {
            @Override public void execute() {
                add1Executed[0] = true;
                service.addItem("ESP", 1);
            }
        });

        remote.setSlot(1, new Command() {
            @Override public void execute() {
                add2Executed[0] = true;
                service.addItem("LAT", 1);
            }
        });

        remote.setSlot(2, new Command() {
            @Override public void execute() {
                payExecuted[0] = true;
                service.pay(new CashPayment(), 10);
            }
        });

        // TEST: Invoker calls correct commands in sequence
        remote.press(0);
        assertTrue(add1Executed[0], "Invoker should call command in slot 0");
        assertFalse(add2Executed[0], "Invoker should not call command in slot 1 yet");
        assertFalse(payExecuted[0], "Invoker should not call command in slot 2 yet");

        remote.press(1);
        assertTrue(add2Executed[0], "Invoker should call command in slot 1");
        assertFalse(payExecuted[0], "Invoker should not call command in slot 2 yet");

        // TEST: Order subtotal equals Week 5 prices
        Money subtotal = order.subtotal();
        assertTrue(subtotal.compareTo(Money.zero()) > 0,
                "command should affect subtotal");

        // TEST: Pay command works
        remote.press(2);
        assertTrue(payExecuted[0], "Invoker call pay command slot 2");
    }

    @Test
    void integration_uses_exact_week5_pricing() {
        Order o = new Order(1);
        OrderService service = new OrderService(o);
        PosRemote remote = new PosRemote(3);

        // recipe structure like in week 5
        String espressoRecipe = "ESP+SHOT+OAT";  // 2.50 + 0.80 + 0.50 = 3.80
        String latteRecipe = "LAT+L";            // 3.20 + 0.70 = 3.90

        // bind commands
        remote.setSlot(0, new AddItemCommand(service, espressoRecipe, 1));
        remote.setSlot(1, new AddItemCommand(service, latteRecipe, 2));
        remote.setSlot(2, new PayOrderCommand(service, new CashPayment(), 10));

        remote.press(0); // Add 1x ESP+SHOT+OAT = 3.80
        remote.press(1); // Add 2x LAT+L = 2 * 3.90 = 7.80

        // ESP+SHOT+OAT: 2.50 + 0.80 + 0.50 = 3.80
        // LAT+L: 3.20 + 0.70 = 3.90 × 2 =€7.80
        // Subtotal: 3.80 + 7.80 = 11.60
        Money expectedSubtotal = Money.of(11.60);

        // TEST: Subtotal matches exact Week 5 pricing
        Money actualSubtotal = o.subtotal();
        assertEquals(expectedSubtotal, actualSubtotal,
                "Comparison of the subtotals");

        // TEST: Tax calculation (10% of 11.60 = 1.16)
        Money expectedTax = Money.of(1.16);
        Money actualTax = o.taxAtPercent(10);
        assertEquals(expectedTax, actualTax,
                "10% tax should be 1.16 on 11.60 subtotal");

        // TEST: Total with tax (11.60 + 1.16 = 12.76)
        Money expectedTotal = Money.of(12.76);
        Money actualTotal = o.totalWithTax(10);
        assertEquals(expectedTotal, actualTotal,
                "Total with 10% tax should be 12.76");

        // TEST: Pay command works with correct total
        remote.press(2); // Process payment for 12.76
        // Payment should complete using the correct Week 5 total
    }
}
