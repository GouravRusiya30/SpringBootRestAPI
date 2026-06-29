package com.gourav.restapi.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PetsTest {
	
	String id = "pet-123";
	Pets pets = new Pets();
	
	@Test
	public void testSetId(){
		pets.setId(id);
		assertEquals(id, pets.getId());
	}
	
	@Test
	public void testSetName(){
		pets.setName("Liam");
		assertEquals("Liam", pets.getName());
	}
	
	@Test
	public void testSetSpecies(){
		pets.setSpecies("cat");
		assertEquals("cat", pets.getSpecies());
	}
	
	@Test
	public void testSetBreed(){
		pets.setBreed("tabby");
		assertEquals("tabby", pets.getBreed());
	}
}
