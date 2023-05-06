package com.ahmeric.teamway.model.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

  /**
   * The timestamp when the error occurred.
   */
  private Date timestamp;

  /**
   * The error message describing the issue.
   */
  private String message;
}
