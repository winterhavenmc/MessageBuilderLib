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

import com.winterhavenmc.library.messagebuilder.core.ports.resources.ConfigurationProvider;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.ResourceManager;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.sound.SoundRepository;

import com.winterhavenmc.library.messagebuilder.models.sound.SoundRecord;
import com.winterhavenmc.library.messagebuilder.models.sound.ValidSoundRecord;

import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.sound.SoundResourceConstant.RESOURCE_NAME;
import static com.winterhavenmc.library.messagebuilder.models.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.models.validation.Parameter.SOUND_RESOURCE_MANAGER;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.throwing;
import static com.winterhavenmc.library.messagebuilder.models.validation.Validator.validate;


/**
 * A class that implements SoundRepository interface
 */
public final class YamlSoundRepository implements SoundRepository
{
	private final Plugin plugin;
	private final ConfigurationProvider configurationProvider;


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
	 */
	public YamlSoundRepository(final Plugin plugin, final ResourceManager soundResourceManager)
	{
		validate(soundResourceManager, Objects::isNull, throwing(PARAMETER_NULL, SOUND_RESOURCE_MANAGER));
		this.configurationProvider = soundResourceManager.getConfigurationProvider();
		this.plugin = plugin;
	}


	@Override
	public SoundRecord getRecord(final Enum<?> soundId)
	{
		final Configuration soundConfiguration = this.configurationProvider.getConfiguration();

		return SoundRecord.of(soundId.name(),
				soundConfiguration.getBoolean(soundId + "." + Field.ENABLED),
				soundConfiguration.getBoolean(soundId + "." + Field.PLAYER_ONLY),
				soundConfiguration.getString(soundId + "." + Field.SOUND_NAME),
				(float) soundConfiguration.getDouble(soundId + "." + Field.VOLUME),
				(float) soundConfiguration.getDouble(soundId + "." + Field.PITCH));
	}


	public SoundRecord getRecord(final String soundId)
	{
		final Configuration soundConfiguration = this.configurationProvider.getConfiguration();

		return SoundRecord.of(soundId,
				soundConfiguration.getBoolean(soundId + "." + Field.ENABLED),
				soundConfiguration.getBoolean(soundId + "." + Field.PLAYER_ONLY),
				soundConfiguration.getString(soundId + "." + Field.SOUND_NAME),
				(float) soundConfiguration.getDouble(soundId + "." + Field.VOLUME),
				(float) soundConfiguration.getDouble(soundId + "." + Field.PITCH));
	}


	@Override
	public Set<String> getKeys()
	{
		return this.configurationProvider.getConfiguration().getKeys(false);
	}


	@Override
	public boolean isValidBukkitSoundName(final String key)
	{
		return isRegistrySound(key);
	}


	@Override
	public boolean isRegistrySound(final String name)
	{
		return Registry.SOUNDS.match(name) != null;
	}


	@Override
	public boolean isValidSoundConfigKey(final String key)
	{
		return this.configurationProvider.getConfiguration().getKeys(false).contains(key);
	}


	/**
	 * Get bukkit sound name for sound config file key
	 *
	 * @param key sound config file key
	 * @return Section - the bukkit sound name for key
	 */
	@Override
	public String getBukkitSoundName(final String key)
	{
		return this.configurationProvider.getConfiguration().getString(key + ".sound");
	}


	/**
	 * Play sound effect for player
	 *
	 * @param sender  the command sender (player) to play sound
	 * @param soundId the sound identifier enum member
	 */
	@Override
	public void play(final CommandSender sender, final Enum<?> soundId)
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

		if (getRecord(soundId) instanceof ValidSoundRecord validSoundRecord
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
				Logger.getLogger(this.getClass().getName()).warning("An error occurred while trying to play the sound '"
						+ validSoundRecord.soundName() + "'. You may need to update the sound name in your "
						+ RESOURCE_NAME + " file.");
			}
		}
	}


	/**
	 * Play sound effect for player
	 *
	 * @param sender  the command sender (player) to play sound
	 * @param soundId the sound identifier enum member
	 */
	public void play(final CommandSender sender, final String soundId)
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

		if (getRecord(soundId) instanceof ValidSoundRecord validSoundRecord
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
				Logger.getLogger(this.getClass().getName()).warning("An error occurred while trying to play the sound '"
						+ validSoundRecord.soundName() + "'. You may need to update the sound name in your "
						+ RESOURCE_NAME + " file.");
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
	public void play(final Location location, final Enum<?> soundId)
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

		if (getRecord(soundId) instanceof ValidSoundRecord validSoundRecord && validSoundRecord.enabled())
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
						+ validSoundRecord.soundName() + "'. You may need to update the sound name in your "
						+ RESOURCE_NAME + " file.");
			}
		}
	}


	@Override
	public boolean soundEffectsDisabled()
	{
		return !plugin.getConfig().getBoolean("sound-effects");
	}


	@Override
	public Optional<String> matchLongest(final Enum<?> messageId)
	{
		int longest = 0;
		String result = null;

		for (String soundId : getKeys())
		{
			if (messageId.name().startsWith(soundId) && soundId.length() > longest)
			{
				longest = soundId.length();
				result = soundId;
			}
		}

		return Optional.ofNullable(result);
	}

}
