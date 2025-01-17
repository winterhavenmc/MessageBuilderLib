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
import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

/**
 * A data object record for message information contained in the language file. This class also contains
 * an enum of fields with their corresponding path key, and a static method for retrieving a record.
 *
 * @param messageKey the key for the message
 * @param enabled the enabled setting for the message
 * @param message the raw message string, with placeholders
 * @param repeatDelay the repeat delay setting for the message
 * @param title the raw title string, with placeholders
 * @param titleFadeIn the title fade in setting for the message
 * @param titleStay the title stay setting for the message
 * @param titleFadeOut the title fade out setting for the message
 * @param subtitle the subtitle for the message
 */
public record MessageRecord(
		String messageKey,
		boolean enabled,
		String message,
		long repeatDelay,
		String title,
		int titleFadeIn,
		int titleStay,
		int titleFadeOut,
		String subtitle) {


	/**
	 * Enum of MessageRecord fields and their corresponding keyPath. This enum is the source of truth for
	 * message record field constants and their corresponding keyPaths. Other field metadata may be
	 * encapsulated in this enum in the future.
	 */
	enum Field {
		ENABLED("ENABLED"),
		REPEAT_DELAY("REPEAT_DELAY"),
		MESSAGE_TEXT("MESSAGE_TEXT"),
		TITLE_TEXT("TITLE_TEXT"),
		TITLE_FADE_IN("TITLE_FADE_IN"),
		TITLE_STAY("TITLE_STAY"),
		TITLE_FADE_OUT("TITLE_FADE_OUT"),
		SUBTITLE_TEXT("SUBTITLE_TEXT"),
		;

		private final String keyPath;

		// class constructor
		Field(final String keyPath) {
			this.keyPath = keyPath;
		}

		// getter for key
		public String toKey() {
			return this.keyPath;
		}
	}


	/**
	 * A static method to retrieve a message record.
	 *
	 * @param messageId the {@link MessageId} for the message to be retrieved from the language file
	 * @param messageSection the message section containing the messages
	 * @return a MessageRecord if an entry could be found for the {@code MessageId}, otherwise an empty Optional.
	 * @param <MessageId> an enum constant that serves as a key to a message entry in the language file
	 */
	public static // scope
	<MessageId extends Enum<MessageId>> // parameter type
	Optional<MessageRecord> // return type
	getRecord(final MessageId messageId, final ConfigurationSection messageSection) {
		if (messageId == null) { throw new IllegalArgumentException(Error.Parameter.NULL_MESSAGE_ID.getMessage()); }
		if (messageSection == null) { throw new IllegalArgumentException(Error.Parameter.NULL_SECTION_MESSAGES.getMessage()); }

		// only allow the 'MESSAGES' section of the language file to be passed as the constructor parameter
		if (!Section.MESSAGES.name().equals(messageSection.getName())) {
			throw new IllegalArgumentException(Error.Parameter.INVALID_SECTION_MESSAGES.getMessage());
		}

		// get entry for messageId
		ConfigurationSection messageEntry = messageSection.getConfigurationSection(messageId.name());
		if (messageEntry == null) { return Optional.empty(); }

		return Optional.of(new MessageRecord(messageId.toString(),
				messageEntry.getBoolean(Field.ENABLED.toKey()),
				messageEntry.getString(Field.MESSAGE_TEXT.toKey()),
				messageEntry.getLong(Field.REPEAT_DELAY.toKey()),
				messageEntry.getString(Field.TITLE_TEXT.toKey()),
				messageEntry.getInt(Field.TITLE_FADE_IN.toKey()),
				messageEntry.getInt(Field.TITLE_STAY.toKey()),
				messageEntry.getInt(Field.TITLE_FADE_OUT.toKey()),
				messageEntry.getString(Field.SUBTITLE_TEXT.toKey())));
	}

}
