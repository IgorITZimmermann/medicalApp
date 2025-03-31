package de.medical.app.service;

import de.medical.app.model.Patient;
import de.medical.app.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient findById(Long id) {
        log.info("Finding patient with id: {}", id);
        return patientRepository.findById(id).orElse(null);
    }

    public Patient save(Patient patient) {
        log.info("Saving patient: {}", patient);
        return patientRepository.save(patient);
    }

    public void deleteById(Long id) {
        log.info("Deleting patient with id: {}", id);
    	patientRepository.deleteById(id);
    }

}


