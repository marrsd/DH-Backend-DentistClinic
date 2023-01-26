package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.domain.Dentist;
import com.clinica.odontologica.domain.Patient;
import com.clinica.odontologica.domain.Turn;
import com.clinica.odontologica.domain.auth.ERole;
import com.clinica.odontologica.domain.auth.User;
import com.clinica.odontologica.dto.*;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.repository.TurnRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class TurnServiceTest {

    @InjectMocks
    private TurnService turnService;

    @Mock
    private TurnRepository turnRepository;

    @Mock
    PatientService patientService;

    @Mock
    private DentistService dentistService;

    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper mapper;

    private TurnDTO turnDTO;

    private PatientDTO patientDTO;

    private DentistDTO dentistDTO;

    private UserDTO userDTO;

    private Turn turn;

    private Patient patient;

    private Dentist dentist;

    private User user;

    @BeforeEach()
    public void init() {

        user = new User(1L, "userabc", "1234", ERole.USER, true, true);
        userDTO = new UserDTO("userabc", true);

        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setDni(1234L);
        patientDTO.setFirstname("Any");
        patientDTO.setLastname("Hernandez");
        patientDTO.setUser(userDTO);

        patient = new Patient();
        patient.setId(1L);
        patient.setDni(1234L);
        patient.setFirstname("Any");
        patient.setLastname("Hernandez");
        patient.setUser(user);

        dentistDTO = new DentistDTO();
        dentistDTO.setId(1L);
        dentistDTO.setRegistrationNumber(1264L);
        dentistDTO.setDni(1374L);
        dentistDTO.setFirstname("Lucy");
        dentistDTO.setLastname("Rodriguez");
        dentistDTO.setUser(userDTO);

        dentist = new Dentist();
        dentist.setId(1L);
        dentist.setRegistrationNumber(1264L);
        dentist.setDni(1374L);
        dentist.setFirstname("Lucy");
        dentist.setLastname("Rodriguez");
        dentist.setUser(user);

        turnDTO = new TurnDTO();
        turnDTO.setId(1L);
        turnDTO.setDentist(dentistDTO);
        turnDTO.setPatient(patientDTO);
        turnDTO.setDateHour(LocalDateTime.now());

        turn = new Turn();
        turn.setId(1L);
        turn.setDentist(dentist);
        turn.setPatient(patient);
        turn.setDateHour(LocalDateTime.now());
    }

    @Test
    @Order(1)
    public void createTurnTest() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(user);
        when(patientService.getById(anyLong())).thenReturn(patientDTO);
        when(dentistService.getById(anyLong())).thenReturn(dentistDTO);

        when(turnRepository.save(any(Turn.class))).thenReturn(turn);

        TurnDTO turnDTO1 = turnService.create(turnDTO);

        assertNotNull(turnDTO1);
    }

    @Test
    @Order(2)
    public void badRequestCreateTurnWithoutDentistTest() {
        turnDTO.setDentist(null);

        assertThrows(IntegrityDataException.class, () -> turnService.create(turnDTO));
    }

    @Test
    @Order(3)
    public void badRequestCreateTurnWithoutPatientTest() {
        turnDTO.setPatient(null);

        assertThrows(IntegrityDataException.class, () -> turnService.create(turnDTO));
    }

    @Test
    @Order(4)
    public void getAllTurnsTest() throws Exception {
        when(turnRepository.findAll(Sort.by(Sort.Direction.DESC, "dateHour"))).thenReturn(Arrays.asList(turn));

        List<TurnDTO> turns = turnService.getAll();

        assertEquals(turns.size(), 1);
    }

    @Test
    @Order(5)
    public void notFoundGetAllTurnsTest() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> turnService.getAll());

        assertEquals(exception.getMessage(), "There aren't registered turns");
    }

    @Test
    @Order(6)
    public void getTurnByIdTest() throws Exception {
        when(turnRepository.findById(anyLong())).thenReturn(Optional.of(turn));

        TurnDTO turnDTO = turnService.getById(1L);

        assertEquals(turnDTO.getDentist().getRegistrationNumber(), turn.getDentist().getRegistrationNumber());
    }

    @Test
    @Order(7)
    public void badRequestGetTurnByNullIdTest() {
        turnDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> turnService.getById(turnDTO.getId()));
    }

    @Test
    @Order(8)
    public void badRequestGetTurnByNegativeIdTest() {
        turnDTO.setId(-2L);

        assertThrows(IllegalArgumentException.class, () -> turnService.getById(turnDTO.getId()));
    }

    @Test
    @Order(9)
    public void notFoundGetTurnByIdTest() {
        turnDTO.setId(51L);

        when(turnRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchDataExistsException.class, () -> turnService.getById(turnDTO.getId()));

        assertTrue(exception.getMessage().contains("The turn with id: " + turnDTO.getId() + " was not found"));
    }

    @Test
    @Order(10)
    public void updateTurnTest() throws Exception {

        turnDTO.setDateHour(LocalDateTime.now().plusDays(1));
        turn.setDateHour(LocalDateTime.now().plusDays(1));

        when(userService.getUserByUsername(anyString())).thenReturn(user);
        when(patientService.getById(anyLong())).thenReturn(patientDTO);
        when(dentistService.getById(anyLong())).thenReturn(dentistDTO);

        when(turnRepository.findById(anyLong())).thenReturn(Optional.of(turn));
        when(turnRepository.save(any(Turn.class))).thenReturn(turn);

        TurnDTO turnDTO1 = turnService.update(turnDTO);

        assertEquals(turnDTO1.getDateHour(), turn.getDateHour());
    }

    @Test
    @Order(11)
    public void badRequestUpdateTurnWithoutDentistTest() {
        turnDTO.setDentist(null);

        assertThrows(IntegrityDataException.class, () -> turnService.update(turnDTO));
    }

    @Test
    @Order(12)
    public void badRequestUpdateTurnWithoutPatientTest() {
        turnDTO.setPatient(null);

        assertThrows(IntegrityDataException.class, () -> turnService.update(turnDTO));
    }

    @Test
    @Order(13)
    public void badRequestUpdateNullTurnTest() {
        turnDTO = null;

        assertThrows(IntegrityDataException.class, () -> turnService.update(turnDTO));
    }

    @Test
    @Order(14)
    public void badRequestUpdateTurnWithoutDateTest() {
        turnDTO.setDateHour(null);

        assertThrows(IntegrityDataException.class, () -> turnService.update(turnDTO));
    }

    @Test
    @Order(15)
    public void notFoundUpdateTurnTest() {
        assertThrows(NoSuchDataExistsException.class, () -> turnService.update(turnDTO));
    }

    @Test
    @Order(16)
    public void deleteTurnById() throws Exception {
        when(turnRepository.findById(anyLong())).thenReturn(Optional.of(turn));

        turnService.delete(patientDTO.getId());

        verify(turnRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @Order(17)
    public void notFoundDeleteTurnTest() {
       turnDTO.setId(60L);

        when(turnRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> turnService.delete(turnDTO.getId()));
    }

    @Test
    @Order(18)
    public void badRequestDeleteTurnWithNullIdTest() {
        turnDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> turnService.delete(turnDTO.getId()));
    }

    @Test
    @Order(19)
    public void badRequestDeleteDentistWithNegativeId() {
        turnDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class, () -> turnService.delete(turnDTO.getId()));
    }
}