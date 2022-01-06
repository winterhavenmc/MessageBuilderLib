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
class YamlFileInstaller {

	private final JavaPlugin plugin;

	YamlFileInstaller(final JavaPlugin plugin) {
		this.plugin = plugin;
	}


	/**
	 * Get set of resource filenames from language directory of plugin jar
	 *
	 * @return Set of filename strings
	 */
	List<String> getFilenames() {

		List<String> fileList = new ArrayList<>();

		try {
			Scanner scan = new Scanner(Objects.requireNonNull(plugin.getResource("language/auto_install.txt")));
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				if (!line.startsWith("#") && line.endsWith(".yml")) {
					fileList.add(line);
				}
			}
		}
		catch (NullPointerException e) {
			plugin.getLogger().warning("language/auto_install.txt file not found in jar.");
		}

		return fileList;
	}


	/**
	 * Install files from plugin jar to plugin data directory
	 *
	 * @param fileList list of filenames to be installed
	 */
	void installFiles(final List<String> fileList) {

		// iterate over list of language files and install from jar if not already present
		for (String filename : fileList) {
			// this check prevents a warning message when files are already installed
			if (new File(plugin.getDataFolder() + File.separator + "language" + File.separator + filename).exists()) {
				continue;
			}
			plugin.saveResource("language/" + filename, false);
			plugin.getLogger().info("Installed localization file: " + filename);
		}
	}

}
