package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ConfigurationProvider;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;

import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class YamlSoundResourceManagerTest
{
	@Mock Plugin pluginMock;
	@Mock ConfigRepository configRepositoryMock;

	@Mock YamlSoundResourceInstaller resourceInstallerMock;
	@Mock YamlSoundResourceLoader resourceLoaderMock;


	@Test
	void getConfigurationProvider_returns_valid_configuration_provider()
	{
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		when(configRepositoryMock.locale()).thenReturn(Locale.US);
		YamlSoundResourceManager soundResourceManager = YamlSoundResourceManager.create(pluginMock, configRepositoryMock);

		// Act
		ConfigurationProvider result = soundResourceManager.getConfigurationProvider();

		// Assert
		assertInstanceOf(YamlSoundConfigurationProvider.class, result);

		// Verify
		verify(pluginMock, atLeastOnce()).getLogger();
		verify(configRepositoryMock, atLeastOnce()).locale();
	}


	@Test
	void installResources()
	{
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		when(configRepositoryMock.locale()).thenReturn(Locale.US);
		YamlSoundResourceManager soundResourceManager = YamlSoundResourceManager.create(pluginMock, configRepositoryMock);

		// Act
		soundResourceManager.installResources();

		// Assert
		//TODO: assert something

		// Verify
		verify(pluginMock, atLeastOnce()).getLogger();
		verify(configRepositoryMock, atLeastOnce()).locale();
	}


	@Test
	@Disabled("needs return values for resourceLoaderMock method(s)")
	void reload_returns_true_on_success()
	{
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		when(configRepositoryMock.locale()).thenReturn(Locale.US);
		YamlSoundResourceManager soundResourceManager = YamlSoundResourceManager.create(pluginMock, configRepositoryMock);

		// Act
		var result = soundResourceManager.reload();

		// Assert
		assertTrue(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getLogger();
		verify(configRepositoryMock, atLeastOnce()).locale();
	}

	@Test
	void reload_returns_false_on_failure()
	{
		// Arrange
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
		when(configRepositoryMock.locale()).thenReturn(Locale.US);
		YamlSoundResourceManager soundResourceManager = YamlSoundResourceManager.create(pluginMock, configRepositoryMock);

		// Act
		var result = soundResourceManager.reload();

		// Assert
		//TODO: fix this test
//		assertFalse(result);

		// Verify
		verify(pluginMock, atLeastOnce()).getLogger();
		verify(configRepositoryMock, atLeastOnce()).locale();
	}

}
