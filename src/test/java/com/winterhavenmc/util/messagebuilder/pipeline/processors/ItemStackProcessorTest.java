/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ItemStackProcessorTest
{
	@Mock Player playerMock;

	ValidRecipient recipient;
	RecordKey messageKey;
	ContextMap contextMap;
	MacroProcessor macroProcessor;


	@BeforeEach
	void setUp()
	{
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		macroProcessor = new ItemStackProcessor();
	}


	@Test @DisplayName("test resolve context for ItemStack without metadata")
	void resolveContext_no_metadata()
	{
		// Arrange
		ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);
		contextMap.put(messageKey, itemStack);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(messageKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(messageKey));
		assertEquals("GOLDEN_AXE", resultMap.get(messageKey));
	}


	@Test @DisplayName("test resolve context with Invalid type")
	void resolveContext_not_an_itemstack()
	{
		// Arrange
		int value = 42;
		contextMap.put(messageKey, value);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(messageKey, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(messageKey));
	}

}
