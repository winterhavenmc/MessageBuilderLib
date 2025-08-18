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

package com.winterhavenmc.library.messagebuilder.pipeline.maps;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.messages.Macro;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MacroObjectMapTest
{
	@Test @DisplayName("put inserts string/value pair in map.")
	void put_inserts_key_value_pair_in_map()
	{
		// Arrange
		Integer number = 42;
		ValidMacroKey macroKey = MacroKey.of(Macro.PAGE_NUMBER).isValid().orElseThrow();
		MacroObjectMap macroObjectMap = new MacroObjectMap();

		// Act
		macroObjectMap.put(macroKey, number);

		// Assert
		assertEquals(Optional.of(42), macroObjectMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test @DisplayName("put inserts string 'NULL' if parameter value is null.")
	void put_inserts_string_NULL_if_parameter_is_null()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("NUMBER").isValid().orElseThrow();
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(macroKey, null);

		// Act
		Optional<Object> result = macroObjectMap.get(macroKey);

		// Assert
		assertEquals(Optional.of("NULL"), result);
	}


	@Test @DisplayName("putIfAbsent inserts string/value pair in map when not already present.")
	void putIfAbsent_inserts_key_value_pair_in_map_when_not_already_present()
	{
		// Arrange
		Integer number = 42;
		ValidMacroKey macroKey = MacroKey.of(Macro.PAGE_NUMBER).isValid().orElseThrow();
		MacroObjectMap macroObjectMap = new MacroObjectMap();

		// Act
		macroObjectMap.putIfAbsent(macroKey, number);

		// Assert
		assertEquals(Optional.of(42), macroObjectMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test @DisplayName("putIfAbsent inserts string/value pair in map when not already present.")
	void putIfAbsent_does_not_insert_key_value_pair_in_map_when_already_present()
	{
		// Arrange
		Integer number1 = 42;
		Integer number2 = 43;
		ValidMacroKey macroKey = MacroKey.of(Macro.PAGE_NUMBER).isValid().orElseThrow();
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(macroKey, number1);

		// Act
		macroObjectMap.putIfAbsent(macroKey, number2);

		// Assert
		assertEquals(Optional.of(42), macroObjectMap.get(macroKey), "Retrieved value should match the original");
	}


	@Test @DisplayName("putIfAbsent inserts string 'NULL' if parameter value is null.")
	void putIfAbsent_inserts_string_NULL_if_parameter_is_null()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("NUMBER").isValid().orElseThrow();
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.putIfAbsent(macroKey, null);

		// Act
		Optional<Object> retrievedValue = macroObjectMap.get(macroKey);

		// Assert
		assertEquals(Optional.of("NULL"), retrievedValue);
	}


	@Test @DisplayName("get retrieves value for string in map.")
	void get_retrieves_value_for_key_in_map()
	{
		// Arrange
		Integer number = 42;
		ValidMacroKey macroKey = MacroKey.of(Macro.PAGE_NUMBER).isValid().orElseThrow();
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(macroKey, number);

		// Act
		Optional<Object> result = macroObjectMap.get(macroKey);

		// Assert
		assertEquals(Optional.of(42), result, "Retrieved value should match the original");
	}


	@Test @DisplayName("get returns empty optional for string not in map.")
	void get_retrieves_empty_optional_for_key_not_in_map()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of(Macro.PAGE_NUMBER).isValid().orElseThrow();
		MacroObjectMap macroObjectMap = new MacroObjectMap();

		// Act
		Optional<Object> result = macroObjectMap.get(macroKey);

		// Assert
		assertEquals(Optional.empty(), result, "Non-existent entry should return empty optional.");
	}

}
