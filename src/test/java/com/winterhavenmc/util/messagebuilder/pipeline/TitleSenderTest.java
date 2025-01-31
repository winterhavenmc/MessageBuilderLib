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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TitleSenderTest {

	@Mock Player playerMock;

	MessageRecord messageRecord;

	@BeforeEach
	void setUp() {
		messageRecord = new MessageRecord(
				MessageId.ENABLED_MESSAGE.name(),
				true,
				false,
				"key",
				List.of("arg1", "arg2"),
				"this is a message.",
				Duration.ofSeconds(3),
				"this is a title.",
				20,
				40,
				30,
				"this is a subtitle.",
				"this is a final message string",
				"this is a final title string",
				"this is a final subtitle string");
	}

	@AfterEach
	void tearDown() {
		playerMock = null;
	}

	@Test
	void send() {
		// Act
		new TitleSender().send(playerMock, messageRecord);

		// Verify
		verify(playerMock, atLeastOnce()).sendTitle(anyString(), anyString(), anyInt(), anyInt(), anyInt());
	}

	@Test
	void send_not_player() {
		// Act
		Entity entityMock = mock(Entity.class, "Mock Entity");
		new TitleSender().send(entityMock, messageRecord);
		// assert? verify?
	}

	@Test
	void send_parameter_null_recipient() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new TitleSender().send(null, messageRecord));
		assertEquals("The parameter 'recipient' cannot be null.", exception.getMessage());
	}

	@Test
	void send_parameter_null_messageRecord() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new TitleSender().send(playerMock, null));
		assertEquals("The parameter 'messageRecord' cannot be null.", exception.getMessage());
	}

}
