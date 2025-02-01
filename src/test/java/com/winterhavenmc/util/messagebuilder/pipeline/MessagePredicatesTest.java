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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.pipeline.MessagePredicates.IS_ENABLED;
import static org.junit.jupiter.api.Assertions.*;

class MessagePredicatesTest {

	@Test
	void test_enabled() {
		MessageRecord messageRecord = new MessageRecord(
				ENABLED_MESSAGE.name(),
				true,
				true,
				"this-is-a_string-key",
				List.of("list", "of", "arguments"),
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle", "", "", "");

		assertTrue(IS_ENABLED.test(messageRecord));
	}

	@Test
	void test_disabled() {
		MessageRecord messageRecord = new MessageRecord(
				ENABLED_MESSAGE.name(),
				false,
				true,
				"this-is-a_string-key",
				List.of("list", "of", "arguments"),
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle", "", "", "");

		assertFalse(IS_ENABLED.test(messageRecord));
	}

}
