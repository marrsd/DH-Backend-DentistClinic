package com.clinica.odontologica.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="Addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @NotBlank(message = "street must not be blank or null")
    private String street;

    @NotNull(message = "Number address must not be blank or null")
    private Integer number;

    @NotBlank(message = "Locality must not be blank or null")
    private String locality;

    @NotBlank(message = "Province must not be blank or null")
    private String province;

    @OneToOne(mappedBy = "address")
    @JsonIgnore
    private Patient patient;

}
