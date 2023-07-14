package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.model.domain.Dentist;
import com.clinica.odontologica.model.domain.auth.ERole;
import com.clinica.odontologica.model.domain.auth.User;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.model.dto.DentistDTO;
import com.clinica.odontologica.model.dto.UserDTO;
import com.clinica.odontologica.repository.DentistRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class DentistServiceTest {

    @InjectMocks
    private DentistService dentistService;

    @Mock
    private DentistRepository dentistRepository;

    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper mapper;

    private DentistDTO dentistDTO;

    private Dentist dentist;

    private User user;

    private UserDTO userDTO;

    @BeforeEach
    public void init() {
        user = new User(1L, "userabc", "1234", ERole.USER, false, false);
        userDTO = new UserDTO("userabc", false);

        dentistDTO = new DentistDTO();
        dentistDTO.setId(1L);
        dentistDTO.setDni(1234L);
        dentistDTO.setRegistrationNumber(145876L);
        dentistDTO.setFirstname("Juan");
        dentistDTO.setLastname("Perez");
        dentistDTO.setUser(userDTO);

        dentist = new Dentist();
        dentist.setId(1L);
        dentist.setDni(1234L);
        dentist.setRegistrationNumber(145876L);
        dentist.setFirstname("Juan");
        dentist.setLastname("Perez");
    }

    @Test
    @Order(1)
    public void createDentistTest() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(user);

        when(dentistRepository.save(any(Dentist.class))).thenReturn(dentist);

        DentistDTO dentist1 = dentistService.create(dentistDTO);

        assertEquals(dentist.getFirstname(), dentist1.getFirstname());
    }

    @Test
    @Order(2)
    public void conflictCreateDentistAlreadyExistsTest() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(user);

        dentistService.create(dentistDTO);

        assertThrows(DataAlreadyExistsException.class, () -> dentistService.create(dentistDTO));
    }

    @Test
    @Order(3)
    public void badRequestCreateDentistTest() {
        dentistDTO.setDni(-1234L);

        dentistDTO.setRegistrationNumber(-15572L);

        assertThrows(IllegalArgumentException.class, () -> dentistService.create(dentistDTO));
    }

    @Test
    @Order(4)
    public void getAllDentistsTest() throws Exception {
        when(dentistRepository.findAll(Sort.by(Sort.Direction.DESC, "firstname"))).thenReturn(Arrays.asList(dentist));

        List<DentistDTO> dentists = dentistService.getAll();

        assertEquals(dentists.get(0).getRegistrationNumber(), dentist.getRegistrationNumber());
    }

    @Test
    @Order(5)
    public void notFoundGetAllDentistsTest() {
        assertThrows(ResourceNotFoundException.class, () -> dentistService.getAll());
    }

    @Test
    @Order(6)
    public void getDentistByIdTest() throws Exception {
        when(dentistRepository.findById(anyLong())).thenReturn(Optional.of(dentist));

        DentistDTO dentistDTO = dentistService.getById(1L);

        assertNotNull(dentistDTO);
    }

    @Test
    @Order(7)
    public void badRequestGetDentistByNullId() {
        dentistDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> dentistService.getById(dentistDTO.getId()));
    }

    @Test
    @Order(8)
    public void badRequestGetDentistByNegativeId() {
        dentistDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class, () -> dentistService.getById(dentistDTO.getId()));
    }

    @Test
    @Order(9)
    public void notFoundGetDentistById() {
        dentistDTO.setId(30L);

        when(dentistRepository.findById(dentistDTO.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> dentistService.getById(dentistDTO.getId()));
    }

    @Test
    @Order(10)
    public void updateDentistTest() throws Exception {
        String lastname = "Perez Jimenez";

        dentistDTO.setLastname(lastname);
        dentist.setLastname(lastname);

        when(dentistRepository.findById(anyLong())).thenReturn(Optional.of(dentist));
        when(dentistRepository.save(any(Dentist.class))).thenReturn(dentist);

        DentistDTO dentistUpdated = dentistService.update(dentistDTO);

        assertEquals(dentistUpdated.getLastname(), dentist.getLastname());
    }

    @Test
    @Order(11)
    public void badRequestUpdateNullDentistsTest() {
        dentistDTO = null;

        assertThrows(IntegrityDataException.class, () -> dentistService.update(dentistDTO));
    }

    @Test
    @Order(12)
    public void notFoundUpdateDentistTest() {
        assertThrows(NoSuchDataExistsException.class, () -> dentistService.update(dentistDTO));
    }

    @Test
    @Order(13)
    public void deleteDentistTest() throws Exception {
        when(dentistRepository.findById(anyLong())).thenReturn(Optional.of(dentist));

        dentistService.delete(dentistDTO.getId());

        verify(dentistRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @Order(14)
    public void notFoundDeleteDentistTest() {
        dentistDTO.setId(15L);

        when(dentistRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> dentistService.delete(dentistDTO.getId()));
    }

    @Test
    @Order(15)
    public void badRequestDeleteDentistWithNullIdTest() {
        dentistDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> dentistService.delete(dentistDTO.getId()));
    }

    @Test
    @Order(16)
    public void badRequestDeleteDentistWithNegativeId() {
        dentistDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class, () -> dentistService.delete(dentistDTO.getId()));
    }
}