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

package com.winterhavenmc.library.messagebuilder.pipeline.sender;

import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.model.language.message.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.message.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.message.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageSenderTest
{
	@Mock Player playerMock;

	Recipient.Valid recipient;
	ValidMessageRecord validMessageRecord;
	FinalMessageRecord finalMessageRecord;
	RecordKey recordKey;
	ConfigurationSection section;


	@BeforeEach
	void setUp()
	{
		recipient = switch(Recipient.of(playerMock))
		{
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		section = new MemoryConfiguration();
		section.set(MessageRecord.Field.ENABLED.toKey(), true);
		section.set(MessageRecord.Field.MESSAGE_TEXT.toKey(), "this is a test message");
		section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), 11);
		section.set(MessageRecord.Field.TITLE_TEXT.toKey(), "this is a test title");
		section.set(MessageRecord.Field.TITLE_FADE_IN.toKey(), 22);
		section.set(MessageRecord.Field.TITLE_STAY.toKey(), 33);
		section.set(MessageRecord.Field.TITLE_FADE_OUT.toKey(), 44);
		section.set(MessageRecord.Field.SUBTITLE_TEXT.toKey(), "this is a test subtitle");

		validMessageRecord = ValidMessageRecord.create(recordKey, section);

		finalMessageRecord = validMessageRecord.withFinalStrings(
				"this is a final message",
				"this is a final title",
				"this is a final subtitle");
	}



	@Test @DisplayName("test send method with valid parameters")
	void testSend_parameters_valid()
	{
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));

		// Act & Assert
		assertDoesNotThrow(() -> new MessageSender(new CooldownMap()).send(recipient, finalMessageRecord));

		// Verify
		verify(playerMock, atLeastOnce()).sendMessage(anyString());
	}


	@Test
	void testSendPlayer()
	{
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		assertDoesNotThrow(() -> new MessageSender(new CooldownMap()).send(recipient, finalMessageRecord));

	}

}
