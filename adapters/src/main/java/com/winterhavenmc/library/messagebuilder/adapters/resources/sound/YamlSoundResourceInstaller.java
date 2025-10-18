package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceInstaller;
import org.bukkit.plugin.Plugin;

import java.io.File;

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
			// test if file exists in plugin data directory to avoid bukkit log message
			if (isInstalled(RESOURCE_NAME.toString()))
			{
				return InstallerStatus.FILE_EXISTS;
			}
			else
			{
				plugin.saveResource(RESOURCE_NAME.toString(), false);
				return ResourceInstaller.InstallerStatus.SUCCESS;
			}
		}
		catch (IllegalArgumentException exception)
		{
			return ResourceInstaller.InstallerStatus.FAIL;
		}
	}


	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param filename the name of the file being verified
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean isInstalled(final String filename)
	{
		return filename != null && new File(plugin.getDataFolder(), filename).exists();
	}

}
