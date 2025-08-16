package com.bluepal.repository;

import com.bluepal.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByEmailOrderByAppointmentDateDesc(String email);

    Page<Appointment> findByOrderByAppointmentDateDesc(Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findAppointmentsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status ORDER BY a.appointmentDate DESC")
    List<Appointment> findByStatus(@Param("status") Appointment.Status status);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status")
    Long countByStatus(@Param("status") Appointment.Status status);

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
}
