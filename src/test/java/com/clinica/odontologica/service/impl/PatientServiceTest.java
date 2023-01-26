package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.domain.Patient;
import com.clinica.odontologica.domain.auth.ERole;
import com.clinica.odontologica.domain.auth.User;
import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.dto.UserDTO;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @Mock
    private ObjectMapper mapper;

    private PatientDTO patientDTO;

    private Patient patient;

    private AddressDTO address;

    private UserDTO userDTO;

    private User user;

    @BeforeEach
    public void init() {

        user = new User(1L, "userabc", "1234", ERole.USER, false, false);
        userDTO = new UserDTO("userabc", false);

        address = new AddressDTO();
        address.setStreet("32A");
        address.setNumber(12);
        address.setLocality("Green");
        address.setProvince("provincia");

        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setDni(1234L);
        patientDTO.setFirstname("Pablito");
        patientDTO.setLastname("Clavito");
        patientDTO.setAddress(address);
        patientDTO.setUser(userDTO);

        patient = new Patient();
        patient.setId(1L);
        patient.setDni(1234L);
        patient.setFirstname("Pablito");
        patient.setLastname("Clavito");
        patient.setDateHourAdmission(LocalDateTime.now());
    }

    @Test
    @Order(1)
    public void createPatientTest() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(user);

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO patient1 = patientService.create(patientDTO);

        assertEquals(patient.getDni(), patient1.getDni());
    }

    @Test
    @Order(2)
    public void conflicCreatePatientAlreadyExistsTest() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(user);

        patientService.create(patientDTO);

        assertThrows(DataAlreadyExistsException.class, () -> patientService.create(patientDTO));
    }

    @Test
    @Order(3)
    public void badRequestCreatePatientTest() {
        patientDTO.setDni(-1234L);

        assertThrows(IllegalArgumentException.class, () -> patientService.create(patientDTO));
    }

    @Test
    @Order(4)
    public void getAllPatientsTest() throws Exception {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));

        List<PatientDTO> patients = patientService.getAll();

        assertNotEquals(patients.size(), 0);
    }

    @Test
    @Order(5)
    public void notFoundGetAllPatientsTest() {
        assertThrows(ResourceNotFoundException.class, () -> patientService.getAll());
    }

    @Test
    @Order(6)
    public void getPatientByIdTest() throws Exception {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        PatientDTO patientDTO = patientService.getById(1L);

        assertEquals(patientDTO.getDni(), patient.getDni());
    }

    @Test
    @Order(7)
    public void badRequestGetPatientByNullId() {
        patientDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> patientService.getById(patientDTO.getId()));
    }

    @Test
    @Order(8)
    public void badRequestGetPatientWithNegativeId() {
        patientDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class, () -> patientService.getById(patientDTO.getId()));
    }

    @Test
    @Order(9)
    public void notFoundGetPatientById() {
        patientDTO.setId(100L);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> patientService.getById(patientDTO.getId()));
    }

    @Test
    @Order(10)
    public void updatePatientTest() throws Exception {
        String lastname = "Lopez";

        patientDTO.setLastname(lastname);
        patient.setLastname(lastname);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO patientUpdated = patientService.update(patientDTO);

        assertEquals(patientUpdated.getLastname(), patient.getLastname());
    }

    @Test
    @Order(11)
    public void badRequestUpdateNullPatientTest() {
        patientDTO = null;

        assertThrows(IntegrityDataException.class, () -> patientService.update(patientDTO));
    }

    @Test
    @Order(12)
    public void notFoundUpdatePatientTest() {
        assertThrows(NoSuchDataExistsException.class, () -> patientService.update(patientDTO));
    }

    @Test
    @Order(13)
    public void deletePatienTest() throws Exception {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        patientService.delete(patientDTO.getId());

        verify(patientRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @Order(14)
    public void notFoundDeletePatientTest() {
        patientDTO.setId(203L);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> patientService.delete(patientDTO.getId()));
    }

    @Test
    @Order(15)
    public void badRequestDeletePatientWithNullIdTest() {
        patientDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> patientService.delete(patientDTO.getId()));
    }

    @Test
    @Order(16)
    public void badRequestDeletePatientWithNegativeId() {
        patientDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class, () -> patientService.delete(patientDTO.getId()));
    }
}