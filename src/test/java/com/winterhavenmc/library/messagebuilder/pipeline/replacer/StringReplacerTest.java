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

package com.winterhavenmc.library.messagebuilder.pipeline.replacer;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FieldResolver;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StringReplacerTest
{
	@Mock FieldResolver fieldResolverMock;
	@Mock PlaceholderMatcher placeholderMatcherMock;

	StringReplacer stringReplacer;
	MacroStringMap stringMap;
	MacroKey macroKey;

	@BeforeEach
	void setUp()
	{
		macroKey = MacroKey.of("ITEM_NAME").orElseThrow();
		stringReplacer = new StringReplacer(fieldResolverMock, placeholderMatcherMock);
		stringMap = new MacroStringMap();
		stringMap.put(macroKey, "REPLACED_STRING");
	}


	@Nested
	class ReplaceMacrosInStringTests
	{
		@Test @DisplayName("Test replaceMacrosInString method with Valid parameters")
		void testReplaceMacrosInString()
		{
			// Arrange
			MacroObjectMap objectMap = new MacroObjectMap();
			String messageString = "Replace this: {ITEM_NAME}";
			when(placeholderMatcherMock.match(messageString)).thenReturn(Stream.of("ITEM_NAME"));
			when(fieldResolverMock.resolve(macroKey, objectMap)).thenReturn(stringMap);

			// Act
			String resultString = stringReplacer.replaceStrings(objectMap, messageString);

			// Assert
			assertEquals("Replace this: REPLACED_STRING", resultString);

			// Verify
			verify(placeholderMatcherMock, atLeastOnce()).match(anyString());
		}


		@Test @DisplayName("Test replaceMacrosInString method with null messageString parameter")
		void testReplaceMacrosInString_parameter_null_messageString()
		{
			// Arrange
			MacroObjectMap macroObjectMap = new MacroObjectMap();

			// Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> stringReplacer.replaceStrings(macroObjectMap, null));

			// Assert
			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}


	@Nested
	class ReplacementsTests
	{
		@Test
		void testReplacements()
		{
			// Arrange
			StringReplacer localStringReplacer = new StringReplacer(fieldResolverMock, placeholderMatcherMock);

			MacroStringMap macroStringMap = new MacroStringMap();
			MacroKey resultMacroKey = MacroKey.of("KEY").orElseThrow();
			macroStringMap.put(resultMacroKey, "value");
			String messageString = "this is a macro replacement string {KEY}.";

			// Act
			String resultString = localStringReplacer.doReplacements(macroStringMap, messageString);

			// Assert
			assertEquals("this is a macro replacement string value.", resultString);
		}

		@Test
		void testReplacements_parameter_nul_replacement_map()
		{
			ValidationException exception = assertThrows(ValidationException.class,
					() -> stringReplacer.doReplacements(null, "some string"));

			assertEquals("The parameter 'replacementMap' cannot be null.", exception.getMessage());
		}

		@Test
		void testReplacements_parameter_nul_message_string()
		{
			MacroStringMap macroStringMap = new MacroStringMap();
			MacroKey key1 = MacroKey.of("KEY1").orElseThrow();
			MacroKey key2 = MacroKey.of("KEY2").orElseThrow();
			macroStringMap.put(key1, "value1");
			macroStringMap.put(key2, "value2");
			macroStringMap.put(key1, "value3");

			ValidationException exception = assertThrows(ValidationException.class,
					() -> stringReplacer.doReplacements(macroStringMap, null));

			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}

}