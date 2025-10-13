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

package com.winterhavenmc.library.messagebuilder.core.ports.resources.sound;

import com.winterhavenmc.library.messagebuilder.models.sound.SoundRecord;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Collection;


/**
 * An interface that facilitates loading a custom sound configuration file and provides methods to play sounds in game
 */
public interface SoundRepository
{
	SoundRecord getRecord(Enum<?> soundId);


	/**
	 * get all keys from the sound configuration
	 *
	 * @return a Collection of Section of sound configuration keys
	 */
	Collection<String> getKeys();


	/**
	 * Reload sound configuration
	 */
	void reload();


	/**
	 * Play sound effect for player
	 *
	 * @param sender  the command sender (player) to play sound
	 * @param soundId the sound identifier enum member
	 */
	void playSound(CommandSender sender, Enum<?> soundId);


	/**
	 * Play sound effect for location
	 *
	 * @param location the location at which to play sound
	 * @param soundId  the sound identifier enum member
	 */
	void playSound(Location location, Enum<?> soundId);


	/**
	 * Test string is valid bukkit sound name
	 *
	 * @param name the string to test
	 * @return true if passed string is contained in the bukkit sounds enum; false if not
	 */
	@Deprecated
	boolean isValidBukkitSoundName(String name);


	boolean isRegistrySound(String name);

	/**
	 * Test string is valid sound config key in sounds.yml file
	 *
	 * @param key the string to test
	 * @return true if passed string is a valid key in sounds.yml file; false if not
	 */
	boolean isValidSoundConfigKey(String key);


	/**
	 * Get bukkit sound name for sound config file key
	 *
	 * @param key sound config file key
	 * @return Section - the bukkit sound name for key
	 */
	String getBukkitSoundName(String key);


	boolean soundEffectsDisabled();
}
