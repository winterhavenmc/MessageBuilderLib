/*
 * Copyright (c) 2022 Tim Savage.
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

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageTests {

	private ServerMock server;
	private PluginMain plugin;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}

	@Disabled
	@Test
	void setMacroTest() {
		PlayerMock testPlayer = server.addPlayer("test_player");
		Message<MessageId, Macro> message = plugin.messageBuilder.compose(testPlayer, MessageId.DURATION_MESSAGE);
		message.setMacro(Macro.DURATION, 1200L);
		assertEquals("Duration is 1 second", message.toString());
	}

//	@Test
//	void send() {
//	}
//
//	@Test
//	void testToString() {
//	}
//
//	@Test
//	void setAltMessage() {
//	}
//
//	@Test
//	void setAltTitle() {
//	}
//
//	@Test
//	void setAltSubtitle() {
//	}

}