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

import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;

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
class ObjectProcessorTest {

	@Mock Player playerMock;
	MacroProcessor macroProcessor;

	ValidRecipient recipient;
	RecordKey recordKey;
	ContextMap contextMap;


	@BeforeEach
	public void setUp() {
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		contextMap = ContextMap.of(recipient, recordKey).orElseThrow();
		macroProcessor = new ObjectProcessor();
	}

	@AfterEach
	public void tearDown() {
		macroProcessor = null;
	}


	@Test
	void resolveContext_integer() {
		// Arrange
		Integer value = 42;
		contextMap.putIfAbsent(recordKey, value);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(recordKey));
		assertEquals("42", resultMap.get(recordKey));
	}


}
