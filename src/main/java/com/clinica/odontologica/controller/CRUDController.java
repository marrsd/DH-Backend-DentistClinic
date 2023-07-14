package com.clinica.odontologica.controller;

import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.exception.IntegrityDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CRUDController<T> {
    ResponseEntity<HttpStatus> register(@RequestBody T t) throws ResourceNotFoundException, IntegrityDataException,
            DataAlreadyExistsException, NoSuchDataExistsException;

    ResponseEntity<List<T>> ListAll() throws ResourceNotFoundException;

    ResponseEntity<T> searchById(@PathVariable Long id)
            throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException;

    ResponseEntity<T> modify(@RequestBody T t) throws ResourceNotFoundException, IntegrityDataException,
            NoSuchDataExistsException, DataAlreadyExistsException;

    ResponseEntity<String> remove(@PathVariable Long id)
            throws ResourceNotFoundException, IntegrityDataException, NoSuchDataExistsException;
}
