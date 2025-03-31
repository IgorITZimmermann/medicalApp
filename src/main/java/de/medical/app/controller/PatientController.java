package de.medical.app.controller;

import de.medical.app.model.Patient;
import de.medical.app.model.User;
import de.medical.app.service.PatientService;
import de.medical.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    public PatientController(PatientService patientService, UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        User currentUser = getCurrentUser();
        if("ROLE_ADMIN".equals(currentUser.getRole())){
            return ResponseEntity.ok(patientService.findAll());
        }
        else {
            return ResponseEntity.ok(currentUser.getPatient());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(Long id){
        User currentUser = getCurrentUser();
        Patient patient = patientService.findById(id);
        if(patient == null){
            return ResponseEntity.notFound().build();
        }
        if("ROLE_ADMIN".equals(currentUser.getRole())){
            return ResponseEntity.ok(patient);
        }
        else {
            return ResponseEntity.ok(patient.equals(currentUser.getPatient()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Patient newData) {
        User currentUser = getCurrentUser();
        Patient existing = patientService.findById(id);

        // Разрешаем, если ADMIN или (USER, но свой пациент)
        if ("ROLE_ADMIN".equals(currentUser.getRole()) ||
                existing.getId().equals(currentUser.getPatient().getId())) {
            existing.setName(newData.getName());
            existing.setBirthDate(newData.getBirthDate());
            patientService.save(existing);
            return ResponseEntity.ok("Patient updated");
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteById(Long id){
        User currentUser = getCurrentUser();
        Patient patient = patientService.findById(id);
        if(patient == null){
            return ResponseEntity.notFound().build();
        }
        if("ROLE_ADMIN".equals(currentUser.getRole())){
            patientService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.status(403).body("You are not allowed to delete this patient");
        }
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }
        return userService.findByUsername(username);
    }

}
