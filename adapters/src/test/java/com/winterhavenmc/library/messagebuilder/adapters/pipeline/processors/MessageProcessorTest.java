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

import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.MacroFieldAccessor;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration.Time4jDurationFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.matchers.RegexPlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value.AtomicResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value.CompositeResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.value.MacroValueResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemDisplayNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.itemname.BukkitItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.adapters.util.Macro;
import com.winterhavenmc.library.messagebuilder.adapters.util.MessageId;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.AccessorRegistry;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ItemRepository;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;

import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.message.Message;
import com.winterhavenmc.library.messagebuilder.core.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.accessors.FieldAccessorRegistry;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.matchers.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.macro.ValueResolver;
import com.winterhavenmc.library.messagebuilder.adapters.pipeline.resolvers.worldname.DefaultResolver;
import com.winterhavenmc.library.messagebuilder.adapters.resources.language.YamlLanguageResourceManager;

import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.message.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.message.MessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.message.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.recipient.Recipient;

import com.winterhavenmc.library.messagebuilder.models.validation.ValidationException;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import org.mockito.Mock;

import java.util.List;


import static com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.models.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MessageProcessorTest
{
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock ConfigRepository configRepositoryMock;
	@Mock DefaultResolver worldNameResolverMock;
	@Mock MessagePipeline messagePipelineMock;
	@Mock YamlLanguageResourceManager languageResourceManagerMock;
	@Mock ItemRepository itemRepositoryMock;

	Recipient.Valid recipient;
	ValidMessageKey messageKey;
	ValidMacroKey macroKey;
	MessageProcessor messageProcessor;
	Message message;
	ConfigurationSection section;
	ValidMessageRecord validMessageRecord;
	List<ValueResolver> resolvers;
	ValueResolver fieldResolver;
	PlaceholderMatcher placeholderMatcher;
	MacroStringMap macroStringMap;


	@BeforeEach
	public void setUp()
	{
		recipient = switch (Recipient.of(playerMock))
		{
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		messageKey = MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow();
		macroKey = MacroKey.of(Macro.OWNER).isValid().orElseThrow();

		message = new ValidMessage(pluginMock, recipient, messageKey, messagePipelineMock);

		Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(configRepositoryMock);
		LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(configRepositoryMock);
		FormatterCtx formatterContainer = new FormatterCtx(configRepositoryMock, time4jDurationFormatter, localeNumberFormatter);
		AccessorCtx adapterContextContainer = new AccessorCtx(worldNameResolverMock,
				new BukkitItemNameResolver(), new BukkitItemDisplayNameResolver(),
				new BukkitItemPluralNameResolver(itemRepositoryMock), formatterContainer);
		AccessorRegistry accessorRegistry = new FieldAccessorRegistry(adapterContextContainer);
		MacroFieldAccessor fieldExtractor = new MacroFieldAccessor(adapterContextContainer);

		CompositeResolver compositeResolver = new CompositeResolver(accessorRegistry, fieldExtractor);
		AtomicResolver atomicResolver = new AtomicResolver(formatterContainer);


		resolvers = List.of(compositeResolver, atomicResolver);
		fieldResolver = new MacroValueResolver(resolvers);
		placeholderMatcher = new RegexPlaceholderMatcher();

		messageProcessor = new MessageProcessor(fieldResolver, placeholderMatcher);

		messageKey = MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow();

		section = new MemoryConfiguration();
		section.set(MessageRecord.Field.ENABLED.toKey(), true);
		section.set(MessageRecord.Field.MESSAGE_TEXT.toKey(), "this is a test message");
		section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), 11);
		section.set(MessageRecord.Field.TITLE_TEXT.toKey(), "this is a test title");
		section.set(MessageRecord.Field.TITLE_FADE_IN.toKey(), 22);
		section.set(MessageRecord.Field.TITLE_STAY.toKey(), 33);
		section.set(MessageRecord.Field.TITLE_FADE_OUT.toKey(), 44);
		section.set(MessageRecord.Field.SUBTITLE_TEXT.toKey(), "this is a test subtitle");

		validMessageRecord = ValidMessageRecord.create(messageKey, section);

		macroStringMap = new MacroStringMap();
		macroStringMap.put(MacroKey.of("RECIPIENT").isValid().orElseThrow(), "player name");
		macroStringMap.put(MacroKey.of("RECIPIENT.NAME").isValid().orElseThrow(), "player name");
	}


	@Test @DisplayName("Test replaceMacros method with Valid parameter")
	void testProcess_valid_parameters()
	{
		// Act
		FinalMessageRecord result = messageProcessor.process(validMessageRecord, message.getObjectMap());

		// Assert
		assertNotNull(result);
	}

}
