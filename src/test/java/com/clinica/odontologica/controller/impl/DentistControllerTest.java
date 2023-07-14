package com.clinica.odontologica.controller.impl;

import com.clinica.odontologica.security.SecurityConfig;
import com.clinica.odontologica.security.manager.CustomAuthenticationManager;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.model.dto.DentistDTO;
import com.clinica.odontologica.model.dto.UserDTO;
import com.clinica.odontologica.service.impl.DentistService;
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

@Import(SecurityConfig.class)
@WebMvcTest(DentistController.class)
class DentistControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private DentistService dentistService;

        @Autowired
        ObjectMapper objectMapper;

        @MockBean
        CustomAuthenticationManager customAuthenticationManager;

        private DentistDTO dentistDTO1;
        private DentistDTO dentistDTO2;
        private UserDTO user1;
        private UserDTO user2;



        @BeforeEach
        public void setUpUsers() {
            user1 = new UserDTO("userfc", true);
            user2 = new UserDTO("userma", false);

            dentistDTO1 = new DentistDTO(1L, 123L, 456L, "Fernando", "Castro", user1);
            dentistDTO2 = new DentistDTO(2L, 789L, 972L, "Maria", "Acosta", user2);
        }

    @Test
    public void createDentistTest() throws Exception {
        when(dentistService.create(any(DentistDTO.class))).thenReturn(dentistDTO1);

        String payloadDentist = objectMapper.writeValueAsString(dentistDTO1);

        this.mockMvc.perform(post("/dentists/new")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDentist))
                .andExpect(status().isCreated());
    }

    @Test
    public void conflictToCreateDentistTest() throws Exception {
        when(dentistService.create(any(DentistDTO.class))).thenThrow(new DataAlreadyExistsException("The dentist with dni: " +dentistDTO1.getDni() + " and registration number: " + dentistDTO1.getRegistrationNumber() + " already exist!"));

        String payloadDentist = objectMapper.writeValueAsString(dentistDTO1);

        this.mockMvc.perform(post("/dentists/new")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDentist))
                .andExpect(status().isConflict());
    }

    @Test
    public void forbbidenToCreateDentistTest() throws Exception {
        when(dentistService.create(any(DentistDTO.class))).thenReturn(dentistDTO1);

        String payloadDentist = objectMapper.writeValueAsString(dentistDTO1);

        this.mockMvc.perform(post("/dentists/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadDentist))
                .andExpect(status().isForbidden());
    }

        @Test
        public void getAllDentistsTest() throws Exception {
                List<DentistDTO> list = new ArrayList<>();
                list.add(dentistDTO1);
                list.add(dentistDTO2);

                when(dentistService.getAll()).thenReturn(list);
                MvcResult response = this.mockMvc.perform(get("/dentists/all")
                                .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()", is(list.size())))
                        .andReturn();

                assertNotNull(response.getResponse());
        }

        @Test
        public void forbbidenToGetAllDentistsTest() throws Exception {
                List<DentistDTO> list = new ArrayList<>();
                list.add(dentistDTO1);
                list.add(dentistDTO2);

                when(dentistService.getAll()).thenReturn(list);
                this.mockMvc.perform(get("/dentists/all")
                            .with(jwt().authorities(new SimpleGrantedAuthority("USER"))))
                        .andExpect(status().isForbidden());
        }

    @Test
    public void notFoundToGetAllDentistsTest() throws Exception {
        when(dentistService.getAll()).thenThrow(new ResourceNotFoundException("There aren't registered dentists"));
        MvcResult response = this.mockMvc.perform(get("/dentists/all")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("ERROR: There aren't registered dentists", response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    public void getDentistByIdTest() throws Exception {
        when(dentistService.getById(anyLong())).thenReturn(dentistDTO1);

        MvcResult response = this.mockMvc.perform(get("/dentists/id/{id}", 1L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber", is(dentistDTO1.getRegistrationNumber().intValue())))
                .andReturn();

        assertEquals(response.getResponse().getContentType(), "application/json");
    }

    @Test
    public void badRequestDentistWithNegativeIdTest() throws Exception {
        when(dentistService.getById(anyLong())).thenThrow(new IllegalArgumentException("Dentist id can´t be negative!"));

        MvcResult response = this.mockMvc.perform(get("/dentists/id/{id}", -1L)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals("ERROR: Dentist id can´t be negative!",
                response.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

        @Test
        public void updateDentistTest() throws Exception {
                dentistDTO1.setLastname("Camila");
                when(dentistService.update(any(DentistDTO.class))).thenReturn(dentistDTO1);

                String payloadDentist = objectMapper.writeValueAsString(dentistDTO1);

                MvcResult response = this.mockMvc.perform(put("/dentists/update")
                                .with(jwt().authorities(new SimpleGrantedAuthority("USER")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadDentist))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                        .andReturn();

                assertEquals(response.getResponse().getContentAsString(), payloadDentist);
        }

        @Test
        public void notFoundUpdateDentistTest() throws Exception {
                dentistDTO2.setLastname("David");
                when(dentistService.update(any(DentistDTO.class)))
                                .thenThrow(new NoSuchDataExistsException(
                                                "The dentist trying to update was not found!"));

                String payloadDentist = objectMapper.writeValueAsString(dentistDTO2);

                MvcResult response = this.mockMvc.perform(put("/dentists/update")
                                .with(jwt().authorities(new SimpleGrantedAuthority("USER")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadDentist))
                        .andExpect(status().isNotFound())
                        .andReturn();

                assertEquals("ERROR: The dentist trying to update was not found!",
                                response.getResponse().getContentAsString(StandardCharsets.UTF_8));
        }

        @Test
        public void deleteDentistById() throws Exception {
                doNothing().when(dentistService).delete(anyLong());

                MvcResult response = this.mockMvc.perform(delete("/dentists/delete/{id}", 1L)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                        .andExpect(status().isOk())
                        .andReturn();

                assertEquals(response.getResponse().getContentAsString(), "Dentist with id: 1 deleted");
        }

        @Test
        public void notFoundDeleteDentistById() throws Exception {
                doThrow(new NoSuchDataExistsException("The dentist with id: 15 was not found!")).when(dentistService)
                                .delete(15L);

                MvcResult response = this.mockMvc.perform(delete("/dentists/delete/{id}", 15L)
                                .with(jwt().authorities(new SimpleGrantedAuthority("ADMIN"))))
                        .andExpect(status().isNotFound())
                        .andReturn();

                assertEquals("ERROR: The dentist with id: 15 was not found!",
                                response.getResponse().getContentAsString(StandardCharsets.UTF_8));
        }
}