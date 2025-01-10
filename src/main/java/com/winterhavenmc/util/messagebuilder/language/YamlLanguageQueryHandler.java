/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.language;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import com.winterhavenmc.util.messagebuilder.language.section.Section;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.SectionQueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.query.QueryHandlerRegistry;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.Error.*;


public class YamlLanguageQueryHandler implements LanguageQueryHandler {

	// constant for name of multiverse plugin
	public static final String MULTIVERSE_CORE = "Multiverse-Core";

	private final Plugin plugin;
	private final Configuration configuration;
	private final SectionQueryHandlerFactory sectionQueryHandlerFactory;
	private final QueryHandlerRegistry queryHandlerRegistry;


	/**
	 * Class constructor
	 *
	 * @param plugin instance of the plugin
	 * @param configuration the language configuration
	 */
	public YamlLanguageQueryHandler(final Plugin plugin, final Configuration configuration) {
		if (plugin == null) { throw new IllegalArgumentException(Parameter.NULL_PLUGIN.getMessage()); }
		if (configuration == null) { throw new IllegalArgumentException(Parameter.NULL_CONFIGURATION.getMessage()); }

		this.plugin = plugin;
		this.configuration = configuration;

		// create the query handler registry
		queryHandlerRegistry = new QueryHandlerRegistry();

		// create the domain factory
		sectionQueryHandlerFactory = new SectionQueryHandlerFactory(configuration);

		// Register the domain handlers in the registry
		queryHandlerRegistry.registerHandler(Section.CONSTANTS, sectionQueryHandlerFactory.createHandler(Section.CONSTANTS));
		queryHandlerRegistry.registerHandler(Section.ITEMS, sectionQueryHandlerFactory.createHandler(Section.ITEMS));
		queryHandlerRegistry.registerHandler(Section.MESSAGES, sectionQueryHandlerFactory.createHandler(Section.MESSAGES));
		queryHandlerRegistry.registerHandler(Section.TIME, sectionQueryHandlerFactory.createHandler(Section.TIME));
	}


	@Override
	public SectionQueryHandler<?> getQueryHandler(Section section) {
		return (SectionQueryHandler<?>) queryHandlerRegistry.getHandler(section);
	}


	@Override
	public Optional<ItemRecord> getItemRecord(final String itemKey) {
		if (itemKey == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }

		SectionQueryHandler<?> queryHandler = getQueryHandler(Section.ITEMS);
		if (queryHandler instanceof ItemQueryHandler itemQueryHandler) {
			return itemQueryHandler.getRecord(itemKey);
		}
		return Optional.empty();
	}


	@Override
	public <MessageId extends Enum<MessageId>> Optional<MessageRecord> getMessageRecord(final MessageId messageId) {
		if (messageId == null) { throw new IllegalArgumentException(Parameter.NULL_MESSAGE_ID.getMessage()); }

		SectionQueryHandler<?> queryHandler = getQueryHandler(Section.MESSAGES);
		if (queryHandler instanceof MessageQueryHandler messageQueryHandler) {
			return messageQueryHandler.getRecord(messageId);
		}
		return Optional.empty();
	}


//	public Optional<String> getString(final String key) {
//		if (key == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }
//
//		ConfigurationSection constantSection = configuration.getConfigurationSection(Section.CONSTANTS.name());
//		if (constantSection == null) { return Optional.empty();	}
//
//		return Optional.ofNullable(constantSection.getString(key));
//	}


//	@Override
//	public List<String> getStringList(final String key) {
//		if (key == null) { throw new IllegalArgumentException(Parameter.NULL_ITEM_KEY.getMessage()); }
//
//		ConfigurationSection constantSection = configuration.getConfigurationSection(Section.CONSTANTS.name());
//		if (constantSection == null) { return Collections.emptyList();	}
//
//		return constantSection.getStringList(key);
//	}


//	/**
//	 * Format the time string with days, hours, minutes and seconds as necessary
//	 *
//	 * @param duration {@code long} a time duration in milliseconds
//	 * @return formatted time String
//	 * @deprecated use the appropriate factory to get a message handler for the query
//	 */
//	@Override
//	@Deprecated
//	public String getTimeString(final long duration) {
//		QueryHandler<?> queryHandler = queryHandlerRegistry.getHandler(Section.TIME);
//		if (queryHandler instanceof TimeQueryHandler timeQueryHandler) {
//			return timeQueryHandler.getTimeString(duration, TimeUnit.SECONDS);
//		}
//		return "";
//	}


//	/**
//	 * Format the time string with days, hours, minutes and seconds as necessary
//	 *
//	 * @param duration {@code long} a time duration in milliseconds
//	 * @return formatted time String
//	 * @deprecated use the appropriate factory to get a message handler for the query
//	 */
//	@Deprecated
//	public String getTimeString(final long duration, final TimeUnit timeUnit) {
//		QueryHandler<?> queryHandler = queryHandlerRegistry.getHandler(Section.TIME);
//		if (queryHandler instanceof TimeQueryHandler timeQueryHandler) {
//			return timeQueryHandler.getTimeString(duration, timeUnit);
//		}
//		return "";
//	}


	//TODO: EVERYTHING BELOW WILL BE MOVED TO A NEW CLASS, TO DROP DEPENDENCY TO CLASS 'Plugin'

//	/**
//	 * Check if Multiverse-Core plugin is enabled
//	 * @param pluginManager an instance of the server's plugin manager
//	 * @return {@code true} if the Multiverse-Core plugin is installed and enabled, {@code false} if not
//	 */
//	boolean isMultiverseInstalled(final PluginManager pluginManager) {
//		if (pluginManager == null) { throw new IllegalArgumentException(Parameter.NULL_PLUGIN_MANAGER.getMessage()); }
//
//		return pluginManager.isPluginEnabled(MULTIVERSE_CORE);
//	}


	/**
	 * Get Multiverse alias or bukkit world name as Optional String
	 * @param world the world whose alias or name being fetching
	 * @return Optional String containing the world alias or name, or empty if not found
	 */
	@Override
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



}
