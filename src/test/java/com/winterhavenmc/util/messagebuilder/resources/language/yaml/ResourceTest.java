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

class ResourceTest {

	@Test
	void getLanguageTag() {
		Resource resource = new Resource("en-US");
		assertEquals("en-US", resource.getLanguageTag());
		assertNotEquals("fr-FR", resource.getLanguageTag());
	}

	@Test
	void getName() {
		Resource resource = new Resource("en-US");
		assertEquals("language/en-US.yml", resource.getName());
		assertNotEquals("language/fr-FR.yml", resource.getName());
	}

	@Test
	void getFileName() {
		Resource resource = new Resource("en-US");
		assertEquals("language" + File.separator + "en-US.yml", resource.getFileName());
		assertNotEquals("language" + File.separator + "fr-FR.yml", resource.getFileName());
	}

}
