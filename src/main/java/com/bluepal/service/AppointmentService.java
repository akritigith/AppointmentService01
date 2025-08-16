package com.bluepal.service;

import com.bluepal.dto.AppointmentDTO;
import com.bluepal.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);

    Page<AppointmentDTO> getAllAppointments(Pageable pageable);

    AppointmentDTO getAppointmentById(Long id);

    List<AppointmentDTO> getUserAppointments(String email);

    AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);

    AppointmentDTO updateAppointmentStatus(Long id, Appointment.Status status);

    void deleteAppointment(Long id);

    List<AppointmentDTO> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<AppointmentDTO> getAppointmentsByStatus(Appointment.Status status);

    Long getAppointmentCountByStatus(Appointment.Status status);

    List<String> getAvailableSlots(LocalDate date);
}
