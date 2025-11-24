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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders;

import com.winterhavenmc.library.messagebuilder.adapters.util.MessageId;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.message.MessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.message.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.models.validation.Parameter.RECIPIENT;


@ExtendWith(MockitoExtension.class)
class MessageSenderTest
{
	@Mock Player playerMock;

	Recipient.Valid recipient;
	ValidMessageRecord validMessageRecord;
	FinalMessageRecord finalMessageRecord;
	ValidMessageKey recordKey;
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

		recordKey = MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow();

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



//	@Test @DisplayName("test send method with valid parameters")
//	void testSend_parameters_valid()
//	{
//		// Arrange
//		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
//
//		// Act & Assert
//		assertDoesNotThrow(() -> new MessageSender(new CooldownMap(), MiniMessage.miniMessage()).send(recipient, finalMessageRecord));
//
//		// Verify
//		verify(playerMock, atLeastOnce()).sendMessage(anyString());
//	}


//	@Test
//	void testSendPlayer()
//	{
//		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
//		assertDoesNotThrow(() -> new MessageSender(new CooldownMap(), MiniMessage.miniMessage()).send(recipient, finalMessageRecord));
//
//	}

}
