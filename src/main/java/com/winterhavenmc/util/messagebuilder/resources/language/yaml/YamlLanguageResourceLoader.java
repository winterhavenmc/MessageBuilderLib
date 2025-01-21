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
import java.util.regex.Pattern;


/**
 * An implementation of the LanguageResourceLoader interface for loading the message configuration from yaml files
 */
public final class YamlLanguageResourceLoader {

	// compiled regex pattern matching valid yaml keys for this application (upper snake case only)
	private final static Pattern UPPER_SNAKE_CASE = Pattern.compile("[A-Z0-9_]+", Pattern.UNICODE_CHARACTER_CLASS);

	// reference to plugin main class instance
	private final Plugin plugin;


	/**
	 * Class constructor
	 *
	 * @param plugin an instance of the plugin main class
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
		return plugin.getConfig().getString(Option.CONFIG_LANGUAGE_KEY.toString());
	}


	/**
	 * Load the language configuration object for the configured language from file and return it, without
	 * loading configuration defaults
	 *
	 * @return Configuration - message configuration object
	 */
	Configuration loadConfiguration()
	{
		return loadConfiguration(new LanguageTag(getConfiguredLanguageTag(plugin)));
	}


	/**
	 * Load the language configuration object for the given IETF language tag and return it, without loading
	 * configuration defaults
	 *
	 * @return {@link Configuration} containing the configuration loaded from the language file
	 */
	Configuration loadConfiguration(final LanguageTag languageTag) {

		// create new YamlConfiguration object
		YamlConfiguration configuration = new YamlConfiguration();

		try // to load specified language file into new YamlConfiguration object
		{
			configuration.load(languageTag.getFileName());
			plugin.getLogger().info("Language file " + languageTag.getFileName() + " successfully loaded.");
		} catch (FileNotFoundException e) {
			plugin.getLogger().severe("Language file " + languageTag.getFileName() + " does not exist.");
		} catch (IOException e) {
			plugin.getLogger().severe("Language file " + languageTag.getFileName() + " could not be read.");
		} catch (InvalidConfigurationException e) {
			plugin.getLogger().severe("Language file " + languageTag.getFileName() + " is not valid yaml.");
		}

		return configuration;
	}


	/**
	 * Test all keys of the configuration object for compliance with this application's key standard.
	 * Nonconforming keys will be noted in the log, but not modified in any way.
	 *
	 * @param configuration the configuration object loaded from the language resource
	 * @return {@code true} if all keys conform ot the standard, {@code false} if not
	 */
	boolean validateKeys(final Configuration configuration)
	{
		for (String key : configuration.getKeys(true)) {
			if (!key.matches(UPPER_SNAKE_CASE.pattern())) {
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
