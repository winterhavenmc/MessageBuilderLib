package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ConfigurationProvider;
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
		// Arrange
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);

		// Act
		ConfigurationProvider result = soundResourceManager.getConfigurationProvider();

		// Assert
		assertInstanceOf(YamlSoundConfigurationProvider.class, result);
	}


	@Test
	void installResources()
	{
		// Arrange
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);

		// Act
		soundResourceManager.installResources();

		// Assert
	}

	@Test
	@Disabled("needs return values for resourceLoaderMock method(s)")
	void reload_returns_true_on_success()
	{
		// Arrange
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);

		// Act
		var result = soundResourceManager.reload();

		// Assert
		assertTrue(result);
	}

	@Test
	void reload_returns_false_on_failure()
	{
		// Arrange
		YamlSoundResourceManager soundResourceManager = new YamlSoundResourceManager(resourceInstallerMock, resourceLoaderMock);

		// Act
		var result = soundResourceManager.reload();

		// Assert
		assertFalse(result);
	}

}
