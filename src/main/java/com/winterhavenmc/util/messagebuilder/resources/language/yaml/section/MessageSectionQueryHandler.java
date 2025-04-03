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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.validation.ExceptionMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.CONFIGURATION_SUPPLIER;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * Handles queries for message records from the current language file. The constructor takes the 'MESSAGES' configuration
 * section as a parameter, and throws an exception if the provided configuration section is not the language file
 * message section.
 */
public class MessageSectionQueryHandler implements QueryHandler<MessageRecord>
{
	private final static Section section = Section.MESSAGES;
	private final YamlConfigurationSupplier configurationSupplier;


	/**
	 * Class constructor
	 *
	 * @param configurationSupplier the configuration supplier that provides access to the configuration object for the language file.
	 */
	public MessageSectionQueryHandler(YamlConfigurationSupplier configurationSupplier)
	{
		validate(configurationSupplier, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, CONFIGURATION_SUPPLIER));

		this.configurationSupplier = configurationSupplier;
	}


	/**
	 * Retrieve a message record from the language file for the provided key
	 *
	 * @param key the MessageId of the message record to be retrieved
	 * @return the message record for the MessageId
	 */
	@Override
	public Optional<MessageRecord> getRecord(final RecordKey key)
	{
		return Optional.ofNullable(configurationSupplier.getSection(section).getConfigurationSection(key.toString()))
				.map(messageEntry -> new MessageRecord(key,
					messageEntry.getBoolean(Field.ENABLED.toKey()),
					messageEntry.getString(Field.MESSAGE_TEXT.toKey()),
					Duration.ofSeconds(messageEntry.getLong(Field.REPEAT_DELAY.toKey())),
					messageEntry.getString(Field.TITLE_TEXT.toKey()),
					messageEntry.getInt(Field.TITLE_FADE_IN.toKey()),
					messageEntry.getInt(Field.TITLE_STAY.toKey()),
					messageEntry.getInt(Field.TITLE_FADE_OUT.toKey()),
					messageEntry.getString(Field.SUBTITLE_TEXT.toKey()),
					"",
					"",
					""));
	}


	/**
	 * Enum of MessageRecord fields and their corresponding keyPath. This enum is the source of truth for
	 * message record field constants and their corresponding keyPaths. Other field metadata may be
	 * encapsulated in this enum in the future.
	 */
	public enum Field
	{
		ENABLED("ENABLED"),
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

}
