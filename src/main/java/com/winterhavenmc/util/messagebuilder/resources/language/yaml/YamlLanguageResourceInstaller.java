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

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageSetting.RESOURCE_AUTO_INSTALL;
import static com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageSetting.RESOURCE_SUBDIRECTORY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.validation.MessageKey.PARAMETER_NULL;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.LANGUAGE_TAG;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RESOURCE_NAME;
import static com.winterhavenmc.util.messagebuilder.validation.Validator.validate;


/**
 * This class is responsible for installing localization resources from the language directory of the jar archive,
 * to language subdirectory of the plugin data directory.
 * Any files listed in auto_install.txt file with a .yml suffix that are stored as a resource within a /language
 * subdirectory in the plugin jar archive will be copied to the /language subdirectory of the plugin data directory.
 */
public final class YamlLanguageResourceInstaller
{
	final static Pattern WHITESPACE = Pattern.compile("\\s", Pattern.UNICODE_CHARACTER_CLASS); // match Unicode whitespace
	final static Pattern TWO_OR_MORE_DOTS = Pattern.compile("[.]{2,}");
	final static Pattern LEADING_SLASHES = Pattern.compile("^/+}");
	final static Pattern TWO_OR_MORE_SLASHES = Pattern.compile("\"/{2,}\"");

	private final Plugin plugin;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public YamlLanguageResourceInstaller(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	/**
	 * Retrieve {@link Set} of resource filenames from a plain text resource in the language directory of the plugin jar.
	 * The plain text resource name elements are currently set in the YamlLanguageSetting enum. The valid pathname for the resource
	 * can be retrieved using the getAutoInstallResourcePath method of this class
	 *
	 * @param autoInstallPathName a {@code String} containing the resource path of the auto install plain text resource
	 * @return Set of filename strings
	 */
	Set<String> getAutoInstallResourceNames(String autoInstallPathName)
	{
		// get input stream for resource
		InputStream resourceInputStream = plugin.getResource(autoInstallPathName);

		// if resource is null return empty set
		if (resourceInputStream == null)
		{
			return Collections.emptySet();
		}

		// use linked hash set to preserve order while eliminating duplicates
		Set<String> resourcePathNames = new LinkedHashSet<>();

		// read file names to be installed from text file
		Scanner scan = new Scanner(resourceInputStream);
		while (scan.hasNextLine())
		{
			String line = scan.nextLine().strip();
			// include only lines that start with the resource subdirectory name and end with ".yml"
			if (line.startsWith(RESOURCE_SUBDIRECTORY + "/") && line.endsWith(".yml")) {
				// further sanitize resource path names and add them to return list
				resourcePathNames.add(sanitizeResourcePath(line));
			}
		}

		return resourcePathNames;
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
	String sanitizeResourcePath(final String resourcePath)
	{
		// strip leading/trailing spaces; strip 2 or more consecutive dots; strip one or more leading slashes
		return resourcePath
				.replaceAll(WHITESPACE.pattern(), "")
				.replaceAll(TWO_OR_MORE_DOTS.pattern(), "")
				.replaceFirst(LEADING_SLASHES.pattern(), "")
				.replaceAll(TWO_OR_MORE_SLASHES.pattern(), "/");
	}


	/**
	 * Install resources listed in auto_install.txt to the plugin data directory
	 */
	void autoInstall()
	{
		for (String resourceName : getAutoInstallResourceNames(getAutoInstallResourcePath()))
		{
			installByName(resourceName);
		}
	}


	String getAutoInstallResourcePath()
	{
		return String.join("/", RESOURCE_SUBDIRECTORY.toString(), RESOURCE_AUTO_INSTALL.toString());
	}


	/**
	 * Install a resource for the given language tag if not already installed in the plugin data directory
	 *
	 * @param languageTag the language tag for the resource to be installed
	 */
	InstallerStatus installIfMissing(final LanguageTag languageTag)
	{
		validate(languageTag, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, LANGUAGE_TAG));

		if (!isInstalledForTag(languageTag))
		{
			return installByName(languageTag.getResourceName());
		}
		return InstallerStatus.FILE_EXISTS;
	}


	/**
	 * Install resource from plugin jar to plugin data directory
	 *
	 * @param resourceName {@code String} the path name of the resource to be installed
	 * @return a {@code Boolean} indicating the success or failure result of the resource installation
	 */
	InstallerStatus installByName(final String resourceName)
	{
		validate(resourceName, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, RESOURCE_NAME));
		validate(resourceName, String::isBlank, () -> new ValidationException(PARAMETER_EMPTY, RESOURCE_NAME));

		if (plugin.getResource(resourceName) == null)
		{
			plugin.getLogger().warning("The resource '" + resourceName
					+ "' listed in the 'auto_install.txt' file could not be found by the installer.");
			return InstallerStatus.UNAVAILABLE;
		}

		// this check prevents a warning message when files are already installed
		// I believe the message is generated by the plugin.saveResource() method, and not the OS
		// A non-destructive, silent file copy method would be preferred
		if (!isInstalled(resourceName)) {

			// save resource to plugin data directory
			plugin.saveResource(resourceName, false);

			// convert resource name to filename
			String resourceFileName = resourceName.replace("/", File.separator);

			// log successful install message if file exists
			if (new File(plugin.getDataFolder(), resourceFileName).exists()) {
				plugin.getLogger().info("Installation of '" + resourceFileName + "' confirmed.");
				return InstallerStatus.SUCCESS;
			}
			else {
				plugin.getLogger().severe("installation failed!" +
						" The file " + resourceFileName + " does not exist in the plugin data directory " +
						"after the install procedure has finished.");
				return  InstallerStatus.FAIL;
			}
		}
		return InstallerStatus.FILE_EXISTS;
	}


	/**
	 * Install resource from plugin jar to plugin data directory
	 *
	 * @param languageTag {@code String} the path name of the resource to be installed
	 * @return a {@code Boolean} indicating the success or failure result of the resource installation
	 */
	InstallerStatus install(final LanguageTag languageTag)
	{
		validate(languageTag, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, LANGUAGE_TAG));

		return installByName(languageTag.getResourceName());
	}


	/**
	 * Check if a resource exists
	 *
	 * @param resourceName the name of the resource
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	boolean resourceExists(final String resourceName)
	{
		return plugin.getResource(resourceName) != null;
	}


	/**
	 * Check if a resource exists
	 *
	 * @param languageTag the tag of the resource being checked for existence in the classpath
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	boolean resourceExists(final LanguageTag languageTag)
	{
		validate(languageTag, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, LANGUAGE_TAG));

		return plugin.getResource(languageTag.getResourceName()) != null;
	}


	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param filename the name of the file being verified
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean isInstalled(final String filename)
	{
		return new File(plugin.getDataFolder(), filename).exists();
	}

	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param languageTag the language tag of the file being verified as installed in the plugin data directory
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean isInstalledForTag(final LanguageTag languageTag)
	{
		validate(languageTag, Objects::isNull, () -> new ValidationException(PARAMETER_NULL, LANGUAGE_TAG));

		return new File(plugin.getDataFolder(), languageTag.getFileName()).exists();
	}

	enum InstallerStatus
	{
		UNAVAILABLE,
		FILE_EXISTS,
		SUCCESS,
		FAIL,
	}
}
