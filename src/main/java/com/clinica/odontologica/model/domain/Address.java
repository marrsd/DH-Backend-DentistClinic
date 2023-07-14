package com.clinica.odontologica.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @NotBlank(message = "Street must not be blank or null")
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
