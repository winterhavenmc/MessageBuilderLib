package com.winterhavenmc.library.messagebuilder.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LocaleProviderTest
{
	@Mock Plugin pluginMock;


	@Test
	void getValidZoneId_returns_valid_zoneId()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("timezone", "UTC");
		when(pluginMock.getConfig()).thenReturn(config);

		// act
		ZoneId zoneId = LocaleProvider.getValidZoneId(pluginMock);

		// Assert
		assertEquals(ZoneId.of("UTC"), zoneId);
	}


	@Test
	void getValidZoneId_with_unset_config_returns_default_zoneId()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		when(pluginMock.getConfig()).thenReturn(config);

		// act
		ZoneId zoneId = LocaleProvider.getValidZoneId(pluginMock);

		// Assert
		assertEquals(ZoneId.systemDefault(), zoneId);
	}


	@Test
	void getValidZoneId_with_invalid_config_returns_default_zoneId()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("timezone", "invalid-timezone");
		when(pluginMock.getConfig()).thenReturn(config);

		// act
		ZoneId zoneId = LocaleProvider.getValidZoneId(pluginMock);

		// Assert
		assertEquals(ZoneId.systemDefault(), zoneId);
	}

}
