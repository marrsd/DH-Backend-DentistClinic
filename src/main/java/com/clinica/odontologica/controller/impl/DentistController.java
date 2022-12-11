package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.controller.CRUDController;
import com.clinica.odontologica.dto.DentistDTO;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.service.impl.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dentists")
public class DentistController implements CRUDController<DentistDTO> {
    private DentistService dentistService;
    @Autowired
    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }


    @PostMapping("/new")
    public ResponseEntity<DentistDTO> register(@RequestBody DentistDTO dentist) throws IntegrityDataException, DataAlreadyExistsException {
        return ResponseEntity.ok(dentistService.create(dentist));
    }

    @GetMapping("/all")
    public ResponseEntity<List<DentistDTO>> ListAll() throws ResourceNotFoundException {
        return ResponseEntity.ok(dentistService.getAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DentistDTO> searchById(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentisDTO = dentistService.getById(id);
        return ResponseEntity.ok(dentisDTO);
    }

    @GetMapping("/registrationNumber/{registrationNumber}")
    public ResponseEntity<DentistDTO> searchByRegistrationNumber(@PathVariable Long registrationNumber) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentisDTO = dentistService.getByRegistrationNum(registrationNumber);
        return ResponseEntity.ok(dentisDTO);
    }

    @GetMapping("/fullName/{firstname}/{lastname}")
    public ResponseEntity<DentistDTO> searchByFullname(@PathVariable String firstname, @PathVariable String lastname) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentisDTO = dentistService.getByFullname(firstname, lastname);
        return ResponseEntity.ok(dentisDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<DentistDTO> modify(@RequestBody DentistDTO dentist) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
            return ResponseEntity.ok(dentistService.update(dentist));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
           dentistService.delete(id);
           return ResponseEntity.status(HttpStatus.OK).body("Dentist with id: " + id + " deleted");
    }

    @DeleteMapping("/delete/registrationNumber/{registrationNumber}")
    public ResponseEntity<String> removeNroMatricula(@PathVariable Long registrationNumber) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        dentistService.deleteNroMatricula(registrationNumber);
        return ResponseEntity.status(HttpStatus.OK).body("Dentist with registration number: " + registrationNumber + " deleted");
    }
}
