package com.ahmeric.teamway.service;

import com.ahmeric.teamway.entity.Shift;
import com.ahmeric.teamway.exception.ErrorRegistry;
import com.ahmeric.teamway.exception.WorkPlanningException;
import com.ahmeric.teamway.model.dto.ShiftDTO;
import com.ahmeric.teamway.repository.ShiftRepository;
import com.ahmeric.teamway.repository.WorkerRepository;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiftService {

  @Autowired
  private ShiftRepository shiftRepository;

  @Autowired
  private WorkerRepository workerRepository;

  private ModelMapper modelMapper = new ModelMapper();

  /**
   * Creates a new shift for a worker.
   *
   * @param shiftDTO the DTO object containing the worker, shift date and shift type
   */
  public void createShift(ShiftDTO shiftDTO) {
    // Validate if worker exists
    checkIfWorkerExist(shiftDTO.getWorker().getId());
    // Validate if the worker has already a shift at the date.
    shiftRepository.findByWorkerIdAndShiftDate(shiftDTO.getWorker().getId(),
            shiftDTO.getShiftDate())
        .ifPresent(data -> {
          throw new WorkPlanningException(ErrorRegistry.WORKER_HAS_SHIFT_AT_SAME_DATE);
        });
    Shift shift = modelMapper.map(shiftDTO, Shift.class);
    shift.setId(null);
    shiftRepository.save(shift);
  }

  /**
   * Updates an existing shift.
   *
   * @param shiftDTO the DTO object containing the id, worker ID, shift date and shift type
   */
  public void updateShift(ShiftDTO shiftDTO) {
    // Validate if worker exists
    checkIfWorkerExist(shiftDTO.getWorker().getId());

    Shift oldShift = shiftRepository.findById(shiftDTO.getId()).get();
    if (!shiftDTO.getWorker().getId().equals(oldShift.getWorker().getId())
        || !shiftDTO.getShiftDate().isEqual(oldShift.getShiftDate())) {
      // Validate if the worker has already a shift at the date.
      shiftRepository.findByWorkerIdAndShiftDate(shiftDTO.getWorker().getId(),
              shiftDTO.getShiftDate())
          .ifPresent(data -> {
            throw new WorkPlanningException(ErrorRegistry.WORKER_HAS_SHIFT_AT_SAME_DATE);
          });
    }
    Shift shift = modelMapper.map(shiftDTO, Shift.class);
    shiftRepository.save(shift);
  }

  /**
   * Retrieves a list of all shifts.
   *
   * @return a list of Shift objects
   */
  public List<ShiftDTO> getShifts() {
    return modelMapper.map(shiftRepository.findAll(), new TypeToken<List<ShiftDTO>>() {
    }.getType());
  }

  /**
   * Retrieves the shifts for a specific worker.
   *
   * @param workerId the ID of the worker
   * @return a list of Shift objects
   */
  public List<ShiftDTO> getShiftsByWorkerId(long workerId) {
    // Validate if worker exists
    checkIfWorkerExist(workerId);
    return modelMapper.map(shiftRepository.findByWorkerId(workerId),
        new TypeToken<List<ShiftDTO>>() {
        }.getType());
  }

  /**
   * Checks if a worker with the specified workerId exists in the system. Throws a
   * WorkPlanningException with an appropriate error message if the worker is not found.
   *
   * @param workerId The ID of the worker to be checked for existence.
   * @throws WorkPlanningException If the worker with the given workerId is not found.
   */
  private void checkIfWorkerExist(long workerId) {
    workerRepository.findById(workerId).orElseThrow(() -> new WorkPlanningException(
        ErrorRegistry.WORKER_NOT_FOUND));
  }

  public void updateWorkerShift(ShiftDTO shiftDTO, Long workerId) {
    // Validate if worker exists
    checkIfWorkerExist(workerId);

    Shift oldShift = shiftRepository.findById(shiftDTO.getId()).get();
    if (!shiftDTO.getShiftDate().isEqual(oldShift.getShiftDate())) {
      // Validate if the worker has already a shift at the date.
      shiftRepository.findByWorkerIdAndShiftDate(workerId, shiftDTO.getShiftDate())
          .ifPresent(data -> {
            throw new WorkPlanningException(ErrorRegistry.WORKER_HAS_SHIFT_AT_SAME_DATE);
          });
    }
    Shift shift = modelMapper.map(shiftDTO, Shift.class);
    shift.setWorker(oldShift.getWorker());
    shiftRepository.save(shift);
  }
}
