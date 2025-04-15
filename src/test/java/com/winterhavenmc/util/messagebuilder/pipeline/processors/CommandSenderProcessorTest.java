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
import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommandSenderProcessorTest
{
	@Mock Player playerMock;
	@Mock World worldMock;

	ValidRecipient recipient;
	RecordKey recordKey;
	ContextMap contextMap;
	MacroProcessor macroProcessor;


	@BeforeEach
	public void setUp()
	{
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		recordKey = RecordKey.of("KEY").orElseThrow();
		contextMap = ContextMap.of(recipient, recordKey).orElseThrow();
		macroProcessor = new CommandSenderProcessor();
	}


	@Test
	void resolveContext_with_player()
	{
		// Arrange
		when(playerMock.getName()).thenReturn("player one");
		when(playerMock.getDisplayName()).thenReturn("&aPlayer One");
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(worldMock.getName()).thenReturn("test_world");
		Location location = new Location(worldMock, 10, 20, 30);
		when(playerMock.getLocation()).thenReturn(location);

		RecordKey macroKey = RecordKey.of("SOME_KEY").orElseThrow();
		contextMap.putIfAbsent(macroKey, playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(macroKey));
		assertNotNull(resultMap.get(macroKey));
	}


	@Test
	void resolveContext_not_command_sender()
	{
		// Arrange
		contextMap.putIfAbsent(recordKey, 42);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertTrue(resultMap.isEmpty());
	}

}
