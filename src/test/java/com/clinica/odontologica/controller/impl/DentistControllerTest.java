package com.clinica.odontologica.controller.impl;


import com.clinica.odontologica.dto.DentistDTO;
import com.clinica.odontologica.service.impl.DentistService;
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

@WebMvcTest(DentistController.class)
class DentistControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DentistService dentistService;
    @Autowired
    ObjectMapper objectMapper;

    private DentistDTO dentistDTO1;
    private DentistDTO dentistDTO2;


    @BeforeEach
    public void init() {
        dentistDTO1 = new DentistDTO(1L, 123L, 456L, "Fernando", "Castro");
        dentistDTO2 = new DentistDTO(2L, 789L, 972L, "Maria", "Acosta");
    }

    @Test
    public void createDentistTest() throws Exception {
        when(dentistService.create(any(DentistDTO.class))).thenReturn(dentistDTO1);

        String payloadDentist = objectMapper.writeValueAsString(dentistDTO1);

        MvcResult response = this.mockMvc.perform(post("/dentists/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDentist))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadDentist);

    }

    @Test
    public void getAllDentistsTest() throws Exception {
        List<DentistDTO> list = new ArrayList<>();
        list.add(dentistDTO1);
        list.add(dentistDTO2);

        when(dentistService.getAll()).thenReturn(list);

        MvcResult response = this.mockMvc.perform(get("/dentists/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(list.size())))
                .andReturn();

        assertNotNull(response.getResponse());
    }

    @Test
    public void getDentistByIdTest() throws Exception {
        when(dentistService.getById(anyLong())).thenReturn(dentistDTO1);

        MvcResult response = this.mockMvc.perform(get("/dentists/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber", is(dentistDTO1.getRegistrationNumber().intValue())))
                .andReturn();

        assertEquals(response.getResponse().getContentType(), "application/json");
    }

    @Test
    public void updateDentistTest() throws Exception {
        dentistDTO1.setLastname("Camila");
        when(dentistService.update(any(DentistDTO.class))).thenReturn(dentistDTO1);

        String payloadDentist = objectMapper.writeValueAsString(dentistDTO1);

        MvcResult response = this.mockMvc.perform(put("/dentists/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDentist))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadDentist);
    }

    @Test
    public void deleteDentistById() throws Exception {
        doNothing().when(dentistService).delete(anyLong());

        MvcResult response = this.mockMvc.perform(delete("/dentists/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), "Dentist with id: 1 deleted");
    }
}