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

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;

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

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
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
		RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		ValidRecipient recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		ContextMap contextMap = ContextMap.of(recipient, recordKey).orElseThrow();
		RecordKey numberRecordKey = RecordKey.of("NUMBER").orElseThrow();
		RecordKey itemStackRecordKey = RecordKey.of("ITEM_STACK").orElseThrow();
		contextMap.put(numberRecordKey, 42);
		contextMap.put(itemStackRecordKey, itemStack);

		// Act
        ResultMap resultMap = contextResolver.resolve(contextMap);

		// Assert
		assertTrue(resultMap.containsKey(numberRecordKey));
		assertEquals("42", resultMap.get(numberRecordKey));
		assertTrue(resultMap.containsKey(itemStackRecordKey));
		assertEquals("STONE", resultMap.get(itemStackRecordKey));
	}

}
