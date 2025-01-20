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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.Option.CONFIG_LANGUAGE_KEY;


/**
 * An implementation of the LanguageResourceLoader interface for loading the message configuration from yaml files
 */
//TODO: This class needs more test coverage. It's mostly null checks and throws missing, and the reload command.
public final class YamlLanguageResourceLoader {

	// constants for plugin configuration keys
	private final static String CONFIG_LOCALE_KEY = "locale";
	private final static String CONFIG_LANGUAGE_KEY = "language";
	public static final String DEFAULT_LANGUAGE_TAG = "en-US";

	// reference to plugin main class
	private final Plugin plugin;


	/**
	 * Class constructor, Two parameter
	 *
	 * @param plugin an instance of the plugin
	 */
	public YamlLanguageResourceLoader(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Gets language tag specified in config.yml.
	 * <p>
	 * it is recommended, but not required, that languages should be specified by their ISO-639 codes,
	 * with two letter lowercase language code and two letter uppercase country code separated by a hyphen.
	 * <p>
	 * <i>example:</i> en-US
	 * <p>
	 * The language yaml file must match the specified tag, with a .yml extension appended.
	 *
	 * @param plugin reference to plugin main class
	 * @return IETF language tag as string from config.yml
	 */
	String getConfiguredLanguageTag(final Plugin plugin)
	{
		return plugin.getConfig().getString(CONFIG_LANGUAGE_KEY);
	}


	/**
	 * Load the language configuration object for the configured language from file and return it, without
	 * loading configuration defaults
	 *
	 * @return Configuration - message configuration object
	 */
	Configuration loadConfiguration()
	{
		return loadConfiguration(getConfiguredLanguageTag(plugin));
	}


	/**
	 * Load the language configuration object for the given IETF language tag and return it, without loading
	 * configuration defaults
	 *
	 * @return {@link Configuration} containing the configuration loaded from the language file
	 */
	Configuration loadConfiguration(final String languageTag) {

		// NOTE: A RESOURCE NAME IS NOT THE SAME AS A FILE NAME. THEY MAY USE DIFFERENT DELIMITERS.
		// A LanguageTag object, when instantiated with a language tag String parameter, has methods to return
		// either languageResource name, which always uses a '/' delimiter, or a file name, which uses the delimiter
		// for the current filesystem, as returned by File.separator.
		LanguageTag languageResource = new LanguageTag(languageTag);

		// create new YamlConfiguration object
		YamlConfiguration configuration = new YamlConfiguration();

		try // to load specified language file into new YamlConfiguration object
		{
			configuration.load(languageResource.getFileName());
			plugin.getLogger().info("Language file " + languageTag + ".yml successfully loaded.");
		} catch (FileNotFoundException e) {
			plugin.getLogger().severe("Language file " + languageTag + ".yml does not exist.");
		} catch (IOException e) {
			plugin.getLogger().severe("Language file " + languageTag + ".yml could not be read.");
		} catch (InvalidConfigurationException e) {
			plugin.getLogger().severe("Language file " + languageTag + ".yml is not valid yaml.");
		}

		return configuration;
	}


	boolean validateKeys(Configuration configuration)
	{
		for (String key : configuration.getKeys(true)) {
			if (!key.matches("[A-Z0-9_]+")) {
				Logger.getLogger(getClass().getName() + "Nonconforming key detected: " + key);
				return false;
			}
		}
		return true;
	}


	public Configuration reload()
	{
		//TODO: Is this right? Needs test case at any rate.
		return loadConfiguration();
	}

}
