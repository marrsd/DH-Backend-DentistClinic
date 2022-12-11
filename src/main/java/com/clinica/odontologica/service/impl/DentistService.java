package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.domain.Dentist;
import com.clinica.odontologica.dto.DentistDTO;
import com.clinica.odontologica.exception.*;
import com.clinica.odontologica.repository.DentistRepository;
import com.clinica.odontologica.service.CRUDService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;

@Service("DentistService")
public class DentistService implements CRUDService<DentistDTO> {

    private static final Logger LOGGER = LogManager.getLogger(DentistService.class);
    private final DentistRepository dentistRepository;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    public DentistService(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    @Override
    public DentistDTO create(DentistDTO dentistDTO) throws DataAlreadyExistsException {
        if(dentistDTO.getDni() < 1 || dentistDTO.getRegistrationNumber() < 1)
            throw new IllegalArgumentException("The dni and the registration number for the dentist must not be negative");

        Dentist dentist = mapper.convertValue(dentistDTO, Dentist.class);

        if(verifyDentistInDB(dentist))
            throw new DataAlreadyExistsException("The dentist with dni: " +dentist.getDni() + " and registration number: " + dentist.getRegistrationNumber() + " already exist!");

        DentistDTO dentistDto = mapper.convertValue(dentistRepository.save(dentist), DentistDTO.class);

        LOGGER.info("Method - Create: Successfully registered dentist "+ dentistDto);
        return dentistDto;
    }

    @Override
    public List<DentistDTO> getAll() throws ResourceNotFoundException {
       List<Dentist> dentists = dentistRepository.findAll(Sort.by(Sort.Direction.DESC, "firstname"));

       if(dentists.isEmpty())
           throw new ResourceNotFoundException("There aren't registered dentists");

       List<DentistDTO> dentistDTOList = new ArrayList<>();

        for (Dentist dentist : dentists) {
            dentistDTOList.add(mapper.convertValue(dentist, DentistDTO.class));
        }

        LOGGER.info("Method - GetAll: List of all dentists: " + dentistDTOList);
        return dentistDTOList;
    }

    @Override
    public DentistDTO getById(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if(id == null)
            throw new IntegrityDataException("Dentist id can't be null!");

        if(id < 1)
            throw new IllegalArgumentException("Dentist id can´t be negative!");

        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The dentist with id: " + id + " was not found!"));

        DentistDTO dentistDTO = mapper.convertValue(dentist, DentistDTO.class);

        LOGGER.info("Method - GetById: Dentist with id: "+ id + ": " + dentistDTO);
        return dentistDTO;
    }

    public DentistDTO getByRegistrationNum(Long registrationNumber) throws IntegrityDataException, NoSuchDataExistsException {
        if(registrationNumber == null)
            throw new IntegrityDataException("Dentist registration number can´t be null");

        if(registrationNumber < 1)
            throw new IllegalArgumentException("Dentist registration number can´t be negative!");

        Dentist dentist = dentistRepository.getByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new NoSuchDataExistsException("The dentist with registration number: " + registrationNumber + " was not found!"));

        DentistDTO dentistDTO = mapper.convertValue(dentist, DentistDTO.class);

        LOGGER.info("Method - GetByRegistrationNumber: Dentist with registration number: "+ registrationNumber + ": " + dentistDTO);
        return dentistDTO;
    }

    public DentistDTO getByFullname(String firstname, String lastname) throws NoSuchDataExistsException, IntegrityDataException {
        if(firstname == null || lastname == null)
            throw new IntegrityDataException("The dentist firstname and lastname must not be null");

        Dentist dentist = dentistRepository.getByFirstnameAndLastname(firstname, lastname)
                .orElseThrow(() -> new NoSuchDataExistsException("The dentist " + firstname + " " + lastname + " was not found!"));

        DentistDTO dentistDTO = mapper.convertValue(dentist, DentistDTO.class);

        LOGGER.info("Method - GetByFullname: Dentist with fullname: "+ firstname + lastname + ": " + dentistDTO);
        return dentistDTO;
    }

    @Override
    public DentistDTO update(DentistDTO dentistDTO) throws IntegrityDataException, NoSuchDataExistsException {
        if(dentistDTO == null)
            throw new IntegrityDataException("The dentist must not be null!");

        if(dentistDTO.getId() == null)
            throw new IntegrityDataException("Dentist id must not be blank or null");

        if(dentistDTO.getId() < 1)
            throw new IllegalArgumentException("Dentist id can´t be negative!");

        Dentist dentistDB = dentistRepository.findById(mapper.convertValue(dentistDTO, Dentist.class).getId())
                .orElseThrow(() -> new NoSuchDataExistsException("The dentist trying to update was not found!"));

        updateValues(dentistDB, dentistDTO);

        DentistDTO dentistUpdated = mapper.convertValue(dentistRepository.save(dentistDB), DentistDTO.class);

        LOGGER.info("Method - Update: Successfully updated dentist: "+dentistUpdated);
        return dentistUpdated;
    }

    @Override
    public void delete(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if(id == null)
            throw new IntegrityDataException("Dentist id can't be null");

        if(id < 1)
            throw new IllegalArgumentException("Dentist id can´t be negative!");

        dentistRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The dentist with id: " + id + " was not found!"));

        LOGGER.info("Method - Delete: Deleted dentist with id: "+ id);
        dentistRepository.deleteById(id);
    }

    public void deleteNroMatricula(Long registrationNumber) throws IntegrityDataException, NoSuchDataExistsException {
        if(registrationNumber == null)
            throw new IntegrityDataException("Dentist registration number can't be null");

        if(registrationNumber < 1)
            throw new IllegalArgumentException("Dentists registration number can´t be negative!");

        dentistRepository.getByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new NoSuchDataExistsException("The dentist with registration number: " + registrationNumber + " was not found!"));

        LOGGER.info("Method - Delete: Deleted dentist with registration number: "+ registrationNumber);
        dentistRepository.deleteByRegistrationNumber(registrationNumber);
    }

    private boolean verifyDentistInDB(Dentist dentist ) {
        return dentistRepository.findAll()
                .stream()
                .anyMatch(d -> d.getDni().equals(dentist.getDni()) || d.getRegistrationNumber().equals(dentist.getRegistrationNumber()));
    }
    private void updateValues(Dentist dentistToUpdate, DentistDTO dentistDTO) {
        Dentist dentist = mapper.convertValue(dentistDTO, Dentist.class);

        dentistToUpdate.setDni(dentist.getDni());
        dentistToUpdate.setFirstname(dentist.getFirstname());
        dentistToUpdate.setLastname(dentist.getLastname());
        dentistToUpdate.setRegistrationNumber(dentist.getRegistrationNumber());
    }
}