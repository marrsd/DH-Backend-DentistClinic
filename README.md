# Dentist Clinic

Dentist Clinic es un ejercicio integrador para la materia Backend de la carrera Certified Tech Developer.

Consiste en un sistema que permite administrar la reserva de turnos de una clínica odontológica.

## Índice

[Requerimientos](#requerimientos)
[Documentación](#documentación)
[Dentist Clinic - API](#dentist-clinic---api)

## Requerimientos

* Existan dos tipos de usuarios:

  * USER
  * ADMIN

* Se debe permitir que cualquier usuario con role USER y debidamente logueado, pueda registrar, actualizar y  eliminar un turno.

* Se debe permitir que cualquier usuario con role ADMIN y debidamente logueado, pueda gestionar odontólogos, pacientes y turnos.

  * Un usuario con role ADMIN debe generar un usuario con role USER único, para asignarlo ya sea al paciente o al odontólogo, necesario para su respectiva creación.

  * Administrar datos de los odontólogos: listar, agregar, modificar y eliminar odontólogos. Registrar apellido, nombre y matrícula de los mismos.

  * Administrar datos de los pacientes: listar, agregar, modificar y eliminar pacientes. De cada uno se almacenan: nombre, apellido, datos de dirección, DNI y fecha de alta.

  * Registrar turnos: se tiene que poder permitir asignar a un paciente un turno con un odontólogo a una determinada fecha y hora.

## Documentación

  * Swagger Link: 
  ```
  http://localhost:8080/swagger-ui.html
  ```
  * [Base de datos](https://github.com/marrsd/DH-Backend-DentistClinic/tree/main/database/dentistClinic.sql)

## Dentist Clinic - API

  ## Endpoints para usuarios

  **Registrar usuario:**

  * Para registrar un usuario debe ingresar username, password e isAdmin.
  * Realizar un `POST` request a `http://localhost/user/singUp`.
  
    Example para usuario ADMIN:
    ```
    {
      "username": "UserA",
      "password": "3hd7sw",
      "isAdmin": true
    }
    ```

    Example para usuario USER:
    ```
    {
      "username": "UserU7rj",
      "password": "jd83j",
      "isAdmin": false
    }
    ```

  **Login:**

  * Para loguearse debe ingresar username y password ya previamente registrado. 
  * Realizar un `POST` request a `http://localhost/authenticate`.

    Example:
    ```
    {
      "username": "UserA",
      "password": "3hd7sw"
    }
    ```
  * Usar el `Token` devuelto en el Response dentro del `Header - Authorization` para que permita las demás acciones.

  ## Endpoints para odontólogos

  * Usar el `Token` devuelto en el Login y anexarlo al `Authorization` de la request.
  * Un usuario ADMIN debe estar previamente registrado y logueado.

  **Crear un odontólogo:**

  * Para crear un odontólogo, se debió haber creado un usuario USER para ser asignado al odontólogo. 
  * Realizar un `POST` request a `http://localhost/dentists/new`. 

    Example:
    ```
    {
      "dni": 1234,
      "registrationNumber": 1263722,
      "firstname": "Jane",
      "lastname": "Doe",
      "user": {
        "username": "UserU7rj",
        "isAdmin": false
      }
    }
    ```

  **Listar todos los odontólogos:**

  * Realizar un `GET` request a `http://localhost/dentists/all`.

  **Obtener un odontólogo por su id:**

  * Realizar un `GET` request a `http://localhost/dentists/id/{id}`.

    Example: `http://localhost/dentists/id/1`
  
  **Obtener un odontólogo por su número de matrícula:**

  * Realizar un `GET` request a `http://localhost/dentists/registrationNumber/{registrationNumber}`.

    Example: `http://localhost/dentists/registrationNumber/1263722`

  **Obtener un odontólogo por su nombre y apellido:**

  * Realizar un `GET` request a `http://localhost/dentists/fullName/{firstname}/{lastname}`.

    Example: `http://localhost/dentists/fullName/Jane/Doe`
  
  **Actualizar un odontólogo:**

  * Realizar un `PUT` request a `http://localhost/dentists/update`.

    Example
    ```
    {
      "id": 1,
      "dni": 1234,
      "registrationNumber": 1263722,
      "firstname": "Jane",
      "lastname": "Doe",
      "user": {
        "username": "UserU7rj",
        "isAdmin": false
      }
    }
    ```

  **Eliminar un odontólogo por su id:**

  * Realizar un `DELETE` request a `http://localhost/dentists/delete/{id}`.

    Example: `http://localhost/dentists/delete/1`

  **Eliminar un odontólogo por su número de matrícula:**

  * Realizar un `DELETE` request a `http://localhost/dentists/delete/registrationNumber/{registrationNumber}`.

    Example: `http://localhost/dentists/delete/registrationNumber/1263722`

  ## Endpoints para pacientes

  * Usar el `Token` devuelto en el Login y anexarlo al `Authorization` de la request.
  * Un usuario ADMIN debe estar previamente registrado y logueado.

  **Crear un paciente:**

  * Para crear un paciente, se debió haber creado un usuario USER para ser asignado al paciente. 
  * Realizar un `POST` request a `http://localhost/patients/new`. 

    Example:
    ```
    {
      "dni": 738946,
      "firstname": "Joe",
      "lastname": "Bloggs",
      "address": {
        "street": "Green",
        "number": 1,
        "locality": "Blue",
        "province": "Nature"
      },
      "user": {
        "username": "UserU8id",
        "isAdmin": false
      }
    }
    ```

  **Listar todos los pacientes:**

  * Realizar un `GET` request a `http://localhost/patients/all`.

  **Obtener un paciente por su id:**

  * Realizar un `GET` request a `http://localhost/patients/id/{id}`.

    Example: `http://localhost/patients/id/1`
  
  **Obtener un paciente por su DNI:**

  * Realizar un `GET` request a `http://localhost/patients/dni/{dni}`.

    Example: `http://localhost/patients/dni/738946`
  
  **Actualizar un paciente:**

  * Realizar un `PUT` request a `http://localhost/patients/update`.

    Example
    ```
    {
      "id": 1,
      "dni": 738946,
      "firstname": "Joe",
      "lastname": "Bloggs",
      "address": {
        "id": 1,
        "street": "Green",
        "number": 1,
        "locality": "Blue",
        "province": "Nature"
      },
      "user": {
        "username": "UserU8id",
        "isAdmin": false
      }
    }
    ```

  **Eliminar un paciente por su id:**

  * Realizar un `DELETE` request a `http://localhost/patients/delete/{id}`.

    Example: `http://localhost/patients/delete/1`

  **Eliminar un paciente por su DNI:**

  * Realizar un `DELETE` request a `http://localhost/patients/delete/dni/{dni}`.

    Example: `http://localhost/patients/delete/dni/738946`

  ## Endpoints para turnos

  * Usar el `Token` devuelto en el Login y anexarlo al `Authorization` de la request.
  * Un usuario ADMIN o un usuario USER deben estar previamente registrados y logueados.

  **Crear un turno:**

  * Para crear un turno, uno o más pacientes u odontológos deben estar previamente creados.
  * Realizar un `POST` request a `http://localhost/turns/new`. 

    Example:
    ```
    {
      "patient": {
        "id": 1,
        "dni": 738946,
        "firstname": "Joe",
        "lastname": "Bloggs",
        "address": {
          "id": 1,
          "street": "Green",
          "number": 1,
          "locality": "Blue",
          "province": "Nature"
        },
        "user": {
          "username": "UserU8id",
          "isAdmin": false
        }
      },
      "dentist": {
        "id": 1,
        "dni": 1234,
        "registrationNumber": 1263722,
        "firstname": "Jane",
        "lastname": "Doe",
        "user": {
          "username": "UserU",
          "isAdmin": false
        }
      },
      "dateHour": "2023-07-17 15:15:00"
    }
    ```

  **Listar todos los turnos:**

  * Esta acción solo debe ser ejecutada por un usuario ADMIN
  * Realizar un `GET` request a `http://localhost/turns/all`.

  **Obtener un turno por su id:**

  * Realizar un `GET` request a `http://localhost/turns/id/{id}`.

    Example: `http://localhost/turns/id/1`
  
  **Obtener un turno por el número de matrícula del odontólogo:**

  * Realizar un `GET` request a `http://localhost/turns/dentistRegistrationNumber/{registrationNumber}`.

    Example: `http://localhost/turns/dentistRegistrationNumber/1263722`

  **Obtener un turno por el DNI del paciente:**

  * Realizar un `GET` request a `http://localhost/turns/patientDni/{dni}`.

    Example: `http://localhost/turns/patientDni/738946`
  
  **Actualizar un turno:**

  * Realizar un `PUT` request a `http://localhost/turns/update`.

    Example:
    ```
    {
      "id": 1,
      "patient": {
        "id": 1,
        "dni": 738946,
        "firstname": "Joe",
        "lastname": "Bloggs",
        "address": {
          "id": 1,
          "street": "Green",
          "number": 1,
          "locality": "Blue",
          "province": "Nature"
        },
        "user": {
          "username": "UserU8id",
          "isAdmin": false
        }
      },
      "dentist": {
        "id": 1,
        "dni": 1234,
        "registrationNumber": 1263722,
        "firstname": "Jane",
        "lastname": "Doe",
        "user": {
          "username": "UserU",
          "isAdmin": false
        }
      },
      "dateHour": "2023-08-17 14:00:00"
    }
    ```

  **Eliminar un turno por su id:**

  * Realizar un `DELETE` request a `http://localhost/turns/delete/{id}`.

    Example: `http://localhost/turns/delete/1`