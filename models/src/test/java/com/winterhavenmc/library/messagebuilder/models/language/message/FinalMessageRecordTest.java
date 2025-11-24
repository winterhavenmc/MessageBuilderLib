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

import com.winterhavenmc.library.messagebuilder.models.time.Tick;
import com.winterhavenmc.library.messagebuilder.models.util.MessageId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FinalMessageRecordTest
{
	private final static Tick TICKS = new Tick();
	FinalMessageRecord validMessageRecord;

	@BeforeEach
	void setUp()
	{
		validMessageRecord = new FinalMessageRecord(
				MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow(),
				true,
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				Duration.of(22, TICKS),
				Duration.of(33, TICKS),
				Duration.of(44, TICKS),
				"this is a test subtitle",
				Optional.of("this is a final message string"),
				Optional.of("this is a final title string"),
				Optional.of("this is a final subtitle string"));
	}


	@Test
	void testKey()
	{
		// Arrange & Act
		ValidMessageKey key = validMessageRecord.key();

		// Assert
		assertEquals(MessageId.ENABLED_MESSAGE.name(), key.toString());
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
		Duration titleFadeIn = validMessageRecord.titleFadeIn();

		// Assert
		assertEquals(Duration.of(22, TICKS), titleFadeIn);
	}

	@Test
	void testTitleStay()
	{
		// Arrange & Act
		Duration titleStay = validMessageRecord.titleStay();

		// Assert
		assertEquals(Duration.of(33, TICKS), titleStay);
	}

	@Test
	void testTitleFadeOut()
	{
		// Arrange & Act
		Duration titleFadeOut = validMessageRecord.titleFadeOut();

		// Assert
		assertEquals(Duration.of(44, TICKS), titleFadeOut);
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
		String result = validMessageRecord.finalMessageString().orElse("");

		// Assert
		assertEquals("this is a final message string", result);
	}

	@Test
	void testFinalTitleString()
	{
		// Arrange & Act
		String result = validMessageRecord.finalTitleString().orElse("");

		// Assert
		assertEquals("this is a final title string", result);
	}

	@Test
	void testFinalSubtitleString()
	{
		// Arrange & Act
		String result = validMessageRecord.finalSubtitleString().orElse("");

		// Assert
		assertEquals("this is a final subtitle string", result);
	}

}
