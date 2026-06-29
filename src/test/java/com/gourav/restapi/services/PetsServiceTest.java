package com.gourav.restapi.services;

import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.request.UpdatePetRequest;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PetsServiceTest {
    @Mock
    private PetsRepository petsRepository;

    @InjectMocks
    private PetsService petsService;

    @Test
    void getAllPetsMapsEntitiesToResponses() {
        given(petsRepository.findAll())
                .willReturn(List.of(new Pets("pet-1", "Liam", "cat", "tabby")));

        List<PetResponse> pets = petsService.getAllPets();

        assertEquals(1, pets.size());
        assertEquals("pet-1", pets.get(0).getId());
        assertEquals("Liam", pets.get(0).getName());
    }

    @Test
    void createPetPersistsAndReturnsResponse() {
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Liam");
        request.setSpecies("cat");
        request.setBreed("tabby");

        given(petsRepository.save(org.mockito.ArgumentMatchers.any(Pets.class)))
                .willReturn(new Pets("pet-1", "Liam", "cat", "tabby"));

        PetResponse response = petsService.createPet(request);

        assertEquals("pet-1", response.getId());
        assertEquals("tabby", response.getBreed());
    }

    @Test
    void updatePetUpdatesExistingEntity() {
        UpdatePetRequest request = new UpdatePetRequest();
        request.setName("Luna");
        request.setSpecies("cat");
        request.setBreed("siamese");

        given(petsRepository.findById("pet-1"))
                .willReturn(Optional.of(new Pets("pet-1", "Liam", "cat", "tabby")));
        given(petsRepository.save(org.mockito.ArgumentMatchers.any(Pets.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        PetResponse response = petsService.updatePet("pet-1", request);

        assertEquals("Luna", response.getName());
        assertEquals("siamese", response.getBreed());
    }

    @Test
    void deletePetDeletesExistingEntity() {
        Pets pet = new Pets("pet-1", "Liam", "cat", "tabby");
        given(petsRepository.findById("pet-1")).willReturn(Optional.of(pet));

        petsService.deletePet("pet-1");

        verify(petsRepository).delete(pet);
    }

    @Test
    void getPetByIdThrowsWhenPetDoesNotExist() {
        given(petsRepository.findById("missing")).willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> petsService.getPetById("missing"));
    }
}
