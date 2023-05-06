package com.ahmeric.teamway.controller;

import com.ahmeric.teamway.model.dto.ShiftDTO;
import com.ahmeric.teamway.model.dto.WorkerDTO;
import com.ahmeric.teamway.model.request.CreateShiftRequest;
import com.ahmeric.teamway.model.request.UpdateShiftRequest;
import com.ahmeric.teamway.model.response.ShiftListForWorkerResponse;
import com.ahmeric.teamway.model.response.ShiftListResponse;
import com.ahmeric.teamway.model.response.ShiftResponse;
import com.ahmeric.teamway.model.response.ShiftShortResponse;
import com.ahmeric.teamway.service.ShiftService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shifts")
@Slf4j
public class ShiftController {

  @Autowired
  private ShiftService shiftService;
  private ModelMapper modelMapper = new ModelMapper();

  /**
   * Creates a new shift for a worker.
   *
   * @param createShiftRequest the request object containing the worker ID, shift date and shift
   *                           type
   * @return an empty response with an OK status
   */
  @PostMapping
  public ResponseEntity<Void> createShift(
      @Valid @RequestBody CreateShiftRequest createShiftRequest) {
    ShiftDTO shiftDTO = modelMapper.map(createShiftRequest, ShiftDTO.class);
    log.info("Creating new shift: {}", createShiftRequest);
    shiftService.createShift(shiftDTO);
    return ResponseEntity.ok().build();
  }

  /**
   * Updates an existing shift.
   *
   * @param createShiftRequest the request object containing the worker ID, shift date and shift
   *                           type
   * @param id                 the ID of the shift to update
   * @return an empty response with an OK status
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<Void> updateShift(@Valid
  @RequestBody CreateShiftRequest createShiftRequest,
      @PathVariable Long id) {
    ShiftDTO shiftDTO = modelMapper.map(createShiftRequest, ShiftDTO.class);
    shiftDTO.setId(id);
    log.info("Updating the shift: {}", createShiftRequest);
    shiftService.updateShift(shiftDTO);
    return ResponseEntity.ok().build();
  }

  /**
   * Updates an existing worker shift.
   *
   * @param updateShiftRequest the request object containing the worker ID, shift date and shift
   *                           type
   * @param id                 the ID of the shift to update
   * @return an empty response with an OK status
   */
  @PatchMapping(path = "/{id}")
  public ResponseEntity<Void> updateWorkerShift(@Valid
  @RequestBody UpdateShiftRequest updateShiftRequest,
      @PathVariable Long id, @RequestParam Long workerId) {
    ShiftDTO shiftDTO = modelMapper.map(updateShiftRequest, ShiftDTO.class);
    shiftDTO.setId(id);
    log.info("Updating the shift: {} for worker: {}", updateShiftRequest, workerId);
    shiftService.updateWorkerShift(shiftDTO, workerId);
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves a list of all shifts.
   *
   * @return a list of ShiftResponse objects
   */
  @GetMapping
  public ResponseEntity<ShiftListResponse> getShifts() {

    List<ShiftResponse> shiftResponses = modelMapper.map(shiftService.getShifts(),
        new TypeToken<List<ShiftResponse>>() {
        }.getType());
    ShiftListResponse response = new ShiftListResponse();
    response.setShifts(shiftResponses);
    return ResponseEntity.ok().body(response);
  }

  /**
   * Retrieves the shifts for a specific worker.
   *
   * @param workerId the ID of the worker
   * @return a ShiftListResponse object containing the worker and their shifts
   */
  @GetMapping(path = "/workers/{workerId}")
  public ResponseEntity<ShiftListForWorkerResponse> getShiftByWorkerId(
      @PathVariable Long workerId) {
    ShiftListForWorkerResponse response = new ShiftListForWorkerResponse();
    List<ShiftDTO> shifts = shiftService.getShiftsByWorkerId(workerId);
    List<ShiftShortResponse> shiftResponses = modelMapper.map(shifts,
        new TypeToken<List<ShiftShortResponse>>() {
        }.getType());
    if (!shifts.isEmpty()) {
      response.setWorker(modelMapper.map(shifts.get(0).getWorker(), WorkerDTO.class));
    }
    response.setShifts(shiftResponses);
    return ResponseEntity.ok().body(response);
  }

}
