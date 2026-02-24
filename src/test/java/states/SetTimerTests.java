package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.timer.*;
import states.stopwatch.*;

class SetTimerTests {

    Context c;

    @BeforeEach
    void setup() {
        c = new Context();
        AbstractTimer.resetInitialValues();
        AbstractStopwatch.resetInitialValues();
        // Navigate to SetTimer state: right() from IdleTimer
        c.right();
    }

    @Test
    @DisplayName("SetTimer: up increments memTimer by 5")
    void testUp() {
        assertEquals(0, AbstractTimer.getMemTimer());
        c.up();
        assertEquals(5, AbstractTimer.getMemTimer());
        c.up();
        assertEquals(10, AbstractTimer.getMemTimer());
        assertSame(SetTimer.getInstance(), c.currentState);
    }

    @Test
    @DisplayName("SetTimer: left resets memTimer to 0")
    void testLeft() {
        c.up(); // memTimer = 5
        c.up(); // memTimer = 10
        assertEquals(10, AbstractTimer.getMemTimer());
        // left() in SetTimer resets memTimer, NOT mode change
        c.currentState = c.currentState.left();
        assertEquals(0, AbstractTimer.getMemTimer());
    }

    @Test
    @DisplayName("SetTimer: right transitions back to IdleTimer")
    void testRight() {
        c.right();
        assertSame(IdleTimer.getInstance(), c.currentState);
    }

    @Test
    @DisplayName("SetTimer: doIt increments memTimer (tick)")
    void testDoIt() {
        assertEquals(0, AbstractTimer.getMemTimer());
        c.tick();
        assertEquals(1, AbstractTimer.getMemTimer());
        c.tick();
        assertEquals(2, AbstractTimer.getMemTimer());
        assertSame(SetTimer.getInstance(), c.currentState);
    }

    @Test
    @DisplayName("SetTimer: displayString shows memTimer value")
    void testDisplayString() {
        assertEquals("memTimer = 0", c.currentState.getDisplayString());
        c.up(); // memTimer = 5
        assertEquals("memTimer = 5", c.currentState.getDisplayString());
    }

    @Test
    @DisplayName("SetTimer: button labels are reset/inc 5/done")
    void testButtonLabels() {
        assertEquals("reset", c.currentState.getLeftText());
        assertEquals("inc 5", c.currentState.getUpText());
        assertEquals("done", c.currentState.getRightText());
    }

    @Test
    @DisplayName("SetTimer: mode is timer")
    void testMode() {
        assertEquals(Mode.timer, c.currentState.getMode());
    }
}
