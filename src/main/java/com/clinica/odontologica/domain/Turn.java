package com.clinica.odontologica.domain;

import com.clinica.odontologica.util.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="Turns")
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "turn_id")
    private Long id;

    @NotNull(message = "Patient must not be blank or null")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="id_patient", referencedColumnName = "patient_id",  nullable = false)
    private Patient patient;

    @NotNull(message = "Dentist must not be blank or null")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name="id_dentist", referencedColumnName = "dentist_id", nullable = false)
    private Dentist dentist;

    @FutureOrPresent(message = "Date and hour of the turn must not be blank or null. Date and hour must not be past. Date and hour format must be yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateHour;
}
