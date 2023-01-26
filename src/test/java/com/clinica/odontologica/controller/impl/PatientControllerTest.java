package com.clinica.odontologica.controller.impl;


import com.clinica.odontologica.securityHelper.SecurityConfigForTest;
import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.dto.UserDTO;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.service.impl.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfigForTest.class)
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JwtDecoder jwtDecoder;

    private PatientDTO patientDTO1;
    private PatientDTO patientDTO2;
    private AddressDTO addressDTO;

    private UserDTO user1;
    private UserDTO user2;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();

        user1 = new UserDTO("userfc", true);
        user2 = new UserDTO("userma", false);

        addressDTO = new AddressDTO(1L, "23A", 15, "Norte", "Verde");
        patientDTO1 = new PatientDTO(1L, 123L, "Juan", "Perez", addressDTO, user1);
        patientDTO2 = new PatientDTO(2L, 456L, "Lily", "Nuñez", addressDTO, user2);
    }

    @Test
    public void createPatientTest() throws Exception {
        when(patientService.create(any(PatientDTO.class))).thenReturn(patientDTO1);

        String payloadPatient = objectMapper.writeValueAsString(patientDTO1);

        this.mockMvc.perform(post("/patients/new")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadPatient))
                .andExpect(status().isCreated());
    }

    @Test
    public void conflictToCreatePatientTest() throws Exception {
        when(patientService.create(any(PatientDTO.class))).thenThrow(new DataAlreadyExistsException("The patient with dni:" + patientDTO1.getDni() + " already exist!"));

        String payloadPatient = objectMapper.writeValueAsString(patientDTO1);

        this.mockMvc.perform(post("/patients/new")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadPatient))
                .andExpect(status().isConflict());
    }

    @Test
    public void forbbidenToCreatePatientTest() throws Exception {
        when(patientService.create(any(PatientDTO.class))).thenReturn(patientDTO1);

        String payloadPatient = objectMapper.writeValueAsString(patientDTO1);

        this.mockMvc.perform(post("/patients/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadPatient))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllPatientsTest() throws Exception {
        List<PatientDTO> list = new ArrayList<>();
        list.add(patientDTO1);
        list.add(patientDTO2);

        when(patientService.getAll()).thenReturn(list);

        MvcResult response = this.mockMvc.perform(get("/patients/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())))
                .andReturn();

        assertNotNull(response.getResponse());
    }

    @Test
    public void forbbidenToGetAllPatientsTest() throws Exception {
        List<PatientDTO> list = new ArrayList<>();
        list.add(patientDTO1);
        list.add(patientDTO2);

        when(patientService.getAll()).thenReturn(list);

        this.mockMvc.perform(get("/patients/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("USER")))                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void notFoundToGetAllPatientsTest() throws Exception {
        when(patientService.getAll()).thenThrow(new ResourceNotFoundException("There aren't registered patients"));

        this.mockMvc.perform(get("/patients/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPatientByIdTest() throws Exception {
        when(patientService.getById(anyLong())).thenReturn(patientDTO1);

        MvcResult response = this.mockMvc.perform(get("/patients/id/{id}", 1L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(patientDTO1.getDni().intValue())))
                .andReturn();

        assertEquals(response.getResponse().getContentType(), "application/json");
    }

    @Test
    public void badRequestDentistsWithNegativeIdTest() throws Exception {
        when(patientService.getById(anyLong())).thenThrow(new IllegalArgumentException("Patient id can´t be negative"));

        MvcResult response = this.mockMvc.perform(get("/patients/id/{id}", -1L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("ERROR: Patient id can´t be negative", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }


    @Test
    public void updatePatientTest() throws Exception {
        patientDTO1.setFirstname("Lucas");
        when(patientService.update(any(PatientDTO.class))).thenReturn(patientDTO1);

        String payloadPatient = objectMapper.writeValueAsString(patientDTO1);

        MvcResult response = this.mockMvc.perform(put("/patients/update")
                .with(jwt().authorities(new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadPatient))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadPatient);
    }

    @Test
    public void notFoundUpdatePatientTest() throws Exception {
        patientDTO2.setFirstname("Maria");
        when(patientService.update(any(PatientDTO.class))).thenThrow(new NoSuchDataExistsException("The patient trying to update was not found"));

        String payloadPatient = objectMapper.writeValueAsString(patientDTO2);

        MvcResult response = this.mockMvc.perform(put("/patients/update")
                        .with(jwt().authorities(new SimpleGrantedAuthority("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadPatient))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("ERROR: The patient trying to update was not found", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void deletePatientById() throws Exception {
        doNothing().when(patientService).delete(anyLong());

        MvcResult response = this.mockMvc.perform(delete("/patients/delete/{id}", 1L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), "Patient with id: 1 deleted");
    }

    @Test
    public void NotFoundDeletePatientById() throws Exception {
        doThrow(new NoSuchDataExistsException("The patient with id: 20 was not found")).when(patientService).delete(20L);

        MvcResult response = this.mockMvc.perform(delete("/patients/delete/{id}", 20L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("ERROR: The patient with id: 20 was not found", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}