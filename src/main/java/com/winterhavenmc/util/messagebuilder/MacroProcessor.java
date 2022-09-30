/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.winterhavenmc.util.TimeUnit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Optional;

public class MacroProcessor {

	private final static String MACRO_DELIMITER = "%";
	private final static String UNKNOWN_VALUE = "???";
	private final LanguageHandler languageHandler;

	MacroProcessor(final LanguageHandler languageHandler) {
		this.languageHandler = languageHandler;
	}


	@SuppressWarnings("unused")
	String replaceMacros(final CommandSender recipient, final MacroObjectMap macroObjectMap, final String messageString) {

		String modifiedMessageString = messageString;

		// only process macro tokens if message string contains a token marker character
		if (modifiedMessageString.contains(MACRO_DELIMITER)) {

			// process built-in macros [ ITEM or ITEM_NAME, WORLD or WORLD_NAME, PLAYER or PLAYER_NAME ]
			// ITEM or ITEM_NAME is always replaced with ITEM_NAME in message file, using plural if ITEM_QUANTITY is != 1
			mapItemName(macroObjectMap, "ITEM", "ITEM_NAME");
			mapWorldName(recipient, macroObjectMap,"WORLD", "WORLD_NAME");
			mapPlayerName(recipient, macroObjectMap, "PLAYER", "PLAYER_NAME");
			mapPlayerLocation(recipient, macroObjectMap, "LOCATION", "PLAYER_LOCATION");

			// iterate over macro map, giving special treatment to certain entries
			for (Map.Entry<String, Object> entry : macroObjectMap.entrySet()) {
				getStringForType(entry);

				// replace macro tokens in message string with values as string
				String macroToken = MACRO_DELIMITER + entry.getKey() + MACRO_DELIMITER;
				if (entry.getValue() != null) {
					modifiedMessageString = modifiedMessageString.replace(macroToken, entry.getValue().toString());
				}
			}
		}

		return modifiedMessageString;
	}


	private void mapItemName(final MacroObjectMap macroObjectMap, final String... keys) {

		int itemQuantity = 1;

		// if macro ITEM_QUANTITY exists and is integer, set quantity
		if (macroObjectMap.containsKey("ITEM_QUANTITY") && macroObjectMap.get("ITEM_QUANTITY") instanceof Integer) {
			itemQuantity = (Integer) macroObjectMap.get("ITEM_QUANTITY");
		}

		String itemName;

		// if item name key is not present in map, use configured name from message file, using itemQuantity to determine plurality
		if (itemQuantity == 1) {
			itemName = languageHandler.getItemName().orElse(UNKNOWN_VALUE);
		}
		else {
			itemName = languageHandler.getItemNamePlural().orElse(UNKNOWN_VALUE);
		}

		// put item name in map for each passed key
		for (String key : keys) {
			macroObjectMap.put(key, itemName);
		}
	}


	private void mapWorldName(final CommandSender recipient, final MacroObjectMap macroObjectMap, final String... keys) {

		for (String key : keys) {
			// if world name is not in map, use message recipient world
			if (!macroObjectMap.containsKey(key)) {
				macroObjectMap.put(key, getWorldName(recipient).orElse(UNKNOWN_VALUE));
			}
			else if (macroObjectMap.get(key) instanceof World world) {
				macroObjectMap.put(key, getWorldName(world).orElse(world.getName()));
			}
			else if (macroObjectMap.get(key) instanceof Location location) {
				macroObjectMap.put(key, getWorldName(location).orElse(UNKNOWN_VALUE));
			}
		}
	}


	private void mapPlayerName(final CommandSender recipient, final MacroObjectMap macroObjectMap, final String... keys) {
		for (String key : keys) {
			// if player name key is not in map, use message recipient name
			if (!macroObjectMap.containsKey(key)) {
				macroObjectMap.put(key, recipient.getName());
			}
		}
	}


	private void mapPlayerLocation(final CommandSender recipient, final MacroObjectMap macroObjectMap, final String... keys) {

		for (String key : keys) {

			// if player location is not in map, use player location
			Location location = null;

			// if entry for key does not exist in macro map, use message recipient location if entity
			if (!macroObjectMap.containsKey(key)) {
				if (recipient instanceof Entity entity) {
					location = entity.getLocation();
				}
			}

			if (macroObjectMap.get(key) instanceof Location) {
				location = (Location) macroObjectMap.get(key);
			}

			mapLocation(key, location, macroObjectMap);
		}
	}

	private void mapLocation(final String key, final Location location, final MacroObjectMap macroObjectMap) {
		if (location != null) {
			String locWorld = getWorldName(location).orElse(UNKNOWN_VALUE);
			String locX = String.valueOf(location.getBlockX());
			String locY = String.valueOf(location.getBlockY());
			String locZ = String.valueOf(location.getBlockZ());
			String locString = locWorld + " [" + locX + ", " + locY + ", " + locZ + "]";
			macroObjectMap.put(key, locString);
			macroObjectMap.put("LOC_WORLD", locWorld);
			macroObjectMap.put("LOC_X", locX);
			macroObjectMap.put("LOC_Y", locY);
			macroObjectMap.put("LOC_Z", locZ);

			macroObjectMap.put(key + "_WORLD", locWorld);
			macroObjectMap.put(key + "_X", locX);
			macroObjectMap.put(key + "_Y", locY);
			macroObjectMap.put(key + "_Z", locZ);
		}
	}


	/**
	 * get current world name of message recipient, using Multiverse alias if available
	 *
	 * @param recipient player to fetch world name
	 * @return {@link Optional} wrapped String containing recipient world name
	 */
	private Optional<String> getWorldName(final CommandSender recipient) {

		// if recipient is null, return empty optional
		if (recipient == null) {
			return Optional.empty();
		}

		// get reference to plugin main class
		JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Message.class);

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		// declare recipient world
		World world;

		// if sender is entity, set worldName to entity world name
		if (recipient instanceof Entity) {
			world = ((Entity) recipient).getWorld();
		}
		else {
			// otherwise, use server first world
			world = recipient.getServer().getWorlds().get(0);
		}

		// set result string to world name
		String resultString = world.getName();

		// if Multiverse is enabled, use Multiverse world alias if available
		if (mvCore != null && mvCore.isEnabled()) {

			// get Multiverse world object
			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);

			// if Multiverse alias is not null or empty, set world name to alias if set
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				resultString = mvWorld.getAlias();
			}
		}

		// return resultString
		return Optional.of(resultString);
	}


	private void getStringForType(Map.Entry<String, Object> entry) {
		// if key ends in "DURATION" and value type is Number, set value to time string
		if (entry.getKey().endsWith("DURATION") && entry.getValue() instanceof Number) {
			entry.setValue(languageHandler.getTimeString((long) entry.getValue()));
		}

		// if key ends in "DURATION_MINUTES" and value type is Number, set value to time string
		else if (entry.getKey().endsWith("DURATION_MINUTES") && entry.getValue() instanceof Number) {
			entry.setValue(languageHandler.getTimeString((long) entry.getValue(), TimeUnit.MINUTES));
		}

		// if entry is CommandSender, set value to name
		else if (entry.getValue() instanceof CommandSender) {
			entry.setValue(((CommandSender) entry.getValue()).getName());
		}

		// if entry is OfflinePlayer, set value to name
		else if (entry.getValue() instanceof OfflinePlayer) {
			entry.setValue(((OfflinePlayer) entry.getValue()).getName());
		}

		// if entry is world, set value to world name
		else if (entry.getValue() instanceof World) {
			entry.setValue(((World) entry.getValue()).getName());
		}
	}


	/**
	 * Get world name from world object, using Multiverse alias if available
	 *
	 * @param world the world object to retrieve name
	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
	 */
	private Optional<String> getWorldName(final World world) {

		// if world is null, return question marks
		if (world == null) {
			return Optional.empty();
		}

		// get reference to plugin main class
		JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Message.class);

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		// get bukkit world name
		String worldName = world.getName();

		// if Multiverse is enabled, get MultiverseWorld object
		if (mvCore != null && mvCore.isEnabled()) {

			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);

			// if Multiverse alias is not null or empty, set worldName to alias
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				worldName = mvWorld.getAlias();
			}
		}

		// return the bukkit world name or Multiverse world alias
		return Optional.of(worldName);
	}


	/**
	 * Get world name for location, using Multiverse alias if available
	 *
	 * @param location the location used to retrieve world name
	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
	 */
	private Optional<String> getWorldName(final Location location) {

		// check for null parameter
		if (location == null) {
			return Optional.empty();
		}

		// get reference to plugin main class
		JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Message.class);

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

		// declare resultString with world name for location
		String resultString;
		if (location.getWorld() != null) {
			resultString = location.getWorld().getName();
		}
		else {
			// get name of first world
			resultString = plugin.getServer().getWorlds().get(0).getName();
		}

		// if Multiverse is enabled, use Multiverse world alias if available
		if (mvCore != null && mvCore.isEnabled()) {

			// get Multiverse world object
			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(location.getWorld());

			// if Multiverse alias is not null or empty, set world name to alias if set
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				resultString = mvWorld.getAlias();
			}
		}

		// return resultString
		return Optional.of(resultString);
	}

}
