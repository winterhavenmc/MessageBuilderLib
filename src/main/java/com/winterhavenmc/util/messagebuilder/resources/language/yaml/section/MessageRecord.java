
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

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Duration;


public sealed interface MessageRecord extends SectionRecord permits ValidMessageRecord, InvalidMessageRecord, FinalMessageRecord
{
	RecordKey key();
	Duration repeatDelay();


	static MessageRecord fromConfiguration(final RecordKey key, final ConfigurationSection section)
	{
		return section == null
				? MessageRecord.empty()
				: ValidMessageRecord.of(key,
						section.getBoolean(Field.ENABLED.toKey()),
						section.getString(Field.MESSAGE_TEXT.toKey()),
						Duration.ofSeconds(section.getLong(Field.REPEAT_DELAY.toKey())),
						section.getString(Field.TITLE_TEXT.toKey()),
						section.getInt(Field.TITLE_FADE_IN.toKey()),
						section.getInt(Field.TITLE_STAY.toKey()),
						section.getInt(Field.TITLE_FADE_OUT.toKey()),
						section.getString(Field.SUBTITLE_TEXT.toKey()));
	}

	static MessageRecord empty()
	{
		return new InvalidMessageRecord(null, null, "Missing configuration section.");
	}

	/**
	 * Enum of ValidMessageRecord fields and their corresponding keyPath. This enum is the source of truth for
	 * message record field constants and their corresponding keyPaths. Other field metadata may be
	 * encapsulated in this enum in the future.
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
