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

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.UUID;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OfflinePlayerProcessorTest
{
	@Mock Player playerMock;
	@Mock OfflinePlayer offlinePlayerMock;
	@Mock Location locationMock;

	ValidRecipient recipient;
	ValidRecordKey macroKey;


	@BeforeEach
	void setUp()
	{
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		macroKey = ValidRecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
	}


	@AfterEach
	public void tearDown()
	{
		offlinePlayerMock = null;
	}


	@Test
	void resolveContextTest()
	{
		// Arrange
		when(offlinePlayerMock.getName()).thenReturn("Offline Player");
		when(offlinePlayerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(offlinePlayerMock.getLocation()).thenReturn(locationMock);

		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = ContextMap.of(recipient, macroKey).orElseThrow();
		contextMap.put(macroKey, offlinePlayerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		// Assert
		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey(macroKey));

		// Verify
		verify(offlinePlayerMock, atLeastOnce()).getName();
		verify(offlinePlayerMock, atLeastOnce()).getUniqueId();
		verify(offlinePlayerMock, atLeastOnce()).getLocation();
	}


	@Test
	void resolveContext_mismatched_type()
	{
		// Arrange
		Duration duration  = Duration.ofMillis(2000);
		ContextMap contextMap = ContextMap.of(recipient, macroKey).orElseThrow();
		contextMap.put(macroKey, duration);
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(macroKey));
	}

}
