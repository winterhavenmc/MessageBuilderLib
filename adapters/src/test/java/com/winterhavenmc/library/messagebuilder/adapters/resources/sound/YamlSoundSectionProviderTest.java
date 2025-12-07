package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


class YamlSoundSectionProviderTest
{
	String configString = """
		COMMAND_SUCCESS:
		  ENABLED: true
		  PLAYER_ONLY: true
		  SOUND_NAME: minecraft:entity.player.levelup
		  VOLUME: 1
		  PITCH: 1.25
		
		COMMAND_FAIL:
		  ENABLED: true
		  PLAYER_ONLY: true
		  SOUND_NAME: minecraft:entity.villager.no
		  VOLUME: 1
		  PITCH: 1
		""";


	@Test
	void getSection_returns_valid_section_given_valid_sectionName() throws InvalidConfigurationException
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.loadFromString(configString);
		Supplier<Configuration> sectionSupplier = () -> configuration;
		YamlSoundSectionProvider sectionProvider = new YamlSoundSectionProvider(sectionSupplier, "COMMAND_SUCCESS");

		// Act
		var result = sectionProvider.getSection();

		// Assert
		assertInstanceOf(ConfigurationSection.class, result);
	}

	@Test
	void getSection_returns_null_given_nonexistent_sectionName() throws InvalidConfigurationException
	{
		// Arrange
		FileConfiguration configuration = new YamlConfiguration();
		configuration.loadFromString(configString);
		Supplier<Configuration> sectionSupplier = () -> configuration;
		YamlSoundSectionProvider sectionProvider = new YamlSoundSectionProvider(sectionSupplier, "NONEXISTENT_ENTRY");

		// Act
		var result = sectionProvider.getSection();

		// Assert
		assertNull(result);
	}

}
