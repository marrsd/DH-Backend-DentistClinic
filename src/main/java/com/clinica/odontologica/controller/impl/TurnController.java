package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.controller.CRUDController;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.model.dto.TurnDTO;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.service.impl.TurnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turns")
@Tag(name = "turns-endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TurnController implements CRUDController<TurnDTO> {

        private TurnService turnService;

        @Autowired
        public TurnController(TurnService turnService) {
                this.turnService = turnService;
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Turn created", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TurnDTO.class)) }),
                        @ApiResponse(responseCode = "409", description = "The turn already exists", content = @Content),
                        @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
        })
        @Operation(summary = "Create new turn")
        @PostMapping("/new")
        public ResponseEntity<HttpStatus> register(@RequestBody TurnDTO turn) throws ResourceNotFoundException,
                        IntegrityDataException, NoSuchDataExistsException, DataAlreadyExistsException {
                turnService.create(turn);
                return new ResponseEntity<>(HttpStatus.CREATED);
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of turns", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TurnDTO.class))) }),
                        @ApiResponse(responseCode = "404", description = "There aren't registers", content = @Content)
        })
        @Operation(summary = "List all turns")
        @GetMapping("/all")
        public ResponseEntity<List<TurnDTO>> ListAll() throws ResourceNotFoundException {
                return ResponseEntity.ok(turnService.getAll());
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Turn found", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TurnDTO.class)) }),
                        @ApiResponse(responseCode = "404", description = "Turn wasn't found", content = @Content),
                        @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
        })
        @Operation(summary = "Get turn by id")
        @GetMapping("/id/{id}")
        public ResponseEntity<TurnDTO> searchById(@PathVariable Long id)
                        throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
                return ResponseEntity.ok(turnService.getById(id));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Turn found", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TurnDTO.class))) }),
                        @ApiResponse(responseCode = "404", description = "Turn wasn't found", content = @Content),
                        @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
        })
        @Operation(summary = "Get turn by dentist's registration number")
        @GetMapping("/dentistRegistrationNumber/{registrationNumber}")
        public ResponseEntity<List<TurnDTO>> searchByDentist(@PathVariable Long registrationNumber)
                        throws IntegrityDataException, NoSuchDataExistsException {
                return ResponseEntity.ok(turnService.getByDentistRegistrationNum(registrationNumber));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Turn found", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TurnDTO.class))) }),
                        @ApiResponse(responseCode = "404", description = "Turn wasn't found", content = @Content),
                        @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
        })
        @Operation(summary = "Get turn by patient's dni")
        @GetMapping("/patientDni/{dni}")
        public ResponseEntity<List<TurnDTO>> searchByPatientDni(@PathVariable Long dni)
                        throws IntegrityDataException, NoSuchDataExistsException {
                return ResponseEntity.ok(turnService.getByPatientDni(dni));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Turn found", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TurnDTO.class)) }),
                        @ApiResponse(responseCode = "409", description = "The turn already exists", content = @Content),
                        @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
        })
        @Operation(summary = "Update a turn")
        @PutMapping("/update")
        public ResponseEntity<TurnDTO> modify(@RequestBody TurnDTO turn) throws ResourceNotFoundException,
                        IntegrityDataException, NoSuchDataExistsException, DataAlreadyExistsException {
                return ResponseEntity.ok(turnService.update(turn));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Turn found", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TurnDTO.class)) }),
                        @ApiResponse(responseCode = "404", description = "Turn wasn't found", content = @Content),
                        @ApiResponse(responseCode = "400", description = "The input format is incorrect", content = @Content)
        })
        @Operation(summary = "Delete turn by id")
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> remove(@PathVariable Long id)
                        throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException {
                turnService.delete(id);
                return ResponseEntity.status(HttpStatus.OK).body("Turn deleted");
        }
}
