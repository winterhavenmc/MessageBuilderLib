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

import com.winterhavenmc.util.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.util.messagebuilder.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.retriever.MessageRetriever;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.MessageSender;
import com.winterhavenmc.util.messagebuilder.pipeline.sender.TitleSender;
import com.winterhavenmc.util.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.model.language.message.FinalMessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.MessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessagePipelineTest
{

	@Mock MessageRetriever messageRetrieverMock;
	@Mock MacroReplacer macroReplacerMock;
	@Mock Player playerMock;
	@Mock MessageSender messageSenderMock;
	@Mock TitleSender titleSenderMock;

	Recipient.Valid recipient;
	CooldownMap cooldownMap;
	MessagePipeline messagePipeline;
	ValidMessageRecord validMessageRecord;
	FinalMessageRecord finalMessageRecord;
	ConfigurationSection section;
	RecordKey recordKey;


	@BeforeEach
	void setUp()
	{
		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		cooldownMap = new CooldownMap();
		messagePipeline = new MessagePipeline(
				messageRetrieverMock,
				macroReplacerMock,
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
	}


	@AfterEach
	void tearDown()
	{
		messageRetrieverMock = null;
		macroReplacerMock = null;
		playerMock = null;
		messageSenderMock = null;
		titleSenderMock = null;
		cooldownMap = null;
		messagePipeline = null;
		validMessageRecord = null;
	}


	@Test @DisplayName("Test process method with Valid parameter")
	void testProcess() {
		// Arrange
		RecordKey recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(messageRetrieverMock.getRecord(recordKey)).thenReturn(validMessageRecord);
		ValidMessage message = new ValidMessage(recipient, RecordKey.of(ENABLED_MESSAGE).orElseThrow(), messagePipeline);

		when(macroReplacerMock.replaceMacros(validMessageRecord, message.getContextMap())).thenReturn(finalMessageRecord);

		// Act & Assert
		assertDoesNotThrow(() -> messagePipeline.process(message));

		// Verify
		verify(playerMock, atLeastOnce()).getUniqueId();
		verify(messageRetrieverMock, atLeastOnce()).getRecord(recordKey);
	}

}
