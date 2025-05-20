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

package com.winterhavenmc.library.messagebuilder.resources.language;

import com.winterhavenmc.library.messagebuilder.resources.ResourceInstaller;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LanguageTag;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import static com.winterhavenmc.library.messagebuilder.resources.language.LanguageSetting.RESOURCE_AUTO_INSTALL;
import static com.winterhavenmc.library.messagebuilder.resources.language.LanguageSetting.RESOURCE_SUBDIRECTORY;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.library.messagebuilder.validation.ErrorMessageKey.PARAMETER_NULL;
import static com.winterhavenmc.library.messagebuilder.validation.Parameter.RESOURCE_NAME;
import static com.winterhavenmc.library.messagebuilder.validation.ValidationHandler.throwing;
import static com.winterhavenmc.library.messagebuilder.validation.Validator.validate;


/**
 * This class is responsible for installing localization resources from the language directory of the jar archive,
 * to the language subdirectory of the plugin data directory.
 * Any files listed in auto_install.txt file with a .yml suffix that are stored as a resource within a /language
 * subdirectory in the plugin jar archive will be copied to the /language subdirectory of the plugin data directory.
 */
public final class LanguageResourceInstaller implements ResourceInstaller
{
	static final Pattern WHITESPACE = Pattern.compile("\\s", Pattern.UNICODE_CHARACTER_CLASS);
	static final Pattern TWO_OR_MORE_DOTS = Pattern.compile("[.]{2,}");
	static final Pattern LEADING_SLASHES = Pattern.compile("^/+");
	static final Pattern TWO_OR_MORE_SLASHES = Pattern.compile("/{2,}");
	private static final String YAML_EXTENSION = ".yml";

	private final Plugin plugin;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public LanguageResourceInstaller(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Install resources listed in auto_install.txt to the plugin data directory
	 */
	@Override
	public void autoInstall()
	{
		Set<String> resourceNames = getAutoInstallResourceNames(getAutoInstallResourcePath());
		for (String resource : resourceNames)
		{
			installByName(resource);
		}
	}


	@Override
	public InstallerStatus installIfMissing(final LanguageTag tag)
	{
		if (!isInstalledForTag(tag))
		{
			return installByName(LanguageResourceManager.getResourceName(tag));
		}
		return InstallerStatus.FILE_EXISTS;
	}


	/**
	 * Install a resource for the given language tag if not already installed in the plugin data directory
	 *
	 * @param languageTag the language tag for the resource to be installed
	 */
	@Override
	public InstallerStatus install(final LanguageTag languageTag)
	{
		return installByName(LanguageResourceManager.getResourceName(languageTag));
	}


	/**
	 * Install resource from plugin jar to plugin data directory
	 *
	 * @param resourceName {@code String} the path name of the resource to be installed
	 * @return a {@code Boolean} indicating the success or failure result of the resource installation
	 */
	@Override
	public InstallerStatus installByName(final String resourceName)
	{
		validate(resourceName, Objects::isNull, throwing(PARAMETER_NULL, RESOURCE_NAME));
		validate(resourceName, String::isBlank, throwing(PARAMETER_EMPTY, RESOURCE_NAME));

		if (plugin.getResource(resourceName) == null)
		{
			plugin.getLogger().warning("[MessageBuilder] Resource not found in plugin JAR: " + resourceName);
			return InstallerStatus.UNAVAILABLE;
		}

		if (isInstalled(resourceName))
		{
			return InstallerStatus.FILE_EXISTS;
		}

		try
		{
			plugin.saveResource(resourceName, false); // Never overwrite
			String filePath = resourceName.replace("/", File.separator);
			File installedFile = new File(plugin.getDataFolder(), filePath);

			if (installedFile.exists())
			{
				plugin.getLogger().info("[MessageBuilder] Installed resource: " + filePath);
				return InstallerStatus.SUCCESS;
			} else
			{
				plugin.getLogger().severe("[MessageBuilder] Installation failed. File missing after save: " + filePath);
				return InstallerStatus.FAIL;
			}
		} catch (Exception e)
		{
			plugin.getLogger().severe("[MessageBuilder] Exception during installation of " + resourceName + ": " + e.getMessage());
			return InstallerStatus.FAIL;
		}
	}


	@Override
	public Set<String> getAutoInstallResourceNames(final String autoInstallPathName)
	{
		InputStream input = plugin.getResource(autoInstallPathName);
		if (input == null)
		{
			plugin.getLogger().warning("[MessageBuilder] auto-install.txt not found at " + autoInstallPathName);
			return Collections.emptySet();
		}

		Set<String> result = new LinkedHashSet<>();
		try (Scanner scanner = new Scanner(input))
		{
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine().strip();
				if (line.startsWith(RESOURCE_SUBDIRECTORY + "/") && line.endsWith(".yml"))
				{
					result.add(sanitizeResourcePath(line));
				}
			}
		}
		return result;
	}


	@Override
	public String getAutoInstallResourcePath()
	{
		return String.join("/", RESOURCE_SUBDIRECTORY.toString(), RESOURCE_AUTO_INSTALL.toString());
	}


	/**
	 * Check if a resource exists
	 *
	 * @param resourceName the name of the resource
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	@Override
	public boolean resourceExists(final String resourceName)
	{
		return plugin.getResource(resourceName) != null;
	}


	/**
	 * Check if a resource exists
	 *
	 * @param languageTag the tag of the resource being checked for existence in the classpath
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	@Override
	public boolean resourceExists(final LanguageTag languageTag)
	{
		return plugin.getResource(LanguageResourceManager.getResourceName(languageTag)) != null;
	}


	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param filename the name of the file being verified
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	@Override
	public boolean isInstalled(final String filename)
	{
		return new File(plugin.getDataFolder(), filename).exists();
	}


	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param languageTag the language tag of the file being verified as installed in the plugin data directory
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	@Override
	public boolean isInstalledForTag(final LanguageTag languageTag)
	{
		return new File(plugin.getDataFolder(), LanguageResourceManager.getFileName(languageTag)).exists();
	}


	/**
	 * sanitize resource path file paths. these are ultimately set by server operators,
	 * and may be running on servers not owned by them. It is essential that filenames
	 * in this list of files to be copied not contain the names of system resources outside
	 * the plugin's data directory.
	 * <p>
	 *     Logic:
	 *     <ol>
	 *     <li>any whitespace characters are to be removed</li>
	 *     <li>any occurrences of two or more consecutive periods (.) are to be removed</li>
	 *     <li>any number of leading slashes (/) are to be removed</li>
	 *     <li>Any occurrence of two or more slashes (/) are to be replaced with a single slash</li>
	 *     </ol>
	 *
	 *
	 * @param resourcePath the {@code String} path name to be sanitized.
	 */
	@Override
	public String sanitizeResourcePath(final String resourcePath)
	{
		return resourcePath
				.replaceAll(WHITESPACE.pattern(), "")
				.replaceAll(TWO_OR_MORE_DOTS.pattern(), "")
				.replaceFirst(LEADING_SLASHES.pattern(), "")
				.replaceAll(TWO_OR_MORE_SLASHES.pattern(), "/");
	}

}
