package com.bluepal.service;

import com.bluepal.dto.AppointmentDTO;
import com.bluepal.entity.Appointment;
import com.bluepal.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserService userService;

    private static final List<String> ALL_SLOTS = List.of(
        "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00", "12:30",
        "14:00", "14:30", "15:00", "15:30",
        "16:00", "16:30"
    );

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setFirstName(appointmentDTO.getFirstName());
        appointment.setLastName(appointmentDTO.getLastName());
        appointment.setEmail(appointmentDTO.getEmail());
        appointment.setPhone(appointmentDTO.getPhone());
        appointment.setService(appointmentDTO.getService());
        appointment.setNotes(appointmentDTO.getNotes());
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setStatus(Appointment.Status.CONFIRMED);

        userService.findEntityByEmail(appointmentDTO.getEmail())
                   .ifPresent(appointment::setUser);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return new AppointmentDTO(savedAppointment);
    }

    @Override
    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findByOrderByAppointmentDateDesc(pageable)
                .map(AppointmentDTO::new);
    }

    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        return new AppointmentDTO(appointment);
    }

    @Override
    public List<AppointmentDTO> getUserAppointments(String email) {
        return appointmentRepository.findByEmailOrderByAppointmentDateDesc(email)
                .stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        appointment.setFirstName(appointmentDTO.getFirstName());
        appointment.setLastName(appointmentDTO.getLastName());
        appointment.setEmail(appointmentDTO.getEmail());
        appointment.setPhone(appointmentDTO.getPhone());
        appointment.setService(appointmentDTO.getService());
        appointment.setNotes(appointmentDTO.getNotes());
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());

        if (appointmentDTO.getStatus() != null) {
            appointment.setStatus(appointmentDTO.getStatus());
        }

        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return new AppointmentDTO(updatedAppointment);
    }

    @Override
    public AppointmentDTO updateAppointmentStatus(Long id, Appointment.Status status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return new AppointmentDTO(updatedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findAppointmentsBetweenDates(startDate, endDate)
                .stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByStatus(Appointment.Status status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Long getAppointmentCountByStatus(Appointment.Status status) {
        return appointmentRepository.countByStatus(status);
    }

    @Override
    public List<String> getAvailableSlots(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentRepository.findAppointmentsBetweenDates(start, end);

        Set<String> bookedSlots = appointments.stream()
                .map(a -> a.getAppointmentDate().toLocalTime().toString().substring(0, 5)) // HH:mm
                .collect(Collectors.toSet());

        return ALL_SLOTS.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }
}
