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

package com.winterhavenmc.util.messagebuilder.pipeline.replacer;

import com.winterhavenmc.util.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import com.winterhavenmc.util.messagebuilder.model.message.Message;
import com.winterhavenmc.util.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.util.messagebuilder.pipeline.matcher.PlaceholderMatcher;
import com.winterhavenmc.util.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.AtomicResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.CompositeResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.FieldResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.util.messagebuilder.pipeline.result.ResultMap;
import com.winterhavenmc.util.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.model.language.message.FinalMessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.MessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.FormatterContainer;
import com.winterhavenmc.util.messagebuilder.resources.configuration.LocaleProvider;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import com.winterhavenmc.util.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.Time4jDurationFormatter;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class MacroReplacerTest
{
	@Mock Player playerMock;
	@Mock LocaleProvider localeProviderMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock MessagePipeline messagePipelineMock;

	Recipient.Valid recipient;
	RecordKey messageKey;
	MacroKey macroKey;
	MacroReplacer macroReplacer;
	Message message;
	ConfigurationSection section;
	ValidMessageRecord validMessageRecord;
	FinalMessageRecord finalMessageRecord;
	List<Resolver> resolvers;
	FieldResolver fieldResolver;
	PlaceholderMatcher placeholderMatcher;


	@BeforeEach
	public void setUp()
	{
		recipient = switch (Recipient.of(playerMock)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		macroKey = MacroKey.of(Macro.OWNER).orElseThrow();

		message = new ValidMessage(recipient, messageKey, messagePipelineMock);

		AdapterContextContainer adapterContextContainer = new AdapterContextContainer(worldNameResolverMock);

		AdapterRegistry adapterRegistry = new AdapterRegistry(adapterContextContainer);
		FieldExtractor fieldExtractor = new FieldExtractor();
		CompositeResolver compositeResolver = new CompositeResolver(adapterRegistry, fieldExtractor);
		Time4jDurationFormatter time4jDurationFormatter = new Time4jDurationFormatter(localeProviderMock);
		LocaleNumberFormatter localeNumberFormatter = new LocaleNumberFormatter(localeProviderMock);
		FormatterContainer formatterContainer = new FormatterContainer(time4jDurationFormatter, localeNumberFormatter);
		AtomicResolver atomicResolver = new AtomicResolver(formatterContainer);


		resolvers = List.of(compositeResolver, atomicResolver);
		fieldResolver = new FieldResolver(resolvers);
		placeholderMatcher = new PlaceholderMatcher();

		macroReplacer = new MacroReplacer(fieldResolver, placeholderMatcher);

		messageKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

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

		finalMessageRecord = validMessageRecord.withFinalStrings(
				"this is a final message",
				"this is a final title",
				"this is a final subtitle");
	}


	@Test @DisplayName("Test replaceMacros method with Valid parameter")
	void testReplaceMacros_valid_parameters()
	{
		// Act
		FinalMessageRecord result = macroReplacer.replaceMacros(validMessageRecord, message.getContextMap());

		// Assert
		assertNotNull(result);
	}


	@Nested
	class ReplaceMacrosInStringTests
	{
		@Test @DisplayName("Test replaceMacrosInString method with Valid parameters")
		void testReplaceMacrosInString()
		{
			ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();
			MacroKey macroKey = MacroKey.of("ITEM_NAME").orElseThrow();
			contextMap.putIfAbsent(macroKey, "TEST_STRING");

			String resultString = macroReplacer.replaceMacrosInString(contextMap, "Replace this: {ITEM_NAME}");
			assertEquals("Replace this: TEST_STRING", resultString);
		}


		@Test @DisplayName("Test replaceMacrosInString method with null messageString parameter")
		void testReplaceMacrosInString_parameter_null_messageString()
		{
			// Arrange
			ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();

			// Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> macroReplacer.replaceMacrosInString(contextMap, null));

			// Assert
			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void testReplacements() {
		// Arrange
		MacroReplacer localMacroReplacer = new MacroReplacer(fieldResolver, placeholderMatcher);

		ResultMap resultMap = new ResultMap();
		MacroKey resultMacroKey = MacroKey.of("KEY").orElseThrow();
		resultMap.put(resultMacroKey, "value");
		String messageString = "this is a macro replacement string {KEY}.";

		// Act
		String resultString = localMacroReplacer.replacements(resultMap, messageString);

		// Assert
		assertEquals("this is a macro replacement string value.", resultString);
	}

	@Test
	void testReplacements_parameter_nul_replacement_map() {
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroReplacer.replacements(null, "some string"));

		assertEquals("The parameter 'replacementMap' cannot be null.", exception.getMessage());
	}

	@Test
	void testReplacements_parameter_nul_message_string() {
		ResultMap resultMap = new ResultMap();
		MacroKey key1 = MacroKey.of("KEY1").orElseThrow();
		MacroKey key2 = MacroKey.of("KEY2").orElseThrow();
		resultMap.put(key1, "value1");
		resultMap.put(key2, "value2");
		resultMap.put(key1, "value3");

		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroReplacer.replacements(resultMap, null));

		assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
	}


	@Test
	void addRecipientContext()
	{
		ConsoleCommandSender console = mock(ConsoleCommandSender.class);
		recipient = switch (Recipient.of(console)) {
			case Recipient.Valid valid -> valid;
			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		MacroKey key = MacroKey.of("RECIPIENT").orElseThrow();
		ContextMap contextMap = ContextMap.of(recipient, messageKey).orElseThrow();

		assertTrue(contextMap.contains(key));
	}

}
