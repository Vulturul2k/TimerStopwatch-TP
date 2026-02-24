Feature: Timer functionality
  As a user of the chronometer application
  I want to use the timer feature
  So that I can count down from a specified time

  Background:
    Given the chronometer is in its initial state

  Scenario: Initial timer state
    Then the current state should be "IdleTimer"
    And the timer value should be 0
    And the memTimer value should be 0
    And the mode should be "timer"

  Scenario: Setting the timer value
    When I press the right button
    Then the current state should be "SetTimer"
    When I press the up button
    Then the memTimer value should be 5
    When I press the up button
    Then the memTimer value should be 10

  Scenario: SetTimer increments memTimer on tick
    When I press the right button
    And I wait for 1 tick
    Then the memTimer value should be 1
    And I wait for 1 tick
    Then the memTimer value should be 2

  Scenario: Resetting memTimer in SetTimer
    When I press the right button
    And I press the up button
    Then the memTimer value should be 5
    When I press the left button
    Then the memTimer value should be 0
    And the current state should be "SetTimer"

  Scenario: Starting the timer countdown
    When I press the right button
    And I press the up button
    And I press the right button
    And I press the up button
    Then the current state should be "RunningTimer"
    And the timer value should be 5

  Scenario: Timer countdown and ringing
    When I press the right button
    And I press the up button
    And I press the right button
    And I press the up button
    Then the current state should be "RunningTimer"
    When I wait for 5 ticks
    Then the current state should be "RingingTimer"
    And the display should show "Time's up !"
    And ringing should be true

  Scenario: Pausing and resuming the timer
    When I press the right button
    And I press the up button
    And I press the up button
    And I press the right button
    And I press the up button
    Then the current state should be "RunningTimer"
    And the timer value should be 10
    When I press the up button
    Then the current state should be "PausedTimer"
    When I wait for 1 tick
    Then the timer value should be 10
    When I press the up button
    Then the current state should be "RunningTimer"

  Scenario: Stopping the timer
    When I press the right button
    And I press the up button
    And I press the right button
    And I press the up button
    Then the current state should be "RunningTimer"
    When I press the right button
    Then the current state should be "IdleTimer"

  Scenario: Returning to idle after ringing
    When I press the right button
    And I press the up button
    And I press the right button
    And I press the up button
    And I wait for 5 ticks
    Then the current state should be "RingingTimer"
    When I press the right button
    Then the current state should be "IdleTimer"
    And ringing should be false
