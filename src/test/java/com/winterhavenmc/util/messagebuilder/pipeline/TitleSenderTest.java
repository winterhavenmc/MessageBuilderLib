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

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TitleSenderTest
{
	// mock player
	@Mock Player playerMock;

	// real message record
	MessageRecord messageRecord;


	@BeforeEach
	void setUp() {
		messageRecord = new MessageRecord(
				ENABLED_MESSAGE.name(),
				true,
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle",
				"this is a final message",
				"this is a final title",
				"this is a final subtitle");

	}

	@AfterEach
	void tearDown() {
		playerMock = null;
		messageRecord = null;
	}


	@Test
	void testSend_player() {
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));

		// Act & Assert
		assertDoesNotThrow(() -> new TitleSender(new CooldownMap()).send(playerMock, messageRecord));

		// Verify
		verify(playerMock, atLeastOnce()).sendTitle(anyString(), anyString(), anyInt(), anyInt(), anyInt());
		verify(playerMock, atLeastOnce()).getUniqueId();
	}

	@Test
	void testSend_console() {
		// Arrange
		ConsoleCommandSender consoleMock = mock(ConsoleCommandSender.class, "Mock Console");

		// Act & Assert
		assertDoesNotThrow(() -> new TitleSender(new CooldownMap()).send(consoleMock, messageRecord));
	}

	@Test
	void testSend_parameter_null_recipient() {
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new TitleSender(new CooldownMap()).send(null, messageRecord));

		// Assert
		assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
	}

	@Test
	void testSend_parameter_null_messageRecord() {
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new TitleSender(new CooldownMap()).send(playerMock, null));

		// Assert
		assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
	}

}
