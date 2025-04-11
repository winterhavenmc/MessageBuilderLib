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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;


class ValidMessageRecordTest
{
	RecordKey recordKey;
	ValidMessageRecord validMessageRecord;
	ConfigurationSection section;

	@BeforeEach
	public void setUp()
	{
		// create record key
		recordKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		// create configuration section for message record entry
		section = new MemoryConfiguration();
		section.set(MessageRecord.Field.ENABLED.toKey(), true);
		section.set(MessageRecord.Field.MESSAGE_TEXT.toKey(), "this is a test message");
		section.set(MessageRecord.Field.REPEAT_DELAY.toKey(), 11);
		section.set(MessageRecord.Field.TITLE_TEXT.toKey(), "this is a test title");
		section.set(MessageRecord.Field.TITLE_FADE_IN.toKey(), 22);
		section.set(MessageRecord.Field.TITLE_STAY.toKey(), 33);
		section.set(MessageRecord.Field.TITLE_FADE_OUT.toKey(), 44);
		section.set(MessageRecord.Field.SUBTITLE_TEXT.toKey(), "this is a test subtitle");

		// create valid message record from record key, message configuration section
		validMessageRecord = ValidMessageRecord.from(recordKey, section);
	}


	@Test
	void testKey()
	{
		// Arrange & Act
		RecordKey key = validMessageRecord.key();

		// Assert
		assertEquals(ENABLED_MESSAGE.name(), key.toString());
	}

	@Test
	void testEnabled()
	{
		// Arrange & Act
		boolean result = validMessageRecord.enabled();

		// Assert
		assertTrue(result);
	}

	@Test
	void testMessage()
	{
		// Arrange & Act
		String message = validMessageRecord.message();

		// Assert
		assertEquals("this is a test message", message);
	}

	@Test
	void testRepeatDelay()
	{
		// Arrange & Act
		Duration result = validMessageRecord.repeatDelay();

		// Assert
		assertEquals(Duration.ofSeconds(11), result);
	}

	@Test
	void testTitle()
	{
		// Arrange & Act
		String title = validMessageRecord.title();

		// Assert
		assertEquals("this is a test title", title);
	}

	@Test
	void testTitleFadeIn()
	{
		// Arrange & Act
		int titleFadeIn = validMessageRecord.titleFadeIn();

		// Assert
		assertEquals(22, titleFadeIn);
	}

	@Test
	void testTitleStay()
	{
		// Arrange & Act
		int titleStay = validMessageRecord.titleStay();

		// Assert
		assertEquals(33, titleStay);
	}

	@Test
	void testTitleFadeOut()
	{
		// Arrange & Act
		int titleFadeOut = validMessageRecord.titleFadeOut();

		// Assert
		assertEquals(44, titleFadeOut);
	}

	@Test
	void testSubtitle()
	{
		// Arrange & Act
		String subtitle = validMessageRecord.subtitle();

		// Assert
		assertEquals("this is a test subtitle", subtitle);
	}

	@Test
	void withFinalStrings()
	{
		// Arrange & Act
		FinalMessageRecord finalMessageRecord = validMessageRecord.withFinalStrings(
				"this is a final message string",
				"this is a final title string",
				"this is a final subtitle string");

		// Assert
		assertEquals("this is a final message string", finalMessageRecord.finalMessageString());
		assertEquals("this is a final title string", finalMessageRecord.finalTitleString());
		assertEquals("this is a final subtitle string", finalMessageRecord.finalSubTitleString());
	}
}
