package com.ahmeric.teamway.model.response;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShiftListResponse {

  private List<ShiftResponse> shifts;

}
