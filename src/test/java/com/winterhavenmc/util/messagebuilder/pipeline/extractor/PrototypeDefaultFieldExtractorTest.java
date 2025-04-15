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

import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


class PrototypeDefaultFieldExtractorTest
{
	@Test
	void testExtractWithSimpleMapping()
    {
		Function<String, Map<String, String>> logic = str -> Map.of("LENGTH", String.valueOf(str.length()), "UPPER", str.toUpperCase());
		PrototypeDefaultFieldExtractor<String> extractor = new PrototypeDefaultFieldExtractor<>(logic);

		RecordKey macroKey = RecordKey.of("STRING").orElseThrow();
		Map<String, String> result = extractor.extract("test", macroKey);

		assertEquals(2, result.size());
		assertEquals("4", result.get("STRING.LENGTH"));
		assertEquals("TEST", result.get("STRING.UPPER"));
	}


	@Test
	void testExtractWithEmptyMap()
    {
		Function<Integer, Map<String, String>> logic = num -> Map.of();
		PrototypeDefaultFieldExtractor<Integer> extractor = new PrototypeDefaultFieldExtractor<>(logic);

		RecordKey macroKey = RecordKey.of("NUMBER").orElseThrow();
		Map<String, String> result = extractor.extract(123, macroKey);

		assertTrue(result.isEmpty());
	}


	@Test
	void testExtractWithCustomObject()
    {
		class CustomType {
			final String name;
			final int age;
			CustomType(String name, int age) { this.name = name; this.age = age; }
		}

		Function<CustomType, Map<String, String>> logic = obj -> Map.of(
				"NAME", obj.name,
				"AGE", String.valueOf(obj.age)
		);

		PrototypeDefaultFieldExtractor<CustomType> extractor = new PrototypeDefaultFieldExtractor<>(logic);

		CustomType custom = new CustomType("Alice", 30);
		RecordKey macroKey = RecordKey.of("CUSTOM").orElseThrow();

		Map<String, String> result = extractor.extract(custom, macroKey);

		System.out.println(result);
		assertEquals(2, result.size());
		assertEquals("Alice", result.get("CUSTOM.NAME"));
		assertEquals("30", result.get("CUSTOM.AGE"));
	}


	@Test
	void testExtractWithSingleField()
    {
		Function<Double, Map<String, String>> logic = num -> Map.of("VALUE", String.format("%.2f", num));
		PrototypeDefaultFieldExtractor<Double> extractor = new PrototypeDefaultFieldExtractor<>(logic);

		RecordKey macroKey = RecordKey.of("PI").orElseThrow();
		Map<String, String> result = extractor.extract(3.14159, macroKey);

		assertEquals(1, result.size());
		assertEquals("3.14", result.get("PI.VALUE"));
	}


	@Test
	void testExtractWithSpecialCharactersInKey()
    {
		Function<String, Map<String, String>> logic = str -> Map.of("SPECIAL-KEY", str);
		PrototypeDefaultFieldExtractor<String> extractor = new PrototypeDefaultFieldExtractor<>(logic);

		RecordKey macroKey = RecordKey.of("TEST").orElseThrow();
		Map<String, String> result = extractor.extract("Hello", macroKey);

		assertEquals(1, result.size());
		assertEquals("Hello", result.get("TEST.SPECIAL-KEY"));
	}

}
