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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.matchers;


import com.winterhavenmc.library.messagebuilder.core.ports.matchers.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.winterhavenmc.library.messagebuilder.adapters.pipeline.replacers.RegexMacroReplacer.BASE_KEY_PATTERN;
import static com.winterhavenmc.library.messagebuilder.adapters.pipeline.replacers.RegexMacroReplacer.FULL_KEY_PATTERN;
import static org.junit.jupiter.api.Assertions.*;

class RegexPlaceholderMatcherTest
{
	@BeforeEach
	public void setUp()
	{
		PlaceholderMatcher placeholderMatcher = new RegexPlaceholderMatcher();
	}


	@Test
	@DisplayName("Retrieve a stream of placeholders from a message string.")
	void getPlaceholderMatcher_returns_only_valid_values()
	{
		// Arrange
		String messageString = "This is a {MESSAGE} with {SEVERAL} {PLACE_HOLDERS} and one {INVALID PLACEHOLDER}.";

		// Act
		Stream<ValidMacroKey> placeholderStream = new RegexPlaceholderMatcher().match(messageString, FULL_KEY_PATTERN);
		Set<ValidMacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains(MacroKey.of("SEVERAL").isValid().orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("MESSAGE").isValid().orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("PLACE_HOLDERS").isValid().orElseThrow()));
		assertFalse(placeholderSet.contains(MacroKey.of("This").isValid().orElseThrow()));
		assertEquals(3, placeholderSet.size());
	}


	@Test
	void getPlaceholderMatcher_does_not_return_invalid_values()
	{
		// Arrange
		String messageString = "This {1MESSAGE} contains {only} invalid {PLACE:HOLDERS} including some {INVALID PLACEHOLDERS} like {this}.";

		// Act
		Stream<ValidMacroKey> placeholderStream = new RegexPlaceholderMatcher().match(messageString, FULL_KEY_PATTERN);
		Set<ValidMacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertEquals(0, placeholderSet.size());
	}


	@Test
	void getPlaceholderMatcher_returns_valid_with_dot_separator()
	{
		// Arrange
		String messageString = "This is a {MESSAGE.DISPLAY_NAME} with {SEVERAL} valid {PLACE_HOLDERS}.";

		// Act
		Stream<ValidMacroKey> placeholderStream = new RegexPlaceholderMatcher().match(messageString, FULL_KEY_PATTERN);
		Set<ValidMacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains(MacroKey.of("MESSAGE.DISPLAY_NAME").isValid().orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("SEVERAL").isValid().orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("PLACE_HOLDERS").isValid().orElseThrow()));
		assertEquals(3, placeholderSet.size());
	}


	@Test
	void testGetPlaceholderStream_base_keys()
	{
		// Arrange
		String messageString = "This is a {MESSAGE.DISPLAY_NAME} with {SEVERAL} {PLACE_HOLDERS} including {INVALID PLACEHOLDERS}.";

		// Act
		Stream<ValidMacroKey> placeholderStream = new RegexPlaceholderMatcher().match(messageString, BASE_KEY_PATTERN);
		Set<ValidMacroKey> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains(MacroKey.of("SEVERAL").isValid().orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("MESSAGE").isValid().orElseThrow()));
		assertTrue(placeholderSet.contains(MacroKey.of("PLACE_HOLDERS").isValid().orElseThrow()));
		assertFalse(placeholderSet.contains(MacroKey.of("MESSAGE.DISPLAY_NAME").isValid().orElseThrow()));
		assertFalse(placeholderSet.contains(MacroKey.of("This").isValid().orElseThrow()));
		assertEquals(3, placeholderSet.size());
	}

}
