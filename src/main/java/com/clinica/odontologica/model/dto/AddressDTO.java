package com.clinica.odontologica.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressDTO {
    private Long id;
    private String street;
    private Integer number;
    private String locality;
    private String province;
}
