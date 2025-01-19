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
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.util.*;


/**
 * Install localization files from language directory in jar archive to language subdirectory
 * of the plugin data directory. Any files listed in auto_install.txt file with a .yml suffix
 * that are stored as a resource within a /language subdirectory in the plugin jar archive will be
 * copied to the /language subdirectory of the plugin data directory.
 */
public final class YamlLanguageResourceInstaller {

	// THIS IS THE OFFICIAL DECLARATION OF THE LANGUAGE SUBDIRECTORY IN RESOURCES AND THE PLUGIN DATA DIRECTORY
	final static String SUBDIRECTORY = "language";
	private final static String AUTO_INSTALL_TXT = SUBDIRECTORY + "/auto_install.txt";

	private final Plugin plugin;


	/**
	 * Class constructor
	 *
	 * @param plugin reference to plugin main class
	 */
	public YamlLanguageResourceInstaller(final Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * package-private getter for auto install filename constant (for testing)
	 *
	 * @return the auto install filename
	 */
	String getAutoInstallResourceName() {
		return AUTO_INSTALL_TXT;
	}


	/**
	 * Get collection of resource filenames from text file in language directory of plugin jar
	 *
	 * @return Set of filename strings
	 */
	Set<String> readResourceNames(String resource) {

		// get input stream for resource
		InputStream resourceInputStream = plugin.getResource(resource);

		// if resource is null return empty set
		if (resourceInputStream == null) {
			return Collections.emptySet();
		}

		// use linked hash set to preserve order while eliminating duplicates
		Set<String> filenames = new LinkedHashSet<>();

		// read file names to be installed from text file
		Scanner scan = new Scanner(resourceInputStream);
		while (scan.hasNextLine()) {
			String line = scan.nextLine().strip();
			// exclude comments and lines not ending with .yml
			if (!line.startsWith("#") && line.endsWith(".yml")) {
				filenames.add(line);
			}
		}

		return filenames;
	}


	/**
	 * Install resources listed in auto_install.txt to the plugin data directory
	 *
	 * @return Map with filenames listed in auto_install.txt as key, and an InstallerStatus enum constant status for value
	 */
	Map<String, InstallerStatus> autoInstall() {
		Map<String, InstallerStatus> resultMap = new LinkedHashMap<>();
		for (String resourceName : readResourceNames(AUTO_INSTALL_TXT)) {
			resultMap.put(resourceName, installByName(resourceName));
		}
		return resultMap;
	}


	/**
	 * Install a resource for the given language tag if not already installed in the plugin data directory
	 *
	 * @param languageTag the language tag for the resource to be installed
	 */
	InstallerStatus installIfMissing(final String languageTag) {
		Resource resource = new Resource(languageTag);

		if (!isInstalledForTag(languageTag)) {
			return installByName(resource.getName());
		}
		return InstallerStatus.FILE_EXISTS;
	}


	/**
	 * Install resource from plugin jar to plugin data directory
	 *
	 * @param resourceName {@code String} the path name of the resource to be installed
	 * @return a {@code Boolean} indicating the success or failure result of the resource installation
	 */
	InstallerStatus installByName(final String resourceName) {
		if (resourceName == null) { throw new IllegalArgumentException(Error.Parameter.NULL_RESOURCE_NAME.getMessage()); }
		if (plugin.getResource(resourceName) == null) {
			plugin.getLogger().warning("The resource '" + resourceName
					+ "' listed in the 'auto_install.txt' file could not be found by the installer.");
			return InstallerStatus.UNAVAILABLE;
		}

		// this check prevents a warning message when files are already installed TODO: there might be a way to do this silently
		if (!isInstalled(resourceName)) {

			// save resource to plugin data directory
			plugin.saveResource(resourceName, false);

			// log successful install message if file exists
			if (new File(plugin.getDataFolder(), resourceName).exists()) {
				plugin.getLogger().info("Installation of '" + resourceName + "' confirmed.");
				return InstallerStatus.SUCCESS;
			}
			else {
				plugin.getLogger().severe("installation of '" + resourceName + "' failed!");
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
	InstallerStatus installForTag(final String languageTag) {
		if (languageTag == null) { throw new IllegalArgumentException(Error.Parameter.NULL_LANGUAGE_TAG.getMessage()); }

		Resource resource = new Resource(languageTag);

		if (plugin.getResource(resource.getName()) == null) {
			plugin.getLogger().warning("The resource '" + resource
					+ "' listed in the 'auto_install.txt' file could not be found by the installer.");
			return InstallerStatus.UNAVAILABLE;
		}

		// this check prevents a warning message when files are already installed TODO: there might be a way to do this silently
		if (!isInstalled(resource.getFileName())) {

			// save resource to plugin data directory
			plugin.saveResource(resource.getName(), false);

			// log successful install message if file exists
			if (new File(plugin.getDataFolder(), resource.getFileName()).exists()) {
				plugin.getLogger().info("Installation of '" + resource + "' confirmed.");
				return InstallerStatus.SUCCESS;
			}
			else {
				plugin.getLogger().severe("installation of '" + resource + "' failed!");
				return  InstallerStatus.FAIL;
			}
		}
		return InstallerStatus.FILE_EXISTS;
	}


	/**
	 * Check if a resource exists
	 *
	 * @param resourceName the name of the resource
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	boolean resourceExists(final String resourceName) {
		return plugin.getResource(resourceName) != null;
	}


	/**
	 * Check if a resource exists
	 *
	 * @param languageTag the tag of the resource being checked for existence in the classpath
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	boolean resourceExistsForTag(final String languageTag) {
		Resource resource = new Resource(languageTag);

		return plugin.getResource(resource.getName()) != null;
	}


	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param filename the name of the file being verified
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean isInstalled(final String filename) {
		return new File(plugin.getDataFolder(), filename).exists();
	}

	/**
	 * Test if resource is installed in the plugin data directory
	 *
	 * @param languageTag the language tag of the file being verified as installed in the plugin data directory
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean isInstalledForTag(final String languageTag) {
		Resource resource = new Resource(languageTag);

		return new File(plugin.getDataFolder(), resource.getFileName()).exists();
	}

}
