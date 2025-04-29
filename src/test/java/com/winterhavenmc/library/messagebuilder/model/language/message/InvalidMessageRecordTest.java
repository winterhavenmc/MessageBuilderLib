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

package com.winterhavenmc.library.messagebuilder.model.language.message;

import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;


class InvalidMessageRecordTest
{
	RecordKey messageKey;
	InvalidMessageRecord invalidMessageRecord;


	@BeforeEach
	void setUp()
	{
		messageKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();
		invalidMessageRecord = MessageRecord.empty(messageKey);
	}


	@Test
	void testReturnType()
	{
		assertInstanceOf(InvalidMessageRecord.class, invalidMessageRecord);
	}


	@Test
	void testKey()
	{
		// Arrange & Act
		RecordKey result = invalidMessageRecord.key();

		// Assert
		assertEquals(messageKey, result);
	}


	@Test
	void testReason()
	{
		// Arrange & Act
		String reason = invalidMessageRecord.reason();

		// Assert
		assertEquals("Missing message section.", reason);
	}

}
