package com.ahmeric.teamway.model.response;

import com.ahmeric.teamway.entity.ShiftType;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShiftShortResponse {

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
   * The start date time of the shift.
   */
  private String shiftStartTime;

  /**
   * The end date time of the shift.
   */
  private String shiftEndTime;

  public String getShiftStartTime() {
    return shiftDate.atTime(shiftType.getStartHour(), 0, 0).toString();
  }

  public String getShiftEndTime() {
    return shiftDate.atTime(shiftType.getEndHour(), 0, 0).toString();
  }
}
