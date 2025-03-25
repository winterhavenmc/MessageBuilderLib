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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ContextResolverTest
{
	@Mock Player playerMock;
    ContextResolver contextResolver;


	@BeforeEach
	void setUp()
	{
        contextResolver = new ContextResolver();
	}

	@AfterEach
	void tearDown()
	{
        playerMock = null;
        contextResolver = null;
	}


	@Test
	void testResolve()
    {
		// Arrange
		ItemStack itemStack = new ItemStack(Material.STONE);
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		contextMap.put("NUMBER", 42);
		contextMap.put("ITEM_STACK", itemStack);

		// Act
        ResultMap resultMap = contextResolver.resolve(contextMap);

		// Assert
		assertTrue(resultMap.containsKey("NUMBER"));
		assertEquals("42", resultMap.get("NUMBER"));
		assertTrue(resultMap.containsKey("ITEM_STACK"));
		assertEquals("STONE", resultMap.get("ITEM_STACK"));
	}


	@Test
	void testResolveContext_parameter_null__map()
    {
		ValidationException exception = assertThrows(ValidationException.class,
				() -> contextResolver.resolve(null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}

}
