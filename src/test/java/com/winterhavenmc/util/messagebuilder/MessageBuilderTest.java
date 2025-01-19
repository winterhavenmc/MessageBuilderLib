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

import com.winterhavenmc.util.messagebuilder.macro.MacroHandler;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.util.Toolkit;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.winterhavenmc.util.messagebuilder.MessageBuilder.TICKS;
import static com.winterhavenmc.util.messagebuilder.util.MockUtility.*;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MessageBuilderTest {

	@Mock Plugin pluginMock;
	@Mock YamlLanguageResourceManager languageResourceManagerMock;
	@Mock YamlLanguageQueryHandler languageQueryHandlerMock;
	@Mock MacroHandler macroQueryHandlerMock;
	@Mock Player playerMock;

	FileConfiguration pluginConfiguration;
	Configuration languageConfiguration;

	MessageBuilder<MessageId, Macro> messageBuilder;


	@BeforeEach
	void setUp() {

		// load configuration from resource
		pluginConfiguration = new YamlConfiguration();
		pluginConfiguration.set("language", "en-US");
		pluginConfiguration.set("locale", "en-US");

		languageConfiguration = loadConfigurationFromResource("language/en-US.yml");
		messageBuilder = new MessageBuilder<>(pluginMock,
				languageResourceManagerMock,
				languageQueryHandlerMock,
				macroQueryHandlerMock);
	}

	@AfterEach
	public void tearDown() {
	}


	@Test
	void testTickUnit() {
		assertEquals(Duration.ofMillis(50), TICKS.getDuration());
	}

	@Test
	void compose() {
		// Arrange & Act
		Message<MessageId, Macro> message = messageBuilder.compose(playerMock, MessageId.ENABLED_MESSAGE);

		// Assert
		assertNotNull(message);
	}

	@Test
	void getToolkit() {
		// Arrange & Act
		Toolkit toolkit = messageBuilder.getToolkit();

		// Assert
		assertNotNull(toolkit);
	}

	@Test
	void getLanguageQueryHandler() {
		LanguageQueryHandler languageQueryHandler = messageBuilder.getLanguageQueryHandler();
		assertNotNull(languageQueryHandler);
	}

	@Test
	void reload() {
	}

}
