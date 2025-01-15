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
import org.bukkit.plugin.Plugin;


public class YamlLanguageResourceManager implements LanguageResourceManager {

	private static YamlLanguageResourceManager instance;

	private final Plugin plugin;
	private final YamlLanguageResourceLoader languageResourceLoader;
	private YamlConfigurationSupplier configurationSupplier;


	/**
	 * Private constructor prevents instantiation except from within this class
	 *
	 * @param plugin an instance of the plugin
	 */
	private YamlLanguageResourceManager(Plugin plugin) {
		this.plugin = plugin;

		// instantiate resource loader and setup
		this.languageResourceLoader = new YamlLanguageResourceLoader(plugin, new YamlLanguageResourceInstaller(plugin));
		this.languageResourceLoader.setup();

		// instantiate supplier with language configuration from resource loader, and assign to field
		this.configurationSupplier = new YamlConfigurationSupplier(languageResourceLoader.loadConfiguration());
	}


	/**
	 * Static method to retrieve an instance of this singleton
	 *
	 * @param plugin an instance of the plugin
	 * @return a new or cached instance of this singleton
	 */
	public static YamlLanguageResourceManager getInstance(Plugin plugin) {
		if (instance == null) {
			synchronized (YamlLanguageResourceManager.class) {
				if (instance == null) {
					instance = new YamlLanguageResourceManager(plugin);
				}
			}
		}
		return instance;
	}


	public boolean reload() {
		// Reload the configuration and update the supplier.
		languageResourceLoader.reload();
		if (languageResourceLoader.loadConfiguration() == null) {
			return false;
		}
		configurationSupplier = new YamlConfigurationSupplier(languageResourceLoader.loadConfiguration());
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
