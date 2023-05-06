package com.ahmeric.teamway.service;

import com.ahmeric.teamway.entity.Worker;
import com.ahmeric.teamway.model.dto.WorkerDTO;
import com.ahmeric.teamway.repository.WorkerRepository;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {

  @Autowired
  private WorkerRepository workerRepository;
  private ModelMapper modelMapper = new ModelMapper();

  /**
   * Creates a new worker.
   *
   * @param worker the request object containing the worker's name
   */
  public void createWorker(WorkerDTO workerDTO) {
    Worker worker = modelMapper.map(workerDTO, Worker.class);
    workerRepository.save(worker);
  }

  /**
   * Retrieves a list of all workers.
   *
   * @return a list of WorkerResponse objects
   */
  public List<WorkerDTO> getWorkers() {
    return modelMapper.map(workerRepository.findAll(), new TypeToken<List<WorkerDTO>>() {
    }.getType());
  }
}
