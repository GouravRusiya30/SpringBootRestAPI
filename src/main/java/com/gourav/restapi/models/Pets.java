package com.gourav.restapi.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "pets")
public class Pets {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String species;

    @NotBlank
    private String breed;

    private Integer age;

    private String color;

    @Builder.Default
    private String adoptionStatus = "AVAILABLE";

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
