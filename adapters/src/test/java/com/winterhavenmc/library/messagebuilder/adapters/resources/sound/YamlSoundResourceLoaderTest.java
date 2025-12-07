package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class YamlSoundResourceLoaderTest
{
	@Mock Plugin pluginMock;
	@Mock ConfigRepository configRepositoryMock;


	@Test
	@Disabled
	void load()
	{
		// Arrange
		YamlConfiguration configuration = new YamlConfiguration();
		Supplier<YamlConfiguration> configurationSupplier = () -> configuration;

		// Act
		YamlSoundResourceLoader resourceLoader = new YamlSoundResourceLoader(pluginMock, configurationSupplier, configRepositoryMock);
		Configuration newConfig = resourceLoader.load();

		// Assert
		assertInstanceOf(Configuration.class, newConfig);
	}


	@Test
	void loadFromResource()
	{
	}
}
