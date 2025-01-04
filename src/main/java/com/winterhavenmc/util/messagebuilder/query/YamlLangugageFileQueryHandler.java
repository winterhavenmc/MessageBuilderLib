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

import com.winterhavenmc.util.messagebuilder.util.TimeString;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.Error.*;


public class YamlLangugageFileQueryHandler implements LanguageFileQueryHandler {

	// constants for configuration section keys
	public static final String ITEM_SECTION = "ITEMS";
	public static final String CONSTANT_SECTION = "CONSTANTS";
	public static final String MESSAGE_SECTION = "MESSAGES";
	public static final String MULTIVERSE_CORE = "Multiverse-Core";

	private final Plugin plugin;
	private final Configuration configuration;


	public YamlLangugageFileQueryHandler(final Plugin plugin, final Configuration configuration) {
		if (plugin == null) { throw new IllegalArgumentException(Parameter.NULL_PLUGIN.getMessage()); }
		if (configuration == null) { throw new IllegalArgumentException(Parameter.NULL_CONFIGURATION.getMessage()); }

		this.plugin = plugin;
		this.configuration = configuration;
	}


	@Override
	public Optional<String> getString(final String key) {
		if (key == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }

		ConfigurationSection constantSection = configuration.getConfigurationSection(CONSTANT_SECTION);
		if (constantSection == null) { return Optional.empty();	}

		return Optional.ofNullable(constantSection.getString(key));
	}


	@Override
	public List<String> getStringList(final String key) {
		if (key == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }

		ConfigurationSection constantSection = configuration.getConfigurationSection(CONSTANT_SECTION);
		if (constantSection == null) { return Collections.emptyList();	}

		return constantSection.getStringList(key);
	}


	@Override
	public Optional<ItemRecord> getItemRecord(final String itemKey) {
		if (itemKey == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }

		// get configuration section for items
		ConfigurationSection itemSection = configuration.getConfigurationSection(ITEM_SECTION);
		if (itemSection == null) { return Optional.empty();	}

		// get configuration section for item key
		ConfigurationSection itemEntry = itemSection.getConfigurationSection(itemKey);
		if (itemEntry == null) { return Optional.empty(); }

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
	 * Check if Multiverse-Core plugin is enabled
	 * @param pluginManager an instance of the server's plugin manager
	 * @return {@code true} if the Multiverse-Core plugin is installed and enabled, {@code false} if not
	 */
	boolean isMultiverseInstalled(final PluginManager pluginManager) {
		if (pluginManager == null) { throw new IllegalArgumentException(Parameter.NULL_PLUGIN_MANAGER.getMessage()); }

		return pluginManager.isPluginEnabled(MULTIVERSE_CORE);
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

		if (pluginManager.isPluginEnabled(MULTIVERSE_CORE)) {

			// get reference to Multiverse-Core if installed
			MultiverseCore mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin(MULTIVERSE_CORE);

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
	 * Format the time string with days, hours, minutes and seconds as necessary
	 *
	 * @param duration {@code long} a time duration in milliseconds
	 * @return formatted time String
	 */
	@Override
	public String getTimeString(final long duration) {
		return new TimeString(this).getTimeString(duration, TimeUnit.SECONDS);
	}


	/**
	 * Format the time string with days, hours, minutes and seconds as necessary
	 *
	 * @param duration {@code long} a time duration in milliseconds
	 * @return formatted time String
	 */
	@Override
	public String getTimeString(final long duration, final TimeUnit timeUnit) {
		return new TimeString(this).getTimeString(duration, timeUnit);
	}

}
