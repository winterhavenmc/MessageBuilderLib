/*
 * Copyright (c) 2024-2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.models.sound;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SoundRecordTest
{
	ValidSoundRecord validSoundRecord;


	@BeforeEach
	void setUp()
	{
		validSoundRecord = new ValidSoundRecord("ENABLED_SOUND", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f);
	}

	@AfterEach
	void tearDown()
	{
		validSoundRecord = null;
	}


	@Test
	void key()
	{
		SoundRecord soundRecord = SoundRecord.of("ENABLED_SOUND", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f);
		assertInstanceOf(ValidSoundRecord.class, soundRecord);
		assertEquals("ENABLED_SOUND", soundRecord.key());
	}


	@Test
	void enabled()
	{
		assertTrue(validSoundRecord.enabled());
	}


	@Test
	void playerOnly()
	{
		assertTrue(validSoundRecord.playerOnly());
	}


	@Test
	void bukkitSoundName()
	{
		assertEquals("ENTITY_VILLAGER_NO", validSoundRecord.soundName());
	}


	@Test
	void volume()
	{
		assertEquals(1.0f, validSoundRecord.volume());
	}


	@Test
	void pitch()
	{
		assertEquals(2.0f, validSoundRecord.pitch());
	}


//	@Test
//	void key_blank()
//	{
//		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
//				new ValidSoundRecord("", true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f));
//		assertEquals("The key was blank.", e.getMessage());
//	}


//	@Test
//	void key_null()
//	{
//		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
//				new ValidSoundRecord(null, true, true, "ENTITY_VILLAGER_NO", 1.0f, 2.0f));
//		assertEquals("The key was null.", e.getMessage());
//	}


//	@Test
//	void soundName_blank()
//	{
//		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
//				new ValidSoundRecord("ENABLED_SOUND", true, true, "", 1.0f, 2.0f));
//		assertEquals("The sound name was blank.", e.getMessage());
//	}


//	@Test
//	void soundName_null()
//	{
//		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
//				new ValidSoundRecord("ENABLED_SOUND", true, true, null, 1.0f, 2.0f));
//		assertEquals("The sound name was null.", e.getMessage());
//	}

}
