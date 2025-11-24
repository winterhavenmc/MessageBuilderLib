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

package com.winterhavenmc.library.messagebuilder.models.language.message;

import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.util.MessageId;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;


class MessageRecordTest
{
	@Test
	void of_valid_section()
	{
		// Arrange
		ValidMessageKey messageKey = MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow();

		ConfigurationSection section = new MemoryConfiguration();
		section.set(MessageRecord.Field.ENABLED.toKey(), true);
		section.set(MessageRecord.Field.MESSAGE_TEXT.toKey(), "this is a test message");
		section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), 11);
		section.set(MessageRecord.Field.TITLE_TEXT.toKey(), "this is a test title");
		section.set(MessageRecord.Field.TITLE_FADE_IN.toKey(), 22);
		section.set(MessageRecord.Field.TITLE_STAY.toKey(), 33);
		section.set(MessageRecord.Field.TITLE_FADE_OUT.toKey(), 44);
		section.set(MessageRecord.Field.SUBTITLE_TEXT.toKey(), "this is a test subtitle");

		// Act
		MessageRecord messageRecord = MessageRecord.of(messageKey, section);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, messageRecord);
	}


	@Test
	void of_null_section()
	{
		// Arrange
		ValidMessageKey messageKey = MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow();

		// Act
		MessageRecord messageRecord = MessageRecord.of(messageKey, null);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, messageRecord);
	}

}
