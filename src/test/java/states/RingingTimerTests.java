package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.timer.*;
import states.stopwatch.*;

class RingingTimerTests {

    Context c;

    @BeforeEach
    void setup() {
        c = new Context();
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
    }

    /** Helper to reach RingingTimer: set timer to 5, run 5 ticks */
    private void reachRinging() {
        c.right(); // SetTimer
        c.up(); // memTimer = 5
        c.right(); // IdleTimer
        c.up(); // RunningTimer (timer=5)
        for (int i = 0; i < 5; i++) {
            c.tick(); // countdown to 0
        }
    }

    @Test
    @DisplayName("RingingTimer: doIt stays in RingingTimer (beeps)")
    void testDoIt() {
        reachRinging();
        assertSame(RingingTimer.Instance(), c.currentState);
        c.tick(); // doIt: beep, stay in RingingTimer
        assertSame(RingingTimer.Instance(), c.currentState);
    }

    @Test
    @DisplayName("RingingTimer: displayString shows Time's up !")
    void testDisplayString() {
        reachRinging();
        assertEquals("Time's up !", c.currentState.getDisplayString());
    }

    @Test
    @DisplayName("RingingTimer: isRinging is true")
    void testIsRinging() {
        reachRinging();
        assertTrue(AbstractTimer.isRinging());
    }

    @Test
    @DisplayName("RingingTimer: left (change mode) exits ringing")
    void testLeftExitsRinging() {
        reachRinging();
        assertTrue(AbstractTimer.isRinging());
        c.left(); // change mode to stopwatch
        assertFalse(AbstractTimer.isRinging());
    }
}
