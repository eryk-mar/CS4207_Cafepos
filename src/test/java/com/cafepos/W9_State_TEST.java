package com.cafepos;
import com.cafepos.domain.state.OrderFSM;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class W9_State_TEST {

    // ===== HAPPY PATH TESTS =====

    @Test
    void orderFSM_HappyPath_TransitionsCorrectly() {
        OrderFSM fsm = new OrderFSM();
        assertEquals("NEW", fsm.status());

        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        fsm.markReady();
        assertEquals("READY", fsm.status());

        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());
    }

    @Test
    void orderFSM_CanCancel_FromNewState() {
        OrderFSM fsm = new OrderFSM();
        assertEquals("NEW", fsm.status());

        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }

    @Test
    void orderFSM_CanCancel_FromPreparingState() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay(); // NEW -> PREPARING
        assertEquals("PREPARING", fsm.status());

        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }

    // ===== INVALID TRANSITION TESTS =====

    @Test
    void newState_RejectsInvalidTransitions() {
        OrderFSM fsm = new OrderFSM();
        assertEquals("NEW", fsm.status());

        // These should not change the state
        fsm.prepare();
        assertEquals("NEW", fsm.status());

        fsm.markReady();
        assertEquals("NEW", fsm.status());

        fsm.deliver();
        assertEquals("NEW", fsm.status());
    }

    @Test
    void preparingState_RejectsInvalidTransitions() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay(); // NEW -> PREPARING
        assertEquals("PREPARING", fsm.status());

        // cannot pay again or deliver before ready
        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        fsm.deliver();
        assertEquals("PREPARING", fsm.status());
    }

    @Test
    void readyState_RejectsInvalidTransitions() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay(); // NEW -> PREPARING
        fsm.markReady(); // PREPARING -> READY
        assertEquals("READY", fsm.status());

        // cannot pay, prepare, or markReady again
        fsm.pay();
        assertEquals("READY", fsm.status());

        fsm.prepare();
        assertEquals("READY", fsm.status());

        fsm.markReady();
        assertEquals("READY", fsm.status());

        // Cannot cancel after ready
        fsm.cancel();
        assertEquals("READY", fsm.status());
    }

    @Test
    void deliveredState_RejectsAllTransitions() {
        OrderFSM fsm = new OrderFSM();
        // go through happy path to DELIVERED
        fsm.pay();
        fsm.markReady();
        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());

        // no transitions should work from terminal state
        fsm.pay();
        assertEquals("DELIVERED", fsm.status());

        fsm.prepare();
        assertEquals("DELIVERED", fsm.status());

        fsm.markReady();
        assertEquals("DELIVERED", fsm.status());

        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());

        fsm.cancel();
        assertEquals("DELIVERED", fsm.status());
    }

    @Test
    void cancelledState_RejectsAllTransitions() {
        OrderFSM fsm = new OrderFSM();
        fsm.cancel(); // NEW -> CANCELLED
        assertEquals("CANCELLED", fsm.status());

        // no transitions should work from terminal state
        fsm.pay();
        assertEquals("CANCELLED", fsm.status());

        fsm.prepare();
        assertEquals("CANCELLED", fsm.status());

        fsm.markReady();
        assertEquals("CANCELLED", fsm.status());

        fsm.deliver();
        assertEquals("CANCELLED", fsm.status());

        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }

    // ===== EDGE CASE TESTS =====

    @Test
    void orderFSM_MultipleInvalidTransitions_FromNew() {
        OrderFSM fsm = new OrderFSM();

        // attempt at multiple invalid operations
        fsm.prepare();
        fsm.markReady();
        fsm.deliver();
        fsm.prepare();

        // should still be in NEW state
        assertEquals("NEW", fsm.status());

        // valid transition
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
    }

    @Test
    void orderFSM_StateTransitions_AreConsistent() {
        // repeated sequence == repeated results
        OrderFSM fsm1 = new OrderFSM();
        OrderFSM fsm2 = new OrderFSM();

        fsm1.pay();
        fsm1.markReady();
        fsm1.deliver();

        fsm2.pay();
        fsm2.markReady();
        fsm2.deliver();

        assertEquals(fsm1.status(), fsm2.status());
        assertEquals("DELIVERED", fsm1.status());
    }
}
