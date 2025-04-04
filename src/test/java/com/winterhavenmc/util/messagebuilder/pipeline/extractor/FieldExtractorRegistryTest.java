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

package com.winterhavenmc.util.messagebuilder.pipeline.extractor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;


class FieldExtractorRegistryTest
{
	private FieldExtractorRegistry registry;

	@BeforeEach
	void setUp()
    {
		registry = new FieldExtractorRegistry();
	}


	@Test
	void testRegisterAndExtractFields()
    {
		registry.registerExtractor(String.class, str -> Map.of("VALUE", str.toUpperCase()));

		Optional<Map<String, Object>> result = registry.extractFields("hello");

		assertTrue(result.isPresent());
		assertEquals(1, result.get().size());
		assertEquals("HELLO", result.get().get("VALUE"));
	}


	@Test
	void testExtractFields_NoExtractorRegistered()
    {
		Optional<Map<String, Object>> result = registry.extractFields(42);

		assertFalse(result.isPresent());
	}

	@Test
	void testExtractFields_NullInput() {
		Optional<Map<String, Object>> result = registry.extractFields(null);

		assertFalse(result.isPresent());
	}

	@Test
	void testExtractorForCustomType() {
		class CustomType {
			final String name;
			CustomType(String name) { this.name = name; }
		}

		registry.registerExtractor(CustomType.class, obj -> Map.of("NAME", obj.name));

		CustomType custom = new CustomType("CustomName");
		Optional<Map<String, Object>> result = registry.extractFields(custom);

		assertTrue(result.isPresent());
		assertEquals(1, result.get().size());
		assertEquals("CustomName", result.get().get("NAME"));
	}

	@Test
	void testOverwritingExtractor() {
		registry.registerExtractor(Integer.class, num -> Map.of("NUMBER", num * 2));
		registry.registerExtractor(Integer.class, num -> Map.of("NUMBER", num * 3));

		Optional<Map<String, Object>> result = registry.extractFields(10);

		assertTrue(result.isPresent());
		assertEquals(1, result.get().size());
		assertEquals(30, result.get().get("NUMBER")); // Last registered extractor should take precedence
	}

}
