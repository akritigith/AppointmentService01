package com.bluepal.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bluepal.dto.AppointmentDTO;
import com.bluepal.entity.Appointment;
import com.bluepal.service.AppointmentServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppointmentController {

    private final AppointmentServiceImpl appointmentService;

    @Autowired
    public AppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }


    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO created = appointmentService.createAppointment(appointmentDTO);
        return ResponseEntity.ok(created);
    }

    // ✅ Admin: Get all appointments with pagination
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AppointmentDTO>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppointmentDTO> result = appointmentService.getAllAppointments(pageable);
        return ResponseEntity.ok(result);
    }

    // ✅ Authenticated users: Get appointment by ID
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    // ✅ Authenticated users: Get appointments by user email
    @GetMapping("/user/{email}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AppointmentDTO>> getUserAppointments(@PathVariable String email) {
        List<AppointmentDTO> appointments = appointmentService.getUserAppointments(email);
        return ResponseEntity.ok(appointments);
    }

    // ✅ Admin: Update appointment details
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO updated = appointmentService.updateAppointment(id, appointmentDTO);
        return ResponseEntity.ok(updated);
    }

    // ✅ Admin: Approve or Deny appointment based on doctor availability
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        String statusStr = statusUpdate.get("status");
        Appointment.Status status;
        try {
            status = Appointment.Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusStr);
        }

        if (status != Appointment.Status.CONFIRMED && status != Appointment.Status.CANCELLED) {
            throw new IllegalArgumentException("Only CONFIRMED or DENIED statuses are allowed.");
        }

        AppointmentDTO updated = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // ✅ Admin: Delete appointment
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }

    // ✅ Admin: Get appointments between two dates
    @GetMapping("/between")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(appointments);
    }

    // ✅ Admin: Get appointments by status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByStatus(@PathVariable String status) {
        Appointment.Status appointmentStatus = Appointment.Status.valueOf(status.toUpperCase());
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByStatus(appointmentStatus);
        return ResponseEntity.ok(appointments);
    }

    // ✅ Admin: Get appointment stats
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getAppointmentStats() {
        Map<String, Long> stats = Map.of(
                "pending", appointmentService.getAppointmentCountByStatus(Appointment.Status.PENDING),
                "confirmed", appointmentService.getAppointmentCountByStatus(Appointment.Status.CONFIRMED),
                "denied", appointmentService.getAppointmentCountByStatus(Appointment.Status.CANCELLED),
                "completed", appointmentService.getAppointmentCountByStatus(Appointment.Status.COMPLETED),
                "cancelled", appointmentService.getAppointmentCountByStatus(Appointment.Status.CANCELLED)
        );
        return ResponseEntity.ok(stats);
    }

    // ✅ Authenticated users: Get available time slots
    @GetMapping("/available-slots")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<String> slots = appointmentService.getAvailableSlots(date);
        return ResponseEntity.ok(slots);
    }
}
