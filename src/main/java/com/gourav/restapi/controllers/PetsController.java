package com.gourav.restapi.controllers;

import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.request.UpdatePetRequest;
import com.gourav.restapi.controllers.payload.response.PagedResponse;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.services.PetsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pets")
public class PetsController {

    private final PetsService petsService;

    public PetsController(PetsService petsService) {
        this.petsService = petsService;
    }

    /**
     * Returns a paginated, filterable list of pets.
     *
     * <p>Query parameters:
     * <ul>
     *   <li>{@code page}           — page number, 0-indexed (default: 0)</li>
     *   <li>{@code size}           — items per page, max 100 (default: 10)</li>
     *   <li>{@code sortBy}         — field to sort by (default: name)</li>
     *   <li>{@code species}        — optional partial species filter (case-insensitive)</li>
     *   <li>{@code adoptionStatus} — optional exact status filter (AVAILABLE, ADOPTED, PENDING)</li>
     * </ul>
     *
     * <p>Examples:
     * <pre>
     *   GET /pets/?page=0&amp;size=10
     *   GET /pets/?species=dog
     *   GET /pets/?adoptionStatus=AVAILABLE&amp;page=1&amp;size=5
     *   GET /pets/?species=cat&amp;adoptionStatus=AVAILABLE
     * </pre>
     */
    @GetMapping(value = "/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public PagedResponse<PetResponse> getAllPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String adoptionStatus) {
        return petsService.getPets(page, size, sortBy, species, adoptionStatus);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public PetResponse getPetById(@PathVariable("id") String id) {
        return petsService.getPetById(id);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public PetResponse modifyPetById(@PathVariable("id") String id,
                                     @Valid @RequestBody UpdatePetRequest request) {
        return petsService.updatePet(id, request);
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody CreatePetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petsService.createPet(request));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable String id) {
        petsService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
