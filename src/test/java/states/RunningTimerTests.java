package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.timer.*;
import states.stopwatch.*;

class RunningTimerTests {

    Context c;

    @BeforeEach
    void setup() {
        c = new Context();
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
    }

    /**
     * Helper to set a timer value and start running:
     * right -> SetTimer, up N times (each +5), right -> IdleTimer, up ->
     * RunningTimer
     */
    private void setTimerAndStart(int increments) {
        c.right(); // SetTimer
        for (int i = 0; i < increments; i++) {
            c.up(); // each adds 5 to memTimer
        }
        c.right(); // back to IdleTimer (memTimer is set)
        c.up(); // IdleTimer -> RunningTimer (timer = memTimer)
    }

    @Test
    @DisplayName("RunningTimer: doIt decrements timer each tick")
    void testDoItDecrement() {
        setTimerAndStart(1); // memTimer = 5, timer starts at 5
        assertSame(RunningTimer.getInstance(), c.currentState);
        assertEquals(5, AbstractTimer.getTimer());
        c.tick();
        assertEquals(4, AbstractTimer.getTimer());
        c.tick();
        assertEquals(3, AbstractTimer.getTimer());
    }

    @Test
    @DisplayName("RunningTimer: transitions to RingingTimer when timer reaches 0")
    void testTransitionToRinging() {
        setTimerAndStart(1); // timer = 5
        // tick 5 times to reach 0
        for (int i = 0; i < 5; i++) {
            c.tick();
        }
        assertSame(RingingTimer.getInstance(), c.currentState);
        assertTrue(AbstractTimer.isRinging());
    }

    @Test
    @DisplayName("RunningTimer: displayString shows timer value")
    void testDisplayString() {
        setTimerAndStart(1); // timer = 5
        assertEquals("timer = 5", c.currentState.getDisplayString());
        c.tick();
        assertEquals("timer = 4", c.currentState.getDisplayString());
    }

    @Test
    @DisplayName("RunningTimer: up transitions to PausedTimer")
    void testUpToPaused() {
        setTimerAndStart(1);
        c.up();
        assertSame(PausedTimer.getInstance(), c.currentState);
    }

    @Test
    @DisplayName("RunningTimer: right transitions to IdleTimer (stop)")
    void testRightToIdle() {
        setTimerAndStart(1);
        c.right();
        assertSame(IdleTimer.getInstance(), c.currentState);
    }

    @Test
    @DisplayName("RunningTimer: button labels are change mode/pause/stop")
    void testButtonLabels() {
        setTimerAndStart(1);
        assertEquals("change mode", c.currentState.getLeftText());
        assertEquals("pause", c.currentState.getUpText());
        assertEquals("stop", c.currentState.getRightText());
    }
}
