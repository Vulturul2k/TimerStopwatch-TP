Feature: Stopwatch functionality
  As a user of the chronometer application
  I want to use the stopwatch feature
  So that I can measure elapsed time and record lap times

  Background:
    Given the chronometer is in its initial state
    When I press the left button
    Then the current state should be "ResetStopwatch"
    And the mode should be "stopwatch"

  Scenario: Initial stopwatch state
    Then the totalTime value should be 0
    And the lapTime value should be 0

  Scenario: Starting the stopwatch
    When I press the up button
    Then the current state should be "RunningStopwatch"
    When I wait for 3 ticks
    Then the totalTime value should be 3

  Scenario: Recording a lap time (split)
    When I press the up button
    And I wait for 3 ticks
    And I press the up button
    Then the current state should be "LaptimeStopwatch"
    And the lapTime value should be 3
    And the display should show "lapTime = 3"

  Scenario: Unsplit returns to running stopwatch
    When I press the up button
    And I wait for 2 ticks
    And I press the up button
    Then the current state should be "LaptimeStopwatch"
    When I press the up button
    Then the current state should be "RunningStopwatch"

  Scenario: Laptime timeout returns to running after 5 ticks
    When I press the up button
    And I wait for 2 ticks
    And I press the up button
    Then the current state should be "LaptimeStopwatch"
    When I wait for 5 ticks
    Then the current state should be "RunningStopwatch"

  Scenario: TotalTime keeps incrementing during laptime display
    When I press the up button
    And I wait for 3 ticks
    And I press the up button
    Then the lapTime value should be 3
    When I wait for 2 ticks
    Then the totalTime value should be 5
    And the lapTime value should be 3

  Scenario: Resetting the stopwatch
    When I press the up button
    And I wait for 3 ticks
    And I press the right button
    Then the current state should be "ResetStopwatch"

  Scenario: Switching between timer and stopwatch preserves history
    When I press the up button
    And I wait for 2 ticks
    Then the current state should be "RunningStopwatch"
    When I press the left button
    Then the mode should be "timer"
    When I press the left button
    Then the current state should be "RunningStopwatch"
    And the mode should be "stopwatch"
