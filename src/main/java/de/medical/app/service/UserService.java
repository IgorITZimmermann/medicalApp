package de.medical.app.service;

import de.medical.app.model.Patient;
import de.medical.app.model.User;
import de.medical.app.repository.PatientRepository;
import de.medical.app.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PatientRepository patientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String rawPassword, String name, LocalDate birthDate) {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setBirthDate(birthDate);
        patientRepository.save(patient);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("ROLE_USER");
        user.setPatient(patient);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
