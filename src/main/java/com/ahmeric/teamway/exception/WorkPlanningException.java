package com.ahmeric.teamway.exception;

import lombok.Getter;

@Getter
public class WorkPlanningException extends RuntimeException {

  /**
   * The error registry entry associated with the exception.
   */
  private final ErrorRegistry errorRegistry;

  public WorkPlanningException(ErrorRegistry errorRegistry) {
    this.errorRegistry = errorRegistry;
  }
}
