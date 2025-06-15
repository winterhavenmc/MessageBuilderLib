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

package com.winterhavenmc.library.messagebuilder.pipeline;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.pipeline.processor.MessageProcessor;
import com.winterhavenmc.library.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.library.messagebuilder.pipeline.sender.MessageSender;
import com.winterhavenmc.library.messagebuilder.pipeline.sender.TitleSender;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.messages.MessageId.NONEXISTENT_ENTRY;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessagePipelineTest
{

	@Mock MessageRetriever messageRetrieverMock;
	@Mock
	MessageProcessor messageProcessorMock;
	@Mock Player playerMock;
	@Mock MessageSender messageSenderMock;
	@Mock TitleSender titleSenderMock;

	Recipient.Valid recipient;
	CooldownMap cooldownMap;
	MessagePipeline messagePipeline;
	ValidMessageRecord validMessageRecord;
	InvalidMessageRecord invalidMessageRecord;
	FinalMessageRecord finalMessageRecord;
	ConfigurationSection section;
	RecordKey recordKey;
	MacroStringMap macroStringMap;


	@BeforeEach
	void setUp()
	{
		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		cooldownMap = new CooldownMap();
		messagePipeline = new MessagePipeline(
				messageRetrieverMock,
				messageProcessorMock,
				cooldownMap,
				List.of(messageSenderMock, titleSenderMock)
		);

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

		macroStringMap = new MacroStringMap();
		macroStringMap.put(MacroKey.of("RECIPIENT").orElseThrow(), "player name");
		macroStringMap.put(MacroKey.of("RECIPIENT.NAME").orElseThrow(), "player name");
	}


	@Test @DisplayName("Test process method with Valid parameter")
	void testInitiate()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(messageRetrieverMock.getRecord(recordKey)).thenReturn(validMessageRecord);
		ValidMessage message = new ValidMessage(recipient, RecordKey.of(ENABLED_MESSAGE).orElseThrow(), messagePipeline);

		when(messageProcessorMock.process(validMessageRecord, message.getObjectMap())).thenReturn(finalMessageRecord);

		// Act & Assert
		assertDoesNotThrow(() -> messagePipeline.initiate(message));

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
		verify(messageRetrieverMock, atLeastOnce()).getRecord(recordKey);
	}

	@Test
	void testInitiate_non_existent_message()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(NONEXISTENT_ENTRY).orElseThrow();
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(messageRetrieverMock.getRecord(recordKey)).thenReturn(invalidMessageRecord);
		ValidMessage message = new ValidMessage(recipient, RecordKey.of(NONEXISTENT_ENTRY).orElseThrow(), messagePipeline);

		// Act & Assert
		messagePipeline.initiate(message);

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
		verify(messageRetrieverMock, atLeastOnce()).getRecord(recordKey);
	}

}
