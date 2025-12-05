package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class YamlLanguageConfigurationProviderTest
{
	private static final String ENABLED_WORLDS_KEY = "enabled-worlds";
	private static final String DISABLED_WORLDS_KEY = "disabled-worlds";

	@Test
	void getConfiguration_returns_valid_configuration()
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.set(ENABLED_WORLDS_KEY, List.of("world", "world_nether", "world_the_end"));
		configuration.set(DISABLED_WORLDS_KEY, List.of());

		YamlLanguageConfigurationProvider languageConfigurationProvider = new YamlLanguageConfigurationProvider(() -> configuration);

		// Act
		Configuration result = languageConfigurationProvider.getConfiguration();


		// Assert
		assertInstanceOf(Configuration.class, result);
	}

}
