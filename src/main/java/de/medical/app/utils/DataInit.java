package de.medical.app.utils;

import de.medical.app.model.Patient;
import de.medical.app.model.User;
import de.medical.app.repository.PatientRepository;
import de.medical.app.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInit {

    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public DataInit(UserRepository userRepository, PatientRepository patientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if(userRepository.findByUsername("admin") == null){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }

        if(userRepository.findByUsername("user1") == null){
           Patient patient = new Patient();
           patient.setName("Max Mustermann");
           patient.setBirthDate(java.time.LocalDate.of(1990, 1, 1));
           patientRepository.save(patient);

           User user = new User();
            user.setUsername("user1");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            user.setPatient(patient);
            userRepository.save(user);
        }


    }

}
