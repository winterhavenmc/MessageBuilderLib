/*
 * Copyright (c) 2022-2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.languages;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


/**
 * An implementation of the LanguageFileLoader interface for loading the message configuration from yaml files
 */
public class YamlLanguageFileLoader implements LanguageFileLoader {

	// the directory name within a plugin data directory where the language yaml files are installed
	private final static String LANGUAGE_FOLDER = "language";
	private final static String CONFIG_LANGUAGE_KEY = "language";

	// reference to plugin main class
	private final Plugin plugin;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public YamlLanguageFileLoader(final Plugin plugin) {
		this.plugin = plugin;
	}


	/**
	 * Get configuration object containing message settings and strings
	 *
	 * @return Configuration - message configuration object
	 */
	@Override
	public Configuration getConfiguration() {

		// get valid language tag using configured language
		String languageTag = getValidLanguageTag(getConfiguredLanguage(plugin));

		// get file object for configured language file
		File languageFile = new File(getLanguageFilename(languageTag));

		// create new YamlConfiguration object
		YamlConfiguration newMessagesConfig = new YamlConfiguration();

		// try to load specified language file into new YamlConfiguration object
		try {
			newMessagesConfig.load(languageFile);
			plugin.getLogger().info("Language file " + languageTag + ".yml successfully loaded.");
		} catch (FileNotFoundException e) {
			plugin.getLogger().severe("Language file " + languageTag + ".yml does not exist.");
		} catch (IOException e) {
			plugin.getLogger().severe("Language file " + languageTag + ".yml could not be read.");
		} catch (InvalidConfigurationException e) {
			plugin.getLogger().severe("Language file " + languageTag + ".yml is not valid yaml.");
		}

		// Set defaults to embedded resource file

		final String resourceName = getResourceName(languageTag);

		// get input stream reader for embedded resource file
		//noinspection ConstantConditions
		Reader defaultConfigStream = new InputStreamReader(plugin.getResource(resourceName), StandardCharsets.UTF_8);

		// load embedded resource stream into Configuration object
		Configuration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);

		// set Configuration object as defaults for messages configuration
		newMessagesConfig.setDefaults(defaultConfig);

		return newMessagesConfig;
	}


	@NotNull
	String getResourceName(String languageTag) {
		// get embedded resource file name; note that forward slash (/) is always used, regardless of platform
		String resourceName = LANGUAGE_FOLDER + "/" + languageTag + ".yml";

		// check if specified language resource exists, otherwise use en-US
		if (plugin.getResource(resourceName) == null) {
			resourceName = LANGUAGE_FOLDER + "/" + "en-US.yml";
		}
		return resourceName;
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
	private String getConfiguredLanguage(final Plugin plugin) {
		return plugin.getConfig().getString(CONFIG_LANGUAGE_KEY);
	}


	/**
	 * Check if a file exists for the provided IETF language tag (ex: fr-CA).
	 * If a file does not exist for the configured language tag, the default (en-US) will be returned.
	 * A resource should always be included in the plugin for the default language tag (en-US) when using this library.
	 *
	 * @param languageTag the IETF language tag
	 * @return if file exists for language tag, return the language tag; else return the default tag (en-US)
	 */
	String getValidLanguageTag(final String languageTag) {

		// if a language file exists for the language tag, return the language tag
		if (getLanguageFile(languageTag).exists()) {
			return languageTag;
		}
		// output language file not found message to log
		plugin.getLogger().warning("Language file '"
				+ getLanguageFilename(languageTag) + "' does not exist. Defaulting to en-US.");

		// return default language tag (en-US)
		return "en-US";
	}

	File getLanguageFile(final String languageTag) {
		return new File(plugin.getDataFolder(), getLanguageFilename(languageTag));
	}


	/**
	 * Get the file path as a string for the provided language tag. This method does not verify the file
	 * exists, but merely returns a string representing the path if such a file was installed.
	 *
	 * @param languageTag IETF language tag
	 * @return current language file name as String
	 */
	static String getLanguageFilename(final String languageTag) {
		return Paths.get(LANGUAGE_FOLDER, languageTag + ".yml").normalize().toString();
	}

}
