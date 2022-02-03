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

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;


/**
 * Install localization files from language directory in jar archive to language subdirectory
 * of the plugin data directory. Any files listed in auto_install.txt file with a .yml suffix
 * that are stored as a resource within a /language subdirectory in the plugin jar archive will be
 * copied to the /language subdirectory of the plugin data directory.
 */
final class YamlFileInstaller {

	private final JavaPlugin plugin;

	YamlFileInstaller(final JavaPlugin plugin) {
		this.plugin = plugin;
	}


	/**
	 * Get collection of resource filenames from text file in language directory of plugin jar
	 *
	 * @return Set of filename strings
	 */
	Collection<String> getResourceFilenames() {

		// auto install file path
		String resourcePath = "language/auto_install.txt";

		// use linked hash set to preserve order while eliminating duplicates
		Collection<String> filenames = new LinkedHashSet<>();

		try {
			// read file names to be installed from text file
			Scanner scan = new Scanner(Objects.requireNonNull(plugin.getResource(resourcePath)));
			while (scan.hasNextLine()) {
				String line = scan.nextLine().trim();
				if (!line.startsWith("#") && line.endsWith(".yml")) {
					filenames.add(line);
				}
			}
		}
		catch (NullPointerException e) {
			// log resource not found error
			plugin.getLogger().warning("resource '" + resourcePath + "' does not exist in jar archive!");
		}

		return filenames;
	}


	/**
	 * Install files from plugin jar to plugin data directory
	 *
	 * @param fileList collection of filenames to be installed
	 */
	void installResourceFiles(final Collection<String> fileList) {

		// iterate over list of language files and install from jar if not already present
		for (String filename : fileList) {

			// get full resource path
			String resourcePath = "language/" + filename;

			// check file exists in jar archive
			if (plugin.getResource(resourcePath) != null) {

				// this check prevents a warning message when files are already installed
				if (new File(plugin.getDataFolder() + File.separator + "language" + File.separator + filename).exists()) {
					continue;
				}

				// save resource to plugin data directory
				plugin.saveResource(resourcePath, false);

				// log successful install message
				plugin.getLogger().info("Installed localization file: " + resourcePath);
			}
			else {
				// log resource not found error
				plugin.getLogger().warning("resource '" + resourcePath + "' does not exist in jar archive!");
			}
		}
	}

}
