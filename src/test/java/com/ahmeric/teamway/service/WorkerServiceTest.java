package com.ahmeric.teamway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.teamway.entity.Worker;
import com.ahmeric.teamway.model.dto.WorkerDTO;
import com.ahmeric.teamway.repository.WorkerRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class WorkerServiceTest {

  @InjectMocks
  private WorkerService workerService;

  @Mock
  private WorkerRepository workerRepository;

  private ModelMapper modelMapper = new ModelMapper();

  private Worker worker1;
  private Worker worker2;
  private WorkerDTO workerDTO1;
  private WorkerDTO workerDTO2;

  @BeforeEach
  public void setUp() {
    worker1 = new Worker(1L, "John", "Doe");
    worker2 = new Worker(2L, "Jane", "Doe");
    workerDTO1 = new WorkerDTO(1L, "John", "Doe");
    workerDTO2 = new WorkerDTO(2L, "Jane", "Doe");
  }

  @Test
  public void testCreateWorker_GivenValidWorkerDTO_ShouldSaveNewWorker() {
    when(workerRepository.save(worker1)).thenReturn(worker1);

    workerService.createWorker(workerDTO1);

    verify(workerRepository, times(1)).save(worker1);
  }

  @Test
  public void testGetWorkers_WhenWorkerExists_ShouldReturnListOfWorkerDTOs() {
    when(workerRepository.findAll()).thenReturn(Arrays.asList(worker1, worker2));

    List<WorkerDTO> workerDTOList = workerService.getWorkers();

    assertEquals(2, workerDTOList.size());
    assertEquals(workerDTO1.getId(), workerDTOList.get(0).getId());
    assertEquals(workerDTO1.getFirstName(), workerDTOList.get(0).getFirstName());
    assertEquals(workerDTO1.getLastName(), workerDTOList.get(0).getLastName());
    assertEquals(workerDTO2.getId(), workerDTOList.get(1).getId());
    assertEquals(workerDTO2.getFirstName(), workerDTOList.get(1).getFirstName());
    assertEquals(workerDTO2.getLastName(), workerDTOList.get(1).getLastName());
    verify(workerRepository, times(1)).findAll();
  }

  @Test
  public void testGetWorkers_WhenWorkersEmpty_ShouldReturnEmptyList() {
    when(workerRepository.findAll()).thenReturn(Arrays.asList());

    List<WorkerDTO> workerDTOList = workerService.getWorkers();

    assertEquals(0, workerDTOList.size());
    verify(workerRepository, times(1)).findAll();
  }
}
