package com.ahmeric.teamway.controller;

import com.ahmeric.teamway.model.dto.WorkerDTO;
import com.ahmeric.teamway.model.request.CreateWorkerRequest;
import com.ahmeric.teamway.model.response.WorkerListResponse;
import com.ahmeric.teamway.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workers")
@Slf4j
public class WorkerController {

  @Autowired
  private WorkerService workerService;

  private ModelMapper modelMapper = new ModelMapper();

  /**
   * Creates a new worker.
   *
   * @param createWorkerRequest the request object containing the worker's name
   * @return an empty response with an OK status
   */
  @PostMapping
  public ResponseEntity<Void> createWorker(
      @Validated @RequestBody CreateWorkerRequest createWorkerRequest) {
    WorkerDTO workerDTO = modelMapper.map(createWorkerRequest, WorkerDTO.class);
    log.info("Creating new work: {}", createWorkerRequest);
    workerService.createWorker(workerDTO);
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves a list of all workers.
   *
   * @return a list of WorkerResponse objects
   */
  @GetMapping
  public ResponseEntity<WorkerListResponse> getWorkers() {

    WorkerListResponse workerListResponse =
        WorkerListResponse.builder().workers(workerService.getWorkers()).build();

    return ResponseEntity.ok().body(workerListResponse);
  }
}
