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

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageSenderTest {

	@Mock Player playerMock;

	MessageRecord messageRecord;

	@BeforeEach
	void setUp() {
		messageRecord = MockUtility.getTestMessageRecord(MessageId.ENABLED_MESSAGE);
	}

	@AfterEach
	void tearDown() {
		playerMock = null;
	}

	@Test
	void send() {
		// Arrange
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));

		// Act
		new MessageSender(new CooldownMap()).send(playerMock, messageRecord);

		// Verify
		verify(playerMock, atLeastOnce()).sendMessage(anyString());
	}

	@Test
	void send_parameter_null_recipient() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new MessageSender(new CooldownMap()).send(null, messageRecord));
		assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
	}

	@Test
	void send_parameter_null_messageRecord() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new MessageSender(new CooldownMap()).send(playerMock, null));
		assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
	}

}
