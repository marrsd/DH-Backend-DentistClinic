package com.clinica.odontologica.controller.impl;


import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.dto.UserDTO;
import com.clinica.odontologica.service.impl.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    ObjectMapper objectMapper;

    private PatientDTO patientDTO1;
    private PatientDTO patientDTO2;
    private AddressDTO addressDTO;

    private UserDTO user1;
    private UserDTO user2;

    @BeforeEach
    public void init() {
        addressDTO = new AddressDTO(1L, "23A", 15, "Norte", "Verde");
        patientDTO1 = new PatientDTO(1L, 123L, "Juan", "Perez", addressDTO);
        patientDTO2 = new PatientDTO(2L, 456L, "Lily", "Nuñez", addressDTO);
    }

    @Test
    public void createPatientTest() throws Exception {
        when(patientService.create(any(PatientDTO.class))).thenReturn(patientDTO1);

        String payloadPatient = objectMapper.writeValueAsString(patientDTO1);

        MvcResult response = this.mockMvc.perform(post("/patients/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadPatient))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadPatient);
    }

    @Test
    public void getAllPatientsTest() throws Exception {
        List<PatientDTO> list = new ArrayList<>();
        list.add(patientDTO1);
        list.add(patientDTO2);

        when(patientService.getAll()).thenReturn(list);

        MvcResult response = this.mockMvc.perform(get("/patients/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())))
                .andReturn();

        assertNotNull(response.getResponse());
    }

    @Test
    public void getPatientByIdTest() throws Exception {
        when(patientService.getById(anyLong())).thenReturn(patientDTO1);

        MvcResult response = this.mockMvc.perform(get("/patients/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni", is(patientDTO1.getDni().intValue())))
                .andReturn();

        assertEquals(response.getResponse().getContentType(), "application/json");

    }

    @Test
    public void updatePatientTest() throws Exception {
        patientDTO1.setFirstname("Lucas");
        when(patientService.update(any(PatientDTO.class))).thenReturn(patientDTO1);

        String payloadPatient = objectMapper.writeValueAsString(patientDTO1);

        MvcResult response = this.mockMvc.perform(put("/patients/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadPatient))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadPatient);
    }

    @Test
    public void deletePatientById() throws Exception {
        doNothing().when(patientService).delete(anyLong());

        MvcResult response = this.mockMvc.perform(delete("/patients/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), "Patient with id: 1 deleted");
    }
}