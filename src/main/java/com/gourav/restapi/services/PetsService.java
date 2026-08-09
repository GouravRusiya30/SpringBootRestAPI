package com.gourav.restapi.services;

import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.request.UpdatePetRequest;
import com.gourav.restapi.controllers.payload.response.PagedResponse;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PetsService {

    private final PetsRepository petsRepository;

    public PetsService(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    /**
     * Returns a paginated, optionally filtered list of pets.
     *
     * @param page           page number (0-indexed)
     * @param size           page size (max 100)
     * @param sortBy         field to sort by (default: name)
     * @param species        optional filter by species (case-insensitive, partial match)
     * @param adoptionStatus optional filter by adoption status (e.g. AVAILABLE, ADOPTED)
     */
    public PagedResponse<PetResponse> getPets(int page, int size, String sortBy,
                                              String species, String adoptionStatus) {
        // Cap page size to prevent abuse
        size = Math.min(size, 100);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Pets> petsPage;

        boolean hasSpecies = StringUtils.hasText(species);
        boolean hasStatus  = StringUtils.hasText(adoptionStatus);

        if (hasSpecies && hasStatus) {
            petsPage = petsRepository.findBySpeciesContainingIgnoreCaseAndAdoptionStatus(
                    species, adoptionStatus, pageable);
        } else if (hasSpecies) {
            petsPage = petsRepository.findBySpeciesContainingIgnoreCase(species, pageable);
        } else if (hasStatus) {
            petsPage = petsRepository.findByAdoptionStatus(adoptionStatus, pageable);
        } else {
            petsPage = petsRepository.findAll(pageable);
        }

        List<PetResponse> content = petsPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return PagedResponse.<PetResponse>builder()
                .content(content)
                .page(petsPage.getNumber())
                .size(petsPage.getSize())
                .totalElements(petsPage.getTotalElements())
                .totalPages(petsPage.getTotalPages())
                .last(petsPage.isLast())
                .build();
    }

    public PetResponse getPetById(String id) {
        return toResponse(findPet(id));
    }

    public PetResponse createPet(CreatePetRequest request) {
        Pets pet = Pets.builder()
                .name(request.getName())
                .species(request.getSpecies())
                .breed(request.getBreed())
                .age(request.getAge())
                .color(request.getColor())
                .adoptionStatus(request.getAdoptionStatus() != null ? request.getAdoptionStatus() : "AVAILABLE")
                .build();
        return toResponse(petsRepository.save(pet));
    }

    public PetResponse updatePet(String id, UpdatePetRequest request) {
        Pets pet = findPet(id);
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        if (request.getAge() != null) {
            pet.setAge(request.getAge());
        }
        if (request.getColor() != null) {
            pet.setColor(request.getColor());
        }
        if (request.getAdoptionStatus() != null) {
            pet.setAdoptionStatus(request.getAdoptionStatus());
        }
        return toResponse(petsRepository.save(pet));
    }

    public void deletePet(String id) {
        Pets pet = findPet(id);
        petsRepository.delete(pet);
    }

    private Pets findPet(String id) {
        return petsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found with id: " + id));
    }

    private PetResponse toResponse(Pets pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .age(pet.getAge())
                .color(pet.getColor())
                .adoptionStatus(pet.getAdoptionStatus())
                .createdAt(pet.getCreatedAt())
                .updatedAt(pet.getUpdatedAt())
                .build();
    }
}
