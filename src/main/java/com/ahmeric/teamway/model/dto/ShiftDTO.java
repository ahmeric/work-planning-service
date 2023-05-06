package com.ahmeric.teamway.model.dto;

import com.ahmeric.teamway.entity.ShiftType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDTO {

  /**
   * The ID of the shift.
   */
  private Long id;

  /**
   * The date of the shift.
   */
  private LocalDate shiftDate;

  /**
   * The type of the shift (MORNING, AFTERNOON, or NIGHT).
   */
  private ShiftType shiftType;

  /**
   * The worker assigned to the shift.
   */
  private WorkerDTO worker;
}
