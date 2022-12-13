package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.dto.DentistDTO;
import com.clinica.odontologica.dto.PatientDTO;
import com.clinica.odontologica.dto.TurnDTO;
import com.clinica.odontologica.service.impl.TurnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurnController.class)
class TurnControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TurnService turnService;

    @Autowired
    ObjectMapper objectMapper;

    private PatientDTO patient;
    private DentistDTO dentist;
    private AddressDTO address;

    private TurnDTO turn;

    @BeforeEach
    public void init() {
        address = new AddressDTO(1L, "23A", 15, "Norte", "Verde");
        patient =  new PatientDTO(1L, 123L, "Juan", "Perez", address);
        dentist = new DentistDTO(2L, 789L, 972L, "Maria", "Acosta");
        turn = new TurnDTO(1L, patient, dentist, LocalDateTime.now().plusDays(1));
    }

    @Test
    public void createTurnTest() throws Exception {
        when(turnService.create(any(TurnDTO.class))).thenReturn(turn);

        String payloadTurn = objectMapper.writeValueAsString(turn);

        MvcResult response = this.mockMvc.perform(post("/turns/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payloadTurn))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadTurn);
    }

    @Test
    public void getAllTurnsTest() throws Exception {
        List<TurnDTO> list = new ArrayList<>(Arrays.asList(turn));
        when(turnService.getAll()).thenReturn(list);

        MvcResult response = this.mockMvc.perform(get("/turns/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", is(list.size())))
                .andReturn();

        assertEquals(response.getResponse().getContentType(), "application/json");
    }

    @Test
    public void getTurnByIdTest() throws Exception {
        when(turnService.getById(anyLong())).thenReturn(turn);

        MvcResult response = this.mockMvc.perform(get("/turns/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patient.dni", is(turn.getPatient().getDni().intValue())))
                .andReturn();

        assertNotNull(response.getResponse());
    }

    @Test
    public void updateTurnTest() throws Exception {
        turn.setDateHour(LocalDateTime.now().plusDays(5));
        when(turnService.update(any(TurnDTO.class))).thenReturn(turn);

        String payloadTurn = objectMapper.writeValueAsString(turn);

        MvcResult response = this.mockMvc.perform(put("/turns/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadTurn))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadTurn);
    }

    @Test
    public void deleteTurnTest() throws Exception {
        doNothing().when(turnService).delete(anyLong());

        MvcResult response = this.mockMvc.perform(delete("/turns/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), "Turn deleted");
    }
}