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
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.Matcher;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer.BASE_KEY_PATTERN;
import static com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer.FULL_KEY_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MacroReplacerTest
{
	@Mock Resolver resolver;
	@Mock Matcher matcher;
	@Mock MacroObjectMap macroObjectMap;

	private MacroReplacer replacer;


	@BeforeEach
	void setUp()
	{
		replacer = new MacroReplacer(resolver, matcher);
	}

	@Test
	void testReplace_with_valid_parameters()
	{
		String input = "Hello {PLAYER.NAME}, welcome to {WORLD.NAME}!";
		String expected = "Hello Steve, welcome to Earth!";

		MacroKey baseKey1 = MacroKey.of("PLAYER.NAME").orElseThrow();
		MacroKey baseKey2 = MacroKey.of("WORLD.NAME").orElseThrow();
		MacroStringMap stringMap = new MacroStringMap();
		stringMap.put(baseKey1, "Steve");
		stringMap.put(baseKey2, "Earth");

		when(matcher.match(input, BASE_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		when(resolver.resolve(baseKey1, macroObjectMap)).thenReturn(stringMap);
		when(resolver.resolve(baseKey2, macroObjectMap)).thenReturn(stringMap);

		when(matcher.match(input, FULL_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		String result = replacer.replace(macroObjectMap, input);

		assertEquals(expected, result);
		verify(matcher).match(input, BASE_KEY_PATTERN);
		verify(matcher).match(input, FULL_KEY_PATTERN);
		verify(resolver).resolve(baseKey1, macroObjectMap);
		verify(resolver).resolve(baseKey2, macroObjectMap);
	}


	@Test
	void testReplace_with_valid_parameters_compound_key()
	{
		String input = "Hello {PLAYER.NAME}, welcome to {WORLD_NAME}!";
		String expected = "Hello Steve, welcome to Earth!";

		MacroKey baseKey1 = MacroKey.of("PLAYER.NAME").orElseThrow();
		MacroKey baseKey2 = MacroKey.of("WORLD_NAME").orElseThrow();
		MacroStringMap stringMap = new MacroStringMap();
		stringMap.put(baseKey1, "Steve");
		stringMap.put(baseKey2, "Earth");

		when(matcher.match(input, BASE_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		when(resolver.resolve(baseKey1, macroObjectMap)).thenReturn(stringMap);
		when(resolver.resolve(baseKey2, macroObjectMap)).thenReturn(stringMap);

		when(matcher.match(input, FULL_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		String result = replacer.replace(macroObjectMap, input);

		assertEquals(expected, result);
		verify(matcher).match(input, BASE_KEY_PATTERN);
		verify(matcher).match(input, FULL_KEY_PATTERN);
		verify(resolver).resolve(baseKey1, macroObjectMap);
		verify(resolver).resolve(baseKey2, macroObjectMap);
	}


	@Test
	void testReplace_with_null_message()
	{
		String result = replacer.replace(macroObjectMap, null);
		assertEquals("", result);
	}


	@Test
	void testReplace_with_Missing_Resolved_Values()
	{
		String input = "This is {UNKNOWN_KEY}";
		MacroKey macroKey = MacroKey.of("UNKNOWN_KEY").orElseThrow();

		when(matcher.match(input, BASE_KEY_PATTERN)).thenReturn(Stream.of(macroKey));
		when(matcher.match(input, FULL_KEY_PATTERN)).thenReturn(Stream.of(macroKey));
		when(resolver.resolve(macroKey, macroObjectMap)).thenReturn(new MacroStringMap());

		String result = replacer.replace(macroObjectMap, input);

		assertEquals("This is {UNKNOWN_KEY}", result); // because map.get returns null
	}

}
