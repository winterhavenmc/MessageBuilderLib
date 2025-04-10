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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

class InvalidMessageRecordTest
{
	InvalidMessageRecord validMessageRecord;

	@BeforeEach
	void setUp()
	{
		validMessageRecord = new InvalidMessageRecord(
				RecordKey.of(ENABLED_MESSAGE).orElseThrow(),
				true,
				"this is a test message",
				Duration.ofSeconds(11),
				"failed to pass validation");
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
	void testReason()
	{
		// Arrange & Act
		String reason = validMessageRecord.reason();

		// Assert
		assertEquals("failed to pass validation", reason);
	}

}
