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

package com.winterhavenmc.util.messagebuilder.pipeline.sender;

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageSenderTest
{
	@Mock Player playerMock;

	ValidRecipient recipient;
	MessageRecord messageRecord;


	@BeforeEach
	void setUp()
	{
		messageRecord = new MessageRecord(
				RecordKey.of(ENABLED_MESSAGE).orElseThrow(),
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


	@Test @DisplayName("test send method with valid parameters")
	void testSend_parameters_valid()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		recipient = switch (RecipientResult.from(playerMock))
		{
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		// Act
		new MessageSender(new CooldownMap()).send(recipient, messageRecord);

		// Verify
		verify(playerMock, atLeastOnce()).sendMessage(anyString());
	}


	@Test @DisplayName("test send method with null recipient parameter")
	@Disabled
	void send_parameter_null_recipient()
	{
		// Arrange
		recipient = switch (RecipientResult.from(playerMock))
		{
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new MessageSender(new CooldownMap()).send(null, messageRecord));
		assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
	}


	@Test @DisplayName("test send method with null messageRecord parameter")
	void send_parameter_null_messageRecord()
	{
		// Arrange
		recipient = switch (RecipientResult.from(playerMock))
		{
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new MessageSender(new CooldownMap()).send(recipient, null));
		assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
	}

}
