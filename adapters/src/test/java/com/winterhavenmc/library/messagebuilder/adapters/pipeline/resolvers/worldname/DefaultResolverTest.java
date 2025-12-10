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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.models.DefaultSymbol.UNKNOWN_WORLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DefaultResolverTest
{
	@Mock World worldMock;


	@Test
	void resolve_returns_valid_world_name_given_valid_world()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		WorldNameResolver resolver = new DefaultResolver();

		// Act
		String result = resolver.resolve(worldMock);

		// Assert
		assertEquals("test_world", result);

		// Verify
		verify(worldMock, atLeastOnce()).getName();
	}


	@Test
	void resolve_returns_unknown_world_symbol_given_invalid_world()
	{
		// Arrange
		WorldNameResolver resolver = new DefaultResolver();

		// Act
		String result = resolver.resolve(null);

		// Assert
		assertEquals(UNKNOWN_WORLD.symbol(), result);
	}

}