package com.gourav.restapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gourav.restapi.config.jwt.JwtUtils;
import com.gourav.restapi.config.services.UserDetailsServiceImpl;
import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
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

    @Test
    void getAllPets() throws Exception {
        given(petsService.getAllPets())
                .willReturn(List.of(new PetResponse("pet-1", "Liam", "cat", "tabby")));

        mvc.perform(get("/pets/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("pet-1"))
                .andExpect(jsonPath("$[0].name").value("Liam"))
                .andExpect(jsonPath("$[0].breed").value("tabby"))
                .andExpect(jsonPath("$[0].species").value("cat"));
    }

    @Test
    void getPetById() throws Exception {
        given(petsService.getPetById("pet-1"))
                .willReturn(new PetResponse("pet-1", "Liam", "cat", "tabby"));

        mvc.perform(get("/pets/pet-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pet-1"))
                .andExpect(jsonPath("$.name").value("Liam"))
                .andExpect(jsonPath("$.breed").value("tabby"))
                .andExpect(jsonPath("$.species").value("cat"));
    }

    @Test
    void createPetReturnsCreatedPet() throws Exception {
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Liam");
        request.setSpecies("cat");
        request.setBreed("tabby");

        given(petsService.createPet(any(CreatePetRequest.class)))
                .willReturn(new PetResponse("pet-1", "Liam", "cat", "tabby"));

        mvc.perform(post("/pets/")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("pet-1"))
                .andExpect(jsonPath("$.name").value("Liam"));
    }

    @Test
    void modifyPetByIdReturnsUpdatedPet() throws Exception {
        CreatePetRequest request = new CreatePetRequest();
        request.setName("Luna");
        request.setSpecies("cat");
        request.setBreed("siamese");

        given(petsService.updatePet(eq("pet-1"), any()))
                .willReturn(new PetResponse("pet-1", "Luna", "cat", "siamese"));

        mvc.perform(put("/pets/pet-1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.breed").value("siamese"));
    }

    @Test
    void deletePetReturnsNoContent() throws Exception {
        mvc.perform(delete("/pets/pet-1"))
                .andExpect(status().isNoContent());

        verify(petsService).deletePet("pet-1");
    }

    @Test
    void createPetRejectsInvalidBody() throws Exception {
        mvc.perform(post("/pets/")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.species").exists())
                .andExpect(jsonPath("$.fieldErrors.breed").exists());
    }
}
