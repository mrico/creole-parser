package eu.mrico.creole.impl;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import eu.mrico.creole.CreoleException;

import static org.junit.Assert.*;

public class CharacterReaderTest {
	
	@Test
	public void peek() throws CreoleException, IOException {
		final String s = "Hello World, I'm a string";
		
		CharacterReader reader = new CharacterReader(new StringReader(s));
		assertEquals(Character.valueOf('H'), reader.next());
		
		assertEquals(Character.valueOf('e'), reader.peek());
		assertEquals(Character.valueOf('e'), reader.next());
		
		assertEquals("llo", reader.peek(3));
		assertEquals(Character.valueOf('l'), reader.next());
		assertEquals(Character.valueOf('l'), reader.next());
		assertEquals(Character.valueOf('o'), reader.next());
		
		assertEquals(Character.valueOf(' '), reader.peek());
		reader.skip(1);
		
		assertEquals(Character.valueOf('W'), reader.next());
		assertEquals(Character.valueOf('o'), reader.peek());
		assertEquals(Character.valueOf('r'), reader.readAhead());			
		assertEquals("orld, I", reader.peek(7));
		
		assertEquals(Character.valueOf('o'), reader.next());
		reader.skip(11);
		assertEquals(Character.valueOf('s'), reader.next());		
		reader.skip(5);
		assertNull(reader.next());				
	}
	
}
