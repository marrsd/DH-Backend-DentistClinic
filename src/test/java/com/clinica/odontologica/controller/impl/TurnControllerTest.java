package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.security.SecurityConfig;
import com.clinica.odontologica.security.manager.CustomAuthenticationManager;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.model.dto.*;
import com.clinica.odontologica.service.impl.TurnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(TurnController.class)
class TurnControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TurnService turnService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomAuthenticationManager customAuthenticationManager;

    private PatientDTO patient;
    private DentistDTO dentist;
    private AddressDTO address;
    private UserDTO user1;
    private UserDTO user2;
    private TurnDTO turn;

    @BeforeEach
    public void setup() {

        user1 = new UserDTO("userfc", true);
        user2 = new UserDTO("userma", false);

        address = new AddressDTO(1L, "23A", 15, "Norte", "Verde");
        patient = new PatientDTO(1L, 123L, "Juan", "Perez", address, user1);
        dentist = new DentistDTO(2L, 789L, 972L, "Maria", "Acosta", user2);
        turn = new TurnDTO(1L, patient, dentist, LocalDateTime.now().plusDays(1));
    }

    @Test
    public void createTurnTest() throws Exception {
        when(turnService.create(any(TurnDTO.class))).thenReturn(turn);

        String payloadTurn = objectMapper.writeValueAsString(turn);

        this.mockMvc.perform(post("/turns/new")
                    .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payloadTurn))
                .andExpect(status().isCreated());
    }

    @Test
    public void badRequestCreateTurnWithoutDentistTest() throws Exception {
        when(turnService.create(any(TurnDTO.class))).thenThrow(new IntegrityDataException("You can't define a dentist null to the turn"));
        turn.setDentist(null);
        String payloadTurn = objectMapper.writeValueAsString(turn);

        MvcResult response = this.mockMvc.perform(post("/turns/new")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadTurn))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("ERROR: You can't define a dentist null to the turn", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void badRequestCreateTurnWithoutPatientTest() throws Exception {
        when(turnService.create(any(TurnDTO.class))).thenThrow(new IntegrityDataException("You can't define a patient null to the turn"));
        turn.setPatient(null);
        String payloadTurn = objectMapper.writeValueAsString(turn);

        MvcResult response = this.mockMvc.perform(post("/turns/new")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadTurn))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("ERROR: You can't define a patient null to the turn", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void ForbbidenToCreateTurnTest() throws Exception {
        when(turnService.create(any(TurnDTO.class))).thenReturn(turn);

        String payloadTurn = objectMapper.writeValueAsString(turn);

        this.mockMvc.perform(post("/turns/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadTurn))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllTurnsTest() throws Exception {
        List<TurnDTO> list = new ArrayList<>(Arrays.asList(turn));
        when(turnService.getAll()).thenReturn(list);

        MvcResult response = this.mockMvc.perform(get("/turns/all")
                .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(list.size())))
                .andReturn();

        assertEquals(response.getResponse().getContentType(), "application/json");
    }

    @Test
    public void notFoundToGetAllTurnsTest() throws Exception {
        when(turnService.getAll()).thenThrow(new ResourceNotFoundException("There aren't registered turns"));

        MvcResult response = this.mockMvc.perform(get("/turns/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("ERROR: There aren't registered turns", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void ForbbidenToGetAllTurnsTest() throws Exception {
        List<TurnDTO> list = new ArrayList<>(Arrays.asList(turn));
        when(turnService.getAll()).thenReturn(list);

        this.mockMvc.perform(get("/turns/all")
                .with(jwt().authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getTurnByIdTest() throws Exception {
        when(turnService.getById(anyLong())).thenReturn(turn);

        MvcResult response = this.mockMvc.perform(get("/turns/id/{id}", 1L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patient.dni", is(turn.getPatient().getDni().intValue())))
                .andReturn();

        assertNotNull(response.getResponse());
    }

    @Test
    public void BadRequestTurnWithNegativeIdTest() throws Exception {
        when(turnService.getById(-1L)).thenThrow(new IllegalArgumentException("Turn id can´t be negative"));

        MvcResult response = this.mockMvc.perform(get("/turns/id/{id}", -1L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("ERROR: Turn id can´t be negative", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void updateTurnTest() throws Exception {
        turn.setDateHour(LocalDateTime.now().plusDays(5));
        when(turnService.update(any(TurnDTO.class))).thenReturn(turn);

        String payloadTurn = objectMapper.writeValueAsString(turn);

        MvcResult response = this.mockMvc.perform(put("/turns/update")
                .with(jwt().authorities(new SimpleGrantedAuthority("USER")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadTurn))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), payloadTurn);
    }

    @Test
    public void badRequestToUpdateTurnTest() throws Exception {
        turn.setDateHour(null);
        when(turnService.update(any(TurnDTO.class)))
                .thenThrow(new IntegrityDataException("You must to define a date for the turn"));

        String payloadTurn = objectMapper.writeValueAsString(turn);

        MvcResult response = this.mockMvc.perform(put("/turns/update")
                .with(jwt().authorities(new SimpleGrantedAuthority("USER")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadTurn))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("ERROR: You must to define a date for the turn",
                response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void deleteTurnTest() throws Exception {
        doNothing().when(turnService).delete(anyLong());

        MvcResult response = this.mockMvc.perform(delete("/turns/delete/{id}", 1L)
                .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(response.getResponse().getContentAsString(), "Turn deleted");
    }

    @Test
    public void NotFoundDeleteTurnTest() throws Exception {
        doThrow(new NoSuchDataExistsException("The turn with id: 50 was not found")).when(turnService).delete(50L);

        MvcResult response = this.mockMvc.perform(delete("/turns/delete/{id}", 50L)
                .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("ERROR: The turn with id: 50 was not found",
                response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}