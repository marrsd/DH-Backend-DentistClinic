package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.dto.DentistDTO;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.dto.TurnDTO;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class TurnServiceTest {

    @Autowired
    private TurnService turnService;
    private TurnDTO turn;

    @Autowired
    private PatientService patientService;
    private PatientDTO patient;

    @Autowired
    private DentistService dentistService;
    private DentistDTO dentist;
    private AddressDTO address;

    @BeforeEach()
    public void init() {
        address = new AddressDTO();
        address.setNumber(23);
        address.setStreet("27B");
        address.setLocality("Norte");
        address.setProvince("America");

        patient = new PatientDTO();
        patient.setDni(1234L);
        patient.setFirstname("Any");
        patient.setLastname("Hernandez");
        patient.setAddress(address);

        dentist = new DentistDTO();
        dentist.setRegistrationNumber(1264L);
        dentist.setDni(1374L);
        dentist.setFirstname("Lucy");
        dentist.setLastname("Rodriguez");

    }

    @Test
    @Order(1)
    public void createTurnTest() throws Exception {
        DentistDTO d = dentistService.create(dentist);
        PatientDTO p = patientService.create(patient);

        turn = new TurnDTO();
        turn.setDentist(d);
        turn.setPatient(p);
        turn.setDateHour(LocalDateTime.now().plusDays(1));

        TurnDTO turnDB = turnService.create(turn);
        assertNotNull(turnDB);
    }

    @Test
    @Order(2)
    public void getAllTurnsTest() throws Exception {
        dentist.setDni(1275L);
        dentist.setRegistrationNumber(583L);
        DentistDTO d = dentistService.create(dentist);

        patient.setDni(748L);
        PatientDTO p = patientService.create(patient);

        turn = new TurnDTO();
        turn.setDentist(d);
        turn.setPatient(p);
        turn.setDateHour(LocalDateTime.now().plusDays(1));

        turnService.create(turn);

        List<TurnDTO> turns = turnService.getAll();

        assertNotEquals(turns.size(), 0);
    }

    @Test
    @Order(3)
    public void getTurnByIdTest() throws Exception {
        dentist.setDni(1725L);
        dentist.setRegistrationNumber(5831L);
        DentistDTO d = dentistService.create(dentist);

        patient.setDni(7258L);
        PatientDTO p = patientService.create(patient);

        turn = new TurnDTO();
        turn.setDentist(d);
        turn.setPatient(p);
        turn.setDateHour(LocalDateTime.now().plusDays(1));

        TurnDTO turnDB = turnService.create(turn);

        TurnDTO turnById = turnService.getById(turnDB.getId());

        assertEquals(turnById.getDentist().getRegistrationNumber(), 5831L);
    }

    @Test
    @Order(4)
    public void updateTurnTest() throws Exception{
        dentist.setDni(1785L);
        dentist.setRegistrationNumber(5823L);
        DentistDTO d = dentistService.create(dentist);

        patient.setDni(7598L);
        PatientDTO p = patientService.create(patient);

        turn = new TurnDTO();
        turn.setDentist(d);
        turn.setPatient(p);
        turn.setDateHour(LocalDateTime.now().plusDays(1));

        TurnDTO t = turnService.create(turn);

        t.setDateHour(LocalDateTime.now().plusDays(2));

        TurnDTO turnById = turnService.getById(t.getId());

        assertEquals(turnById.getDateHour().getMonth(), LocalDateTime.now().getMonth());
    }

    @Test
    @Order(5)
    public void deleteTurnById() throws Exception {
        dentist.setDni(1785L);
        dentist.setRegistrationNumber(5823L);
        DentistDTO d = dentistService.create(dentist);

        patient.setDni(7598L);
        PatientDTO p = patientService.create(patient);

        turn = new TurnDTO();
        turn.setDentist(d);
        turn.setPatient(p);
        turn.setDateHour(LocalDateTime.now().plusDays(1));

        TurnDTO t = turnService.create(turn);

        turnService.delete(t.getId());

        NoSuchDataExistsException e = assertThrows(NoSuchDataExistsException.class, () -> turnService.getById(t.getId()));

        assertEquals(e.getMessage(), "The turn with id: " + t.getId() + " was not found");

    }

}