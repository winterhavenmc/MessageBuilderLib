package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceInstaller;
import org.bukkit.plugin.Plugin;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.sound.SoundResourceConstant.RESOURCE_NAME;


public final class YamlSoundResourceInstaller implements ResourceInstaller
{
	private final Plugin plugin;


	public YamlSoundResourceInstaller(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	public ResourceInstaller.InstallerStatus install()
	{
		try
		{
			plugin.saveResource(RESOURCE_NAME.toString(), false);
			return ResourceInstaller.InstallerStatus.SUCCESS;
		}
		catch (IllegalArgumentException exception)
		{
			return ResourceInstaller.InstallerStatus.FAIL;
		}
	}

}
