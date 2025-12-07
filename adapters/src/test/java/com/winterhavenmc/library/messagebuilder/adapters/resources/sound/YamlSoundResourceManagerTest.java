package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import org.bukkit.configuration.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class YamlSoundResourceManagerTest
{
	@Mock YamlSoundResourceInstaller resourceInstallerMock;
	@Mock YamlSoundResourceLoader resourceLoaderMock;


	@Test
	void getConfigurationProvider_returns_valid_configuration_provider()
	{
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);
		var result = soundResourceManager.getConfigurationProvider();

		assertInstanceOf(YamlSoundConfigurationProvider.class, result);
	}

	@Test
	void getSectionProvider()
	{
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);
		var result = soundResourceManager.getSectionProvider("COMMAND_SUCCESS");

		assertInstanceOf(YamlSoundSectionProvider.class, result);
	}

	@Test
	void installResources()
	{
	}

	@Test
	@Disabled("needs return values for resourceLoaderMock method(s)")
	void reload_returns_true_on_success()
	{
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);
		var result = soundResourceManager.reload();

		assertTrue(result);
	}

	@Test
	void reload_returns_false_on_failure()
	{
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);
		var result = soundResourceManager.reload();

		assertFalse(result);
	}

}
