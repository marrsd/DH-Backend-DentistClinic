package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.domain.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    private Address address;

    @BeforeEach
    public void init() {
        address = new Address();
        address.setId(1L);
        address.setNumber(12);
        address.setLocality("North");
        address.setProvince("Campestre");
        address.setStreet("14A");
    }

    @AfterEach
    public void destroyAll() {
        addressRepository.deleteAll();
    }

    @Test
    public void createAddressTest() {
        Address address1 = addressRepository.save(address);
        assertEquals(address1, address);
    }

    @Test
    public void getAllAddressesTest() {
        addressRepository.save(address);
        List<Address> addressList = addressRepository.findAll();

        assertEquals(addressList.size(), 1);
    }

    @Test
    public void getByIdAddressTest() {
        Address address1 = addressRepository.save(address);
        Optional<Address> address2 = addressRepository.findById(address1.getId());

        assertNotNull(address2);
    }

    @Test
    public void getAddressByStreetAndNumberTest() {
        Address address1 = addressRepository.save(address);
        Optional<Address> address2 = addressRepository.getByStreetAndNumber(address1.getStreet(), address1.getNumber());

        assertEquals(address1, address2.get());
    }

    @Test()
    public void deleteAddressByIdTest() {
        Address address1 = addressRepository.save(address);

        addressRepository.deleteById(address1.getId());
        Optional<Address> address2 = addressRepository.findById(address1.getId());

        assertEquals(Optional.empty(), address2);
    }
}