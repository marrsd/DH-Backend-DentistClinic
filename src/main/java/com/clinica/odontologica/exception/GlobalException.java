package com.clinica.odontologica.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalException {

    private static final Logger LOGGER = LogManager.getLogger(GlobalException.class);

    @ExceptionHandler({ConstraintViolationException.class })
    public ResponseEntity<String> constraintViolationException(ConstraintViolationException e) {
        LOGGER.error(e.getMessage());

        final List<String> errors = new ArrayList<String>();
        List<Class<?>> classes =  new ArrayList<Class<?>>();

        for (final ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        for (final ConstraintViolation<?> violation : e.getConstraintViolations()) {
            classes.add(violation.getRootBeanClass());
        }
        String[] str = classes.get(0).getName().split("\\.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: " + str[4] + ": " + errors);
    }

    @ExceptionHandler({ DataAlreadyExistsException.class })
    public ResponseEntity<String> personAlreadyExixtsException(DataAlreadyExistsException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler({ NoSuchDataExistsException.class })
    public ResponseEntity<String> noSuchPersonExixtsException(NoSuchDataExistsException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<String> notFoundException(ResourceNotFoundException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler({ IntegrityDataException.class })
    public ResponseEntity<String> integrityDataException(IntegrityDataException e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<String> ilegalArgumentException(IllegalArgumentException  e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<String> otherExceptionsHandler(Exception e, WebRequest request) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
    }

}
