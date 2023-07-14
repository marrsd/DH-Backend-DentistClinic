package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.domain.Dentist;
import com.clinica.odontologica.model.domain.auth.ERole;
import com.clinica.odontologica.model.domain.auth.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class DentistRepositoryTest {

    @Autowired
    DentistRepository dentistRepository;

    private Dentist dentist;

    private User user;

    @BeforeEach
    public void init() {
        user = new User(1L, "userabc", "1234", ERole.USER, false, false);

        dentist = new Dentist();
        dentist.setId(1L);
        dentist.setDni(1234L);
        dentist.setRegistrationNumber(145876L);
        dentist.setFirstname("Juan");
        dentist.setLastname("Perez");
        dentist.setUser(user);
    }

    @AfterEach
    public void destroyAll() {
        dentistRepository.deleteAll();
    }

    @Test
    public void createDentistTest() {
        Dentist dentist1 = dentistRepository.save(dentist);

        assertEquals(dentist.getRegistrationNumber(), dentist1.getRegistrationNumber());
    }

    @Test
    public void getAllDentistsTest() {
        dentistRepository.save(dentist);
        List<Dentist> dentistList = dentistRepository.findAll();

        assertEquals(dentistList.size(), 1);
    }

    @Test
    public void getDentistByIdTest() {
        Dentist dentist1 = dentistRepository.save(dentist);

        Optional<Dentist> dentist2 = dentistRepository.findById(dentist1.getId());

        assertEquals(dentist1, dentist2.get());
    }

    @Test
    public void getDentistByRegistrationNumberTest() {
        Dentist dentist1 = dentistRepository.save(dentist);

        Optional<Dentist> dentist2 = dentistRepository.getByRegistrationNumber(dentist1.getRegistrationNumber());

        assertNotNull(dentist2.get());
    }

    @Test
    public void getDentistByFullnameTest() {
        Dentist dentist1 = dentistRepository.save(dentist);

        Dentist dentist2 = dentistRepository.getByFirstnameAndLastname(dentist1.getFirstname(), dentist1.getLastname())
                .get();

        assertEquals(dentist1.getLastname(), dentist2.getLastname());
    }

    @Test
    public void deleteDentistById() {
        Dentist dentist1 = dentistRepository.save(dentist);

        dentistRepository.deleteById(dentist1.getId());
        Optional<Dentist> dentist2 = dentistRepository.findById(dentist1.getId());

        assertEquals(Optional.empty(), dentist2);
    }

}