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

import com.winterhavenmc.util.TimeUnit;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;


/**
 * provides common methods for the installation and management of
 * localized language files for bukkit plugins.
 */
public class YamlLanguageHandler implements LanguageHandler {

	// string constant for language key in plugin config file
	private final static String CONFIG_LANGUAGE_KEY = "language";

	// string constant for configuration section key
	private final static String LOCATION_SECTION = "LOCATIONS";

	// reference to main plugin
	private Plugin plugin;

	// language file installer and loader
	private LanguageFileLoader languageFileLoader;

	// configuration object for messages file
	private Configuration configuration;


	/**
	 * class constructor, no parameter
	 * must use setters for all fields
	 */
	public YamlLanguageHandler() { }


	/**
	 * class constructor, one parameter
	 * must use setters for languageFileInstaller and languageFileLoader fields
	 *
	 * @param plugin the plugin main class
	 */
	public YamlLanguageHandler(final Plugin plugin) {

		// reference to plugin main class
		this.plugin = plugin;

		// load message configuration from file loader
		configuration = new YamlLanguageFileLoader(plugin).getConfiguration();
	}


	/**
	 * class constructor, three parameter
	 * all fields are provided as parameters
	 *
	 * @param plugin the plugin main class
	 */
	public YamlLanguageHandler(final Plugin plugin,
	                           final LanguageFileLoader languageFileLoader) {

		// reference to plugin main class
		this.plugin = plugin;
		this.languageFileLoader = languageFileLoader;

		// load message configuration from file
		configuration = languageFileLoader.getConfiguration();
	}

	/**
	 * setter for plugin (do we really need a setter for plugin? maybe for testing.)
	 * @param plugin a new plugin to replace the existing plugin
	 */
	void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * setter for fileLoader
	 * @param languageFileLoader a new FileLoader to replace the existing fileLoader
	 */
	void setFileLoader(LanguageFileLoader languageFileLoader) {
		this.languageFileLoader = languageFileLoader;
	}

	// for testing setters
	boolean isPluginSet() {
		return this.plugin != null;
	}
	boolean isFileLoaderSet() {
		return this.languageFileLoader != null;
	}


	@Override
	public Configuration getConfiguration() {
		return configuration;
	}


	@Override
	public String getConfigLanguage() {
		return plugin.getConfig().getString(CONFIG_LANGUAGE_KEY);
	}


	/**
	 * Get spawn display name from language file
	 *
	 * @return the formatted display name for the world spawn, or empty string if key not found
	 */
	@Override
	public Optional<String> getSpawnDisplayName() {
		return Optional.ofNullable(configuration.getString(LOCATION_SECTION + ".SPAWN.DISPLAY_NAME"));
	}


	/**
	 * Get home display name from language file
	 *
	 * @return the formatted display name for home, or empty string if key not found
	 */
	@Override
	public Optional<String> getHomeDisplayName() {
		return Optional.ofNullable(configuration.getString(LOCATION_SECTION + ".HOME.DISPLAY_NAME"));
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
	 * Retrieve an arbitrary string from the language file with the specified key.
	 *
	 * @param path the message path for the string being retrieved
	 * @return the retrieved string, or null if no matching key found
	 */
	public Optional<String> getString(final String path) {
		return Optional.ofNullable(configuration.getString(path));
	}


	/**
	 * Get List of String by path in message file
	 *
	 * @param path the message path for the string list being retrieved
	 * @return List of String - the string list retrieved by path from message file
	 */
	public List<String> getStringList(final String path) {
		return configuration.getStringList(path);
	}


	/**
	 * Reload messages from yaml file into Configuration object. If the yaml language file
	 * does not exist in the plugin data directory, it will be re-copied from the jar resource.
	 * If a file does not exist and a resource cannot be found, the en-US language file
	 * or resource will be used to load the configuration.
	 */
	@Override
	public void reload() {
		configuration = new YamlLanguageFileLoader(plugin).getConfiguration();
	}

}
