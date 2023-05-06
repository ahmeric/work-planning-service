package com.ahmeric.teamway.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.teamway.model.dto.WorkerDTO;
import com.ahmeric.teamway.model.request.CreateWorkerRequest;
import com.ahmeric.teamway.model.response.WorkerListResponse;
import com.ahmeric.teamway.service.WorkerService;
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
public class WorkerControllerTest {

  @InjectMocks
  private WorkerController workerController;

  @Mock
  private WorkerService workerService;

  private ModelMapper modelMapper = new ModelMapper();

  private WorkerDTO workerDTO1;
  private WorkerDTO workerDTO2;
  private List<WorkerDTO> workerDTOList;

  @BeforeEach
  public void setUp() {
    workerDTO1 = WorkerDTO.builder().id(1L).firstName("John").lastName("Doe").build();
    workerDTO2 = WorkerDTO.builder().id(2L).firstName("Jane").lastName("Doe").build();
    workerDTOList = Arrays.asList(workerDTO1, workerDTO2);
  }

  @Test
  public void testCreateWorker_GivenValidRequest_ShouldReturnCreatedStatus() {
    CreateWorkerRequest request = new CreateWorkerRequest("John", "Doe");
    WorkerDTO workerDTO = modelMapper.map(request, WorkerDTO.class);

    workerController.createWorker(request);

    assertEquals("John", workerDTO.getFirstName());
    assertEquals("Doe", workerDTO.getLastName());
    verify(workerService, times(1)).createWorker(workerDTO);
  }

  @Test
  public void testGetWorkers_WhenCalled_ShouldReturnListOfWorkers() {
    when(workerService.getWorkers()).thenReturn(workerDTOList);

    ResponseEntity<WorkerListResponse> response = workerController.getWorkers();
    WorkerListResponse responseBody = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(workerDTOList, responseBody.getWorkers());
    verify(workerService, times(1)).getWorkers();
  }
}
