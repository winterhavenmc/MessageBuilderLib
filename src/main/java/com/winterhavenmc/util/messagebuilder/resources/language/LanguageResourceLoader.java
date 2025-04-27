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

package com.winterhavenmc.util.messagebuilder.resources.language;

import com.winterhavenmc.util.messagebuilder.resources.configuration.LanguageTag;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;


/**
 * This class is responsible for the loading of the language file from the plugin data directory into
 * a configuration object. The configuration object is loaded from file when ever the getConfiguration method
 * is called. The class does not store the configuration; each invocation of the getConfiguration method will
 * result in a new configuration object loaded from the currently configured language file, or the us-EN language
 * file if a file for the currently configured language cannot be found in the plugin data directory.
 */
public final class LanguageResourceLoader implements com.winterhavenmc.util.messagebuilder.resources.ResourceLoader
{
	private final Plugin plugin;


	/**
	 * Class constructor
	 *
	 * @param plugin an instance of the plugin main class
	 */
	public LanguageResourceLoader(final Plugin plugin)
	{
		this.plugin = plugin;
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
	 * @return Optional {@code LanguageTag} or an empty Optional if config setting is null or empty
	 */
	@Override
	public Optional<LanguageTag> getConfiguredLanguageTag()
	{
		String configLanguageTag = plugin.getConfig().getString(LanguageSetting.CONFIG_LANGUAGE_KEY.toString());

		return configLanguageTag == null || configLanguageTag.isBlank()
				? Optional.empty()
				: LanguageTag.of(Locale.forLanguageTag(configLanguageTag));
	}


	@Override
	public Locale getConfiguredLocale()
	{
		return getConfiguredLanguageTag()
				.map(tag -> Locale.forLanguageTag(tag.toString()))
				.orElse(Locale.getDefault());
	}


	/**
	 * Load the language configuration object for the configured language from file and return it. The returned
	 * configuration object contains no default values loaded, by design
	 *
	 * @return Configuration - message configuration object
	 */
	@Override
	public Configuration load()
	{
        return LanguageTag.of(getConfiguredLocale())
				.map(this::load)
				.orElse(null);
    }


	/**
	 * Load the language configuration object for the configured language from file and return it. The returned
	 * configuration object contains no default values loaded, by design
	 *
	 * @return {@link Configuration} containing the configuration loaded from the language file
	 */
	@Override
	public Configuration load(final LanguageTag languageTag)
	{
		YamlConfiguration configuration = new YamlConfiguration();
		File languageFile = new File(plugin.getDataFolder(), LanguageResourceManager.getFileName(languageTag));

		try
		{
			configuration.load(languageFile);
			plugin.getLogger().info("Language file " + languageFile + " successfully loaded.");
		}

		catch (FileNotFoundException e)
		{
			plugin.getLogger().severe("Language file " + languageFile + " does not exist.");
		}

		catch (IOException e)
		{
			plugin.getLogger().severe("Language file " + languageFile + " could not be read.");
		}

		catch (InvalidConfigurationException e)
		{
			plugin.getLogger().severe("Language file " + languageFile + " is not valid yaml.");
		}

		return configuration;
	}

}
