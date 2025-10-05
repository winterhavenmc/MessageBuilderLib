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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.replacers;

import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.ports.matchers.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.macro.ValueResolver;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.adapters.pipeline.replacers.RegexMacroReplacer.BASE_KEY_PATTERN;
import static com.winterhavenmc.library.messagebuilder.adapters.pipeline.replacers.RegexMacroReplacer.FULL_KEY_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MacroReplacerTest
{
	@Mock ValueResolver resolverMock;
	@Mock PlaceholderMatcher matcherMock;
	@Mock MacroObjectMap macroObjectMapMock;

	private RegexMacroReplacer replacer;


	@BeforeEach
	void setUp()
	{
		replacer = new RegexMacroReplacer(resolverMock, matcherMock);
	}


	@Test
	void testReplace_with_valid_parameters()
	{
		String input = "Hello {PLAYER.NAME}, welcome to {WORLD.NAME}!";
		String expected = "Hello Steve, welcome to Earth!";

		ValidMacroKey baseKey1 = MacroKey.of("PLAYER.NAME").isValid().orElseThrow();
		ValidMacroKey baseKey2 = MacroKey.of("WORLD.NAME").isValid().orElseThrow();
		MacroStringMap stringMap = new MacroStringMap();
		stringMap.put(baseKey1, "Steve");
		stringMap.put(baseKey2, "Earth");

		when(matcherMock.match(input, BASE_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		when(resolverMock.resolve(baseKey1, macroObjectMapMock)).thenReturn(stringMap);
		when(resolverMock.resolve(baseKey2, macroObjectMapMock)).thenReturn(stringMap);

		when(matcherMock.match(input, FULL_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		String result = replacer.replace(macroObjectMapMock, input);

		assertEquals(expected, result);
		verify(matcherMock).match(input, BASE_KEY_PATTERN);
		verify(matcherMock).match(input, FULL_KEY_PATTERN);
		verify(resolverMock).resolve(baseKey1, macroObjectMapMock);
		verify(resolverMock).resolve(baseKey2, macroObjectMapMock);
	}


	@Test
	void testReplace_with_valid_parameters_compound_key()
	{
		String input = "Hello {PLAYER.NAME}, welcome to {WORLD_NAME}!";
		String expected = "Hello Steve, welcome to Earth!";

		ValidMacroKey baseKey1 = MacroKey.of("PLAYER.NAME").isValid().orElseThrow();
		ValidMacroKey baseKey2 = MacroKey.of("WORLD_NAME").isValid().orElseThrow();
		MacroStringMap stringMap = new MacroStringMap();
		stringMap.put(baseKey1, "Steve");
		stringMap.put(baseKey2, "Earth");

		when(matcherMock.match(input, BASE_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		when(resolverMock.resolve(baseKey1, macroObjectMapMock)).thenReturn(stringMap);
		when(resolverMock.resolve(baseKey2, macroObjectMapMock)).thenReturn(stringMap);

		when(matcherMock.match(input, FULL_KEY_PATTERN))
				.thenReturn(Stream.of(baseKey1, baseKey2));

		String result = replacer.replace(macroObjectMapMock, input);

		assertEquals(expected, result);
		verify(matcherMock).match(input, BASE_KEY_PATTERN);
		verify(matcherMock).match(input, FULL_KEY_PATTERN);
		verify(resolverMock).resolve(baseKey1, macroObjectMapMock);
		verify(resolverMock).resolve(baseKey2, macroObjectMapMock);
	}


	@Test
	void testReplace_with_valid_parameters_compound_key2()
	{
		String input = "Death chest is owned by {DEATH_CHEST.OWNER}";
		String expected = "Death chest is owned by Steve";

		ValidMacroKey key1 = MacroKey.of("DEATH_CHEST.OWNER").isValid().orElseThrow();
		MacroStringMap stringMap = new MacroStringMap();
		stringMap.put(key1, "Steve");

		when(matcherMock.match(input, BASE_KEY_PATTERN))
				.thenReturn(Stream.of(key1));

		when(resolverMock.resolve(key1, macroObjectMapMock)).thenReturn(stringMap);

		when(matcherMock.match(input, FULL_KEY_PATTERN))
				.thenReturn(Stream.of(key1));

		String result = replacer.replace(macroObjectMapMock, input);

		assertEquals(expected, result);
		verify(matcherMock).match(input, BASE_KEY_PATTERN);
		verify(matcherMock).match(input, FULL_KEY_PATTERN);
		verify(resolverMock).resolve(key1, macroObjectMapMock);
	}


	@Test
	void testReplace_with_valid_parameters_compound_key3()
	{
		String input = "Death chest expires in {DEATH_CHEST.EXPIRATION.DURATION}";
		String expected = "Death chest expires in 1 hour, 15 minutes";

		ValidMacroKey key1 = MacroKey.of("DEATH_CHEST.EXPIRATION.DURATION").isValid().orElseThrow();
		MacroStringMap stringMap = new MacroStringMap();
		stringMap.put(key1, "1 hour, 15 minutes");

		when(matcherMock.match(input, BASE_KEY_PATTERN))
				.thenReturn(Stream.of(key1));

		when(resolverMock.resolve(key1, macroObjectMapMock)).thenReturn(stringMap);

		when(matcherMock.match(input, FULL_KEY_PATTERN))
				.thenReturn(Stream.of(key1));

		String result = replacer.replace(macroObjectMapMock, input);

		assertEquals(expected, result);
		verify(matcherMock).match(input, BASE_KEY_PATTERN);
		verify(matcherMock).match(input, FULL_KEY_PATTERN);
		verify(resolverMock).resolve(key1, macroObjectMapMock);
	}


	@Test
	void testReplace_with_null_message()
	{
		String result = replacer.replace(macroObjectMapMock, null);
		assertEquals("", result);
	}


	@Test
	void testReplace_with_Missing_Resolved_Values()
	{
		String input = "This is {UNKNOWN_KEY}";
		ValidMacroKey macroKey = MacroKey.of("UNKNOWN_KEY").isValid().orElseThrow();

		when(matcherMock.match(input, BASE_KEY_PATTERN)).thenReturn(Stream.of(macroKey));
		when(matcherMock.match(input, FULL_KEY_PATTERN)).thenReturn(Stream.of(macroKey));
		when(resolverMock.resolve(macroKey, macroObjectMapMock)).thenReturn(new MacroStringMap());

		String result = replacer.replace(macroObjectMapMock, input);

		assertEquals("This is {UNKNOWN_KEY}", result); // because map.get returns null
	}


	@Test
	void baseKey_pattern_matches()
	{
		String original = "{DEATH_CHEST.PROTECTION.DURATION}";

		java.util.regex.Matcher m = BASE_KEY_PATTERN.matcher(original);

		m.find();

		assertEquals("DEATH_CHEST", m.group(1));

	}


	@Test
	void fullKey_pattern_matches()
	{
		String original = "{DEATH_CHEST.PROTECTION.DURATION}";

		java.util.regex.Matcher m = FULL_KEY_PATTERN.matcher(original);

		m.find();

		assertEquals("DEATH_CHEST.PROTECTION.DURATION", m.group(1));

	}

}
