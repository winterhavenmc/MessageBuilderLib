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

package com.winterhavenmc.library.messagebuilder.adapters.resources.language;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.*;

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.LanguageResourceManager;
import com.winterhavenmc.library.messagebuilder.models.configuration.LanguageTag;
import com.winterhavenmc.library.messagebuilder.models.language.Section;
import org.bukkit.configuration.Configuration;

import java.io.File;

import static com.winterhavenmc.library.messagebuilder.adapters.resources.language.LanguageConfigConstant.RESOURCE_SUBDIRECTORY;


/**
 * This class is responsible for the management and lifecycle of the language resource.
 * The language resource is made available as a Bukkit {@link Configuration} object,
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
public final class YamlLanguageResourceManager implements LanguageResourceManager
{
	private final ResourceLoader resourceLoader;
	private final ResourceInstaller resourceInstaller;
	private Configuration languageConfiguration;


	/**
	 * Class constructor
	 *
	 * @param resourceInstaller a YamlLanguageResourceInstaller instance
	 * @param resourceLoader  a YamlLanguageResourceLoader instance
	 */
	public YamlLanguageResourceManager(final ResourceInstaller resourceInstaller,
									   final ResourceLoader resourceLoader)
	{
		this.resourceInstaller = resourceInstaller;
		this.resourceLoader = resourceLoader;

		installResources();
		this.languageConfiguration = resourceLoader.load();
	}


	/**
	 * package-private constructor for testing purposes
	 *
	 * @param installer a YamlLanguageResourceInstaller instance
	 * @param loader  a YamlLanguageResourceLoader instance
	 * @param configuration a bukkit Configuration representing the language resource
	 */
	public YamlLanguageResourceManager(final ResourceInstaller installer,
									   final ResourceLoader loader,
									   final Configuration configuration)
	{
		this.resourceInstaller = installer;
		this.resourceLoader = loader;
		this.languageConfiguration = configuration;
	}

	/**
	 * Constructs the resource path (in the JAR) for a given language tag, e.g. "language/en-US.yml".
	 */
	public static String getResourceName(LanguageTag languageTag)
	{
		return String.join("/", RESOURCE_SUBDIRECTORY.toString(), languageTag.toString()) + ".yml";
	}

	/**
	 * Retrieve the name of the potential language resource file as installed in the plugin data directory, as a Section.
	 *
	 * @return {@code Section} representation of the potential language resource file installed in the plugin data directory
	 */
	public static String getFileName(LanguageTag languageTag)
	{
		return String.join(File.separator, RESOURCE_SUBDIRECTORY.toString(), languageTag.toString()) + ".yml";
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
		installResources();

		Configuration newConfig = resourceLoader.load();
		if (newConfig != null)
		{
			this.languageConfiguration = newConfig;
			return true;
		}

		return false; // keep the old config if reload failed
	}


	/**
	 * Retrieve the configuration provider, a container that carries the current configuration
	 *
	 * @return the configuration provider
	 */
	@Override
	public SectionProvider getSectionProvider(Section section)
	{
		return new YamlLanguageSectionProvider(() -> languageConfiguration, section);
	}


	@Override
	public ConfigurationProvider getConfigurationProvider()
	{
		return new YamlLanguageConfigurationProvider(() -> languageConfiguration);
	}


	/**
	 * Installs any language resource files listed in auto-install.txt if they are not already installed.
	 */
	@Override
	public void installResources()
	{
		resourceInstaller.install();
	}

}
