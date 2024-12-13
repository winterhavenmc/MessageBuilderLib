/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;


/**
 * Install localization files from language directory in jar archive to language subdirectory
 * of the plugin data directory. Any files listed in auto_install.txt file with a .yml suffix
 * that are stored as a resource within a /language subdirectory in the plugin jar archive will be
 * copied to the /language subdirectory of the plugin data directory.
 */
final class YamlFileInstaller {

	private final Plugin plugin;
	private final static String subdirectory = "language";
	private final static String autoInstallFilename = "auto_install.txt";
	private final static String autoInstallFullPathname = subdirectory + "/" + autoInstallFilename;


	/**
	 * Class constructor
	 * @param plugin reference to plugin main class
	 */
	YamlFileInstaller(final Plugin plugin) {
		this.plugin = plugin;
	}


	/**
	 * Install resource files from plugin jar to plugin data directory
	 */
	void install() {
		// iterate over list of language files and install from jar if not already present
		for (String filename : getAutoInstallFilenames()) {
			install(filename);
		}
	}


	/**
	 * Install resource from plugin jar to plugin data directory
	 * @param filename {@code String} the filename of the resource to be installed
	 */
	void install(final String filename) {

		// get full resource path
		String resourcePath = subdirectory + "/" + filename;

		// check that resource exists in jar archive
		if (plugin.getResource(resourcePath) != null) {

			// this check prevents a warning message when files are already installed
			if (!verifyResourceInstalled(filename)) {
				// save resource to plugin data directory
				plugin.saveResource(resourcePath, false);

				// log successful install message
				plugin.getLogger().info("Installed localization file: " + resourcePath);
			}
		}
		else {
			// log resource not found error
			plugin.getLogger().warning("resource '" + resourcePath + "' does not exist in jar archive!");
		}

	}


	/**
	 * Get collection of resource filenames from text file in language directory of plugin jar
	 *
	 * @return Set of filename strings
	 */
	Collection<String> getAutoInstallFilenames() {

		// use linked hash set to preserve order while eliminating duplicates
		Collection<String> filenames = new LinkedHashSet<>();

		try {
			// read file names to be installed from text file
			Scanner scan = new Scanner(Objects.requireNonNull(plugin.getResource(autoInstallFullPathname)));
			while (scan.hasNextLine()) {
				String line = scan.nextLine().strip();
				// exclude comments and lines not ending with .yml
				if (!line.startsWith("#") && line.endsWith(".yml")) {
					filenames.add(line);
				}
			}
		}
		catch (NullPointerException e) {
			// log resource not found error if language/auto_install.txt is not present in jar archive
			plugin.getLogger().warning("resource '" + autoInstallFullPathname + "' does not exist in jar archive!");
		}

		return filenames;
	}


	/**
	 * Test if language/auto_install.txt file resource is embedded in jar
	 * @return boolean {@code true} if embedded auto install resource file exists, {@code false} if it does not
	 */
	boolean verifyResourceExists(final String filename) {
		return this.plugin.getResource(filename) != null;
	}


	/**
	 * Test if resource is installed in language subdirectory within plugin data directory
 	 * @param filename the filename to verify
	 * @return boolean true if a file with the filename exists in the plugin data directory, false if not
	 */
	boolean verifyResourceInstalled(final String filename) {
		return new File(plugin.getDataFolder() + File.separator + subdirectory + File.separator + filename).exists();
	}

}
