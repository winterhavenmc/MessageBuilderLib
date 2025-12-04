package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoggingLocaleSettingTest
{
	@Mock Plugin pluginMock;


	@Test
	void get_returns_locale_with_valid_string_config()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("locale", "fr-FR");

		when(pluginMock.getConfig()).thenReturn(config);

		// Act
		LoggingLocaleSetting localeSetting = new LoggingLocaleSetting(pluginMock);
		Locale result = localeSetting.get().locale();

		// Assert
		assertEquals(Locale.FRANCE, result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void get_returns_locale_with_valid_map_config()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("locale.log", "fr-FR");

		when(pluginMock.getConfig()).thenReturn(config);

		// Act
		LoggingLocaleSetting localeSetting = new LoggingLocaleSetting(pluginMock);
		Locale result = localeSetting.get().locale();

		// Assert
		assertEquals(Locale.FRANCE, result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void get_returns_system_default_locale_with_invalid_config()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("locale", "INVALID");

		when(pluginMock.getConfig()).thenReturn(config);

		// Act
		LoggingLocaleSetting localeSetting = new LoggingLocaleSetting(pluginMock);
		Locale result = localeSetting.get().locale();

		// Assert
		assertEquals(Locale.getDefault(), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void get_returns_system_default_locale_with_missing_config_entry()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();

		when(pluginMock.getConfig()).thenReturn(config);

		// Act
		LoggingLocaleSetting localeSetting = new LoggingLocaleSetting(pluginMock);
		Locale result = localeSetting.get().locale();

		// Assert
		assertEquals(Locale.getDefault(), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}

}
