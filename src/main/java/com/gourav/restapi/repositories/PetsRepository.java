package com.gourav.restapi.repositories;

import com.gourav.restapi.models.Pets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PetsRepository extends MongoRepository<Pets, String> {

    /** Filter by species (case-insensitive partial match). */
    Page<Pets> findBySpeciesContainingIgnoreCase(String species, Pageable pageable);

    /** Filter by exact adoption status (e.g. AVAILABLE, ADOPTED, PENDING). */
    Page<Pets> findByAdoptionStatus(String adoptionStatus, Pageable pageable);

    /** Filter by both species and adoption status combined. */
    Page<Pets> findBySpeciesContainingIgnoreCaseAndAdoptionStatus(
            String species, String adoptionStatus, Pageable pageable);
}
