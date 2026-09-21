package com.gourav.restapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gourav.restapi.config.jwt.JwtUtils;
import com.gourav.restapi.config.services.UserDetailsServiceImpl;
import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.response.PagedResponse;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.services.PetsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetsController.class)
@AutoConfigureMockMvc(addFilters = false)
class PetsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PetsService petsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    // ── GET /pets/ ─────────────────────────────────────────────────────────

    @Test
    void getAllPets_ReturnsPagedResponse() throws Exception {
        PetResponse pet = new PetResponse("pet-1", "Liam", "cat", "tabby",
                null, null, "AVAILABLE", null, null);

        PagedResponse<PetResponse> pagedResponse = PagedResponse.<PetResponse>builder()
                .content(List.of(pet))
                .page(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .last(true)
                .build();

        given(petsService.getPets(eq(0), eq(10), eq("name"), isNull(), isNull()))
                .willReturn(pagedResponse);

        mvc.perform(get("/pets/")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("pet-1"))
                .andExpect(jsonPath("$.content[0].name").value("Liam"))
                .andExpect(jsonPath("$.content[0].species").value("cat"))
                .andExpect(jsonPath("$.content[0].breed").value("tabby"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void getAllPets_PassesFiltersToService() throws Exception {
        PagedResponse<PetResponse> emptyPage = PagedResponse.<PetResponse>builder()
                .content(List.of())
                .page(0).size(10).totalElements(0).totalPages(0).last(true)
                .build();

        given(petsService.getPets(eq(0), eq(10), eq("name"), eq("dog"), eq("AVAILABLE")))
                .willReturn(emptyPage);

        mvc.perform(get("/pets/")
                        .param("species", "dog")
                        .param("adoptionStatus", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));

        verify(petsService).getPets(0, 10, "name", "dog", "AVAILABLE");
    }

    // ── GET /pets/{id} ─────────────────────────────────────────────────────

    @Test
    void getPetById_ReturnsPet() throws Exception {
        given(petsService.getPetById("pet-1"))
                .willReturn(new PetResponse("pet-1", "Liam", "cat", "tabby",
                        null, null, "AVAILABLE", null, null));

        mvc.perform(get("/pets/pet-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pet-1"))
                .andExpect(jsonPath("$.name").value("Liam"))
                .andExpect(jsonPath("$.species").value("cat"))
                .andExpect(jsonPath("$.breed").value("tabby"));
    }

    // ── POST /pets/ ────────────────────────────────────────────────────────

    @Test
    void createPet_ReturnsCreatedPet() throws Exception {
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Liam");
        request.setSpecies("cat");
        request.setBreed("tabby");

        given(petsService.createPet(any(CreatePetRequest.class)))
                .willReturn(new PetResponse("pet-1", "Liam", "cat", "tabby",
                        null, null, "AVAILABLE", null, null));

        mvc.perform(post("/pets/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("pet-1"))
                .andExpect(jsonPath("$.name").value("Liam"));
    }

    @Test
    void createPet_RejectsInvalidBody() throws Exception {
        mvc.perform(post("/pets/")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.species").exists())
                .andExpect(jsonPath("$.fieldErrors.breed").exists());
    }

    // ── PUT /pets/{id} ─────────────────────────────────────────────────────

    @Test
    void modifyPetById_ReturnsUpdatedPet() throws Exception {
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Luna");
        request.setSpecies("cat");
        request.setBreed("siamese");

        given(petsService.updatePet(eq("pet-1"), any()))
                .willReturn(new PetResponse("pet-1", "Luna", "cat", "siamese",
                        null, null, "AVAILABLE", null, null));

        mvc.perform(put("/pets/pet-1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.breed").value("siamese"));
    }

    // ── DELETE /pets/{id} ──────────────────────────────────────────────────

    @Test
    void deletePet_ReturnsNoContent() throws Exception {
        mvc.perform(delete("/pets/pet-1"))
                .andExpect(status().isNoContent());

        verify(petsService).deletePet("pet-1");
    }
}
