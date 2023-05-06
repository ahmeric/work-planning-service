package com.ahmeric.teamway.model.response;

import com.ahmeric.teamway.model.dto.WorkerDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerListResponse {

  private List<WorkerDTO> workers;

}
