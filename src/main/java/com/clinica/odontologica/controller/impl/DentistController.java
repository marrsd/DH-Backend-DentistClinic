package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.controller.CRUDController;
import com.clinica.odontologica.dto.DentistDTO;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.service.impl.DentistService;

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
@RequestMapping("/dentists")
public class DentistController implements CRUDController<DentistDTO> {
    private DentistService dentistService;
    @Autowired
    public DentistController(DentistService dentistService) {
        this.dentistService = dentistService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dentist created", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DentistDTO.class)) }),
            @ApiResponse(responseCode = "409", description = "The dentist already exists", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
    })
    @Operation(summary = "Create new dentist")
    @PostMapping("/new")
    public ResponseEntity<DentistDTO> register(@RequestBody DentistDTO dentist) throws IntegrityDataException, DataAlreadyExistsException {
        return ResponseEntity.ok(dentistService.create(dentist));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of dentists", content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DentistDTO.class)) )}),
            @ApiResponse(responseCode = "404", description = "There aren't registers", content = @Content)
    })
    @Operation(summary = "List all dentists")
    @GetMapping("/all")
    public ResponseEntity<List<DentistDTO>> ListAll() throws ResourceNotFoundException {
        return ResponseEntity.ok(dentistService.getAll());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dentist found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DentistDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Dentist wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Get dentist by id")
    @GetMapping("/id/{id}")
    public ResponseEntity<DentistDTO> searchById(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentisDTO = dentistService.getById(id);
        return ResponseEntity.ok(dentisDTO);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dentist found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DentistDTO.class) )}),
            @ApiResponse(responseCode = "404", description = "Dentist wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Get dentist by registration number")
    @GetMapping("/registrationNumber/{registrationNumber}")
    public ResponseEntity<DentistDTO> searchByRegistrationNumber(@PathVariable Long registrationNumber) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentisDTO = dentistService.getByRegistrationNum(registrationNumber);
        return ResponseEntity.ok(dentisDTO);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dentist found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DentistDTO.class) )}),
            @ApiResponse(responseCode = "404", description = "Dentist wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Get dentist by firstname and lastname")
    @GetMapping("/fullName/{firstname}/{lastname}")
    public ResponseEntity<DentistDTO> searchByFullname(@PathVariable String firstname, @PathVariable String lastname) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentisDTO = dentistService.getByFullname(firstname, lastname);
        return ResponseEntity.ok(dentisDTO);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dentist found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DentistDTO.class) )}),
            @ApiResponse(responseCode = "404", description = "Dentist wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Update a dentist")
    @PutMapping("/update")
    public ResponseEntity<DentistDTO> modify(@RequestBody DentistDTO dentist) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
            return ResponseEntity.ok(dentistService.update(dentist));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dentist deleted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DentistDTO.class) )}),
            @ApiResponse(responseCode = "404", description = "Dentist wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Delete a dentist by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
           dentistService.delete(id);
           return ResponseEntity.status(HttpStatus.OK).body("Dentist with id: " + id + " deleted");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dentist deleted", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = DentistDTO.class) )}),
            @ApiResponse(responseCode = "404", description = "Dentist wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content )
    })
    @Operation(summary = "Delete a dentist by registration number")
    @DeleteMapping("/delete/registrationNumber/{registrationNumber}")
    public ResponseEntity<String> removeNroMatricula(@PathVariable Long registrationNumber) throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
        dentistService.deleteByRegistrationNumber(registrationNumber);
        return ResponseEntity.status(HttpStatus.OK).body("Dentist with registration number: " + registrationNumber + " deleted");
    }
}
