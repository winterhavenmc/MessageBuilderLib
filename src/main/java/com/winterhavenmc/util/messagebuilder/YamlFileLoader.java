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

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;


final class YamlFileLoader
{
	// reference to plugin main class
	private final Plugin plugin;

	// the directory name within a plugin data directory where the language yaml files are installed
	private final String directoryName = "language";


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	YamlFileLoader(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Get configuration object containing message settings and strings
	 *
	 * @return Configuration - message configuration object
	 */
	Configuration getMessages()
	{
		// check that file exists for language
		String confirmedLanguage = languageFileExists(plugin, getConfiguredLanguage(plugin));

		// get file object for configured language file
		File languageFile = new File(getLanguageFilename(plugin, confirmedLanguage));

		// create new YamlConfiguration object
		YamlConfiguration newMessagesConfig = new YamlConfiguration();

		// try to load specified language file into new YamlConfiguration object
		try
		{
			newMessagesConfig.load(languageFile);
			plugin.getLogger().info("Language file " + confirmedLanguage + ".yml successfully loaded.");
		}
		catch (FileNotFoundException e)
		{
			plugin.getLogger().severe("Language file " + confirmedLanguage + ".yml does not exist.");
		}
		catch (IOException e)
		{
			plugin.getLogger().severe("Language file " + confirmedLanguage + ".yml could not be read.");
		}
		catch (InvalidConfigurationException e)
		{
			plugin.getLogger().severe("Language file " + confirmedLanguage + ".yml is not valid yaml.");
		}

		// Set defaults to embedded resource file

		final String resourceName = getResourceName(confirmedLanguage);

		// get input stream reader for embedded resource file
		//noinspection ConstantConditions
		Reader defaultConfigStream = new InputStreamReader(plugin.getResource(resourceName), StandardCharsets.UTF_8);

		// load embedded resource stream into Configuration object
		Configuration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);

		// set Configuration object as defaults for messages configuration
		newMessagesConfig.setDefaults(defaultConfig);

		return newMessagesConfig;
	}


	String getResourceName(String confirmedLanguage)
	{
		// get embedded resource file name; note that forward slash (/) is always used, regardless of platform
		String resourceName = directoryName + "/" + confirmedLanguage + ".yml";

		// check if specified language resource exists, otherwise use en-US
		if (plugin.getResource(resourceName) == null)
		{
			resourceName = directoryName + "/" + "en-US.yml";
		}
		return resourceName;
	}


	/**
	 * Gets language tag specified in config.yml.
	 * <p>
	 * it is recommended, but not required, that languages should be specified by their ISO-639 codes,
	 * with two letter lowercase language code and two letter uppercase country code separate by a hyphen.<br>
	 * Example: en-US
	 * <p>
	 * The language yaml file must match the specified tag, with a .yml extension appended.
	 *
	 * @param plugin reference to plugin main class
	 * @return IETF language tag as string from config.yml
	 */
	private String getConfiguredLanguage(final Plugin plugin)
	{
		return plugin.getConfig().getString("language");
	}


	/**
	 * Check if a file exists for the provided IETF language tag (ex: en-US)
	 *
	 * @param language the IETF language tag
	 * @return if file exists for language tag, return the language tag; else return the default tag (en-US)
	 */
	String languageFileExists(final Plugin plugin, final String language)
	{
		// get a file object for language tag by adding prefixing for directory name and .yml suffix
		File languageFile = new File(getLanguageFilename(plugin, language));

		// if a language file exists for the language tag, return the language tag
		if (languageFile.exists())
		{
			return language;
		}

		// output language file not found message to log
		plugin.getLogger().info("Language file " + language + ".yml does not exist. Defaulting to en-US.");

		// return default language tag (en-US)
		return "en-US";
	}


	/**
	 * Get the file path as a string for the provided language identifier. This method does not verify the file
	 * exists, but merely returns a string representing the path if such a file was installed.
	 *
	 * @param plugin reference to plugin main class
	 * @param language IETF language tag
	 * @return current language file name as String
	 */
	String getLanguageFilename(final Plugin plugin, final String language)
	{
		return plugin.getDataFolder() + File.separator + directoryName + File.separator + language + ".yml";
	}

}
