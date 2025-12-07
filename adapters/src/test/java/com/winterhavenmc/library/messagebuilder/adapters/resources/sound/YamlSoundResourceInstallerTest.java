package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceInstaller;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class YamlSoundResourceInstallerTest
{
	@Mock Plugin pluginMock;


	@Test
	void install_returns_success()
	{
		// Act
		YamlSoundResourceInstaller resourceInstaller = new YamlSoundResourceInstaller(pluginMock);
		ResourceInstaller.InstallerStatus result = resourceInstaller.install();

		// Assert
		assertEquals(ResourceInstaller.InstallerStatus.SUCCESS, result);
	}


	@Test
	void isInstalled_returns_false()
	{
		// Act
		YamlSoundResourceInstaller resourceInstaller = new YamlSoundResourceInstaller(pluginMock);
		ResourceInstaller.InstallerStatus result = resourceInstaller.install();

		// Assert
		assertFalse(resourceInstaller.isInstalled("en-US.yml"));
	}

}
