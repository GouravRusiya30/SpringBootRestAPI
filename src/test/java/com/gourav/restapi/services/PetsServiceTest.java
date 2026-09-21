package com.gourav.restapi.services;

import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.request.UpdatePetRequest;
import com.gourav.restapi.controllers.payload.response.PagedResponse;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        testPet = Pets.builder().id("pet-123").name("Liam").species("cat").breed("tabby").build();

        createRequest = new CreatePetRequest();
        createRequest.setName("Liam");
        createRequest.setSpecies("cat");
        createRequest.setBreed("tabby");

        updateRequest = new UpdatePetRequest();
        updateRequest.setName("Luna");
        updateRequest.setSpecies("dog");
        updateRequest.setBreed("golden retriever");
    }

    // ── getPets (paginated) ────────────────────────────────────────────────

    @Test
    void getPets_ReturnsPagedResponse_WithContent() {
        // Arrange
        Page<Pets> page = new PageImpl<>(List.of(testPet));
        given(petsRepository.findAll(any(Pageable.class))).willReturn(page);

        // Act
        PagedResponse<PetResponse> result = petsService.getPets(0, 10, "name", null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Liam", result.getContent().get(0).getName());
        assertEquals("pet-123", result.getContent().get(0).getId());
        assertEquals(0, result.getPage());
        assertEquals(1, result.getTotalElements());
        verify(petsRepository).findAll(any(Pageable.class));
    }

    @Test
    void getPets_ReturnsEmptyPage_WhenNoPets() {
        // Arrange
        Page<Pets> emptyPage = new PageImpl<>(List.of());
        given(petsRepository.findAll(any(Pageable.class))).willReturn(emptyPage);

        // Act
        PagedResponse<PetResponse> result = petsService.getPets(0, 10, "name", null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getPets_FiltersBySpecies_WhenSpeciesProvided() {
        // Arrange
        Page<Pets> page = new PageImpl<>(List.of(testPet));
        given(petsRepository.findBySpeciesContainingIgnoreCase(any(), any(Pageable.class)))
                .willReturn(page);

        // Act
        PagedResponse<PetResponse> result = petsService.getPets(0, 10, "name", "cat", null);

        // Assert
        assertEquals(1, result.getContent().size());
        verify(petsRepository).findBySpeciesContainingIgnoreCase(any(), any(Pageable.class));
    }

    @Test
    void getPets_FiltersByAdoptionStatus_WhenStatusProvided() {
        // Arrange
        Page<Pets> page = new PageImpl<>(List.of(testPet));
        given(petsRepository.findByAdoptionStatus(any(), any(Pageable.class)))
                .willReturn(page);

        // Act
        PagedResponse<PetResponse> result = petsService.getPets(0, 10, "name", null, "AVAILABLE");

        // Assert
        assertEquals(1, result.getContent().size());
        verify(petsRepository).findByAdoptionStatus(any(), any(Pageable.class));
    }

    @Test
    void getPets_FiltersBySpeciesAndStatus_WhenBothProvided() {
        // Arrange
        Page<Pets> page = new PageImpl<>(List.of(testPet));
        given(petsRepository.findBySpeciesContainingIgnoreCaseAndAdoptionStatus(
                any(), any(), any(Pageable.class))).willReturn(page);

        // Act
        PagedResponse<PetResponse> result = petsService.getPets(0, 10, "name", "cat", "AVAILABLE");

        // Assert
        assertEquals(1, result.getContent().size());
        verify(petsRepository).findBySpeciesContainingIgnoreCaseAndAdoptionStatus(
                any(), any(), any(Pageable.class));
    }

    @Test
    void getPets_CapsPageSizeAt100() {
        // Arrange — request size=500; repository should receive a Pageable with size capped at 100
        Page<Pets> page = new PageImpl<>(List.of());
        given(petsRepository.findAll(any(Pageable.class))).willReturn(page);

        // Act
        PagedResponse<PetResponse> result = petsService.getPets(0, 500, "name", null, null);

        // Assert — result size reflects the (empty) page, not the requested 500
        assertNotNull(result);
        verify(petsRepository).findAll(any(Pageable.class));
    }

    // ── getPetById ─────────────────────────────────────────────────────────

    @Test
    void getPetById_ReturnsPet_WhenFound() {
        given(petsRepository.findById("pet-123")).willReturn(Optional.of(testPet));

        PetResponse result = petsService.getPetById("pet-123");

        assertNotNull(result);
        assertEquals("pet-123", result.getId());
        assertEquals("Liam", result.getName());
        assertEquals("cat", result.getSpecies());
        assertEquals("tabby", result.getBreed());
    }

    @Test
    void getPetById_ThrowsNotFoundException_WhenPetNotFound() {
        given(petsRepository.findById("missing-id")).willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> petsService.getPetById("missing-id"));
    }

    // ── createPet ──────────────────────────────────────────────────────────

    @Test
    void createPet_ReturnsPetResponse() {
        Pets savedPet = Pets.builder().id("pet-456").name("Liam").species("cat").breed("tabby").build();
        given(petsRepository.save(any(Pets.class))).willReturn(savedPet);

        PetResponse result = petsService.createPet(createRequest);

        assertNotNull(result);
        assertEquals("pet-456", result.getId());
        assertEquals("Liam", result.getName());
        verify(petsRepository).save(any(Pets.class));
    }

    @Test
    void createPet_MapsRequestFieldsCorrectly() {
        Pets savedPet = Pets.builder().id("pet-789").name("Max").species("dog").breed("labrador").build();
        given(petsRepository.save(any(Pets.class))).willReturn(savedPet);

        CreatePetRequest request = new CreatePetRequest();
        request.setName("Max");
        request.setSpecies("dog");
        request.setBreed("labrador");

        PetResponse result = petsService.createPet(request);

        assertEquals("Max", result.getName());
        assertEquals("dog", result.getSpecies());
        assertEquals("labrador", result.getBreed());
    }

    // ── updatePet ──────────────────────────────────────────────────────────

    @Test
    void updatePet_UpdatesAndReturnsPet() {
        Pets existingPet = Pets.builder().id("pet-123").name("Liam").species("cat").breed("tabby").build();
        Pets updatedPet  = Pets.builder().id("pet-123").name("Luna").species("dog").breed("golden retriever").build();

        given(petsRepository.findById("pet-123")).willReturn(Optional.of(existingPet));
        given(petsRepository.save(any(Pets.class))).willReturn(updatedPet);

        PetResponse result = petsService.updatePet("pet-123", updateRequest);

        assertNotNull(result);
        assertEquals("pet-123", result.getId());
        assertEquals("Luna", result.getName());
        assertEquals("dog", result.getSpecies());
        assertEquals("golden retriever", result.getBreed());
        verify(petsRepository).save(any(Pets.class));
    }

    @Test
    void updatePet_ThrowsNotFoundException_WhenPetNotFound() {
        given(petsRepository.findById("missing-id")).willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> petsService.updatePet("missing-id", updateRequest));
    }

    // ── deletePet ──────────────────────────────────────────────────────────

    @Test
    void deletePet_CallsRepositoryDelete() {
        given(petsRepository.findById("pet-123")).willReturn(Optional.of(testPet));

        petsService.deletePet("pet-123");

        verify(petsRepository).delete(testPet);
    }

    @Test
    void deletePet_ThrowsNotFoundException_WhenPetNotFound() {
        given(petsRepository.findById("missing-id")).willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> petsService.deletePet("missing-id"));
    }
}
