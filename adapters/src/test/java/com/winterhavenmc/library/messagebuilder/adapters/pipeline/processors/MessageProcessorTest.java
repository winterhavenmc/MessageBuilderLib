/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.processors;

import com.winterhavenmc.library.messagebuilder.adapters.util.Macro;
import com.winterhavenmc.library.messagebuilder.adapters.util.MessageId;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.core.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;

import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.matchers.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlLanguageResourceManager;

import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.message.MessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.message.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;

import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageProcessorTest
{
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock ConfigRepository configRepositoryMock;
	@Mock MessagePipeline messagePipelineMock;
	@Mock YamlLanguageResourceManager languageResourceManagerMock;
	@Mock ItemRepository itemRepositoryMock;

	@Mock ValidMessage messageMock;

	@Mock ValueResolver valueResolverMock;
	@Mock PlaceholderMatcher placeholderMatcherMock;

	Recipient.Valid validRecipient;
	ValidMessageKey messageKey;
	ValidMacroKey macroKey;
	MessageProcessor messageProcessor;
	ConfigurationSection section;
	ValidMessageRecord validMessageRecord;
	List<ValueResolver> resolvers;
	MacroStringMap macroStringMap;
	MacroObjectMap objectMap;

	String languageFile = """
			MESSAGES:
			  ENABLED_MESSAGE:
			    ENABLED: true
			    MESSAGE_TEXT: "This is an enabled message"
			
			  DISABLED_MESSAGE:
			    ENABLED: false
			    MESSAGE_TEXT: "This is a disabled message"
			""";

	@BeforeEach
	public void setUp() throws InvalidConfigurationException
	{
		FileConfiguration configuration = new YamlConfiguration();
		configuration.loadFromString(languageFile);
		Recipient.Valid validRecipient = new Recipient.Valid(playerMock);
		objectMap = new MacroObjectMap();
		messageKey = MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow();
		macroKey = MacroKey.of(Macro.OWNER).isValid().orElseThrow();
		ConfigurationSection messageEntry = configuration.getConfigurationSection("MESSAGES.ENABLED_MESSAGE");
		validMessageRecord = (ValidMessageRecord) MessageRecord.of(messageKey, messageEntry);
	}


	@Test @DisplayName("Test process method with Valid parameter")
	void testProcess_valid_parameters()
	{
		// Arrange
		when(messageMock.getObjectMap()).thenReturn(objectMap);

		// Act
		MessageProcessor messageProcessor = new MessageProcessor(valueResolverMock, placeholderMatcherMock);
		FinalMessageRecord result = messageProcessor.process(validMessageRecord, messageMock.getObjectMap());

		// Assert
		assertInstanceOf(FinalMessageRecord.class, result);
	}

}
