package com.cafepos.ui.demo;

import com.cafepos.app.events.EventBus;
import com.cafepos.app.events.OrderCreated;
import com.cafepos.app.events.OrderPaid;
import com.cafepos.app.command.*;
import com.cafepos.domain.*;
import com.cafepos.infra.Wiring;
import com.cafepos.domain.menu.*;
import com.cafepos.domain.common.Money;
import com.cafepos.domain.payment.CardPayment;
import com.cafepos.infra.printing.LegacyPrinterAdapter;
import com.cafepos.infra.printing.Printer;
import com.cafepos.domain.state.OrderFSM;
import com.cafepos.ui.ConsoleView;
import com.cafepos.ui.OrderController;
import com.cafepos.infra.vendor.legacy.LegacyThermalPrinter;

public final class FinalizedDemo {
    public static void main(String[] args) {
        // Setup for our system
        EventBus eventBus = new EventBus();
        var wiring = Wiring.createDefault();
        var view = new ConsoleView();
        Printer printer = new LegacyPrinterAdapter(new LegacyThermalPrinter());

        // 1. EVENT BUS
        System.out.println("1. EVENT BUS PATTERN");
        eventBus.on(OrderCreated.class, event ->
                System.out.println("   [Event] Order created: " + event.orderId()));
        eventBus.on(OrderPaid.class, event ->
                System.out.println("   [Event] Order paid: " + event.orderId()));
        System.out.println("   NOTE: Event buses were created to compliment the state pattern");
        // 2. COMPOSITE & ITERATOR PATTERNS
        System.out.println("\n2. COMPOSITE & ITERATOR PATTERNS");
        Menu menu = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        coffee.add(new MenuItem("Espresso", Money.of(3.50), false));
        coffee.add(new MenuItem("Latte", Money.of(4.00), false));
        coffee.add(new MenuItem("Cappuccino", Money.of(3.80), false));
        menu.add(coffee);

        System.out.println("   Menu structure:");
        menu.print();

        // 3. COMMAND PATTERN
        System.out.println("\n3. COMMAND PATTERN");
        long orderId = 5001L;
        Order order = new Order(orderId);
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);

        // Setup commands
        remote.setSlot(0, new AddItemCommand(service, "LAT", 2));
        remote.setSlot(1, new AddItemCommand(service, "ESP", 1));
        remote.setSlot(2, new PayOrderCommand(service, new CardPayment("1234567890123456"), 10));

        System.out.println("   Initial order state:");
        System.out.println("   Items: " + order.items().size() + ", Total: $" + order.totalWithTax(10));

        remote.press(0); // Add 2 lattes
        System.out.println("   After adding 2 lattes:");
        System.out.println("   Items: " + order.items().size() + ", Total: $" + order.totalWithTax(10));

        remote.press(1); // Add 1 espresso
        System.out.println("   After adding 1 espresso:");
        System.out.println("   Items: " + order.items().size() + ", Total: $" + order.totalWithTax(10));

        remote.undo(); // Remove last item
        System.out.println("   After undo:");
        System.out.println("   Items: " + order.items().size() + ", Total: $" + order.totalWithTax(10));

        // 4. STATE PATTERN
        System.out.println("\n4. STATE PATTERN");
        OrderFSM fsm = new OrderFSM();
        eventBus.emit(new OrderCreated(orderId));
        System.out.println("   Initial state: " + fsm.status());
        fsm.pay();
        eventBus.emit(new OrderPaid(orderId));
        System.out.println("   After payment: " + fsm.status());
        fsm.markReady();
        System.out.println("   After marking ready: " + fsm.status());
        fsm.deliver();
        System.out.println("   After delivery: " + fsm.status());

        remote.press(2); // Process payment via command

        // 5. ADAPTER PATTERN
        System.out.println("\n5. ADAPTER PATTERN");
        String receipt = "Order #" + orderId + "\n" +
                "LAT x2: $8.00\n" +
                "Subtotal: $8.00\n" +
                "Tax (10%): $0.80\n" +
                "Total: $8.80\n" +
                "Thank you!";
        printer.print(receipt);

        // 6. MVC PATTERN
        System.out.println("\n6. MVC PATTERN");
        var controller = new OrderController(wiring.repo(), wiring.checkout());
        controller.createOrder(5002L);
        controller.addItem(5002L, "CAP", 1);
        String mvcReceipt = controller.checkout(5002L, 10);
        view.print("\nMVC Order Receipt:\n" + mvcReceipt);
    }
}