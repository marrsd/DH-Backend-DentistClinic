package com.clinica.odontologica.model.domain;

import com.clinica.odontologica.model.domain.auth.User;
import com.clinica.odontologica.util.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Dni must not be blank or null")
    private Long dni;

    @NotBlank(message = "Firstname must not be blank or null")
    private String firstname;

    @NotBlank(message = "Lastname must not be blank or null")
    private String lastname;

    @JsonIgnore
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateHourAdmission;

    @NotNull(message = "Address must not be blank or null")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_address", referencedColumnName = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Turn> turns = new HashSet<>();

    @NotNull(message = "User must not be blank or null")
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "user_id", nullable = false)
    private User user;
}
