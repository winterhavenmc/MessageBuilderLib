
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

package com.winterhavenmc.library.messagebuilder.models.language;

import com.winterhavenmc.library.messagebuilder.models.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import org.bukkit.configuration.ConfigurationSection;


/**
 * A sealed interface representing a structured message record loaded from the {@code MESSAGES}
 * section of a language YAML file.
 * <p>
 * A {@code MessageRecord} contains all the information required to display text or titles
 * to players using templated and macro-resolved content. This includes:
 *
 * <ul>
 *   <li>A main message body (chat message)</li>
 *   <li>An optional title/subtitle display</li>
 *   <li>Timing controls (title fade-in, stay, and fade-out durations)</li>
 *   <li>Optional repeat delay for messages that would otherwise repeat too frequently</li>
 * </ul>
 *
 * <h2>Implementations</h2>
 * <ul>
 *   <li>{@link ValidMessageRecord} – A fully validated message loaded from configuration</li>
 *   <li>{@link InvalidMessageRecord} – A message record representing an invalid or missing configuration</li>
 *   <li>{@link FinalMessageRecord} – An immutable, rendered message derived from a valid record</li>
 * </ul>
 *
 * <h2>Factory Methods</h2>
 * Use {@link #of(ValidMessageKey, ConfigurationSection)} to parse a configuration section,
 * or {@link InvalidMessageRecord#empty(RecordKey, InvalidRecordReason)} to create an invalid placeholder when parsing fails.
 *
 * @see SectionRecord SectionRecord
 * @see ValidMessageKey
 */
public sealed interface MessageRecord extends SectionRecord permits ValidMessageRecord, InvalidMessageRecord, FinalMessageRecord
{
	/**
	 * Factory method that constructs a {@code MessageRecord} from a YAML configuration section.
	 * Returns a {@link ValidMessageRecord} if the section is non-null and valid,
	 * or an {@link InvalidMessageRecord} if the section is missing.
	 *
	 * @param messageKey the unique record string used to identify this message
	 * @param messageEntry the corresponding YAML configuration section
	 * @return a valid or invalid {@code MessageRecord} depending on input
	 */
	static MessageRecord of(final ValidMessageKey messageKey, final ConfigurationSection messageEntry)
	{
		return (messageEntry == null)
				? MessageRecord.empty(messageKey, InvalidRecordReason.MESSAGE_ENTRY_MISSING)
				: ValidMessageRecord.create(messageKey, messageEntry);
	}


	/**
	 * Returns an {@link InvalidMessageRecord} representing a missing or invalid message definition.
	 *
	 * @param messageKey the string associated with the missing record
	 * @return a placeholder {@code MessageRecord} indicating an empty or unresolved message
	 */
	static InvalidMessageRecord empty(RecordKey messageKey, InvalidRecordReason reason)
	{
		return new InvalidMessageRecord(messageKey, reason);
	}


	/**
	 * Enum representing the fields defined in a {@link ValidMessageRecord}.
	 * <p>
	 * Each constant in this enum maps to a specific string in the YAML configuration.
	 * This enum provides a single source of truth for these keys, which may be
	 * decoupled from the enum constant names in the future.
	 *
	 * <p>This design ensures central schema management and supports future extensions
	 * such as metadata annotations or type hints per field.
	 */
	enum Field
	{
		ENABLED("ENABLED"),
		REPEAT_DELAY("REPEAT_DELAY"),
		MESSAGE_TEXT("MESSAGE_TEXT"),
		TITLE_TEXT("TITLE_TEXT"),
		TITLE_FADE_IN("TITLE_FADE_IN"),
		TITLE_STAY("TITLE_STAY"),
		TITLE_FADE_OUT("TITLE_FADE_OUT"),
		SUBTITLE_TEXT("SUBTITLE_TEXT");

		private final String keyString;

		Field(final String keyString) {
			this.keyString = keyString;
		}

		/**
		 * Returns the raw YAML string associated with this field.
		 *
		 * @return the field string
		 */
		public String toKey() {
			return this.keyString;
		}

		@Override
		public String toString() {
			return this.keyString;
		}
	}

}
