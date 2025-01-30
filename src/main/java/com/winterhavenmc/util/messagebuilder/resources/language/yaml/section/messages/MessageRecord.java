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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;
import java.util.Optional;
import java.util.List;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.INVALID_SECTION;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.MESSAGE_ID;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.Parameter.MESSAGE_SECTION;


/**
 * A data object record for message information contained in the language file. This class also contains
 * an enum of fields with their corresponding path key, and a static method for retrieving a record.
 *
 * @param messageId the key for the message
 * @param enabled the enabled setting for the message
 * @param message the raw message string, with placeholders
 * @param repeatDelay the repeat delay setting for the message
 * @param title the raw title string, with placeholders
 * @param titleFadeIn the title fade in setting for the message
 * @param titleStay the title stay setting for the message
 * @param titleFadeOut the title fade out setting for the message
 * @param subtitle the subtitle for the message
 */
public record MessageRecord<MessageId extends Enum<MessageId>> (
		MessageId messageId,
		boolean enabled,
		boolean translatable,
		String translatableKey,
		List<String> translatableArgs,
		String message,
		Duration repeatDelay,
		String title,
		int titleFadeIn,
		int titleStay,
		int titleFadeOut,
		String subtitle,
		String finalMessageString,
		String finalTitleString,
		String finalSubTitleString)
{

	/**
	 * Enum of MessageRecord fields and their corresponding keyPath. This enum is the source of truth for
	 * message record field constants and their corresponding keyPaths. Other field metadata may be
	 * encapsulated in this enum in the future.
	 */
	public enum Field
	{
		ENABLED("ENABLED"),
		TRANSLATABLE("TRANSLATABLE"),
		TRANSLATABLE_KEY("TRANSLATABLE_KEY"),
		TRANSLATABLE_ARGS("TRANSLATABLE_ARGS"),
		REPEAT_DELAY("REPEAT_DELAY"),
		MESSAGE_TEXT("MESSAGE_TEXT"),
		TITLE_TEXT("TITLE_TEXT"),
		TITLE_FADE_IN("TITLE_FADE_IN"),
		TITLE_STAY("TITLE_STAY"),
		TITLE_FADE_OUT("TITLE_FADE_OUT"),
		SUBTITLE_TEXT("SUBTITLE_TEXT"),
		;

		private final String key;

		// class constructor
		Field(final String key) {
			this.key = key;
		}

		// getter for key
		public String toKey() {
			return this.key;
		}
	}


	/**
	 * A static method to retrieve a message record.
	 *
	 * @param messageId the {@code MessageId} for the message to be retrieved from the language file
	 * @param messageSection the message section containing the messages
	 * @return a MessageRecord if an entry could be found for the {@code MessageId}, otherwise an empty Optional.
	 * @param <MessageId> an enum constant that serves as a key to a message entry in the language file
	 */
	public static // scope
	<MessageId extends Enum<MessageId>> // parameter type
	Optional<MessageRecord<MessageId>> // return type
	getRecord(final MessageId messageId, final ConfigurationSection messageSection)
	{
		if (messageId == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_ID); }
		if (messageSection == null) { throw new LocalizedException(PARAMETER_NULL, MESSAGE_SECTION); }

		// only allow the 'MESSAGES' section of the language file to be passed as the constructor parameter
		if (!Section.MESSAGES.name().equals(messageSection.getName())) {
			throw new LocalizedException(INVALID_SECTION, Section.MESSAGES.name());
		}

		// get entry for messageId
		ConfigurationSection messageEntry = messageSection.getConfigurationSection(messageId.name());
		if (messageEntry == null) { return Optional.empty(); }

		return Optional.of(new MessageRecord<>(messageId,
				messageEntry.getBoolean(Field.ENABLED.toKey()),
				messageEntry.getBoolean(Field.TRANSLATABLE.toKey()),
				messageEntry.getString(Field.TRANSLATABLE_KEY.toKey()),
				messageEntry.getStringList(Field.TRANSLATABLE_ARGS.toKey()),
				messageEntry.getString(Field.MESSAGE_TEXT.toKey()),
				Duration.ofSeconds(messageEntry.getLong(Field.REPEAT_DELAY.toKey())),
				messageEntry.getString(Field.TITLE_TEXT.toKey()),
				messageEntry.getInt(Field.TITLE_FADE_IN.toKey()),
				messageEntry.getInt(Field.TITLE_STAY.toKey()),
				messageEntry.getInt(Field.TITLE_FADE_OUT.toKey()),
				messageEntry.getString(Field.SUBTITLE_TEXT.toKey()),
				"",
				"",
				"")
		);
	}


	/**
	 * Create a duplicate record with the final message string fields populated
	 *
	 * @param newFinalMessageString final message string
	 * @param newFinalTitleString final title string
	 * @param newFinalSubTitleString final subtitle string
	 * @return a new {@code MessageRecord} with the final message string fields populated
	 */
	public Optional<MessageRecord<MessageId>> withFinalStrings(final String newFinalMessageString,
	                                                           final String newFinalTitleString,
	                                                           final String newFinalSubTitleString)
	{
		return Optional.of(new MessageRecord<>(
				this.messageId,
				this.enabled,
				this.translatable,
				this.translatableKey,
				this.translatableArgs,
				this.message,
				this.repeatDelay,
				this.title,
				this.titleFadeIn,
				this.titleStay,
				this.titleFadeOut,
				this.subtitle,
				newFinalMessageString,
				newFinalTitleString,
				newFinalSubTitleString)
		);
	}

}
