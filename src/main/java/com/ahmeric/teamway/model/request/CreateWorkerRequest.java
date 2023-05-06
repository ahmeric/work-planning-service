package com.ahmeric.teamway.model.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateWorkerRequest {

  /**
   * The first name of the worker.
   */
  @NotNull(message = "{api.validation.not.null.first.name}")
  private String firstName;

  /**
   * The last name of the worker.
   */
  @NotNull(message = "{api.validation.not.null.last.name}")
  private String lastName;
}
