package com.gourav.restapi.utils;

import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DbSeeder {
    @Autowired
    PetsRepository petsRepository;

    @EventListener
    public void savePets(ApplicationReadyEvent event) {
        petsRepository.deleteAll();
        List<Pets> petsList = Arrays.asList(new Pets("Spike", "Dog", "Bulldog"),
                new Pets("Tom", "Cat", "Regular"),
                new Pets("Jerry", "Mouse", "special"));

        petsRepository.saveAll(petsList);
    }
}
