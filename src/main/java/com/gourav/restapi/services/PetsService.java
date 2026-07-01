package com.gourav.restapi.services;

import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.request.UpdatePetRequest;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PetsService {
    private final PetsRepository petsRepository;

    public PetsService(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    public List<PetResponse> getAllPets() {
        return petsRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PetResponse getPetById(String id) {
        return toResponse(findPet(id));
    }

    public PetResponse createPet(CreatePetRequest request) {
        Pets pet = new Pets(request.getName(), request.getSpecies(), request.getBreed());
        return toResponse(petsRepository.save(pet));
    }

    public PetResponse updatePet(String id, UpdatePetRequest request) {
        Pets pet = findPet(id);
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        return toResponse(petsRepository.save(pet));
    }

    public void deletePet(String id) {
        Pets pet = findPet(id);
        petsRepository.delete(pet);
    }

    private Pets findPet(String id) {
        return petsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));
    }

    private PetResponse toResponse(Pets pet) {
        return new PetResponse(pet.getId(), pet.getName(), pet.getSpecies(), pet.getBreed());
    }
}
