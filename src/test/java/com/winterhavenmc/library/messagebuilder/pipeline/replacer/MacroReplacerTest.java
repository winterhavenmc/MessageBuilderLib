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

package com.winterhavenmc.library.messagebuilder.pipeline.replacer;

import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterRegistry;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroObjectMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.model.message.Message;
import com.winterhavenmc.library.messagebuilder.model.message.ValidMessage;
import com.winterhavenmc.library.messagebuilder.messages.Macro;
import com.winterhavenmc.library.messagebuilder.messages.MessageId;
import com.winterhavenmc.library.messagebuilder.pipeline.extractor.FieldExtractor;
import com.winterhavenmc.library.messagebuilder.pipeline.matcher.PlaceholderMatcher;
import com.winterhavenmc.library.messagebuilder.pipeline.MessagePipeline;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.AtomicResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.CompositeResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FieldResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.Resolver;
import com.winterhavenmc.library.messagebuilder.pipeline.result.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.model.recipient.Recipient;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.language.FinalMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.Time4jDurationFormatter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RECIPIENT;
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
			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
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
			MacroObjectMap macroObjectMap = new MacroObjectMap();
			MacroKey macroKey = MacroKey.of("ITEM_NAME").orElseThrow();
			macroObjectMap.putIfAbsent(macroKey, "TEST_STRING");

			String resultString = macroReplacer.replaceMacrosInString(macroObjectMap, "Replace this: {ITEM_NAME}");
			assertEquals("Replace this: TEST_STRING", resultString);
		}


		@Test @DisplayName("Test replaceMacrosInString method with null messageString parameter")
		void testReplaceMacrosInString_parameter_null_messageString()
		{
			// Arrange
			MacroObjectMap macroObjectMap = new MacroObjectMap();

			// Act
			ValidationException exception = assertThrows(ValidationException.class,
					() -> macroReplacer.replaceMacrosInString(macroObjectMap, null));

			// Assert
			assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
		}
	}


	@Test
	void testReplacements() {
		// Arrange
		MacroReplacer localMacroReplacer = new MacroReplacer(fieldResolver, placeholderMatcher);

		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey resultMacroKey = MacroKey.of("KEY").orElseThrow();
		macroStringMap.put(resultMacroKey, "value");
		String messageString = "this is a macro replacement string {KEY}.";

		// Act
		String resultString = localMacroReplacer.replacements(macroStringMap, messageString);

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
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey key1 = MacroKey.of("KEY1").orElseThrow();
		MacroKey key2 = MacroKey.of("KEY2").orElseThrow();
		macroStringMap.put(key1, "value1");
		macroStringMap.put(key2, "value2");
		macroStringMap.put(key1, "value3");

		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroReplacer.replacements(macroStringMap, null));

		assertEquals("The parameter 'messageString' cannot be null.", exception.getMessage());
	}


//	@Test
//	void addRecipientContext()
//	{
//		ConsoleCommandSender console = mock(ConsoleCommandSender.class);
//		recipient = switch (Recipient.of(console)) {
//			case Recipient.Valid valid -> valid;
//			case Recipient.Proxied ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
//			case Recipient.Invalid ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
//		};
//		MacroKey key = MacroKey.of("RECIPIENT").orElseThrow();
//		MacroObjectMap contextMap = MacroObjectMap.of(recipient, messageKey).orElseThrow();
//
//		assertTrue(contextMap.contains(key));
//	}

}
