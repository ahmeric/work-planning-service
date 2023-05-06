package com.ahmeric.teamway.exception;

import com.ahmeric.teamway.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorRegistry {


  WORKER_HAS_SHIFT_AT_SAME_DATE(2001,
      "api.error.worker.has.shift.at.same.date",
      HttpStatus.BAD_REQUEST),
  WORKER_NOT_FOUND(2002,
      "api.error.worker.not.found",
      HttpStatus.NOT_FOUND);

  /**
   * The error associated with the registry entry.
   */
  private Error error;

  /**
   * The HTTP status associated with the registry entry.
   */
  private HttpStatus status;

  ErrorRegistry(int code, String messageKey, HttpStatus status) {
    this.error = new Error(code, MessageUtils.getMessage(messageKey));
    this.status = status;
  }
}
