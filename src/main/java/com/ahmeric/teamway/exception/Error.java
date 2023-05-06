package com.ahmeric.teamway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {

  /**
   * The error code.
   */
  private int code;

  /**
   * The error description.
   */
  private String description;
}
