package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.model.domain.Address;
import com.clinica.odontologica.exception.IntegrityDataException;
import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.model.dto.AddressDTO;
import com.clinica.odontologica.repository.AddressRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    AddressRepository addressRepository;

    private AddressDTO addressDTO;

    private Address address;

    @BeforeEach
    public void init() {
        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setStreet("27A");
        addressDTO.setNumber(9);
        addressDTO.setLocality("Sur");
        addressDTO.setProvince("Floresta");

        address = new Address();
        address.setId(1L);
        address.setStreet("27A");
        address.setNumber(9);
        address.setLocality("Sur");
        address.setProvince("Floresta");

    }

    @Test
    @Order(1)
    public void createAddressTest() throws Exception {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO addressDTO1 = addressService.create(addressDTO);

        assertTrue(addressDTO1 != null);
    }

    @Test
    @Order(2)
    public void badRequestCreateAddressTest() {
        addressDTO.setNumber(-9);

        assertThrows(IntegrityDataException.class, () -> addressService.create(addressDTO));
    }

    @Test
    @Order(3)
    public void getAllAddressesTest() throws Exception {
        when(addressRepository.findAll()).thenReturn(Arrays.asList(address));

        List<AddressDTO> patients = addressService.getAll();

        assertNotEquals(patients.size(), 0);
    }

    @Test
    @Order(4)
    public void notFoundGetAllAddressesTest() {
        assertThrows(ResourceNotFoundException.class, () -> addressService.getAll());
    }

    @Test
    @Order(5)
    public void getAddressByIdTest() throws Exception {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        AddressDTO address = addressService.getById(addressDTO.getId());

        assertEquals(address.getProvince(), address.getProvince());
    }

    @Test
    @Order(6)
    public void badRequestGetAddressByNullIdTest() {
        addressDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> addressService.getById(addressDTO.getId()));
    }

    @Test
    @Order(7)
    public void badRequestGetAddressByNegativeIdTest() {
        addressDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class, () -> addressService.getById(addressDTO.getId()));
    }

    @Test
    @Order(8)
    public void notFoundGetAddressByIdTest() {
        addressDTO.setId(80L);

        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> addressService.getById(addressDTO.getId()));
    }

    @Test
    @Order(9)
    public void updateAddressTest() throws Exception {
        String locality = "Norte";

        addressDTO.setLocality(locality);
        address.setLocality(locality);

        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        AddressDTO addressDTO1 = addressService.update(addressDTO);

        assertEquals(addressDTO1.getLocality(), address.getLocality());
    }

    @Test
    @Order(10)
    public void badRequestUpdateNullAddressTest() {
        addressDTO = null;

        assertThrows(IntegrityDataException.class, () -> addressService.update(addressDTO));
    }

    @Test
    @Order(11)
    public void notFoundUpdateAddressTest() {
        assertThrows(NoSuchDataExistsException.class, () -> addressService.update(addressDTO));
    }

    @Test
    @Order(12)
    public void deleteAddressTest() throws Exception {
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        addressService.delete(addressDTO.getId());

        verify(addressRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @Order(13)
    public void notFoundDeleteAddressTest() {
        addressDTO.setId(15L);

        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchDataExistsException.class, () -> addressService.delete(addressDTO.getId()));
    }

    @Test
    @Order(14)
    public void badRequestDeleteAddressWithNullIdTest() {
        addressDTO.setId(null);

        assertThrows(IntegrityDataException.class, () -> addressService.delete(addressDTO.getId()));
    }

    @Test
    @Order(15)
    public void badRequestDeleteAddressWithNegativeId() {
        addressDTO.setId(-1L);

        assertThrows(IllegalArgumentException.class, () -> addressService.delete(addressDTO.getId()));
    }

}