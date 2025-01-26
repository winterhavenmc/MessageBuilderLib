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

package com.winterhavenmc.util.messagebuilder;

import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageTest {

	// declare mocks
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock World worldMock;

	MessageBuilder<MessageId, Macro> messageBuilder;


	@BeforeEach
	public void setUp() throws IOException {

		when(playerMock.getWorld()).thenReturn(worldMock);
		when(playerMock.getLocation()).thenReturn(new Location(worldMock, 3.0, 4.0, 5.0));

		// create real instance of MessageBuilder
		messageBuilder = new MessageBuilder<>(pluginMock);
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		playerMock = null;
		worldMock = null;
		messageBuilder = null;
	}

	@Disabled
	@Test
	void setMacroTest() {
		Message<MessageId, Macro> message = messageBuilder.compose(playerMock, MessageId.DURATION_MESSAGE);
		message.setMacro(Macro.DURATION, 1200L);
		assertEquals("Duration is 1 second", message.toString());
	}

}