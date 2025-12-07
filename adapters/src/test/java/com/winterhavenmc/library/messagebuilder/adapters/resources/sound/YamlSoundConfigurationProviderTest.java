package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class YamlSoundConfigurationProviderTest
{

	@Test
	void getConfiguration_returns_configuration_object()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		YamlSoundConfigurationProvider soundConfigurationProvider = new YamlSoundConfigurationProvider(() -> configuration);

		// Act
		Configuration result = soundConfigurationProvider.getConfiguration();

		// Assert
		assertInstanceOf(Configuration.class, result);
	}
}
