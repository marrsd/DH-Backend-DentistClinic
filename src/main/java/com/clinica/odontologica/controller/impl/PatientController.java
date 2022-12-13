package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.controller.CRUDController;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.service.impl.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController implements CRUDController<PatientDTO> {
    private PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "409", description = "The patient already exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
    })
    @Operation(summary = "Create new patient")
    @PostMapping("/new")
    public ResponseEntity<PatientDTO> register(@RequestBody PatientDTO patient) throws IntegrityDataException, DataAlreadyExistsException {
        return ResponseEntity.ok(patientService.create(patient));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of patients", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PatientDTO.class)) )}),
            @ApiResponse(responseCode = "404", description = "There aren't registers", content = @Content)
    })
    @Operation(summary = "List all patients")
    @GetMapping("/all")
    public ResponseEntity<List<PatientDTO>> ListAll() throws ResourceNotFoundException {
        return ResponseEntity.ok(patientService.getAll());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Get patient by id")
    @GetMapping("/id/{id}")
    public ResponseEntity<PatientDTO> searchById(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Get patient by dni")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<PatientDTO> searchByDni(@PathVariable Long dni) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(patientService.getByDni(dni));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Updatea a patient")
    @PutMapping("/update")
    public ResponseEntity<PatientDTO> modify(@RequestBody PatientDTO patient) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        return ResponseEntity.ok(patientService.update(patient));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient deleted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Delete patient by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        patientService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Patient with id: " + id + " deleted");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient deleted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Patient wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Delete patient by dni")
    @DeleteMapping("/delete/dni/{dni}")
    public ResponseEntity<String> removeByDni(@PathVariable Long dni) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        patientService.deleteByDni(dni);
        return ResponseEntity.status(HttpStatus.OK).body("Patient with dni: " + dni + " deleted");
    }
}
