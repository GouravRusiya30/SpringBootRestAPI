package com.gourav.restapi.controllers.payload.request;

import jakarta.validation.constraints.Min;
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

    @Min(0)
    private Integer age;

    @Size(max = 50)
    private String color;

    /** One of: AVAILABLE, ADOPTED, PENDING. Defaults to AVAILABLE if not provided. */
    private String adoptionStatus;
}
