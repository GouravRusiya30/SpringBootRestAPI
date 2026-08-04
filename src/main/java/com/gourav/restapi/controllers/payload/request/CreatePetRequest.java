package com.gourav.restapi.controllers.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePetRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String species;

    @NotBlank
    @Size(max = 100)
    private String breed;

    private Integer age;

    private String color;

    private String adoptionStatus;
}
