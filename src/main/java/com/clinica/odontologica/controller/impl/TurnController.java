package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.controller.CRUDController;
import com.clinica.odontologica.dto.TurnDTO;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.service.impl.TurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/turns")
public class TurnController implements CRUDController<TurnDTO> {

    private TurnService turnService;
    @Autowired
    public TurnController(TurnService turnService) {
        this.turnService = turnService;
    }


    @PostMapping("/new")
    public ResponseEntity<TurnDTO> register(@RequestBody TurnDTO turn) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException, DataAlreadyExistsException {
       return ResponseEntity.ok(turnService.create(turn));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TurnDTO>> ListAll() throws ResourceNotFoundException {
        return ResponseEntity.ok(turnService.getAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<TurnDTO> searchById(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(turnService.getById(id));
    }

    @GetMapping("/dentistRegistrationNumber/{registrationNumber}")
    public ResponseEntity<TurnDTO> searchByDentist(@PathVariable Long registrationNumber) throws IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(turnService.getByDentistRegistrationNum(registrationNumber));
    }

    @GetMapping("/patientDni/{dni}")
    public ResponseEntity<TurnDTO> searchByPatientDni(@PathVariable Long dni) throws IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(turnService.getByPatientDni(dni));
    }

    @PutMapping("/update")
    public ResponseEntity<TurnDTO> modify(@RequestBody TurnDTO turn) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
         return ResponseEntity.ok(turnService.update(turn));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        turnService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Turn deleted");
    }
}
