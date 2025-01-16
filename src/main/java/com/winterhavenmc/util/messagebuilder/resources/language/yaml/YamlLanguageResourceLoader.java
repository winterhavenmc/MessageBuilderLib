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

import com.winterhavenmc.util.messagebuilder.util.Error;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageResourceInstaller.SUBDIRECTORY;


/**
 * An implementation of the LanguageResourceLoader interface for loading the message configuration from yaml files
 */
//TODO: This class needs more test coverage. It's mostly null checks and throws missing, and the reload command.
public final class YamlLanguageResourceLoader {

	// constants for plugin configuration keys
	private final static String CONFIG_LOCALE_KEY = "locale";
	private final static String CONFIG_LANGUAGE_KEY = "language";

	// reference to plugin main class
	private final Plugin plugin;

	YamlLanguageResourceInstaller resourceInstaller;


	/**
	 * Class constructor, Two parameter
	 *
	 * @param plugin an instance of the plugin
	 * @param resourceInstaller a language
	 */
	YamlLanguageResourceLoader(final Plugin plugin, final YamlLanguageResourceInstaller resourceInstaller)
	{
		this.plugin = plugin;
		this.resourceInstaller = resourceInstaller;
	}


	// avoid creating installer if the file we need is already installed
	public void setup()
	{
		// get new instance of installer
		this.resourceInstaller = new YamlLanguageResourceInstaller(plugin);

		// install any language resource files listed in auto_install.txt to plugin data directory
		resourceInstaller.autoInstall();
	}


	/**
	 * Load the language configuration object for the configured language from file and return it, withou
	 * loading configuration defaults
	 *
	 * @return Configuration - message configuration object
	 */
	Configuration loadConfiguration()
	{
		// get valid language tag using configured language
		String languageTag = getValidLanguageTag(getConfiguredLanguage(plugin));

		return loadConfiguration(languageTag);
	}


	/**
	 * Load the language configuration object for the given IETF language tag and return it, without loading
	 * configuration defaults
	 *
	 * @return {@link Configuration} containing the configuration loaded from the language file
	 */
	Configuration loadConfiguration(final String languageTag) {

		// get filename for language tag
		String filename = getLanguageFilename(languageTag);

		// if file not installed for language tag in plugin data directory, try to install from resource
		resourceInstaller.installIfMissing(filename);

		// if file is still not installed, use en-US
		if (fileAbsent(filename)) {
			filename = getLanguageFilename("en-US");
			resourceInstaller.installIfMissing(filename);
		}

		// get file object for configured language file
		File languageFile = new File(filename);

		// create new YamlConfiguration object
		YamlConfiguration configuration = new YamlConfiguration();

		try // to load specified language file into new YamlConfiguration object
		{
			configuration.load(languageFile);
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


	/**
	 * Load a default configuration from a resource into an existing configuration
	 *
	 * @param resource the input stream for the resource
	 * @param configuration the newly created language configuration
	 * @return the configuration with a default configuration loaded from the resource, if available
	 */
	Configuration getConfigurationDefaults(InputStream resource, Configuration configuration)
	{
		// get input stream reader for embedded resource file
		Reader inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);

		// load embedded resource stream into Configuration object
		Configuration defaultConfiguration = YamlConfiguration.loadConfiguration(inputStreamReader);

		// set Configuration object as defaults for configuration
		configuration.setDefaults(defaultConfiguration);

		return configuration;
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
	String getConfiguredLanguage(final Plugin plugin)
	{
		return plugin.getConfig().getString(CONFIG_LANGUAGE_KEY);
	}


	/**
	 * Check if a file exists in the plugin data directory for the provided IETF language tag (ex: fr-CA).
	 * If a file does not exist for the configured language tag, the default (en-US) will be returned.
	 * A resource should always be included in the plugin for the default language tag (en-US) when using this library.
	 *
	 * @param languageTag the IETF language tag
	 * @return if file exists for language tag, return the language tag; else return the default tag (en-US)
	 */
	String getValidLanguageTag(final String languageTag)
	{
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


	/**
	 * Check if a language resource exists for a given language tag, and return its name as {@code String} if
	 * the file exists, or else return the name for the default language file 'language/en-US.yml', which
	 * should always be included in the plugin resource directory of any plugin using this library.
	 *
	 * @param languageTag the IETF Language Tag to specify a language file associated with the language tag
	 * @return the resource name for a file associated with the language tag if it exists, or the default
	 * language file, 'language/en-US.yml'.
	 */
	String getValidResourceName(String languageTag)
	{
		if (languageTag == null) { throw new IllegalArgumentException(Error.Parameter.NULL_RESOURCE_NAME.getMessage()); }

		// get embedded resource file name; note that forward slash (/) is always used, regardless of platform
		String resourceName = String.join("/",SUBDIRECTORY, languageTag).concat(".yml");

		// check if specified language resource exists, otherwise use en-US
		if (plugin.getResource(resourceName) == null) {
			resourceName = SUBDIRECTORY + "/" + "en-US.yml";
		}
		return resourceName;
	}


	/**
	 * Get a {@code File} object for the language file installed in the plugin data directory for
	 * the given IETF language tag. If a file does not exist for the given language tag, the default
	 * language file 'language/en-US.yml' will be used.
	 *
	 * @param languageTag the IETF language tag to convert to a filename
	 * @return {@code File} the filename corresponding to the IETF language tag given
	 */
	File getLanguageFile(final String languageTag)
	{
		return new File(plugin.getDataFolder(), getLanguageFilename(languageTag));
	}


	/**
	 * Get the file path as a string for the provided language tag. This method does not verify the file
	 * exists, but merely returns a string representing the path if such a file was installed.
	 *
	 * @param languageTag IETF language tag
	 * @return current language file name as String
	 */
	static String getLanguageFilename(final String languageTag)
	{
		return Paths.get(SUBDIRECTORY, languageTag + ".yml").normalize().toString();
	}


	boolean fileAbsent(final String languageTag) {
		return !this.resourceInstaller.verifyResourceInstalled(getLanguageFilename(languageTag));
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
