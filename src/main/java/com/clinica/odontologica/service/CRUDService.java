package com.clinica.odontologica.service;

import com.clinica.odontologica.exception.NoSuchDataExistsException;
import com.clinica.odontologica.exception.DataAlreadyExistsException;
import com.clinica.odontologica.exception.ResourceNotFoundException;
import com.clinica.odontologica.exception.IntegrityDataException;

import java.util.List;

public interface CRUDService<T> {
    T create(T t) throws IntegrityDataException, DataAlreadyExistsException, NoSuchDataExistsException;
    List<T> getAll() throws ResourceNotFoundException;
    T getById(Long id) throws IntegrityDataException, NoSuchDataExistsException;
    T update(T t) throws IntegrityDataException, NoSuchDataExistsException, DataAlreadyExistsException;
    void delete(Long id) throws IntegrityDataException, NoSuchDataExistsException;
}
