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

package com.winterhavenmc.library.messagebuilder.resources.language;

import com.winterhavenmc.library.messagebuilder.model.language.Section;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LanguageTag;

import org.bukkit.configuration.Configuration;

import java.io.File;

import static com.winterhavenmc.library.messagebuilder.resources.language.LanguageSetting.RESOURCE_SUBDIRECTORY;


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
 * 
 */
public final class LanguageResourceManager implements SectionResourceManager
{
	private final LanguageResourceLoader languageResourceLoader;
	private final LanguageResourceInstaller languageResourceInstaller;
	private Configuration languageConfiguration;


	/**
	 * Class constructor
	 *
	 * @param resourceInstaller a LanguageResourceInstaller instance
	 * @param resourceLoader  a LanguageResourceLoader instance
	 */
	public LanguageResourceManager(final LanguageResourceInstaller resourceInstaller,
								   final LanguageResourceLoader resourceLoader)
	{
		this.languageResourceLoader = resourceLoader;
		this.languageResourceInstaller = resourceInstaller;

		// install any auto install files if necessary
		languageResourceInstaller.autoInstall();

		// get newly loaded configuration from loader
		this.languageConfiguration = languageResourceLoader.load();
	}


	/**
	 * package-private constructor for testing purposes
	 *
	 * @param resourceInstaller a LanguageResourceInstaller instance
	 * @param resourceLoader  a LanguageResourceLoader instance
	 * @param languageConfiguration a bukkit Configuration representing the language resource
	 */
	LanguageResourceManager(final LanguageResourceInstaller resourceInstaller,
								   final LanguageResourceLoader resourceLoader,
								   final Configuration languageConfiguration)
	{
		this.languageResourceInstaller = resourceInstaller;
		this.languageResourceLoader = resourceLoader;
		this.languageConfiguration = languageConfiguration;
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
	@Override
	public boolean reload()
	{
		// install any resources whose corresponding files are absent
		languageResourceInstaller.autoInstall();

		// Reload the configuration and get the new configuration from the loader
		this.languageConfiguration = languageResourceLoader.load();

		return true;
	}


	/**
	 * Retrieve the configuration provider, a container that carries the current configuration
	 *
	 * @return the configuration provider
	 */
	public SectionProvider getSectionProvider(Section section)
	{
		return new LanguageSectionProvider(() -> languageConfiguration, section);
	}


	/**
	 * Retrieve the name of the potential language resource associated with this language tag, as a String
	 *
	 * @return {@code String} representation of the potential language resource associated with this language tag
	 */
	public static String getResourceName(final LanguageTag languageTag)
	{
		return String.join("/", RESOURCE_SUBDIRECTORY.toString(), languageTag.toString()).concat(".yml");
	}


	/**
	 * Retrieve the name of the potential language resource file as installed in the plugin data directory, as a String.
	 *
	 * @return {@code String} representation of the potential language resource file installed in the plugin data directory
	 */
	public static String getFileName(final LanguageTag languageTag)
	{
		return String.join(File.separator, RESOURCE_SUBDIRECTORY.toString(), languageTag.toString()).concat(".yml");
	}

}
