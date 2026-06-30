package com.gourav.restapi.services;

import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.request.UpdatePetRequest;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PetsServiceTest {

    @Mock
    private PetsRepository petsRepository;

    @InjectMocks
    private PetsService petsService;

    private Pets testPet;
    private CreatePetRequest createRequest;
    private UpdatePetRequest updateRequest;

    @BeforeEach
    void setUp() {
        testPet = new Pets("pet-123", "Liam", "cat", "tabby");
        
        createRequest = new CreatePetRequest();
        createRequest.setName("Liam");
        createRequest.setSpecies("cat");
        createRequest.setBreed("tabby");

        updateRequest = new UpdatePetRequest();
        updateRequest.setName("Luna");
        updateRequest.setSpecies("dog");
        updateRequest.setBreed("golden retriever");
    }

    @Test
    void getAllPets_ReturnsAllPets() {
        // Arrange
        List<Pets> petsList = List.of(testPet);
        given(petsRepository.findAll()).willReturn(petsList);

        // Act
        List<PetResponse> result = petsService.getAllPets();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Liam", result.get(0).getName());
        assertEquals("pet-123", result.get(0).getId());
        verify(petsRepository).findAll();
    }

    @Test
    void getAllPets_ReturnsEmptyList_WhenNoProjects() {
        // Arrange
        given(petsRepository.findAll()).willReturn(List.of());

        // Act
        List<PetResponse> result = petsService.getAllPets();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPetById_ReturnsPet_WhenFound() {
        // Arrange
        given(petsRepository.findById("pet-123")).willReturn(Optional.of(testPet));

        // Act
        PetResponse result = petsService.getPetById("pet-123");

        // Assert
        assertNotNull(result);
        assertEquals("pet-123", result.getId());
        assertEquals("Liam", result.getName());
        assertEquals("cat", result.getSpecies());
        assertEquals("tabby", result.getBreed());
    }

    @Test
    void getPetById_ThrowsNotFoundException_WhenPetNotFound() {
        // Arrange
        given(petsRepository.findById("missing-id")).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> petsService.getPetById("missing-id"));
    }

    @Test
    void createPet_ReturnsPetResponse() {
        // Arrange
        Pets savedPet = new Pets("pet-456", "Liam", "cat", "tabby");
        given(petsRepository.save(any(Pets.class))).willReturn(savedPet);

        // Act
        PetResponse result = petsService.createPet(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals("pet-456", result.getId());
        assertEquals("Liam", result.getName());
        verify(petsRepository).save(any(Pets.class));
    }

    @Test
    void updatePet_UpdatesAndReturnsPet() {
        // Arrange
        Pets existingPet = new Pets("pet-123", "Liam", "cat", "tabby");
        Pets updatedPet = new Pets("pet-123", "Luna", "dog", "golden retriever");
        
        given(petsRepository.findById("pet-123")).willReturn(Optional.of(existingPet));
        given(petsRepository.save(any(Pets.class))).willReturn(updatedPet);

        // Act
        PetResponse result = petsService.updatePet("pet-123", updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("pet-123", result.getId());
        assertEquals("Luna", result.getName());
        assertEquals("dog", result.getSpecies());
        assertEquals("golden retriever", result.getBreed());
        verify(petsRepository).save(any(Pets.class));
    }

    @Test
    void updatePet_ThrowsNotFoundException_WhenPetNotFound() {
        // Arrange
        given(petsRepository.findById("missing-id")).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, 
                     () -> petsService.updatePet("missing-id", updateRequest));
    }

    @Test
    void deletePet_CallsRepositoryDelete() {
        // Arrange
        given(petsRepository.findById("pet-123")).willReturn(Optional.of(testPet));

        // Act
        petsService.deletePet("pet-123");

        // Assert
        verify(petsRepository).delete(testPet);
    }

    @Test
    void deletePet_ThrowsNotFoundException_WhenPetNotFound() {
        // Arrange
        given(petsRepository.findById("missing-id")).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> petsService.deletePet("missing-id"));
    }

    @Test
    void createPet_MapsRequestCorrectly() {
        // Arrange
        Pets savedPet = new Pets("pet-789", "Max", "dog", "labrador");
        given(petsRepository.save(any(Pets.class))).willReturn(savedPet);

        CreatePetRequest request = new CreatePetRequest();
        request.setName("Max");
        request.setSpecies("dog");
        request.setBreed("labrador");

        // Act
        PetResponse result = petsService.createPet(request);

        // Assert
        assertEquals("Max", result.getName());
        assertEquals("dog", result.getSpecies());
        assertEquals("labrador", result.getBreed());
    }
}
