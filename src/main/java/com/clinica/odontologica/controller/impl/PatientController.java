package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.controller.CRUDController;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.service.impl.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController implements CRUDController<PatientDTO> {
    private PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @PostMapping("/new")
    public ResponseEntity<PatientDTO> register(@RequestBody PatientDTO patient) throws IntegrityDataException, DataAlreadyExistsException {
        return ResponseEntity.ok(patientService.create(patient));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientDTO>> ListAll() throws ResourceNotFoundException {
        return ResponseEntity.ok(patientService.getAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PatientDTO> searchById(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PatientDTO> searchByDni(@PathVariable Long dni) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(patientService.getByDni(dni));
    }

    @PutMapping("/update")
    public ResponseEntity<PatientDTO> modify(@RequestBody PatientDTO patient) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(patientService.update(patient));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        patientService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Patient with id: " + id + " deleted");
    }

    @DeleteMapping("/delete/dni/{dni}")
    public ResponseEntity<String> removeByDni(@PathVariable Long dni) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        patientService.deleteByDni(dni);
        return ResponseEntity.status(HttpStatus.OK).body("Patient with dni: " + dni + " deleted");
    }
}
