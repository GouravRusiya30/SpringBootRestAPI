package com.gourav.restapi.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.gourav.restapi.models.Pets;

public interface PetsRepository extends MongoRepository<Pets, String> {
}
