package com.gourav.restapi.controllers;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.gourav.restapi.models.Pets;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PetsController.class)
public class PetsControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PetsController petsController;

	@Test
	public void getAllPets() throws Exception {
		ObjectId id = ObjectId.get();
		Pets pets = new Pets(id, "Liam", "cat", "tabby");

		List<Pets> allPets = singletonList(pets);

		given(petsController.getAllPets()).willReturn(allPets);

		mvc.perform(get("/pets/").contentType("application/json;charset=UTF-8")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Liam")).andExpect(jsonPath("$[0].breed").value("tabby"))
				.andExpect(jsonPath("$[0].species").value("cat")).andReturn();
	}

	@Test
	public void getPetById() throws Exception {
		ObjectId id = ObjectId.get();
		Pets pets = new Pets(id, "Liam", "cat", "tabby");

		given(petsController.getPetById(id)).willReturn(pets);

		mvc.perform(get("/pets/" + id + "/").contentType("application/json;charset=UTF-8")).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Liam")).andExpect(jsonPath("$.breed").value("tabby"))
				.andExpect(jsonPath("$.species").value("cat")).andReturn();
	}

	@Test
	public void createPet() throws Exception {
		Pets pets = new Pets();

		pets.setName("Liam");
		pets.setBreed("tabby");
		pets.setSpecies("cat");

		given(petsController.createPet(pets)).willReturn(pets);

		mvc.perform(post("/pets/").contentType("application/json;charset=UTF-8")).andExpect(status().isOk());
	}
}
