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

import com.winterhavenmc.util.messagebuilder.languages.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;


/**
 * provides common methods for the installation and management of
 * localized language files for bukkit plugins.
 */
public class YamlLanguageHandler implements LanguageHandler {

	// string constant for language key in plugin config file
	private final static String CONFIG_LANGUAGE_KEY = "language";

	// reference to main plugin class
	private Plugin plugin;

	// language file installer and loader
	private LanguageFileInstaller languageFileInstaller;
	private LanguageFileLoader languageFileLoader;

	// configuration object for language file
	private Configuration configuration;


	/**
	 * class constructor, no parameter
	 * must use setters for all fields before use
	 */
	public YamlLanguageHandler() {
		this.plugin = null;
		this.languageFileInstaller = null;
		this.languageFileLoader = null;
		this.configuration = null;
	}


	/**
	 * class constructor, three parameter
	 * all fields are provided as parameters
	 *
	 * @param plugin                 the plugin main class
	 * @param languageFileInstaller  the language file installer to be used by the language handler
	 * @param languageFileLoader     the language file loader to be used by the language handler
	 */
	public YamlLanguageHandler(final Plugin plugin,
							   final LanguageFileInstaller languageFileInstaller,
	                           final LanguageFileLoader languageFileLoader) {

		// reference to plugin main class
		this.plugin = plugin;
		this.languageFileInstaller = languageFileInstaller;
		this.languageFileLoader = languageFileLoader;

		// load message configuration from file
		configuration = languageFileLoader.getConfiguration();
	}

	/**
	 * setter for plugin
	 * @param plugin a new plugin to replace the existing plugin
	 */
	void setPlugin(final Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * setter for fileInstaller
	 * @param languageFileInstaller a new FileLoader to replace the existing fileLoader
	 */
	void setFileInstaller(final LanguageFileInstaller languageFileInstaller) {
		this.languageFileInstaller = languageFileInstaller;
	}

	/**
	 * setter for fileLoader
	 * @param languageFileLoader a new FileLoader to replace the existing fileLoader
	 */
	void setFileLoader(final LanguageFileLoader languageFileLoader) {
		this.languageFileLoader = languageFileLoader;
	}

	// for testing setters
	boolean isPluginSet() {
		return this.plugin != null;
	}

	boolean isFileLoaderSet() {
		return this.languageFileLoader != null;
	}

	boolean isFileInstallerSet() {
		return this.languageFileInstaller != null;
	}


	/**
	 * Get the language configuration from the configured language yaml file.
	 *
	 * @return a configuration object loaded with values from the configured language file, or the default en-US.yml
	 * language file if the configured file could not be found.
	 */
	@Override
	public Configuration getConfiguration() {
		return configuration;
	}


	/**
	 * Get configured language from plugin config.yml file. Note that Locale support is coming, as documented elsewhere.
	 *
	 * @return The IETF language tag as a String specifying the language file to load. If the setting could not be found,
	 * it will default to 'en-US'. An en-US.yml language file should always be included in the plugin's resources, to
	 * ensure that default messages are displayed by the plugin.
	 */
	@Override
	public String getConfigLanguage() {
		return plugin.getConfig().getString(CONFIG_LANGUAGE_KEY);
	}


	/**
	 * Reload messages from yaml file into Configuration object. If the yaml language file
	 * does not exist in the plugin data directory, it will be re-copied from the jar resource.
	 * If a file does not exist and a resource cannot be found, the en-US language file
	 * or resource will be used to load the configuration.
	 */
	@Override
	public void reload() {
		new YamlLanguageFileInstaller(plugin).install();
		configuration = new YamlLanguageFileLoader(plugin).getConfiguration();
	}

}
