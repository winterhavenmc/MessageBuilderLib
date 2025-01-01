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

package com.winterhavenmc.util.messagebuilder.query;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import com.winterhavenmc.util.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.Error.*;


public class ConfigurationQueryHandler implements QueryHandler {

	// constants for configuration section keys
	public static final String ITEM_SECTION = "ITEMS";
	public static final String CONSTANT_SECTION = "CONSTANTS";
	public static final String MESSAGE_SECTION = "MESSAGES";

	private final Plugin plugin;
	private final Configuration configuration;


	public ConfigurationQueryHandler(final Plugin plugin, final Configuration configuration) {
		if (configuration == null) { throw new IllegalArgumentException(Parameter.NULL_CONFIGURATION.getMessage()); }
		this.plugin = plugin;
		this.configuration = configuration;
	}


	@Override
	public Optional<ItemRecord> getItemRecord(final String itemKey) {
		if (itemKey == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for items
		ConfigurationSection itemSect = configuration.getConfigurationSection(ITEM_SECTION);
		if (itemSect == null) {
			return Optional.empty();
		}

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSect.getConfigurationSection(itemKey);
		if (itemEntry == null) {
			return Optional.empty();
		}

		// return new ItemRecord
		return Optional.of(new ItemRecord(itemKey,
				Optional.ofNullable(itemEntry.getString(ItemField.NAME_SINGULAR.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(ItemField.NAME_PLURAL.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(ItemField.INVENTORY_NAME_SINGULAR.getKeyPath())),
				Optional.ofNullable(itemEntry.getString(ItemField.INVENTORY_NAME_PLURAL.getKeyPath())),
				itemEntry.getStringList(ItemField.LORE.getKeyPath())));
	}


	@Override
	public <MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(final MessageId messageId) {
		if (messageId == null) { throw new IllegalArgumentException(Parameter.NULL_MESSAGE_ID.getMessage()); }

		// get configuration section for message id
		ConfigurationSection messageSection = configuration.getConfigurationSection(MESSAGE_SECTION);
		if (messageSection == null) { return Optional.empty(); }

		// get entry for messageId
		ConfigurationSection messageEntry = messageSection.getConfigurationSection(messageId.toString());
		if (messageEntry == null) { return Optional.empty(); }

		return Optional.of(new MessageRecord(messageId.toString(),
				messageEntry.getBoolean(MessageField.ENABLED.toKey()),
				messageEntry.getString(MessageField.MESSAGE.toKey()),
				messageEntry.getLong(MessageField.REPEAT_DELAY.toKey()),
				messageEntry.getString(MessageField.TITLE.toKey()),
				messageEntry.getInt(MessageField.TITLE_FADE_IN.toKey()),
				messageEntry.getInt(MessageField.TITLE_STAY.toKey()),
				messageEntry.getInt(MessageField.TITLE_FADE_OUT.toKey()),
				messageEntry.getString(MessageField.SUBTITLE.toKey())));
	}


	public enum ItemField {
		NAME_SINGULAR("NAME.SINGULAR"),
		NAME_PLURAL("NAME.PLURAL"),
		INVENTORY_NAME_SINGULAR("INVENTORY_NAME_SINGULAR.SINGULAR"),
		INVENTORY_NAME_PLURAL("INVENTORY_NAME_SINGULAR.PLURAL"),
		LORE("LORE");

		private final String keyPath;

		ItemField(String keyPath) {
			this.keyPath = keyPath;
		}

		public String getKeyPath() {
			return this.keyPath;
		}
	}


	/**
	 * Enum of MessageRecord fields with their corresponding string key
	 */
	public enum MessageField {
		ENABLED("enabled"),
		MESSAGE("message"),
		REPEAT_DELAY("repeat-delay"),
		TITLE("title"),
		TITLE_FADE_IN("title-fade-in"),
		TITLE_STAY("title-stay"),
		TITLE_FADE_OUT("title-fade-out"),
		SUBTITLE("subtitle"),
		;

		private final String key;

		MessageField(final String key) {
			this.key = key;
		}

		public String toKey() {
			return key;
		}
	}


	//TODO: EVERYTHING BELOW WILL BE MOVED TO A NEW CLASS, TO DROP DEPENDENCY TO CLASS 'Plugin'

	/**
	 * Get Multiverse alias or bukkit world name as Optional String
	 * @param world the world whose alias or name being fetching
	 * @return Optional String containing the world alias or name, or empty if not found
	 */
	public Optional<String> getWorldName(final World world) {
		// if world is null, return empty optional
		if (world == null) {
			return Optional.empty();
		}
		// return multiverse alias or bukkit world name as optional string
		return Optional.of(getWorldAlias(world).orElse(world.getName()));
	}

	/**
	 * Get world name from world object, using Multiverse alias if available
	 *
	 * @param world the world object to retrieve name
	 * @return bukkit world name or multiverse alias as {@link Optional} wrapped String
	 */
	public Optional<String> getWorldAlias(final World world) {
		if (world == null) { return Optional.empty(); }

		String worldName = world.getName();

		Server server = plugin.getServer();
		PluginManager pluginManager = server.getPluginManager();

		if (pluginManager.isPluginEnabled("Multiverse-Core")) {

			// get reference to Multiverse-Core if installed
			MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");

			// if Multiverse is enabled, get MultiverseWorld object
			if (mvCore != null && mvCore.isEnabled()) {

				MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);

				// if Multiverse alias is not null or empty, set worldName to alias
				if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
					worldName = mvWorld.getAlias();
				}
			}
		}

		// return the bukkit world name or Multiverse world alias
		return Optional.ofNullable(worldName);
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

		// get server instance from static reference to access the plugin manager and worlds
		// note this is the only processor that needed external access, so we are resorting to static references
		// in order to avoid otherwise unnecessary dependency injection
		Server server = Bukkit.getServer();

		// get reference to Multiverse-Core if installed
		MultiverseCore mvCore = (MultiverseCore) server.getPluginManager().getPlugin("Multiverse-Core");

		// declare resultString with world name for location
		String resultString;
		if (location.getWorld() != null) {
			resultString = location.getWorld().getName();
		}
		else {
			// get name of first world
			resultString = server.getWorlds().getFirst().getName();
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


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary
	 *
	 * @param duration a time duration in milliseconds
	 * @return formatted time string
	 */
	@Override
	public String getTimeString(final long duration) {
		return new TimeString(configuration).getTimeString(duration, TimeUnit.SECONDS);
	}

	@Override
	public String getTimeString(final long duration, final TimeUnit timeUnit) {
		return new TimeString(configuration).getTimeString(duration, timeUnit);
	}




	/**
	 * Get spawn display name from language file
	 *
	 * @return the formatted display name for the world spawn, or empty string if key not found
	 */
	@Override
	public Optional<String> getSpawnDisplayName() {
		return Optional.ofNullable(configuration.getString(CONSTANT_SECTION + ".SPAWN.DISPLAY_NAME"));
	}

	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, or empty string if key not found
	 */
	@Override
	public Optional<String> getHomeDisplayName() {
		return Optional.ofNullable(configuration.getString(CONSTANT_SECTION + ".HOME.DISPLAY_NAME"));
	}


	/**
	 * Retrieve an arbitrary string from the language file with the specified key.
	 *
	 * @param path the message path for the string being retrieved
	 * @return the retrieved string, or null if no matching key found
	 */
	@Override
	public Optional<String> getString(final String path) {
		return Optional.ofNullable(configuration.getString(path));
	}


	/**
	 * Get List of String by path in message file
	 *
	 * @param path the message path for the string list being retrieved
	 * @return List of String - the string list retrieved by path from message file
	 */
	@Override
	public List<String> getStringList(final String path) {
		return configuration.getStringList(path);
	}

}
