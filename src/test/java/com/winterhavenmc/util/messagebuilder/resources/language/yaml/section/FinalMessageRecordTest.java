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

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.message.FinalMessageRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FinalMessageRecordTest
{
	FinalMessageRecord validMessageRecord;

	@BeforeEach
	void setUp()
	{
		validMessageRecord = new FinalMessageRecord(
				RecordKey.of(ENABLED_MESSAGE).orElseThrow(),
				true,
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle",
				"this is a final message string",
				"this is a final title string",
				"this is a final subtitle string");
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
		boolean enabled = validMessageRecord.enabled();

		// Assert
		assertTrue(enabled);
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
		Duration repeatDelay = validMessageRecord.repeatDelay();

		// Assert
		assertEquals(Duration.ofSeconds(11), repeatDelay);
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
	void testFinalMessageString()
	{
		// Arrange & Act
		String result = validMessageRecord.finalMessageString();

		// Assert
		assertEquals("this is a final message string", result);
	}

	@Test
	void testFinalTitleString()
	{
		// Arrange & Act
		String result = validMessageRecord.finalTitleString();

		// Assert
		assertEquals("this is a final title string", result);
	}

	@Test
	void testFinalSubtitleString()
	{
		// Arrange & Act
		String result = validMessageRecord.finalSubTitleString();

		// Assert
		assertEquals("this is a final subtitle string", result);
	}

}
