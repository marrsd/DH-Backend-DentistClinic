package com.clinica.odontologica.model.domain;

import com.clinica.odontologica.model.domain.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Dentist")
public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dentist_id")
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Dni must not be blank or null")
    private Long dni;

    @Column(unique = true)
    @NotNull(message = "Registration number must not be blank or null")
    private Long registrationNumber;

    @NotBlank(message = "Firstname must not be blank or null")
    private String firstname;

    @NotBlank(message = "Lastname must not be blank ot null")
    private String lastname;

    @OneToMany(mappedBy = "dentist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Turn> turns = new HashSet<>();

    @NotNull(message = "User must not be blank or null. You need to create one first")
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", referencedColumnName = "user_id", nullable = false)
    private User user;
}
