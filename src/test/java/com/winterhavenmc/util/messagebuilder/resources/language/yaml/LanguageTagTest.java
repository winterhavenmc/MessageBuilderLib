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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class LanguageTagTest {

	@Nested
	class ConstructorTests {
		@Test
		void testConstructor_locale() {
			// Arrange
			LanguageTag languageTag = new LanguageTag(Locale.US);

			// Act
			String result = languageTag.getLanguageTag();

			// Assert
			assertEquals("en-US", result);
		}

		@Test
		void testConstructor_languageTag() {
			// Arrange
			LanguageTag languageTag = new LanguageTag("en-US");

			// Act
			Locale result = languageTag.getLocale();

			// Assert
			assertEquals(Locale.US, result);
		}
	}

	@Test
	void testGetLanguageTag() {
		// Arrange
		LanguageTag languageTag = new LanguageTag("en-US");

		// Act & Assert
		assertEquals("en-US", languageTag.getLanguageTag());
		assertNotEquals("fr-FR", languageTag.getLanguageTag());
	}

	@Test
	void testGetResourceName() {
		// Arrange
		LanguageTag languageTag = new LanguageTag("en-US");

		// Act & Assert
		assertEquals("language/en-US.yml", languageTag.getResourceName());
		assertNotEquals("language/fr-FR.yml", languageTag.getResourceName());
	}

	@Test
	void testGetFileName() {
		// Arrange
		LanguageTag languageTag = new LanguageTag("en-US");

		// Act & Assert
		assertEquals("language" + File.separator + "en-US.yml", languageTag.getFileName());
		assertNotEquals("language" + File.separator + "fr-FR.yml", languageTag.getFileName());
	}

	@Test
	void testGetLocale() {
		// Arrange
		LanguageTag languageTag = new LanguageTag("en-US");

		// Act
		Locale locale = languageTag.getLocale();

		// Assert
		assertEquals(Locale.US, locale);
	}

}
