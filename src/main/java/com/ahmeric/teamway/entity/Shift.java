package com.ahmeric.teamway.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"worker_id", "shift_date"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shift {

  /**
   * The ID of the shift.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The worker assigned to the shift.
   */
  @ManyToOne
  @JoinColumn(name = "worker_id")
  private Worker worker;

  /**
   * The date of the shift.
   */
  @Column(name = "shift_date")
  private LocalDate shiftDate;

  /**
   * The type of the shift (MORNING, AFTERNOON, or NIGHT).
   */
  @Enumerated(EnumType.STRING)
  private ShiftType shiftType;

}
