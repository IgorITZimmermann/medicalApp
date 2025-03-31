package de.medical.app.controller;

import de.medical.app.model.Appointment;
import de.medical.app.model.User;
import de.medical.app.repository.AppointmentRepository;
import de.medical.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;

    private final UserService userService;

    public AppointmentController(AppointmentRepository appointmentRepository, UserService userService) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        User currentUser = getCurrentUser();
        if ("ROLE_ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.ok(appointmentRepository.findAll());
        } else {
            Long patientId = currentUser.getPatient().getId();
            List<Appointment> appointments = appointmentRepository.findAll().stream()
                    .filter(appointment -> appointment.getPatient().getId().equals(patientId))
                    .toList();
            return ResponseEntity.ok(appointments);
        }
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment request) {
        User currentUser = getCurrentUser();
        if ("ROLE_ADMIN".equals(currentUser.getRole())) {
            request.setPatient(currentUser.getPatient());
        }
        appointmentRepository.save(request);
        return ResponseEntity.ok("Appointment created");
    }


    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }
        return userService.findByUsername(username);
    }
}
