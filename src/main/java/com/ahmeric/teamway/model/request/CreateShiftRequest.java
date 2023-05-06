package com.ahmeric.teamway.model.request;

import com.ahmeric.teamway.entity.ShiftType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateShiftRequest {

  /**
   * The ID of the worker who will be assigned the shift.
   */
  @NotNull(message = "{api.validation.not.null.worker.id}")
  private Long workerId;

  /**
   * The date of the shift.
   */
  @NotNull(message = "{api.validation.not.null.shift.date}")
  private LocalDate shiftDate;

  /**
   * The type of the shift (MORNING, AFTERNOON, or NIGHT).
   */
  @NotNull(message = "{api.validation.not.null.shift.type}")
  private ShiftType shiftType;
}
