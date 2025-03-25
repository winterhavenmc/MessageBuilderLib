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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.Message;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PlaceholderExtractorTest
{
	@Mock Player playerMock;
	@Mock MessageProcessor messageProcessorMock;
	MacroReplacer macroReplacer;

	Message message;

	@BeforeEach
	public void setUp() {
		message = new Message(playerMock, MessageId.ENABLED_MESSAGE.name(), messageProcessorMock);
		macroReplacer = new MacroReplacer();
	}

	@AfterEach
	public void tearDown() {
		playerMock = null;
		macroReplacer = null;
	}


	@Test
	void testGetPlaceholderStream() {
		// Arrange
		String messageString = "This is a {MESSAGE} with {SEVERAL} {PLACE_HOLDERS}.";

		// Act
		Stream<String> placeholderStream = new PlaceholderExtractor().extract(messageString);
		Set<String> placeholderSet = placeholderStream.collect(Collectors.toSet());

		// Assert
		assertFalse(placeholderSet.isEmpty());
		assertTrue(placeholderSet.contains("SEVERAL"));
		assertTrue(placeholderSet.contains("MESSAGE"));
		assertTrue(placeholderSet.contains("PLACE_HOLDERS"));
		assertFalse(placeholderSet.contains("This"));
	}

}
