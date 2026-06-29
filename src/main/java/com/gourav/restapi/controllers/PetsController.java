package com.gourav.restapi.controllers;

import java.util.List;

import com.gourav.restapi.controllers.payload.request.CreatePetRequest;
import com.gourav.restapi.controllers.payload.request.UpdatePetRequest;
import com.gourav.restapi.controllers.payload.response.PetResponse;
import com.gourav.restapi.services.PetsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pets")
public class PetsController {
	private final PetsService petsService;

	public PetsController(PetsService petsService) {
		this.petsService = petsService;
	}

	@GetMapping(value = "/")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<PetResponse> getAllPets() {
		return petsService.getAllPets();
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
