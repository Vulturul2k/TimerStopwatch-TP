package states;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import states.stopwatch.*;
import states.timer.*;

class TestScenarios {

	Context c;

	// JUnit Setup
	@BeforeEach
	void setupJUnit() {
		setupContext();
	}

	// Cucumber Setup
	@io.cucumber.java.Before
	public void setupCucumber() {
		setupContext();
	}

	private void setupContext() {
		c = new Context();
		// before each test, reset the timer values to avoid interference between tests:
		AbstractTimer.resetInitialValues();
		AbstractStopwatch.resetInitialValues();
	}

	// This is more a kind of integration test than a real unit test
	@Test
	@DisplayName("Complete integration scenario: timer set, run, pause, stopwatch, history, ringing")
	void completeScenario() {
		assertEquals(IdleTimer.getInstance(), c.currentState);
		assertEquals(0, AbstractTimer.getMemTimer());

		c.right(); // start incrementing the memTimer variable
		c.tick();
		assertSame(SetTimer.getInstance(), c.currentState);
		assertEquals(1, AbstractTimer.getMemTimer());
		assertEquals(0, AbstractTimer.getTimer());

		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer());
		assertEquals(0, AbstractTimer.getTimer());

		c.right(); // stop incrementing the memTimer variable
		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer());
		assertEquals(0, AbstractTimer.getTimer());

		c.up(); // start running the timer
		assertEquals(2, AbstractTimer.getTimer(), "value of timer ");
		c.tick();
		assertEquals(2, AbstractTimer.getMemTimer(), "value of memTimer ");
		assertEquals(1, AbstractTimer.getTimer(), "value of timer ");

		c.up(); // pause the timer
		c.tick();
		assertSame(PausedTimer.getInstance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(), "value of memTimer ");
		assertEquals(1, AbstractTimer.getTimer(), "value of timer ");

		c.left(); // go to stopwatch mode
		c.tick();
		assertSame(ResetStopwatch.getInstance(), c.currentState);
		assertEquals(0, AbstractStopwatch.getTotalTime(), "value of totalTime ");
		assertEquals(0, AbstractStopwatch.getLapTime(), "value of lapTime ");

		c.up(); // start running the stopwatch
		c.tick();
		assertSame(RunningStopwatch.getInstance(), c.currentState);
		assertEquals(1, AbstractStopwatch.getTotalTime(), "value of totalTime ");
		assertEquals(0, AbstractStopwatch.getLapTime(), "value of lapTime ");

		c.up(); // record stopwatch laptime
		c.tick();
		assertSame(LaptimeStopwatch.getInstance(), c.currentState);
		assertEquals(2, AbstractStopwatch.getTotalTime(), "value of totalTime ");
		assertEquals(1, AbstractStopwatch.getLapTime(), "value of lapTime ");

		c.left(); // go back to timer mode (remembering history state)
		c.tick();
		assertSame(PausedTimer.getInstance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(), "value of memTimer ");
		assertEquals(1, AbstractTimer.getTimer(), "value of timer ");

		c.up(); // continue running timer
		assertSame(RunningTimer.getInstance(), c.currentState);
		c.tick();
		// automatic switch to ringing timer since timer has reached 0...
		assertSame(RingingTimer.getInstance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(), "value of memTimer ");
		assertEquals(0, AbstractTimer.getTimer(), "value of timer ");

		c.right(); // return to idle timer state
		c.tick();
		assertSame(IdleTimer.getInstance(), c.currentState);
		assertEquals(2, AbstractTimer.getMemTimer(), "value of memTimer ");
		assertEquals(0, AbstractTimer.getTimer(), "value of timer ");
	}

	// --- Cucumber Step Definitions ---

	@io.cucumber.java.en.Given("the chronometer is in its initial state")
	public void the_chronometer_is_in_its_initial_state() {
		assertNotNull(c);
		assertSame(IdleTimer.getInstance(), c.currentState);
	}

	@io.cucumber.java.en.When("I press the left button")
	public void i_press_the_left_button() {
		c.left();
	}

	@io.cucumber.java.en.When("I press the up button")
	public void i_press_the_up_button() {
		c.up();
	}

	@io.cucumber.java.en.When("I press the right button")
	public void i_press_the_right_button() {
		c.right();
	}

	@io.cucumber.java.en.When("I wait for {int} tick(s)")
	public void i_wait_for_ticks(int ticks) {
		for (int i = 0; i < ticks; i++) {
			c.tick();
		}
	}

	@io.cucumber.java.en.Then("the current state should be {string}")
	public void the_current_state_should_be(String expectedState) {
		assertEquals(expectedState, c.currentState.getClass().getSimpleName());
	}

	@io.cucumber.java.en.Then("the mode should be {string}")
	public void the_mode_should_be(String expectedMode) {
		assertEquals(expectedMode, c.currentState.getMode().name());
	}

	@io.cucumber.java.en.Then("the timer value should be {int}")
	public void the_timer_value_should_be(int expected) {
		assertEquals(expected, AbstractTimer.getTimer());
	}

	@io.cucumber.java.en.Then("the memTimer value should be {int}")
	public void the_memtimer_value_should_be(int expected) {
		assertEquals(expected, AbstractTimer.getMemTimer());
	}

	@io.cucumber.java.en.Then("the totalTime value should be {int}")
	public void the_totaltime_value_should_be(int expected) {
		assertEquals(expected, AbstractStopwatch.getTotalTime());
	}

	@io.cucumber.java.en.Then("the lapTime value should be {int}")
	public void the_laptime_value_should_be(int expected) {
		assertEquals(expected, AbstractStopwatch.getLapTime());
	}

	@io.cucumber.java.en.Then("the display should show {string}")
	public void the_display_should_show(String expected) {
		assertEquals(expected, c.currentState.getDisplayString());
	}

	@io.cucumber.java.en.Then("ringing should be true")
	public void ringing_should_be_true() {
		assertTrue(AbstractTimer.isRinging());
	}

	@io.cucumber.java.en.Then("ringing should be false")
	public void ringing_should_be_false() {
		assertFalse(AbstractTimer.isRinging());
	}

}
