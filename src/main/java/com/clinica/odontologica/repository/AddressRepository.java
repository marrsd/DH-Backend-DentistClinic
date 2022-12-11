package com.clinica.odontologica.repository;

import com.clinica.odontologica.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    public Optional<Address> getByStreetAndNumber(String street, Integer number);
}
