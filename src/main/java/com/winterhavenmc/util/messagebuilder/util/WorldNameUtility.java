/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.util;


public class WorldNameUtility {

//	private final PluginManager pluginManager;


//	/**
//	 * Class constructor
//	 * @param pluginManager the server plugin manager
//	 */
//	public WorldNameUtility(final PluginManager pluginManager) {
//		this.pluginManager = pluginManager;
//	}
//
//
//	/**
//	 * Get Multiverse alias or bukkit world name as Optional String
//	 * @param world the world whose alias or name being fetching
//	 * @return Optional String containing the world alias or name, or empty if not found
//	 */
//	public Optional<String> getWorldName(final World world) {
//		// if world is null, return empty optional
//		if (world == null) {
//			return Optional.empty();
//		}
//		// return multiverse alias or bukkit world name as optional string
//		return Optional.of(getWorldAlias(world).orElse(world.getName()));
//	}

//	/**
//	 * Get world name from world object, using Multiverse alias if available
//	 *
//	 * @param world the world object to retrieve name
//	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
//	 */
//	public Optional<String> getWorldAlias(final World world) {
//		if (world == null) { return Optional.empty(); }
//
//		String worldName = null;
//
//		pluginManager.isPluginEnabled("Multiverse-Core");
//
//		// get reference to Multiverse-Core if installed
//		MultiverseCore mvCore = (MultiverseCore) pluginManager.getPlugin("Multiverse-Core");
//
//		// if Multiverse is enabled, get MultiverseWorld object
//		if (mvCore != null && mvCore.isEnabled()) {
//
//			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);
//
//			// if Multiverse alias is not null or empty, set worldName to alias
//			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
//				worldName = mvWorld.getAlias();
//			}
//		}
//
//		// return the bukkit world name or Multiverse world alias
//		return Optional.ofNullable(worldName);
//	}


//	/**
//	 * Get world name for location, using Multiverse alias if available
//	 *
//	 * @param location the location used to retrieve world name
//	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
//	 */
//	private Optional<String> getWorldName(final Location location) {
//
//		// check for null parameter
//		if (location == null) {
//			return Optional.empty();
//		}
//
//		// get server instance from static reference to access the plugin manager and worlds
//		// note this is the only processor that needed external access, so we are resorting to static references
//		// in order to avoid otherwise unnecessary dependency injection
//		Server server = Bukkit.getServer();
//
//		// get reference to Multiverse-Core if installed
//		MultiverseCore mvCore = (MultiverseCore) server.getPluginManager().getPlugin("Multiverse-Core");
//
//		// declare resultString with world name for location
//		String resultString;
//		if (location.getWorld() != null) {
//			resultString = location.getWorld().getName();
//		}
//		else {
//			// get name of first world
//			resultString = server.getWorlds().getFirst().getName();
//		}
//
//		// if Multiverse is enabled, use Multiverse world alias if available
//		if (mvCore != null && mvCore.isEnabled()) {
//
//			// get Multiverse world object
//			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(location.getWorld());
//
//			// if Multiverse alias is not null or empty, set world name to alias if set
//			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
//				resultString = mvWorld.getAlias();
//			}
//		}
//
//		// return resultString
//		return Optional.of(resultString);
//	}

}
