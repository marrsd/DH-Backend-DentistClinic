package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.domain.Address;
import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.repository.AddressRepository;
import com.clinica.odontologica.service.CRUDService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("AdressService")
public class AddressService implements CRUDService<AddressDTO> {

    private static final Logger LOGGER = LogManager.getLogger(AddressService.class);
    private final AddressRepository addressRepository;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public AddressDTO create(AddressDTO addressDTO) throws IntegrityDataException {

        Address address = mapper.convertValue(addressDTO, Address.class);

        AddressDTO addressDto = mapper.convertValue(addressRepository.save(address), AddressDTO.class);

        LOGGER.info("Method - Create: Successfully registered address: " + addressDto);
        return addressDto;
    }

    @Override
    public List<AddressDTO> getAll() throws ResourceNotFoundException {
        List<Address> addresses = addressRepository.findAll();

        if(addresses.isEmpty())
            throw new ResourceNotFoundException("There aren't registered addresses");

        List<AddressDTO> addressDTOList = new ArrayList<>();

        for (Address address : addresses) {
            addressDTOList.add(mapper.convertValue(address, AddressDTO.class));
        }

        LOGGER.info("Method - GetAll: List of all addresses: " + addressDTOList);
        return addressDTOList;
    }

    @Override
    public AddressDTO getById(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if(id == null)
            throw new IntegrityDataException("Address id cant' be null");

        if(id < 1)
            throw new IllegalArgumentException("Address id can´t be negative");

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The address with id: " + id + " was not found"));

        AddressDTO addressDTO = mapper.convertValue(address, AddressDTO.class);

        LOGGER.info("Method - GetById: Address with id: "+ id + ": " + addressDTO);
        return addressDTO;
    }

    public AddressDTO getByStreetNumber(String street, Integer number) throws IntegrityDataException, NoSuchDataExistsException {
        if(street == null && number == null)
            throw new IntegrityDataException("The street and the number can't be null");

        Address address = addressRepository.getByStreetAndNumber(street, number)
                .orElseThrow(() -> new NoSuchDataExistsException("The address " + street + number + " was not found"));

        AddressDTO addressDTO = mapper.convertValue(address, AddressDTO.class);

        LOGGER.info("Method - GetByStreetAndNumber: Address with street" + street + " and numbber: "+ number +" " + address);
        return addressDTO;
    }

    @Override
    public AddressDTO update(AddressDTO addressDTO) throws IntegrityDataException, NoSuchDataExistsException {
        if(addressDTO == null)
            throw new IntegrityDataException("The address must not be null");

        if(addressDTO.getNumber() < 1)
            throw new IllegalArgumentException("The address number must not be negative");

        Address addressDB = addressRepository.findById(mapper.convertValue(addressDTO, Address.class).getId())
                .orElseThrow(() -> new NoSuchDataExistsException("The address trying to update was not found"));

        updateValues(addressDB, addressDTO);

        AddressDTO addressUpdated = mapper.convertValue(addressRepository.save(addressDB), AddressDTO.class);

        LOGGER.info("Method - Update: Successfully updated address: "+ addressUpdated);
        return addressUpdated;
    }

    @Override
    public void delete(Long id) throws IntegrityDataException, NoSuchDataExistsException {
        if(id == null)
            throw new IntegrityDataException("Address id cant' be null");

        if(id < 1)
            throw new IllegalArgumentException("Address id can´t be negative");

        addressRepository.findById(id)
                .orElseThrow(() -> new NoSuchDataExistsException("The address with id: " + id + " was not found"));

        LOGGER.info("Method - Delete: Deleted address with id: "+ id);
        addressRepository.deleteById(id);
    }

    private void updateValues(Address addressToUpdate, AddressDTO addressDTO) {

        Address address = mapper.convertValue(addressDTO, Address.class);

        addressToUpdate.setStreet(address.getStreet());
        addressToUpdate.setLocality(address.getLocality());
        addressToUpdate.setNumber(address.getNumber());
        addressToUpdate.setProvince(address.getProvince());

    }
}
