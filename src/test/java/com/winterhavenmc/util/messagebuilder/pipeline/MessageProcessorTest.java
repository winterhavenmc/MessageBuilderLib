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

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageProcessorTest {

	@Mock MessageRetriever messageRetrieverMock;
	@Mock MacroReplacer macroReplacerMock;
	@Mock Player playerMock;
	@Mock MessageSender messageSenderMock;
	@Mock TitleSender titleSenderMock;

	CooldownMap cooldownMap;
	MessageProcessor messageProcessor;
	MessageRecord messageRecord;


	@BeforeEach
	void setUp() {
		cooldownMap = new CooldownMap();
		messageProcessor = new MessageProcessor(
				messageRetrieverMock,
				macroReplacerMock,
				cooldownMap,
				messageSenderMock,
				titleSenderMock);

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
		macroReplacerMock = null;
		playerMock = null;

		cooldownMap = null;
		messageProcessor = null;
	}

	@Test
	void process() {
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(messageRetrieverMock.getRecord(ENABLED_MESSAGE.name())).thenReturn(Optional.of(messageRecord));
		Message message = new Message(playerMock, ENABLED_MESSAGE.name(), messageProcessor);
		when(macroReplacerMock.replaceMacros(messageRecord, message)).thenReturn(Optional.ofNullable(messageRecord));

		// Act & Assert
		assertDoesNotThrow(() -> messageProcessor.process(message));

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
		verify(messageRetrieverMock, atLeastOnce()).getRecord(anyString());
	}


	@Test
	void process_parameter_null_message() {
		ValidationException exception = assertThrows(ValidationException.class,
				() -> messageProcessor.process(null));

		assertEquals("The parameter 'message' cannot be null.", exception.getMessage());
	}

}
