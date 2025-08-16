package com.bluepal.dto;

import com.bluepal.entity.Appointment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private Long id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotBlank(message = "Service is required")
    private String service;
    
    private String notes;
    
    @NotNull(message = "Appointment date is required")
    private LocalDateTime appointmentDate;
    
    private Appointment.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public AppointmentDTO() {}

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.firstName = appointment.getFirstName();
        this.lastName = appointment.getLastName();
        this.email = appointment.getEmail();
        this.phone = appointment.getPhone();
        this.service = appointment.getService();
        this.notes = appointment.getNotes();
        this.appointmentDate = appointment.getAppointmentDate();
        this.status = appointment.getStatus();
        this.createdAt = appointment.getCreatedAt();
        this.updatedAt = appointment.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public Appointment.Status getStatus() { return status; }
    public void setStatus(Appointment.Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() {
    	return createdAt; 
    	}
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
