package com.ahmeric.teamway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahmeric.teamway.entity.Shift;
import com.ahmeric.teamway.entity.ShiftType;
import com.ahmeric.teamway.entity.Worker;
import com.ahmeric.teamway.exception.ErrorRegistry;
import com.ahmeric.teamway.exception.WorkPlanningException;
import com.ahmeric.teamway.model.dto.ShiftDTO;
import com.ahmeric.teamway.model.dto.WorkerDTO;
import com.ahmeric.teamway.repository.ShiftRepository;
import com.ahmeric.teamway.repository.WorkerRepository;
import com.ahmeric.teamway.utils.MessageUtils;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShiftServiceTest {

  @InjectMocks
  private ShiftService shiftService;

  @Mock
  private ShiftRepository shiftRepository;

  @Mock
  private WorkerRepository workerRepository;

  private ModelMapper modelMapper = new ModelMapper();

  private Worker worker1;
  private Worker worker2;
  private Shift shift1;
  private Shift shift2;
  private ShiftDTO shiftDTO1;
  private ShiftDTO shiftDTO2;

  @BeforeEach
  public void setUp() {
    worker1 = new Worker(1L, "John", "Doe");
    worker2 = new Worker(2L, "Jane", "Doe");
    shift1 = new Shift(1L, worker1, LocalDate.of(2023, 5, 10), ShiftType.MORNING);
    shift2 = new Shift(2L, worker1, LocalDate.of(2023, 5, 11), ShiftType.AFTERNOON);
    shiftDTO1 = new ShiftDTO(1L, LocalDate.of(2023, 5, 10), ShiftType.MORNING,
        new WorkerDTO(1L, "John", "Doe"));
    shiftDTO2 = new ShiftDTO(2L, LocalDate.of(2023, 5, 11), ShiftType.AFTERNOON,
        new WorkerDTO(1L, "John", "Doe"));
    // Mock the MessageUtils.getMessage method
    try (MockedStatic<MessageUtils> utilitiesMock = Mockito.mockStatic(MessageUtils.class)) {
      for (ErrorRegistry errorRegistry : ErrorRegistry.values()) {
        utilitiesMock.when(() -> MessageUtils.getMessage(errorRegistry.getError().getDescription()))
            .thenReturn("Mocked " + errorRegistry.getError().getDescription());
      }
    }
  }

  @Test
  public void testCreateShift_WhenValidInput_ShouldCreateShift() {
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(shiftRepository.findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate())).thenReturn(Optional.empty());
    Shift shiftToSave = shift1;
    shiftToSave.setId(null);
    when(shiftRepository.save(shiftToSave)).thenReturn(shift1);

    shiftService.createShift(shiftDTO1);

    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(1)).findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate());
    verify(shiftRepository, times(1)).save(shift1);
  }

  @Test
  public void testCreateShift_WhenShiftAtSameDateExists_ShouldThrowException() {
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(shiftRepository.findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate())).thenReturn(Optional.of(shift1));

    assertThrows(WorkPlanningException.class, () -> shiftService.createShift(shiftDTO1));

    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(1)).findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate());
  }

  @Test
  public void testCreateShift_WhenWorkerDoesNotExist_ShouldThrowException() {
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(workerRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(WorkPlanningException.class, () -> shiftService.createShift(shiftDTO1));

    verify(workerRepository, times(1)).findById(worker1.getId());
  }

  @Test
  public void testUpdateShift_WhenValidInput_ShouldUpdateShift() {
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(shiftRepository.findById(shift1.getId())).thenReturn(Optional.of(shift1));
    when(shiftRepository.save(shift1)).thenReturn(shift1);

    shiftService.updateShift(shiftDTO1);

    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(1)).findById(shift1.getId());
    verify(shiftRepository, times(0)).findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate());
    verify(shiftRepository, times(1)).save(shift1);
  }

  @Test
  public void testUpdateShift_WhenExistingShiftAtSameDate_ShouldThrowException() {
    shiftDTO1.setShiftDate(shiftDTO1.getShiftDate().plusDays(1));
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(shiftRepository.findById(shift1.getId())).thenReturn(Optional.of(shift1));
    when(shiftRepository.findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate())).thenReturn(Optional.of(shift1));

    assertThrows(WorkPlanningException.class, () -> shiftService.updateShift(shiftDTO1));

    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(1)).findById(shift1.getId());
    verify(shiftRepository, times(1)).findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate());
  }

  @Test
  public void testUpdateShift_WhenWorkerDoesNotExist_ShouldThrowException() {
    when(shiftRepository.findById(shift1.getId())).thenReturn(Optional.of(shift1));
    when(workerRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(WorkPlanningException.class, () -> shiftService.updateShift(shiftDTO1));

    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(0)).findById(shift1.getId());

  }

  @Test
  public void testGetShifts_WhenShiftsExist_ShouldReturnListOfShifts() {
    List<Shift> shifts = Arrays.asList(shift1, shift2);
    when(shiftRepository.findAll()).thenReturn(shifts);

    List<ShiftDTO> result = shiftService.getShifts();

    assertEquals(shifts.size(), result.size());
    verify(shiftRepository, times(1)).findAll();
  }

  @Test
  public void testGetShiftsByWorkerId_WhenWorkerHasShifts_ShouldReturnListOfShifts() {
    List<Shift> shifts = Arrays.asList(shift1, shift2);
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(shiftRepository.findByWorkerId(worker1.getId())).thenReturn(shifts);

    List<ShiftDTO> result = shiftService.getShiftsByWorkerId(worker1.getId());

    assertEquals(shifts.size(), result.size());
    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(1)).findByWorkerId(worker1.getId());
  }

  @Test
  public void testGetShiftsByWorkerId_WhenWorkerDoesNotExist_ShouldThrowException() {
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.empty());

    assertThrows(WorkPlanningException.class,
        () -> shiftService.getShiftsByWorkerId(worker1.getId()));

    verify(workerRepository, times(1)).findById(worker1.getId());
  }


  @Test
  public void testUpdateWorkerShift_WhenValidInput_ShouldUpdateShift() {
    shiftDTO1.setShiftDate(shiftDTO1.getShiftDate().plusDays(1));
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(shiftRepository.findById(shift1.getId())).thenReturn(Optional.of(shift1));
    when(shiftRepository.findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate())).thenReturn(Optional.empty());
    when(shiftRepository.save(shift1)).thenReturn(shift1);

    shiftService.updateWorkerShift(shiftDTO1, worker1.getId());
    shift1.setShiftDate(shift1.getShiftDate().plusDays(1));
    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(1)).findById(shift1.getId());
    verify(shiftRepository, times(1)).findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate());
    verify(shiftRepository, times(1)).save(shift1);
  }

  @Test
  public void testUpdateWorkerShift_WhenExistingShiftAtSameDate_ShouldThrowException() {
    shiftDTO1.setShiftDate(shiftDTO1.getShiftDate().plusDays(1));
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.of(worker1));
    when(shiftRepository.findById(shift1.getId())).thenReturn(Optional.of(shift1));
    when(shiftRepository.findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate())).thenReturn(Optional.of(shift1));

    assertThrows(WorkPlanningException.class,
        () -> shiftService.updateWorkerShift(shiftDTO1, worker1.getId()));

    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(1)).findById(shift1.getId());
    verify(shiftRepository, times(1)).findByWorkerIdAndShiftDate(worker1.getId(),
        shiftDTO1.getShiftDate());
  }

  @Test
  public void testUpdateWorkerShift_WhenWorkerDoesNotExist_ShouldThrowException() {
    when(workerRepository.findById(worker1.getId())).thenReturn(Optional.empty());
    when(shiftRepository.findById(shift1.getId())).thenReturn(Optional.of(shift1));

    assertThrows(WorkPlanningException.class,
        () -> shiftService.updateWorkerShift(shiftDTO1, worker1.getId()));

    verify(workerRepository, times(1)).findById(worker1.getId());
    verify(shiftRepository, times(0)).findById(shift1.getId());
  }

}




