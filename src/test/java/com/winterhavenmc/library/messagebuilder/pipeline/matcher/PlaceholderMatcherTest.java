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

package com.winterhavenmc.library.messagebuilder.pipeline.matcher;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import org.junit.jupiter.api.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer.BASE_KEY_PATTERN;
import static com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer.FULL_KEY_PATTERN;
import static org.junit.jupiter.api.Assertions.*;


class PlaceholderMatcherTest
{
	@BeforeEach
	public void setUp()
	{
		PlaceholderMatcher placeholderMatcher = new PlaceholderMatcher();
	}


	@Test
	void testGetPlaceholderStream()
	{
		// Arrange
		String messageString = "This is a {MESSAGE} with {SEVERAL} {PLACE_HOLDERS} some {INVALID PLACEHOLDERS}.";

		// Act
		Stream<MacroKey> placeholderStream = new PlaceholderMatcher().match(messageString, FULL_KEY_PATTERN);
		Set<MacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains(MacroKey.of("SEVERAL").orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("MESSAGE").orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("PLACE_HOLDERS").orElseThrow()));
		assertFalse(placeholderSet.contains(MacroKey.of("This").orElseThrow()));
		assertEquals(3, placeholderSet.size());
	}


	@Test
	void testGetPlaceholderStream_strict()
	{
		// Arrange
		String messageString = "Invalid placeholders: {1MESSAGE} {several} {PLACE:HOLDERS} some {INVALID PLACEHOLDERS} like {this}.";

		// Act
		Stream<MacroKey> placeholderStream = new PlaceholderMatcher().match(messageString, FULL_KEY_PATTERN);
		Set<MacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertEquals(0, placeholderSet.size());
	}


	@Test
	void testGetPlaceholder_with_dot_separators()
	{
		String messageString = "This is a {MESSAGE.DISPLAY_NAME} with {SEVERAL} {PLACE_HOLDERS}.";

		// Act
		Stream<MacroKey> placeholderStream = new PlaceholderMatcher().match(messageString, FULL_KEY_PATTERN);
		Set<MacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains(MacroKey.of("MESSAGE.DISPLAY_NAME").orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("SEVERAL").orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("PLACE_HOLDERS").orElseThrow()));
		assertEquals(3, placeholderSet.size());
	}


	@Test
	void testGetPlaceholderStream_base_keys()
	{
		// Arrange
		String messageString = "This is a {MESSAGE.DISPLAY_NAME} with {SEVERAL} {PLACE_HOLDERS} including {INVALID PLACEHOLDERS}.";

		// Act
		Stream<MacroKey> placeholderStream = new PlaceholderMatcher().match(messageString, BASE_KEY_PATTERN);
		Set<MacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains(MacroKey.of("SEVERAL").orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("MESSAGE").orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("PLACE_HOLDERS").orElseThrow()));
		assertFalse(placeholderSet.contains(MacroKey.of("MESSAGE.DISPLAY_NAME").orElseThrow()));
		assertFalse(placeholderSet.contains(MacroKey.of("This").orElseThrow()));
		assertEquals(3, placeholderSet.size());
	}

}
