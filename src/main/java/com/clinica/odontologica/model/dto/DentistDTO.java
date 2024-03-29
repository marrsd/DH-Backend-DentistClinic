package com.clinica.odontologica.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DentistDTO {
    private Long id;
    private Long dni;
    private Long registrationNumber;
    private String firstname;
    private String lastname;
    private UserDTO user;
}
