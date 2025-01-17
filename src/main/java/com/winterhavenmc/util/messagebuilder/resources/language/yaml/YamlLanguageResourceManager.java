/*
 * Copyright (c) 2025 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageResourceManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.Plugin;


/**
 * This class is responsible for the management and lifecycle of the language resource. It is implemented
 * as a singleton, so that only one instance is involved in any loading or reloading of the resource to
 * prevent access contention. The language resource is made available as a Bukkit {@link Configuration} object,
 * which is loaded into a {@code Supplier} that is provided to classes that have a need to access the
 * language configuration object. This supplier will return an up-to-date version of the language configuration
 * object to any consumers, even if the language resource has been reloaded since the creation of the supplier.
 * A convenience method is provided to query the current language setting in the plugin configuration so that
 * changes to this setting may result in a different language resource being used for any subsequent reload operations.
 * <p>
 * The static {@code getInstance} method should be used to acquire an instance of this singleton class. It can be accessed
 * globally, anywhere within this library using this static method.
 * </p>
 */
public final class YamlLanguageResourceManager implements LanguageResourceManager {

	private static YamlLanguageResourceManager instance;

	private final Plugin plugin;
	private final YamlLanguageResourceLoader languageResourceLoader;
	private Configuration languageConfiguration;
	private YamlConfigurationSupplier configurationSupplier;


	/**
	 * Private constructor prevents instantiation except from within this class
	 *
	 * @param plugin an instance of the plugin
	 */
	private YamlLanguageResourceManager(final Plugin plugin, final YamlLanguageResourceLoader resourceLoader) {
		this.plugin = plugin;
		this.languageResourceLoader = resourceLoader;
		this.languageResourceLoader.setup();

		// get newly loaded configuration from loader
		this.languageConfiguration = languageResourceLoader.loadConfiguration();

		// instantiate supplier with language configuration from resource loader, and assign to field
		this.configurationSupplier = new YamlConfigurationSupplier(languageConfiguration);
	}


	/**
	 * Static method to retrieve an instance of this singleton
	 *
	 * @param plugin an instance of the plugin
	 * @return a new or cached instance of this singleton
	 */
	public static YamlLanguageResourceManager getInstance(final Plugin plugin, final YamlLanguageResourceLoader resourceLoader) {
		if (instance == null) {
			synchronized (YamlLanguageResourceManager.class) {
				if (instance == null) {
					instance = new YamlLanguageResourceManager(plugin, resourceLoader);
				}
			}
		}
		return instance;
	}


	/**
	 * Reload the language resource. This method first calls the reload method in the resource loader,
	 * and receives the new configuration object as the return value. If the new configuration object
	 * is null, the old configuration object is not replace, and the method returns {@code false}.
	 * If the new configuration exists, a new configuration supplier is created with the
	 * new configuration, and the method returns {@code true}.
	 *
	 * @return {@code true} if the configuration was successfully reloaded, {@code false} if it failed
	 */
	public boolean reload() {
		// Reload the configuration and get the new configuration from the loader
		Configuration newConfiguration = languageResourceLoader.reload();

	//TODO: intelliJ says this can never happen
		// if the new configuration is null, leave the old configuration in place, and return false
		if (newConfiguration == null) {
			return false;
		}

		// if new configuration differs from existing configuration, replace stored configuration with
		// new configuration and create a new supplier with the new configuration
		if (!languageConfiguration.equals(newConfiguration)) {
			languageConfiguration = newConfiguration;
			configurationSupplier = new YamlConfigurationSupplier(languageConfiguration);
		}
		// return success
		return true;
	}


	/**
	 * Retrieve the configuration supplier, a container that carries the current configuration
	 *
	 * @return the configuration supplier
	 */
	public YamlConfigurationSupplier getConfigurationSupplier() {
		return configurationSupplier;
	}


	/**
	 * Get setting for language from plugin config file
	 *
	 * @return a string containing the IETF language tag set in the plugin config file
	 */
	@Override
	public String getConfiguredLanguage() {
		return plugin.getConfig().getString("language");
	}

}
