package com.gourav.restapi.controllers.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class PetResponse {
    private String id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String color;
    private String adoptionStatus;
    private Instant createdAt;
    private Instant updatedAt;
}
