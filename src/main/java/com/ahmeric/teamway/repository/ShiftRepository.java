package com.ahmeric.teamway.repository;

import com.ahmeric.teamway.entity.Shift;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

  // Find shifts by worker ID
  List<Shift> findByWorkerId(Long workerId);

  // Find a shift by worker ID and date
  Optional<Shift> findByWorkerIdAndShiftDate(Long workerId, LocalDate date);

}
