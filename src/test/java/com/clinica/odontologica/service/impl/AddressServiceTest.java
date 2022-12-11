package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    private AddressDTO address;

    @BeforeEach
    public void init() {
        address = new AddressDTO();
        address.setStreet("27A");
        address.setNumber(9);
        address.setLocality("Sur");
        address.setProvince("Floresta");
    }

    @Test
    @Order(1)
    public void createAddressTest() throws Exception {
        AddressDTO a = addressService.create(address);
        assertTrue(a != null);
    }

    @Test
    @Order(2)
    public void getAllAddressesTest() throws Exception{
        addressService.create(address);

        List<AddressDTO> patients = addressService.getAll();
        assertNotEquals(patients.size(), 0);

    }

    @Test
    @Order(3)
    public void getAddressByIdTest() throws Exception {
        AddressDTO a = addressService.create(address);

        AddressDTO address = addressService.getById(a.getId());
        assertEquals(address.getProvince(), "Floresta");

    }

    @Test
    @Order(4)
    public void updateAddressTest() throws Exception {
        String localidad = "Norte";

        AddressDTO a = addressService.create(address);

        a.setLocality(localidad);

        addressService.update(a);
        assertEquals(a.getLocality(), localidad);

    }

    @Test
    @Order(5)
    public void deleteAddressTest() throws Exception {
        AddressDTO a = addressService.create(address);

        addressService.delete(a.getId());

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, () ->  addressService.getById(a.getId()));
        assertEquals(exc.getMessage(), "The address with id: " + a.getId() + " was not found");
    }

}