package com.gourav.restapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a required role is not found in the database.
 * This typically means the roles collection has not been seeded.
 * See README.md for the db.roles.insertMany(...) seed command.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String roleName) {
        super("Role '" + roleName + "' not found in the database. " +
              "Please seed the roles collection — see README.md for instructions.");
    }
}
