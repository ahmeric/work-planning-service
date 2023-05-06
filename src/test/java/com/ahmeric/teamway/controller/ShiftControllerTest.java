package com.ahmeric.teamway.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.teamway.entity.ShiftType;
import com.ahmeric.teamway.model.dto.ShiftDTO;
import com.ahmeric.teamway.model.dto.WorkerDTO;
import com.ahmeric.teamway.model.request.CreateShiftRequest;
import com.ahmeric.teamway.model.request.UpdateShiftRequest;
import com.ahmeric.teamway.model.response.ShiftListForWorkerResponse;
import com.ahmeric.teamway.model.response.ShiftListResponse;
import com.ahmeric.teamway.service.ShiftService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ShiftControllerTest {

  @InjectMocks
  private ShiftController shiftController;

  @Mock
  private ShiftService shiftService;

  private ModelMapper modelMapper = new ModelMapper();

  private ShiftDTO shiftDTO1;
  private ShiftDTO shiftDTO2;
  private List<ShiftDTO> shiftDTOList;

  @BeforeEach
  public void setUp() {
    WorkerDTO workerDTO1 = WorkerDTO.builder().id(1L).firstName("John").lastName("Doe").build();
    WorkerDTO workerDTO2 = WorkerDTO.builder().id(2L).firstName("Jane").lastName("Doe").build();

    shiftDTO1 = ShiftDTO.builder()
        .id(1L)
        .worker(workerDTO1)
        .shiftDate(LocalDate.of(2023, 5, 1))
        .shiftType(ShiftType.MORNING)
        .build();
    shiftDTO2 = ShiftDTO.builder()
        .id(2L)
        .worker(workerDTO2)
        .shiftDate(LocalDate.of(2023, 5, 2))
        .shiftType(ShiftType.NIGHT)
        .build();

    shiftDTOList = Arrays.asList(shiftDTO1, shiftDTO2);
  }

  @Test
  public void testCreateShift_GivenValidRequest_ShouldReturnOkStatus() {
    CreateShiftRequest request = new CreateShiftRequest(1L, LocalDate.of(2023, 5, 1),
        ShiftType.MORNING);
    ShiftDTO shiftDTO = modelMapper.map(request, ShiftDTO.class);

    shiftController.createShift(request);

    assertEquals(1L, shiftDTO.getWorker().getId());
    assertEquals(LocalDate.of(2023, 5, 1), shiftDTO.getShiftDate());
    assertEquals(ShiftType.MORNING, shiftDTO.getShiftType());
    verify(shiftService, times(1)).createShift(shiftDTO);
  }

  @Test
  public void testUpdateShift_GivenValidRequest_ShouldReturnOkStatus() {
    CreateShiftRequest request = new CreateShiftRequest(2L, LocalDate.of(2023, 5, 2),
        ShiftType.NIGHT);
    ShiftDTO shiftDTO = modelMapper.map(request, ShiftDTO.class);
    shiftDTO.setId(2L);

    shiftController.updateShift(request, 2L);

    assertEquals(2L, shiftDTO.getId());
    assertEquals(2L, shiftDTO.getWorker().getId());
    assertEquals(LocalDate.of(2023, 5, 2), shiftDTO.getShiftDate());
    assertEquals(ShiftType.NIGHT, shiftDTO.getShiftType());
    verify(shiftService, times(1)).updateShift(shiftDTO);
  }

  @Test
  public void testUpdateWorkerShift_GivenValidRequest_ShouldReturnOkStatus() {
    UpdateShiftRequest request = new UpdateShiftRequest(LocalDate.of(2023, 5, 3), ShiftType.NIGHT);
    ShiftDTO shiftDTO = modelMapper.map(request, ShiftDTO.class);
    shiftDTO.setId(1L);

    shiftController.updateWorkerShift(request, 1L, 1L);

    assertEquals(1L, shiftDTO.getId());
    assertEquals(LocalDate.of(2023, 5, 3), shiftDTO.getShiftDate());
    assertEquals(ShiftType.NIGHT, shiftDTO.getShiftType());
    verify(shiftService, times(1)).updateWorkerShift(shiftDTO, 1L);
  }

  @Test
  public void testGetShifts_WhenCalled_ShouldReturnShiftListResponse() {
    when(shiftService.getShifts()).thenReturn(shiftDTOList);

    ResponseEntity<ShiftListResponse> response = shiftController.getShifts();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().getShifts().size());
    verify(shiftService, times(1)).getShifts();
    assertEquals(1L, response.getBody().getShifts().get(0).getId());
    assertEquals(LocalDate.of(2023, 5, 1), response.getBody().getShifts().get(0).getShiftDate());
    assertEquals(ShiftType.MORNING, response.getBody().getShifts().get(0).getShiftType());

  }

  @Test
  public void testGetShiftByWorkerId_GivenValidWorkerId_ShouldReturnShiftListForWorkerResponse() {
    when(shiftService.getShiftsByWorkerId(1L)).thenReturn(Arrays.asList(shiftDTO1));

    ResponseEntity<ShiftListForWorkerResponse> response = shiftController.getShiftByWorkerId(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1L, response.getBody().getWorker().getId());
    assertEquals("John", response.getBody().getWorker().getFirstName());
    assertEquals("Doe", response.getBody().getWorker().getLastName());
    assertEquals(1, response.getBody().getShifts().size());
    verify(shiftService, times(1)).getShiftsByWorkerId(1L);
  }
}
