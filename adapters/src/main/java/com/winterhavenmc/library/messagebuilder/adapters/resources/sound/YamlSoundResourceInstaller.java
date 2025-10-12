package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceInstaller;
import org.bukkit.plugin.Plugin;


public class YamlSoundResourceInstaller
{
	static final String FILE_NAME = "sounds.yml";

	private final Plugin plugin;


	public YamlSoundResourceInstaller(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	public ResourceInstaller.InstallerStatus install()
	{
		try
		{
			plugin.saveResource(FILE_NAME, false);
			return ResourceInstaller.InstallerStatus.SUCCESS;
		}
		catch (IllegalArgumentException exception)
		{
			return ResourceInstaller.InstallerStatus.FAIL;
		}
	}

}
