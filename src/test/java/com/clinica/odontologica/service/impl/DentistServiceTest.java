package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.dto.DentistDTO;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class DentistServiceTest {

    @Autowired
    DentistService dentistService;

    private DentistDTO dentist;
    
    @BeforeEach
    public void init() {
        dentist = new DentistDTO();
        dentist.setFirstname("Juan");
        dentist.setLastname("Perez");
        dentist.setDni(1234L);
        dentist.setRegistrationNumber(145876L);
    }

    @Test
    @Order(1)
    public void createDentistTest() throws Exception {
        DentistDTO d = new DentistDTO();
        d.setId(1L);
        d.setDni(1234L);
        d.setRegistrationNumber(145876L);
        d.setFirstname("Juan");
        d.setLastname("Perez");

        dentistService.create(dentist);
        assertEquals(d.getDni(), dentist.getDni());
    }

    @Test
    @Order(2)
    public void getAllDentistsTest() throws Exception{
        dentist.setDni(1634L);
        dentist.setRegistrationNumber(14876L);
        dentistService.create(dentist);

        List<DentistDTO> dentists = dentistService.getAll();
        assertNotEquals(dentists.size(), 0);

    }

    @Test
    @Order(3)
    public void getDentistByIdTest() throws Exception {
        dentist.setDni(1664L);
        dentist.setRegistrationNumber(13876L);
        DentistDTO d = dentistService.create(dentist);

        DentistDTO dentist = dentistService.getById(d.getId());
        assertNotNull(dentist);

    }
    @Test
    @Order(4)
    public void updateDentistTest() throws Exception {
        String apellido = "Perez Jimenez";

        dentist.setDni(16234L);
        dentist.setRegistrationNumber(12676L);
        DentistDTO d = dentistService.create(dentist);

        d.setLastname(apellido);

        dentistService.update(d);
        assertEquals(d.getLastname(), apellido);

    }
    @Test
    @Order(5)
    public void deleteDentistTest() throws Exception {
        dentist.setDni(16212L);
        dentist.setRegistrationNumber(14576L);
        DentistDTO d = dentistService.create(dentist);

        dentistService.delete(d.getId());

        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, () ->  dentistService.getById(d.getId()));
        assertEquals(exc.getMessage(), "The dentist with id: " + d.getId() + " was not found!");

    }
}