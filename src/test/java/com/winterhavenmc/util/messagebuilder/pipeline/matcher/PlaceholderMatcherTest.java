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

package com.winterhavenmc.util.messagebuilder.pipeline.matcher;

import com.winterhavenmc.util.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.util.messagebuilder.model.message.Message;
import com.winterhavenmc.util.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import com.winterhavenmc.util.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.util.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.util.messagebuilder.pipeline.replacer.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.AtomicResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.CompositeResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.FieldResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.util.messagebuilder.model.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.util.messagebuilder.model.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.util.messagebuilder.model.locale.LocaleSupplier;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.ResolverContextContainer;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.Time4jDurationFormatter;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PlaceholderMatcherTest
{
	@Mock Player playerMock;
	@Mock LocaleSupplier localeSupplierMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock MessagePipeline messagePipelineMock;

	ValidRecipient recipient;
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
		Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(localeSupplierMock);
		LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(localeSupplierMock);
		ResolverContextContainer resolverContextContainer = new ResolverContextContainer(time4jDurationFormatter, localeNumberFormatter);

		AtomicResolver atomicResolver = new AtomicResolver(resolverContextContainer);
		List<Resolver> resolvers = List.of(compositeResolver, atomicResolver);

		PlaceholderMatcher placeholderMatcher = new PlaceholderMatcher();

		recipient = switch (Recipient.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
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
