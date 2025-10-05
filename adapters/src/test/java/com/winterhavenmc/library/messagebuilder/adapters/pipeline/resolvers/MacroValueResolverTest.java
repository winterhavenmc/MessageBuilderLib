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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.field.AtomicResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.field.CompositeResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.field.MacroValueResolver;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.ports.resolvers.macro.ValueResolver;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MacroValueResolverTest
{
	@Mock CompositeResolver compositeResolver;
	@Mock AtomicResolver atomicResolver;

	List<ValueResolver> resolvers;
	MacroStringMap stringMap;


	@BeforeEach
	void setUp()
	{
		resolvers = List.of(compositeResolver, atomicResolver);
		stringMap = new MacroStringMap();
	}


	@Test
	void resolve_for_keys()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("KEY").isValid().orElseThrow();
		MacroObjectMap objectMap = new MacroObjectMap();
		when(compositeResolver.resolve(macroKey, objectMap)).thenReturn(stringMap);
		when(atomicResolver.resolve(macroKey, objectMap)).thenReturn(stringMap);

		MacroValueResolver resolver = new MacroValueResolver(resolvers);

		// Act
		MacroStringMap result = resolver.resolve(macroKey, objectMap);

		// Assert
		assertInstanceOf(MacroStringMap.class, result);
	}


	@Test
	void testResolve()
	{
		// Arrange
		ValidMacroKey macroKey1 = MacroKey.of("KEY1").isValid().orElseThrow();

		MacroObjectMap objectMap = new MacroObjectMap();
		objectMap.put(macroKey1, "STRING");

		stringMap.put(macroKey1, "STRING");


		when(compositeResolver.resolve(macroKey1, objectMap)).thenReturn(stringMap);
		when(atomicResolver.resolve(macroKey1, objectMap)).thenReturn(stringMap);

		MacroValueResolver resolver = new MacroValueResolver(resolvers);

		// Act
		MacroStringMap result = resolver.resolve(macroKey1, objectMap);

		// Assert
		assertInstanceOf(MacroStringMap.class, result);
	}

}
