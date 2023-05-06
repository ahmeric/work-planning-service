package com.ahmeric.teamway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDTO {

  /**
   * The ID of the worker.
   */
  private Long id;

  /**
   * The first name of the worker.
   */
  private String firstName;

  /**
   * The last name of the worker.
   */
  private String lastName;
}
