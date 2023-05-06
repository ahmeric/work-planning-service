package com.ahmeric.teamway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ShiftType {
  /**
   * Morning shift from 0 to 8 hours.
   */
  MORNING(0, 8),

  /**
   * Afternoon shift from 8 to 16 hours.
   */
  AFTERNOON(8, 16),

  /**
   * Night shift from 16 to 24 hours.
   */
  NIGHT(16, 24);

  /**
   * The starting hour of the shift.
   */
  private int startHour;

  /**
   * The ending hour of the shift.
   */
  private int endHour;

}

