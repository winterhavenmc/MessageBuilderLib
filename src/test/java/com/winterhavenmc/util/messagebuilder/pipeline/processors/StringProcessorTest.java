/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.util.RecordKey;

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class StringProcessorTest {

	@Mock Player playerMock;

	ValidRecipient recipient;
	RecordKey macroKey;
	ContextMap contextMap;


	@BeforeEach
	void setUp() {
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		macroKey = RecordKey.of("KEY").orElseThrow();
		contextMap = ContextMap.of(recipient, macroKey).orElseThrow();
	}


	@AfterEach
	public void tearDown()
	{
		playerMock = null;
		recipient = null;
		macroKey = null;
	}


	@Test
	void resolveContext()
	{
		String stringObject = "some name";

		contextMap.put(macroKey, stringObject);

		MacroProcessor macroProcessor = new StringProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		assertTrue(resultMap.containsKey(macroKey));
		assertEquals(stringObject, resultMap.get(macroKey));
	}


	@Test
	void resolveContext_not_string()
	{
		Duration duration  = Duration.ofMillis(2000);

		contextMap.put(macroKey, duration);

		MacroProcessor macroProcessor = new StringProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		assertFalse(resultMap.containsKey(macroKey));
	}

}
