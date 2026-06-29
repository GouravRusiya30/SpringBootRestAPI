package com.gourav.restapi.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Pets() {}

    public Pets(String id, String name, String species, String breed) {
      this.id = id;
      this.name = name;
      this.species = species;
      this.breed = breed;
    }

    public Pets(String name, String species, String breed) {
        this.name = name;
        this.species = species;
        this.breed = breed;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
}
