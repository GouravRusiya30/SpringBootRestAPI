package com.gourav.restapi.utils;

import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the pets collection with sample data on startup.
 * Only runs on the "local" profile and ONLY if the collection is empty,
 * so existing data is never wiped.
 */
@Component
@Profile("local")
public class DbSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DbSeeder.class);

    private final PetsRepository petsRepository;

    public DbSeeder(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    @EventListener
    public void savePets(ApplicationReadyEvent event) {
        if (petsRepository.count() > 0) {
            logger.info("DbSeeder: pets collection already has data — skipping seed.");
            return;
        }

        logger.info("DbSeeder: seeding pets collection with sample data...");
        List<Pets> petsList = List.of(
                Pets.builder().name("Spike").species("Dog").breed("Bulldog")
                        .age(3).color("Brown").adoptionStatus("AVAILABLE").build(),
                Pets.builder().name("Tom").species("Cat").breed("Regular")
                        .age(2).color("Grey").adoptionStatus("AVAILABLE").build(),
                Pets.builder().name("Jerry").species("Mouse").breed("Special")
                        .age(1).color("Brown").adoptionStatus("ADOPTED").build()
        );

        petsRepository.saveAll(petsList);
        logger.info("DbSeeder: seeded {} pets.", petsList.size());
    }
}
