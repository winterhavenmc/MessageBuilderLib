/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.resources.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class LanguageSettingTest
{
	@Mock File fileMock;

	LanguageSetting languageSetting;
	Optional<LanguageTag> languageTag;

	@BeforeEach
	void setUp()
	{
		languageTag = LanguageTag.of("en-US");
		languageSetting = new LanguageSetting("name", fileMock, languageTag);
	}

	@Test
	void exists()
	{
		assertFalse(languageSetting.exists());
	}



	@Test
	void isConformant()
	{
		assertTrue(languageSetting.isConformant());
	}

	@Test
	void testToString()
	{
		assertEquals("name", languageSetting.toString());
	}

	@Test
	void name()
	{
		assertEquals("name", languageSetting.name());
	}

	@Test
	void file()
	{
		assertEquals(fileMock, languageSetting.file());
	}

	@Test
	void tag()
	{
		assertEquals(languageTag, languageSetting.tag());
	}

}
