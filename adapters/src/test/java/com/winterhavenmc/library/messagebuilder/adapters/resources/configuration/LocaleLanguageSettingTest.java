package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LocaleLanguageSettingTest
{
	@Mock Plugin pluginMock;


	@Test
	void get_returns_language_string_with_valid_string_config()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("language", "fr-FR");

		when(pluginMock.getConfig()).thenReturn(config);

		// Act
		LocaleLanguageSetting languageSetting = new LocaleLanguageSetting(pluginMock);
		String result = languageSetting.get().name();

		// Assert
		assertEquals("fr-FR", result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void get_returns_system_default_language_string_with_missing_config_entry()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();

		when(pluginMock.getConfig()).thenReturn(config);

		// Act
		LocaleLanguageSetting languageSetting = new LocaleLanguageSetting(pluginMock);
		String result = languageSetting.get().name();

		// Assert
		assertEquals("en-US", result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}

}
