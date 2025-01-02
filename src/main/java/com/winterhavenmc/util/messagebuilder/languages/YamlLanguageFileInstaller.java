/*
 * Copyright (c) 2022-2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.languages;

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
public class YamlLanguageFileInstaller implements LanguageFileInstaller {

	private final static String AUTO_INSTALL_TXT = "language/auto_install.txt";

	private final Plugin plugin;


	/**
	 * Class constructor
	 * @param plugin reference to plugin main class
	 */
	public YamlLanguageFileInstaller(final Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * package-private getter for auto install filename constant (for testing)
	 * @return the auto install filename
	 */
	public String getAutoInstallFilename() {
		return AUTO_INSTALL_TXT;
	}

	/**
	 * Install resource files from plugin jar to plugin data directory
	 *
	 * @return {@code int} the number of files installed
	 */
	@Override
	public int install() {
		int count = 0;
		// iterate over list of language files and install from jar if not already present
		for (String filename : getAutoInstallFilenames()) {
			install(filename);
			count++;
		}
		return count;
	}


	/**
	 * Install resource from plugin jar to plugin data directory
	 * @param resourceName {@code String} the path name of the resource to be installed
	 */
	private void install(final String resourceName) {

		if (plugin.getResource(resourceName) == null) {
			plugin.getLogger().warning("The resource '" + resourceName
					+ "' listed in the 'auto_install.txt' file could not be found by the installer.");
			return;
		}

		// this check prevents a warning message when files are already installed
		if (!verifyResourceInstalled(resourceName)) {

			// save resource to plugin data directory
			plugin.saveResource(resourceName, false);

			// log successful install message if file exists
			if (new File(plugin.getDataFolder(), resourceName).exists()) {
				plugin.getLogger().info("Installation of '" + resourceName + "' confirmed.");
			}
			else {
				plugin.getLogger().severe("installation of '" + resourceName + "' failed!");
			}
		}
	}


	/**
	 * Get collection of resource filenames from text file in language directory of plugin jar
	 *
	 * @return Set of filename strings
	 */
	Set<String> getAutoInstallFilenames() {

		// get input stream for resource
		InputStream resourceInputStream = plugin.getResource(AUTO_INSTALL_TXT);

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
	 * Test if a resource exists
	 *
	 * @param resourceName the name of the resource
	 * @return {@code true} if the resource exists, {@code false} if it does not
	 */
	boolean resourceExists(final String resourceName) {
		return plugin.getResource(resourceName) != null;
	}


	/**
	 * Test if resource is installed in the plugin data directory
	 *
 	 * @param filename the name of the file being verified
	 * @return {@code true} if a file with the filename exists in the plugin data directory, {@code false} if not
	 */
	boolean verifyResourceInstalled(final String filename) {
		return new File(plugin.getDataFolder(), filename).exists();
	}

}
