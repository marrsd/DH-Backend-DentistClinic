package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.model.domain.Dentist;
import com.clinica.odontologica.model.domain.Patient;
import com.clinica.odontologica.model.domain.Turn;
import com.clinica.odontologica.model.domain.auth.User;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.model.dto.DentistDTO;
import com.clinica.odontologica.model.dto.PatientDTO;
import com.clinica.odontologica.model.dto.TurnDTO;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.repository.TurnRepository;
import com.clinica.odontologica.service.CRUDService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TurnService implements CRUDService<TurnDTO> {

    private static final Logger LOGGER = LogManager.getLogger(DentistService.class);

    private final TurnRepository turnRepository;

    @Qualifier("PatientService")
    private final PatientService patientService;

    @Qualifier("DentistService")
    private final DentistService dentistService;

    @Qualifier("UserService")
    private final UserService userService;

    @Autowired
    private final ObjectMapper mapper;

    @Autowired
    public TurnService(TurnRepository turnRepository, PatientService patientService, DentistService dentistService,
            UserService userService) {
        this.turnRepository = turnRepository;
        this.patientService = patientService;
        this.dentistService = dentistService;
        this.userService = userService;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public TurnDTO create(TurnDTO turnDTO)
            throws IntegrityDataException, DataAlreadyExistsException, NoSuchDataExistsException {

        if (turnDTO.getDentist() == null)
            throw new IntegrityDataException("You can't define a dentist null to the turn");

        if (turnDTO.getPatient() == null)
            throw new IntegrityDataException("You can't define a patient null to the turn");

        if (verifyFreeTurn(turnDTO))
            throw new DataAlreadyExistsException("The turn is already reserved, try another one.");

        Patient patient = getUserFromPatient(turnDTO);
        Dentist dentist = getUserFromDentist(turnDTO);

        Turn turn = mapper.convertValue(turnDTO, Turn.class);

        turn.setPatient(patient);
        turn.setDentist(dentist);

        TurnDTO turnDto = mapper.convertValue(turnRepository.save(turn), TurnDTO.class);

        LOGGER.info("Method - Create: Successfully registered turn: " + turnDto);
        return turnDto;
    }

    @Override
    public List<TurnDTO> getAll() throws ResourceNotFoundException {
        List<Turn> turns = turnRepository.findAll(Sort.by(Sort.Direction.DESC, "dateHour"));

        if (turns.isEmpty())
            throw new ResourceNotFoundException("There aren't registered turns");

        List<TurnDTO> turnDTOList = new ArrayList<>();

        for (Turn turn : turns) {
            turnDTOList.add(mapper.convertValue(turn, TurnDTO.class));
        }

        LOGGER.info("Method - GetAll: List of all turns: " + turnDTOList);
        return turnDTOList;
    }

    @Override
    public TurnDTO getById(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if (id == null)
            throw new IntegrityDataException("Turn id cant' be null");

        if (id < 1)
            throw new IllegalArgumentException("Turn id can´t be negative");

        Turn turn = turnRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The turn with id: " + id + " was not found"));

        TurnDTO turnDTO = mapper.convertValue(turn, TurnDTO.class);

        LOGGER.info("Method - GetById: Turn with id: " + id + ": " + turnDTO);
        return turnDTO;
    }

    public List<TurnDTO> getByDentistRegistrationNum(Long registrationNum)
            throws IntegrityDataException, NoSuchDataExistsException {
        if (registrationNum == null)
            throw new IntegrityDataException("You need a dentis's registration number to get turns");

        List<Turn> turns = turnRepository.getByDentistRegistrationNumber(registrationNum)
                .orElseThrow(() -> new NoSuchDataExistsException(
                        "There aren't turn with the dentist with registration number: " + registrationNum));

        List<TurnDTO> turnDTOList = new ArrayList<>();

        for (Turn turn : turns) {
            turnDTOList.add(mapper.convertValue(turn, TurnDTO.class));
        }

        LOGGER.info("Method - GetByDentistRegistrationNumber: Turn with dentist with registration number: "
                + registrationNum + ": " + turnDTOList);
        return turnDTOList;
    }

    public List<TurnDTO> getByPatientDni(Long dni) throws IntegrityDataException, NoSuchDataExistsException {
        if (dni == null)
            throw new IntegrityDataException("You need a patient's dni to get turns");

        List<Turn> turns = turnRepository.getByPatientDni(dni)
                .orElseThrow(() -> new NoSuchDataExistsException("There aren't turn for the patient with dni: " + dni));

        List<TurnDTO> turnDTOList = new ArrayList<>();

        for (Turn turn : turns) {
            turnDTOList.add(mapper.convertValue(turn, TurnDTO.class));
        }

        LOGGER.info("Method - GetByPatientDni: Turn with patient with dni: " + dni + ": " + turnDTOList);
        return turnDTOList;
    }

    @Override
    public TurnDTO update(TurnDTO turnDTO)
            throws IntegrityDataException, NoSuchDataExistsException, DataAlreadyExistsException {
        if (turnDTO == null)
            throw new IntegrityDataException("The turn can´t be null");

        if (turnDTO.getId() == null)
            throw new IntegrityDataException("Turn id cant' be null");

        if (turnDTO.getDentist() == null)
            throw new IntegrityDataException("The dentist can't be null");

        if (turnDTO.getPatient() == null)
            throw new IntegrityDataException("The patient can't be null");

        if (turnDTO.getDateHour() == null)
            throw new IntegrityDataException("You must to define a date for the turn");

        Turn turnDB = turnRepository.findById(mapper.convertValue(turnDTO, Turn.class).getId())
                .orElseThrow(() -> new NoSuchDataExistsException(
                        "You can't update a turn doesn't exist. You must to register it first"));

        if (verifyFreeTurn(turnDTO))
            throw new DataAlreadyExistsException("The turn is already reserved, try another one.");

        updateValues(turnDB, turnDTO);

        TurnDTO turnUpdated = mapper.convertValue(turnRepository.save(turnDB), TurnDTO.class);

        LOGGER.info("Method - Update: Successfully updated patient: " + turnUpdated);
        return turnUpdated;
    }

    @Override
    public void delete(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if (id == null)
            throw new IntegrityDataException("Turn id cant' be null");

        if (id < 1)
            throw new IllegalArgumentException("Turn id can´t be negative");

        turnRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The turn with id: " + id + " was not found"));

        LOGGER.info("Method - Delete: Deleted turn with id: " + id);
        turnRepository.deleteById(id);
    }

    private boolean verifyFreeTurn(TurnDTO turnDto) throws IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentist = dentistService.getById(turnDto.getDentist().getId());

        return turnRepository.findAll()
                .stream()
                .map(turn -> mapper.convertValue(turn, TurnDTO.class))
                .anyMatch(turn -> turn.getDateHour().equals(turnDto.getDateHour())
                        && turn.getDentist().getRegistrationNumber().equals(dentist.getRegistrationNumber()));
    }

    private void updateValues(Turn turnToUpdate, TurnDTO turnDTO)
            throws IntegrityDataException, NoSuchDataExistsException {
        Patient patient = getUserFromPatient(turnDTO);
        Dentist dentist = getUserFromDentist(turnDTO);

        turnToUpdate.setDateHour(mapper.convertValue(turnDTO, Turn.class).getDateHour());
        turnToUpdate.setPatient(patient);
        turnToUpdate.setDentist(dentist);
    }

    private Patient getUserFromPatient(TurnDTO turnDTO) throws IntegrityDataException, NoSuchDataExistsException {
        PatientDTO patientDTO = patientService.getById(turnDTO.getPatient().getId());
        User userPatient = userService.getUserByUsername(patientDTO.getUser().getUsername());
        Patient patient = mapper.convertValue(patientDTO, Patient.class);
        patient.setUser(userPatient);

        return patient;
    }

    private Dentist getUserFromDentist(TurnDTO turnDTO) throws IntegrityDataException, NoSuchDataExistsException {
        DentistDTO dentistDTO = dentistService.getById(turnDTO.getDentist().getId());
        User userDentist = userService.getUserByUsername(dentistDTO.getUser().getUsername());
        Dentist dentist = mapper.convertValue(dentistDTO, Dentist.class);
        dentist.setUser(userDentist);

        return dentist;
    }

}
