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
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.recordkey.ValidRecordKey;
import com.winterhavenmc.util.messagebuilder.util.MultiverseHelper;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.World;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WorldProcessorTest
{
	@Mock Player playerMock;
	@Mock World worldMock;

	ValidRecordKey macroKey;
	ValidRecordKey messageKey;
	ValidRecipient recipient;

	static MockedStatic<MultiverseHelper> mockStatic;


	@BeforeAll
	static void preSetup()
	{
		mockStatic = mockStatic(MultiverseHelper.class);
	}

	@BeforeEach
	void setUp()
	{
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		macroKey = ValidRecordKey.of("KEY").orElseThrow();
		messageKey = ValidRecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
	}

	@AfterEach
	void tearDown()
	{
		playerMock = null;
		worldMock = null;
	}


	@Test
	void resolveContext_with_multiverse()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		mockStatic.when(() -> MultiverseHelper.getAlias(worldMock)).thenReturn(Optional.of("MV Alias"));
		ValidRecordKey macroKey = ValidRecordKey.of("SOME_WORLD").orElseThrow();
		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(macroKey, worldMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(macroKey));
		assertEquals("MV Alias", resultMap.get(macroKey));
	}


	@Test
	void resolveContext_without_multiverse()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		mockStatic.when(() -> MultiverseHelper.getAlias(worldMock)).thenReturn(Optional.empty());

		ValidRecordKey recordKey = ValidRecordKey.of("SOME_WORLD").orElseThrow();
		ContextMap contextMap = ContextMap.of(recipient, recordKey).orElseThrow();
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(recordKey, worldMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(recordKey));
		assertEquals("test_world", resultMap.get(recordKey));
	}


	@Test
	void resolveContext_with_null_world()
	{
		// Arrange
		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(macroKey, null);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		// Assert
		assertNull(resultMap.get(macroKey));
	}

}
