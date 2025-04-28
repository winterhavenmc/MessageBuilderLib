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

package com.winterhavenmc.library.messagebuilder.pipeline.matcher;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.model.message.Message;
import com.winterhavenmc.library.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.messages.MessageId;

import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.library.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.AtomicResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.CompositeResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FieldResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.Time4jDurationFormatter;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PlaceholderMatcherTest
{
	@Mock Player playerMock;
	@Mock LocaleProvider localeProviderMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock MessagePipeline messagePipelineMock;

	Recipient.Valid recipient;
	MacroReplacer macroReplacer;
	Message message;


	@BeforeEach
	public void setUp()
	{
		RecordKey messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();

		AdapterContextContainer adapterContextContainer = new AdapterContextContainer(worldNameResolverMock);

		AdapterRegistry adapterRegistry = new AdapterRegistry(adapterContextContainer);
		FieldExtractor fieldExtractor = new FieldExtractor();

		CompositeResolver compositeResolver = new CompositeResolver(adapterRegistry, fieldExtractor);
		Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(localeProviderMock);
		LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(localeProviderMock);
		FormatterContainer formatterContainer = new FormatterContainer(time4jDurationFormatter, localeNumberFormatter);

		AtomicResolver atomicResolver = new AtomicResolver(formatterContainer);
		List<Resolver> resolvers = List.of(compositeResolver, atomicResolver);

		PlaceholderMatcher placeholderMatcher = new PlaceholderMatcher();

		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Proxied ignored-> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};

		message = new ValidMessage(recipient, messageKey, messagePipelineMock);
		macroReplacer = new MacroReplacer(new FieldResolver(resolvers), placeholderMatcher);
	}


	@Test
	void testGetPlaceholderStream()
	{
		// Arrange
		String messageString = "This is a {MESSAGE} with {SEVERAL} {PLACE_HOLDERS}.";

		// Act
		Stream<String> placeholderStream = new PlaceholderMatcher().match(messageString);
		Set<String> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertFalse(placeholderSet.isEmpty());
		assertTrue(placeholderSet.contains("SEVERAL"));
		assertTrue(placeholderSet.contains("MESSAGE"));
		assertTrue(placeholderSet.contains("PLACE_HOLDERS"));
		assertFalse(placeholderSet.contains("This"));
	}

}
