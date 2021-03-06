package com.gourav.restapi.controllers;

import java.util.List;
import javax.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gourav.restapi.models.Pets;
import com.gourav.restapi.repositories.PetsRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/pets")
public class PetsController {
	@Autowired
	private PetsRepository repository;

	@GetMapping(value = "/")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<Pets> getAllPets() {
		return repository.findAll();
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public Pets getPetById(@PathVariable("id") ObjectId id) {
		return repository.findById(id);
	}

	@PutMapping(value = "/{id}")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public void modifyPetById(@PathVariable("id") ObjectId id, @Valid @RequestBody Pets pets) {
		pets.setId(id);
		repository.save(pets);
	}

	@PostMapping(value = "/")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public Pets createPet(@Valid @RequestBody Pets pets) {
		pets.setId(ObjectId.get());
		repository.save(pets);
		return pets;
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deletePet(@PathVariable ObjectId id) {
		repository.delete(repository.findById(id));
	}
}
