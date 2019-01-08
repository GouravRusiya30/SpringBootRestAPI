package com.gourav.restApi.models;

import static org.junit.Assert.*;
import org.bson.types.ObjectId;
import org.junit.Test;

public class PetsTest {
	
	ObjectId _id = ObjectId.get();
	Pets pets = new Pets();
	
	@Test
	public void testSet_id(){
		pets.set_id(_id);
		assertTrue(pets.get_id().equals(_id.toHexString()));
	}
	
	@Test
	public void testSetName(){
		pets.setName("Liam");
		assertTrue(pets.getName().equals("Liam"));
	}
	
	@Test
	public void testSetSpecies(){
		pets.setSpecies("cat");
		assertTrue(pets.getSpecies().equals("cat"));
	}
	
	@Test
	public void testSetBreed(){
		pets.setBreed("tabby");
		assertTrue(pets.getBreed().equals("tabby"));
	}
}
