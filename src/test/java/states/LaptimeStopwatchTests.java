package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;
import states.timer.*;

class LaptimeStopwatchTests {

    Context c;

    @BeforeEach
    void setup() {
        c = new Context();
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
    }

    /** Helper: go to stopwatch, start running, tick a few times, then split */
    private void reachLaptime(int ticksBefore) {
        c.left(); // IdleTimer -> ResetStopwatch
        c.up(); // ResetStopwatch -> RunningStopwatch
        for (int i = 0; i < ticksBefore; i++) {
            c.tick(); // increment totalTime
        }
        c.up(); // RunningStopwatch -> LaptimeStopwatch (split)
    }

    @Test
    @DisplayName("LaptimeStopwatch: entry freezes lapTime = totalTime at moment of split")
    void testEntry() {
        reachLaptime(3); // totalTime was 3 at split
        assertSame(LaptimeStopwatch.getInstance(), c.currentState);
        assertEquals(3, AbstractStopwatch.getLapTime());
    }

    @Test
    @DisplayName("LaptimeStopwatch: doIt continues incrementing totalTime")
    void testDoItContinuesTotal() {
        reachLaptime(3);
        int totalBefore = AbstractStopwatch.getTotalTime();
        c.tick();
        assertEquals(totalBefore + 1, AbstractStopwatch.getTotalTime());
        // lapTime stays frozen
        assertEquals(3, AbstractStopwatch.getLapTime());
    }

    @Test
    @DisplayName("LaptimeStopwatch: timeout returns to RunningStopwatch after 5 ticks")
    void testTimeout() {
        reachLaptime(2);
        assertSame(LaptimeStopwatch.getInstance(), c.currentState);
        // tick 5 times to trigger timeout
        for (int i = 0; i < 5; i++) {
            c.tick();
        }
        assertSame(RunningStopwatch.getInstance(), c.currentState);
    }

    @Test
    @DisplayName("LaptimeStopwatch: displayString shows lapTime")
    void testDisplayString() {
        reachLaptime(4);
        assertEquals("lapTime = 4", c.currentState.getDisplayString());
    }

    @Test
    @DisplayName("LaptimeStopwatch: up (unsplit) returns to RunningStopwatch")
    void testUp() {
        reachLaptime(2);
        c.up(); // unsplit
        assertSame(RunningStopwatch.getInstance(), c.currentState);
    }

    @Test
    @DisplayName("LaptimeStopwatch: button labels are change mode/unsplit/(unused)")
    void testButtonLabels() {
        reachLaptime(2);
        assertEquals("change mode", c.currentState.getLeftText());
        assertEquals("unsplit", c.currentState.getUpText());
        assertEquals("(unused)", c.currentState.getRightText());
    }
}
