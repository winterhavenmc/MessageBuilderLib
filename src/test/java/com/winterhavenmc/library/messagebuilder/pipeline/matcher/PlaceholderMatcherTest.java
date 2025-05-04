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

import org.junit.jupiter.api.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		String messageString = "This is a {MESSAGE} with {SEVERAL} {PLACE_HOLDERS}.";

		// Act
		Stream<String> placeholderStream = new PlaceholderMatcher().match(messageString);
		Set<String> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains("SEVERAL"));
		assertTrue(placeholderSet.contains("MESSAGE"));
		assertTrue(placeholderSet.contains("PLACE_HOLDERS"));
		assertFalse(placeholderSet.contains("This"));
		assertEquals(3, placeholderSet.size());
	}

	@Test
	void testGetPlaceholder_with_dot_separators()
	{
		// Arrange
		String messageString = "This is a {MESSAGE.DISPLAY_NAME} with {SEVERAL} {PLACE_HOLDERS}.";

		// Act
		Stream<String> placeholderStream = new PlaceholderMatcher().match(messageString);
		Set<String> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertTrue(placeholderSet.contains("MESSAGE.DISPLAY_NAME"));
		assertEquals(3, placeholderSet.size());
	}

}
