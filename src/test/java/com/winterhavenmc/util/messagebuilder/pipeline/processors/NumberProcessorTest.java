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

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class NumberProcessorTest
{
	@Mock Player playerMock;

	MacroProcessor macroProcessor;
	ValidRecipient recipient;
	ContextMap contextMap;


	@BeforeEach
	public void setUp()
	{
		macroProcessor = new NumberProcessor();
		RecordKey key = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		contextMap = ContextMap.of(recipient, key).orElseThrow();
	}


	@AfterEach
	public void tearDown()
	{
		playerMock = null;
		macroProcessor = null;
		contextMap = null;
	}


	@Test
	void resolveContext_integer()
	{
		// Arrange
		RecordKey key = RecordKey.of("SOME_INTEGER").orElseThrow();
		Integer number = 42;
		contextMap.put(key, number);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(key.toString()));
		assertEquals("42", resultMap.get(key.toString()));
	}


	@Test
	void resolveContext_null_integer()
	{
		// Arrange
		RecordKey key = RecordKey.of("KEY").orElseThrow();
		contextMap.put(key, null);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(key.toString()));
	}


	@Test
	void resolveContext_long()
	{
		// Arrange
		RecordKey key = RecordKey.of("SOME_LONG").orElseThrow();
		Long number = 420L;
		contextMap.put(key, number);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(key.toString()));
		assertEquals("420", resultMap.get(key.toString()));
	}


	@Test
	void resolveContext_null_long()
	{
		// Arrange
		RecordKey key = RecordKey.of("KEY").orElseThrow();
		contextMap.put(key, null);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(key.toString()));
	}

}
