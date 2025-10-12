/*
 * Copyright (c) 2022-2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.library.messagebuilder.adapters.resources.sound;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundRepository;

import com.winterhavenmc.library.messagebuilder.models.sound.SoundRecord;
import com.winterhavenmc.library.messagebuilder.models.sound.ValidSoundRecord;

import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;


/**
 * A class that implements SoundRepository interface
 */
public class YamlSoundRepository implements SoundRepository
{
	private final Plugin plugin;
	private final YamlConfiguration soundsConfig;
	private final String soundFileName = "sounds.yml";

	enum Field
	{
		ENABLED,
		PLAYER_ONLY,
		SOUND_NAME,
		VOLUME,
		PITCH,
	}


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public YamlSoundRepository(final Plugin plugin)
	{
		this.plugin = plugin;
		File soundFile = new File(plugin.getDataFolder(), soundFileName);

		// install sounds.yml if not already present and resource exists
		// this is only wrapped in a conditional to prevent log message when file already exists
		if (!soundFile.exists() && plugin.getResource(soundFileName) != null)
		{
			plugin.saveResource(soundFileName, false);
		}

		this.soundsConfig = new YamlConfiguration();

		try
		{
			this.soundsConfig.load(soundFile);
		}
		catch (IOException ioException)
		{
			plugin.getLogger().severe(ioException.getLocalizedMessage());
		}
		catch (InvalidConfigurationException invalidConfigurationException)
		{
			throw new RuntimeException(invalidConfigurationException);
		}
	}


	@Override
	public SoundRecord getSoundRecord(final Enum<?> soundId)
	{
		return SoundRecord.of(soundId.name(),
				soundsConfig.getBoolean(soundId + "." + Field.ENABLED),
				soundsConfig.getBoolean(soundId + "." + Field.PLAYER_ONLY),
				soundsConfig.getString(soundId + "." + Field.SOUND_NAME),
				(float) soundsConfig.getDouble(soundId + "." + Field.VOLUME),
				(float) soundsConfig.getDouble(soundId + "." + Field.PITCH));
	}


	@Override
	public Set<String> getKeys()
	{
		return this.soundsConfig.getKeys(false);
	}


	@Override
	public boolean isValidBukkitSoundName(final String key)
	{
		return isRegistrySound(key);
	}


	public boolean isRegistrySound(final String name)
	{
		return Registry.SOUNDS.match(name) != null;
	}


	@Override
	public boolean isValidSoundConfigKey(final String key)
	{
		return this.soundsConfig.getKeys(false).contains(key);
	}


	/**
	 * Get bukkit sound name for sound config file key
	 *
	 * @param key sound config file key
	 * @return String - the bukkit sound name for key
	 */
	@Override
	public String getBukkitSoundName(final String key)
	{
		return this.soundsConfig.getString(key + ".sound");
	}


	/**
	 * Load sound configuration from yaml file in plugin data folder
	 */
	public void reload()
	{
		// get File object for sound file
		File soundFile = new File(plugin.getDataFolder().getPath(), soundFileName);

		// copy resource to plugin data directory if it does not already exist there
		if (!soundFile.exists())
		{
			plugin.saveResource(soundFileName, false);
		}
		try
		{
			soundsConfig.load(soundFile);
		}
		catch (IllegalArgumentException | IOException | InvalidConfigurationException exception)
		{
			plugin.getLogger().severe(exception.getLocalizedMessage());
			throw new RuntimeException(exception);
		}
	}


	/**
	 * Play sound effect for player
	 *
	 * @param sender  the command sender (player) to play sound
	 * @param soundId the sound identifier enum member
	 */
	@Override
	public void playSound(final CommandSender sender, final Enum<?> soundId)
	{
		// if sound effects are configured false, do nothing and return
		if (soundEffectsDisabled())
		{
			return;
		}

		// if sender is not a player do nothing and return
		if (!(sender instanceof Player player))
		{
			return;
		}

		if (getSoundRecord(soundId) instanceof ValidSoundRecord validSoundRecord
				&& validSoundRecord.enabled())
		{
			// check that sound name is valid
			if (Registry.SOUNDS.match(validSoundRecord.soundName()) != null)
			{
				// if sound is set player only, use player.playSound()
				if (validSoundRecord.playerOnly())
				{
					player.playSound(player.getLocation(), Objects.requireNonNull(Registry.SOUNDS
							.match(validSoundRecord.soundName())), validSoundRecord.volume(), validSoundRecord.pitch());
				}
				// else use world.playSound() so other players in vicinity can hear
				else
				{
					player.getWorld().playSound(player.getLocation(), Objects.requireNonNull(Registry.SOUNDS
							.match(validSoundRecord.soundName())), validSoundRecord.volume(), validSoundRecord.pitch());
				}
			}
			else
			{
				plugin.getLogger().warning("An error occurred while trying to play the sound '"
						+ validSoundRecord.soundName() + "'. You probably need to update the sound name in your "
						+ soundFileName + " file.");
			}
		}
	}


	/**
	 * Play sound effect for location
	 *
	 * @param location the location at which to play sound
	 * @param soundId  the sound identifier enum member
	 */
	@Override
	public void playSound(final Location location, final Enum<?> soundId)
	{

		// if location is null, do nothing and return
		if (location == null)
		{
			return;
		}

		// if sound effects are configured false, do nothing and return
		if (soundEffectsDisabled())
		{
			return;
		}

		if (getSoundRecord(soundId) instanceof ValidSoundRecord validSoundRecord && validSoundRecord.enabled())
		{
			// check that sound name is valid
			if (validSoundRecord.soundName() != null && Registry.SOUNDS.match(validSoundRecord.soundName()) != null)
			{
				// else use world.playSound() so other players in vicinity can hear
				if (location.getWorld() != null)
				{
					location.getWorld().playSound(location,
							Objects.requireNonNull(Registry.SOUNDS.match(validSoundRecord.soundName())),
							validSoundRecord.volume(),
							validSoundRecord.pitch());
				}
			}
			else
			{
				plugin.getLogger().warning("An error occurred while trying to play the sound '"
						+ validSoundRecord.soundName() + "'. You probably need to update the sound name in your "
						+ soundFileName + " file.");
			}
		}
	}


	boolean soundEffectsDisabled()
	{
		return !plugin.getConfig().getBoolean("sound-effects");
	}

}
