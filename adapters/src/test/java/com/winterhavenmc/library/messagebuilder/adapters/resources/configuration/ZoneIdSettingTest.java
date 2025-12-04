package com.winterhavenmc.library.messagebuilder.adapters.resources.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ZoneIdSettingTest
{
	@Mock Plugin pluginMock;


	@Test
	void get_returns_zoneId_with_valid_config()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("timezone", "Pacific/Pago_Pago");

		when(pluginMock.getConfig()).thenReturn(config);
		ZoneIdSetting zoneIdSetting = new ZoneIdSetting(pluginMock);

		// Act
		ZoneId result = zoneIdSetting.get();

		// Assert
		assertEquals(ZoneId.of("Pacific/Pago_Pago"), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}


	@Test
	void get_returns_system_default_zoneId_with_invalid_config()
	{
		// Arrange
		FileConfiguration config = new YamlConfiguration();
		config.set("timezone", "INVALID");

		when(pluginMock.getConfig()).thenReturn(config);
		ZoneIdSetting zoneIdSetting = new ZoneIdSetting(pluginMock);

		// Act
		ZoneId result = zoneIdSetting.get();

		// Assert
		assertEquals(ZoneId.systemDefault(), result);

		// Verify
		verify(pluginMock, atLeastOnce()).getConfig();
	}

}
