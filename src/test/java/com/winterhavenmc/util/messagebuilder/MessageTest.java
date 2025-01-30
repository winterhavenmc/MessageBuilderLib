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

import com.winterhavenmc.util.messagebuilder.macro.MacroReplacer;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MessageTest {

/*
 Required Parameters of class under test:
	private final CommandSender recipient;
	private final MessageId messageId;
	private final LanguageQueryHandler languageQueryHandler;
	private final Replacer macroReplacer;
*/


	// declare mocks
	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock World worldMock;
	@Mock LanguageQueryHandler languageQueryHandlerMock;
	@Mock MacroReplacer<MessageId> macroReplacerMock;


	FileConfiguration pluginConfiguration;

	@BeforeEach
	public void setUp() throws IOException {

		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");
		pluginConfiguration.set("titles-enabled", true);

//		when(pluginMock.getConfig()).thenReturn(pluginConfiguration);
//		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));

//		when(playerMock.getWorld()).thenReturn(worldMock);
//		when(playerMock.getLocation()).thenReturn(new Location(worldMock, 3.0, 4.0, 5.0));
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		playerMock = null;
		worldMock = null;
	}

	@Test
	void setMacro() {
	}

	@Test
	void testSetMacro() {
	}

	@Test
	void getMessageRecord() {
	}

	@Test
	void send() {
	}

	@Test
	void getMacro() {
	}

//	 * @param plugin       reference to plugin main class
//	 * @param languageQueryHandler the ItemRecord handler for message records
//	 * @param macroReplacer reference to macro processor class
//	 * @param recipient    message recipient
//	 * @param messageId    message identifier

}