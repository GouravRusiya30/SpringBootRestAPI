package com.gourav.restapi.controllers.payload.response;

public class PetResponse {
    private final String id;
    private final String name;
    private final String species;
    private final String breed;

    public PetResponse(String id, String name, String species, String breed) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }
}
