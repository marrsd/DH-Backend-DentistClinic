package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.domain.Patient;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.exception.*;
import com.clinica.odontologica.repository.PatientRepository;
import com.clinica.odontologica.service.CRUDService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service("PatientService")
public class PatientService implements CRUDService<PatientDTO> {

    private static final Logger LOGGER = LogManager.getLogger(PatientService.class);
    private final PatientRepository patientRepository;
    @Qualifier("AddressService")
    private final AddressService addressService;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    public PatientService(AddressService addressService, PatientRepository patientRepository) {
        this.addressService = addressService;
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientDTO create(PatientDTO patientDTO) throws DataAlreadyExistsException {
        if(patientDTO.getDni() < 1 )
            throw new IllegalArgumentException("The dni for the patient must not be negative or 0");

        Patient patient = mapper.convertValue(patientDTO, Patient.class);

        if(verifyPatientDB(patient))
            throw new DataAlreadyExistsException("The patient with dni:" + patient.getDni() + " already exist!");

        PatientDTO patientDto = mapper.convertValue(patientRepository.save(patient), PatientDTO.class);

        LOGGER.info("Method - Create: Successfully registered patient: " + patientDto);
        return patientDto;
    }

    @Override
    public List<PatientDTO> getAll() throws ResourceNotFoundException {
        List<Patient> patients = patientRepository.findAll();

        if(patients.isEmpty())
            throw new ResourceNotFoundException("There aren't registered patients");

        List<PatientDTO> patientDTOList = new ArrayList<>();

        for (Patient patient : patients) {
            patientDTOList.add(mapper.convertValue(patient, PatientDTO.class));
        }

        LOGGER.info("Method - GetAll: List of all patients: " + patientDTOList);
        return patientDTOList;
    }

    @Override
    public PatientDTO getById(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if (id == null)
            throw new IntegrityDataException("Patient id cant' be null");
        if (id < 1)
            throw new IllegalArgumentException("Patient id can´t be negative");

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The patient with id: " + id + " was not found"));

        PatientDTO patientDTO = mapper.convertValue(patient, PatientDTO.class);

        LOGGER.info("Method - GetById: Patient with id: "+ id + ": " + patientDTO);
        return patientDTO;
    }

    public PatientDTO getByDni(Long dni) throws IntegrityDataException, NoSuchDataExistsException {
        if (dni == null)
            throw new IntegrityDataException("Patient dni cant' be null");

        if (dni < 1)
            throw new IllegalArgumentException("Patient dni can´t be negative");

        Patient patient = patientRepository.getByDni(dni)
                .orElseThrow(() -> new NoSuchDataExistsException("The patient with dni: " + dni + " was not found"));

        LOGGER.info("Method - GetByDni: Patient with dni: "+ dni);
        return mapper.convertValue(patient, PatientDTO.class);
    }

    @Override
    public PatientDTO update(PatientDTO patientDTO) throws IntegrityDataException, NoSuchDataExistsException {
        if(patientDTO == null)
            throw new IntegrityDataException("The patient must not be null");

        if(patientDTO.getId() == null)
            throw new IntegrityDataException("The patient id must not be blank or null");

        if(patientDTO.getId() < 1)
            throw new IllegalArgumentException("Patient id must not be negative");

        addressService.getById(patientDTO.getAddress().getId());
        Patient patientDB = patientRepository.findById(mapper.convertValue(patientDTO, Patient.class).getId())
                .orElseThrow(() -> new NoSuchDataExistsException("The patient trying to update was not found"));

        updateValues(patientDB, patientDTO);

        PatientDTO patientUpdated = mapper.convertValue(patientRepository.save(patientDB), PatientDTO.class);

        LOGGER.info("Method - Update: Successfully updated patient: "+ patientUpdated);
       return patientUpdated;
    }

    @Override
    public void delete(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if(id == null)
            throw new IntegrityDataException("Patient id cant' be null");

        if(id < 1)
            throw new IllegalArgumentException("Patient id can´t be negative");

        patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The patient with id: " + id + " was not found"));

        LOGGER.info("Method - DeleteById: Deleted patient with id: "+ id);
        patientRepository.deleteById(id);
    }

    public void deleteByDni(Long dni) throws IntegrityDataException, NoSuchDataExistsException {
        if(dni == null)
            throw new IntegrityDataException("Patient dni cant' b null");

        if(dni < 1)
            throw new IllegalArgumentException("Patient dni can't be negative");

       patientRepository.getByDni(dni)
                .orElseThrow(() -> new NoSuchDataExistsException("The patient with dni: " + dni + " was not found"));

        LOGGER.info("Method - DeleteByDni: Deleted patient with dni: "+ dni);
        patientRepository.deleteByDni(dni);
    }


    private boolean verifyPatientDB(Patient patient) {
       return patientRepository.findAll()
                .stream()
                .anyMatch(d -> d.getDni().equals(patient.getDni()));
    }

    private void updateValues(Patient patientToUpdate, PatientDTO patientDTO) {

        Patient patient = mapper.convertValue(patientDTO, Patient.class);

        patientToUpdate.setDni(mapper.convertValue(patientDTO, Patient.class).getDni());
        patientToUpdate.setFirstname(patient.getFirstname());
        patientToUpdate.setLastname(patient.getLastname());
        patientToUpdate.setAddress(patient.getAddress());
    }
}
