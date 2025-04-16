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

package com.winterhavenmc.util.messagebuilder.pipeline.resolver;

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CompositeResolverTest
{
	@Mock Player playerMock;
    AtomicResolver resolver;


	@BeforeEach
	void setUp()
	{
        resolver = new AtomicResolver();
	}


	@Test
	void testResolve()
    {
		// Arrange
		ItemStack itemStack = new ItemStack(Material.STONE);
		RecordKey messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();

		ValidRecipient recipient = switch (RecipientResult.from(playerMock))
		{
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		MacroKey numberMacroKey = MacroKey.of("NUMBER").orElseThrow();
		MacroKey itemMacroKey = MacroKey.of("ITEM_STACK").orElseThrow();

		contextMap.putIfAbsent(numberMacroKey, 42);
		contextMap.putIfAbsent(itemMacroKey, itemStack);

		// Act
        ResultMap resultMap = resolver.resolve(numberMacroKey, contextMap);

		// Assert
//		assertTrue(resultMap.containsKey(numberMacroKey));
		assertEquals("42", resultMap.get(numberMacroKey));
//		assertTrue(resultMap.containsKey(itemMacroKey));
//		assertEquals("STONE", resultMap.get(itemMacroKey));
	}

}
