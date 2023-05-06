package com.ahmeric.teamway.model.response;

import com.ahmeric.teamway.model.dto.WorkerDTO;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class ShiftListForWorkerResponse {

  /**
   * The worker for whom the list of shifts is retrieved.
   */
  private WorkerDTO worker;

  /**
   * The list of shifts for the worker.
   */
  private List<ShiftShortResponse> shifts;

}
