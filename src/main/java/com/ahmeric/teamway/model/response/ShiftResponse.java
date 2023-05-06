package com.ahmeric.teamway.model.response;

import com.ahmeric.teamway.model.dto.WorkerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class ShiftResponse extends ShiftShortResponse {

  /**
   * The worker assigned to the shift.
   */
  private WorkerDTO worker;

}
