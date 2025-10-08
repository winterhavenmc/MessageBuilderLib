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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline;

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.cooldown.MessageCooldownMap;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.processors.MessageProcessor;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders.KyoriMessageSender;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.senders.KyoriTitleSender;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.cooldown.CooldownMap;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.retrievers.MessageRetriever;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.winterhavenmc.library.messagebuilder.adapters.util.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;


@ExtendWith(MockitoExtension.class)
class MessagePipelineTest
{
	@Mock MessageRetriever messageRetrieverMock;
	@Mock MessageProcessor messageProcessorMock;
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock KyoriMessageSender messageSenderMock;
	@Mock KyoriTitleSender titleSenderMock;

	Recipient.Valid recipient;
	CooldownMap cooldownMap;
	MessagePipeline messagePipeline;
	ValidMessageRecord validMessageRecord;
	InvalidMessageRecord invalidMessageRecord;
	FinalMessageRecord finalMessageRecord;
	ConfigurationSection section;
	ValidMessageKey recordKey;
	MacroStringMap macroStringMap;


	@BeforeEach
	void setUp()
	{
		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		cooldownMap = new MessageCooldownMap();
		messagePipeline = new MessagePipeline(
				messageRetrieverMock,
				messageProcessorMock,
				cooldownMap,
				List.of(messageSenderMock, titleSenderMock)
		);

		recordKey = MessageKey.of(ENABLED_MESSAGE).isValid().orElseThrow();

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
		macroStringMap.put(MacroKey.of("RECIPIENT").isValid().orElseThrow(), "player name");
		macroStringMap.put(MacroKey.of("RECIPIENT.NAME").isValid().orElseThrow(), "player name");
	}


//	@Test @DisplayName("Test process method with Valid parameter")
//	void testInitiate()
//	{
//		// Arrange
//		ValidMessageKey recordKey = MessageKey.of(ENABLED_MESSAGE).isValid().orElseThrow();
//		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
//		when(messageRetrieverMock.getRecord(recordKey)).thenReturn(validMessageRecord);
//		ValidMessage message = new ValidMessage(pluginMock, recipient, MessageKey.of(ENABLED_MESSAGE).isValid().orElseThrow(), messagePipeline);
//
//		when(messageProcessorMock.process(validMessageRecord, message.getObjectMap())).thenReturn(finalMessageRecord);
//
//		// Act & Assert
//		assertDoesNotThrow(() -> messagePipeline.initiate(message));
//
//		// Verify
//		verify(playerMock, atLeastOnce()).getUniqueId();
//		verify(messageRetrieverMock, atLeastOnce()).getRecord(recordKey);
//	}
//
//	@Test
//	void testInitiate_non_existent_message()
//	{
//		// Arrange
//		ValidMessageKey recordKey = MessageKey.of(NONEXISTENT_ENTRY).isValid().orElseThrow();
//		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
//		when(messageRetrieverMock.getRecord(recordKey)).thenReturn(invalidMessageRecord);
//		ValidMessage message = new ValidMessage(pluginMock, recipient, MessageKey.of(NONEXISTENT_ENTRY).isValid().orElseThrow(), messagePipeline);
//
//		// Act & Assert
//		messagePipeline.initiate(message);
//
//		// Verify
//		verify(playerMock, atLeastOnce()).getUniqueId();
//		verify(messageRetrieverMock, atLeastOnce()).getRecord(recordKey);
//	}

}
