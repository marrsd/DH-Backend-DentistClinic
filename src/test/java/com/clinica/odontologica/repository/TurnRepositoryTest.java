package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.domain.Address;
import com.clinica.odontologica.model.domain.Dentist;
import com.clinica.odontologica.model.domain.Patient;
import com.clinica.odontologica.model.domain.Turn;
import com.clinica.odontologica.model.domain.auth.ERole;
import com.clinica.odontologica.model.domain.auth.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class TurnRepositoryTest {

    @Autowired
    private TurnRepository turnRepository;

    private Turn turn;

    private User user;

    private Dentist dentist;

    private Patient patient;

    private Address address;

    @BeforeEach
    public void init() {
        user = new User(1L, "userabc", "1234", ERole.ADMIN, true, false);

        address = new Address();
        address.setId(1L);
        address.setNumber(12);
        address.setLocality("North");
        address.setProvince("Campestre");
        address.setStreet("14A");

        patient = new Patient();
        patient.setId(1L);
        patient.setDni(1234L);
        patient.setFirstname("Pablito");
        patient.setLastname("Clavito");
        patient.setDateHourAdmission(LocalDateTime.now());
        patient.setUser(user);
        patient.setAddress(address);

        dentist = new Dentist();
        dentist.setId(1L);
        dentist.setDni(1234L);
        dentist.setRegistrationNumber(145876L);
        dentist.setFirstname("Juan");
        dentist.setLastname("Perez");
        dentist.setUser(user);

        turn = new Turn();
        turn.setId(1L);
        turn.setDentist(dentist);
        turn.setPatient(patient);
        turn.setDateHour(LocalDateTime.now().plusDays(1));
    }

    @AfterEach
    public void destroyAll() {
        turnRepository.deleteAll();
    }

    @Test
    public void createTurnTest() {
        Turn turn1 = turnRepository.save(turn);

        assertEquals(turn.getPatient().getDni(), turn1.getPatient().getDni());
    }

    @Test
    public void getAllTurnTest() {
        List<Turn> turnList = turnRepository.findAll();

        assertEquals(turnList.size(), 0);
    }

    @Test
    public void getAllTurnByDentistRegistrationNumberTest() {
        turnRepository.save(turn);

        List<Turn> turnList = turnRepository.getByDentistRegistrationNumber(turn.getDentist().getRegistrationNumber())
                .get();

        assertEquals(turnList.size(), 1);
    }

    @Test
    public void getAllTurnsByPatientDniTest() {
        turnRepository.save(turn);

        List<Turn> turnList = turnRepository.getByPatientDni(turn.getPatient().getDni()).get();

        assertEquals(turnList.size(), 1);
    }

    @Test
    public void getTurnByIdTest() {
        Turn turn1 = turnRepository.save(turn);

        Optional<Turn> turn2 = turnRepository.findById(turn1.getId());

        assertEquals(turn1.getId(), turn2.get().getId());
    }

    @Test
    public void deleteTurnById() {
        Turn turn1 = turnRepository.save(turn);

        turnRepository.deleteById(turn1.getId());
        Optional<Turn> turn2 = turnRepository.findById(turn1.getId());

        assertEquals(Optional.empty(), turn2);
    }

}