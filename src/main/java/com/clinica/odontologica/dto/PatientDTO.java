package com.clinica.odontologica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientDTO {
    private Long id;
    private Long dni;
    private String firstname;
    private String lastname;
    private AddressDTO address;
}
