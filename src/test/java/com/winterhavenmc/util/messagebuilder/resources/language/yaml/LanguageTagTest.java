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

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTagTest {

	@Test
	void testGetLanguageTag() {
		LanguageTag languageTag = new LanguageTag("en-US");
		assertEquals("en-US", languageTag.getLanguageTag());
		assertNotEquals("fr-FR", languageTag.getLanguageTag());
	}

	@Test
	void testGetResourceName() {
		LanguageTag languageTag = new LanguageTag("en-US");
		assertEquals("language/en-US.yml", languageTag.getResourceName());
		assertNotEquals("language/fr-FR.yml", languageTag.getResourceName());
	}

	@Test
	void testGetFileName() {
		LanguageTag languageTag = new LanguageTag("en-US");
		assertEquals("language" + File.separator + "en-US.yml", languageTag.getFileName());
		assertNotEquals("language" + File.separator + "fr-FR.yml", languageTag.getFileName());
	}

	@Test
	void testGetFile() {
		LanguageTag languageTag = new LanguageTag("en-US");
		File file = languageTag.getFile();
		assertEquals("", file.getName());
	}
}
