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

import com.winterhavenmc.util.messagebuilder.resources.language.LanguageResourceHandler;
import com.winterhavenmc.util.messagebuilder.query.QueryHandlerFactory;
import org.bukkit.configuration.Configuration;

import java.util.Locale;


/**
 * provides common methods for the installation and management of
 * localized language files for bukkit plugins.
 */
public class YamlLanguageResourceHandler implements LanguageResourceHandler {

	// string constant for language key in plugin config file
	private final static String CONFIG_LOCALE_KEY = "locale";
	private final static String CONFIG_LANGUAGE_KEY = "language";

	// default locale
	private Locale locale = Locale.US;

	// plugin configuration
	private final Configuration pluginConfiguration;

	// configuration supplier
	YamlConfigurationSupplier yamlLanguageConfigurationSupplier;

	// language file loader
	private YamlLanguageResourceLoader languageResourceLoader;



	/**
	 * class constructor, three parameter
	 * all fields are provided as parameters
	 *
	 * @param pluginConfiguration        the plugin configuration
	 * @param languageResourceLoader     the language file loader to be used by the language handler
	 */
	public YamlLanguageResourceHandler(final Configuration pluginConfiguration,
	                                   final YamlLanguageResourceLoader languageResourceLoader)
	{
		// set fields from parameters
		this.pluginConfiguration = pluginConfiguration;
		this.languageResourceLoader = languageResourceLoader;

		// instantiate supplier
		yamlLanguageConfigurationSupplier = new YamlConfigurationSupplier(languageResourceLoader.getConfiguration());

		// instantiate QueryHandlerFactory
		QueryHandlerFactory queryHandlerFactory = new QueryHandlerFactory(yamlLanguageConfigurationSupplier);



		// load message configuration from file
//		languageConfig = languageResourceLoader.getConfiguration();

		// get locale from plugin configuration if set
		//TODO: Mock static method to enable and test this
//		String languageTag = pluginConfig.getString(CONFIG_LOCALE_KEY);
//		if (languageTag != null) {
//			locale = Locale.forLanguageTag(languageTag);
//		}
	}


	/**
	 * get configuration supplier
	 *
	 * @return the configuration supplier
	 */
	@Override
	public YamlConfigurationSupplier getConfigurationSupplier() {
		return this.yamlLanguageConfigurationSupplier;
	}

	/**
	 * class constructor, no parameter
	 * must use setters for all fields before use
	 */
	public YamlLanguageResourceHandler() {
		this.pluginConfiguration = null;
		this.languageResourceLoader = null;
		this.yamlLanguageConfigurationSupplier = null;
		this.locale = null;
	}

	/**
	 * setter for fileLoader
	 * @param languageResourceLoader a new FileLoader to replace the existing fileLoader
	 */
	void setFileLoader(final YamlLanguageResourceLoader languageResourceLoader) {
		this.languageResourceLoader = languageResourceLoader;
	}

	/**
	 * Setter for locale
	 * @param locale the locale to set
	 */
	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	/**
	 * Get the current locale
	 * @return the current locale
	 */
	public Locale getLocale() {
		return this.locale;
	}


	boolean isFileLoaderSet() {
		return this.languageResourceLoader != null;
	}

	boolean isLocaleSet() {
		return this.locale != null;
	}


	/**
	 * Get the language configuration held by this language handler.
	 *
	 * @return a configuration object loaded with values from the configured language file, or the default en-US.yml
	 * language file if the configured file could not be found.
	 */
	public Configuration getPluginConfiguration() {
		return yamlLanguageConfigurationSupplier.get();
	}


	/**
	 * Get the language configuration held by this language handler.
	 *
	 * @return a configuration object loaded with values from the configured language file, or the default en-US.yml
	 * language file if the configured file could not be found.
	 */
	public YamlConfigurationSupplier getLanguageConfigurationSupplier() {
		return yamlLanguageConfigurationSupplier;
	}


	/**
	 * Get configured language from plugin config.yml file. Note that Locale support is coming, as documented elsewhere.
	 *
	 * @return The IETF language tag as a String specifying the language file to load. If the setting could not be found,
	 * it will default to 'en-US'. An en-US.yml language file should always be included in the plugin's resources, to
	 * ensure that default messages are displayed by the plugin.
	 */
	@Override
	public String getConfiguredLanguage() {
		return pluginConfiguration.getString(CONFIG_LANGUAGE_KEY);
	}


	/**
	 * Reload messages from yaml file into Configuration object. If the yaml language file
	 * does not exist in the plugin data directory, it will be re-copied from the jar resource.
	 * If a file does not exist and a resource cannot be found, the en-US language file
	 * or resource will be used to load the configuration.
	 *
	 * @return {@code true} if the reload was successful, {@code false} if it was not
	 */
	@Override
	public boolean reload() {
		languageResourceLoader.reload();
		Configuration newConfiguration = languageResourceLoader.getConfiguration();
		if (newConfiguration != null) {
			yamlLanguageConfigurationSupplier = new YamlConfigurationSupplier(newConfiguration);
			return true;
		}
		return false;
	}

}
