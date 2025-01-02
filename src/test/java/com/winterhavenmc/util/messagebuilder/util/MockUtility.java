/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.util;

import com.winterhavenmc.util.messagebuilder.languages.YamlLanguageFileInstaller;
import com.winterhavenmc.util.messagebuilder.languages.YamlLanguageFileLoader;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class MockUtility {

	private static final Logger LOGGER = Logger.getLogger(MockUtility.class.getName());

	public final static String LANGUAGE_EN_US_YML = "language/en-US.yml";
	public final static String AUTO_INSTALL_TXT = "language/auto_install.txt";

	private static File tempDataDir;


	/**
	 * Private constructor to prevent instantiation
	 */
	private MockUtility() {	}


	/**
	 * Gets or creates the temporary data directory for testing.
	 *
	 * @return the temporary data directory as a {@link File}
	 */
	public static File getDataDir() {
		if (tempDataDir == null) {
			tempDataDir = createTempDataDir();
			if (tempDataDir.isDirectory()) {
				LOGGER.info("Temporary data directory successfully created!");
			}
		}
		return tempDataDir;
	}


	/**
	 * Installs a resource file from the classpath to the temporary data directory.
	 *
	 * @param resourceName the name of the resource file in the classpath
	 * @return {@code true} if the resource was successfully copied, {@code false} otherwise
	 * @throws IOException if an error occurs during the file operation or if the resource cannot be found
	 */
	public static boolean installResource(final String resourceName) throws IOException {
		Path targetDirPath = getDataDir().toPath();
		return installResource(resourceName, targetDirPath);
	}


	/**
	 * Installs a resource file from the classpath to the specified target directory.
	 *
	 * @param resourceName  the name of the resource file in the classpath
	 * @param targetDirPath the target directory where the file should be installed
	 * @return {@code true} if the resource was successfully copied, {@code false} otherwise
	 * @throws IOException if an error occurs during the file operation or if the resource cannot be found
	 */
	public static boolean installResource(final String resourceName, final Path targetDirPath) throws IOException {
		if (resourceName == null) { throw new IllegalArgumentException(Error.Parameter.NULL_RESOURCE_NAME.getMessage()); }
		if (resourceName.isEmpty()) { throw new IllegalArgumentException(Error.Parameter.EMPTY_RESOURCE_NAME.getMessage()); }
		if (targetDirPath == null) { throw new IllegalArgumentException(Error.Parameter.NULL_DIRECTORY_PATH.getMessage()); }

		// Ensure the target directory exists
		Files.createDirectories(targetDirPath);

		// Get the resource as an InputStream
		try (var inputStream = getResourceStream(resourceName)) {
			if (inputStream == null) {
				throw new IOException("Resource '" + resourceName + "' not found in the classpath.");
			}

			// Resolve the full path to the target file
			Path targetFilePath = targetDirPath.resolve(resourceName);

			// create subdirectories
			Files.createDirectories(targetFilePath.getParent());

			// Copy the resource to the target directory
			Files.copy(inputStream, targetFilePath); // DO NOT REPLACE EXISTING FILES
			return true;
		}
	}


	/**
	 * Retrieves an InputStream for the specified resource from the classpath.
	 *
	 * @param resourceName the name of the resource file
	 * @return an InputStream for the resource, or {@code null} if the resource cannot be found
	 */
	public static InputStream getResourceStream(final String resourceName) {
		return MockUtility.class.getClassLoader().getResourceAsStream(resourceName);
	}


	/**
	 * Creates a temporary data directory for testing.
	 *
	 * @return the created directory as a {@link File}
	 */
	private static File createTempDataDir() {
		try {
			Path tempDir = Files.createTempDirectory("getQueryHandler-data");
			// tempDir.toFile().deleteOnExit();
			tempDir.toFile();
			return tempDir.toFile();
		} catch (IOException e) {
			throw new IllegalStateException("Failed to create temporary data directory.", e);
		}
	}


	/**
	 * Get a resource as a File object for the named resource
	 *
	 * @param name the name of the resource to return as a file object
	 * @return a file object for the named resource
	 * @throws URISyntaxException Let's hope it doesn't.
	 */
	public static File getResourceFile(final String name) throws URISyntaxException {
		return Paths.get(Thread.currentThread().getContextClassLoader().getResource(name).toURI()).toAbsolutePath().toFile();
	}


	/**
	 * Creates a mock Plugin instance with a logger and optional configuration.
	 *
	 * @param configValues a map of configuration keys and values, or null for no configuration
	 * @return a mock Plugin instance
	 */
	public static Plugin createMockPlugin(Map<String, Object> configValues) {
		Plugin mockPlugin = mock(Plugin.class, "MockPlugin");

		// Mock the plugin logger
		Logger mockLogger = Logger.getLogger("MockPluginLogger");
		when(mockPlugin.getLogger()).thenReturn(mockLogger);

		// Mock the configuration
		FileConfiguration mockConfig = mock(FileConfiguration.class, "MockPluginConfig");
		if (configValues != null) {
			configValues.forEach((key, value) -> when(mockConfig.get(key)).thenReturn(value));
		}
		when(mockPlugin.getConfig()).thenReturn(mockConfig);

		// return real file input streams for mock plugin resources
		doAnswer(invocation -> getResourceStream(invocation.getArgument(0)))
				.when(mockPlugin).getResource(anyString());

		return mockPlugin;
	}


	/**
	 * Creates a mock Plugin instance with a logger and optional configuration.
	 *
	 * @return a mock Plugin instance
	 */
	public static Plugin createMockPlugin() {

		Plugin mockPlugin = mock(Plugin.class, "MockPlugin");

		// Mock the plugin logger
		Logger mockLogger = Logger.getLogger("MockPluginLogger");
		when(mockPlugin.getLogger()).thenReturn(mockLogger);

		// return a temporary data folder
		when(mockPlugin.getDataFolder()).thenReturn(MockUtility.getDataDir());

		// create a real pluginConfig
		FileConfiguration pluginConfig = new YamlConfiguration();
		pluginConfig.set("locale", "en-US");
		pluginConfig.set("language", "en-US");

		// create a real default configuration
		Configuration defaultConfiguration = new MemoryConfiguration();
		pluginConfig.set("locale", "en-US");
		pluginConfig.set("language", "en-US");

		// set the pluginConfig defaults to defaultConfiguration
		pluginConfig.addDefaults(defaultConfiguration);

		// return pluginConfig for plugin.getConfig()
		when(mockPlugin.getConfig()).thenReturn(pluginConfig);

//		// Mock the configuration
//		FileConfiguration mockPluginConfig = mock(FileConfiguration.class, "MockPluginConfig");
//		when(mockPlugin.getConfig()).thenReturn(mockPluginConfig);
//		when(mockPluginConfig.getString("locale")).thenReturn("en-US");
//		when(mockPluginConfig.getString("language")).thenReturn("en-US");
//		when(mockPluginConfig.getString("locale", "en-US")).thenReturn("en-US");
//		when(mockPluginConfig.getString("language", "en-US")).thenReturn("en-US");

		// return real file input streams for mock plugin resources
		doAnswer(invocation -> getResourceStream(invocation.getArgument(0)))
				.when(mockPlugin).getResource(anyString());

		// install resource when saveResource is called
		doAnswer(invocation -> installResource(invocation.getArgument(0), tempDataDir.toPath()))
				.when(mockPlugin).saveResource(anyString(), eq(false));

		return mockPlugin;
	}


	/**
	 * Creates a mock YamlLanguageFileInstaller instance for testing
	 *
	 * @return a mock YamlLanguageFileInstaller instance
	 */
	public static YamlLanguageFileInstaller createMockFileInstaller() {
		YamlLanguageFileInstaller mockFileInstaller = mock(YamlLanguageFileInstaller.class, "MockFileInstaller");
		when(mockFileInstaller.install()).thenReturn(1);
		return mockFileInstaller;
	}


	/**
	 * Creates a mock World instance for testing
	 *
	 * @param worldName the world name to be returned by the {@code getName()} method
	 * @param worldUid  the world UUID to be returned by the {@code getUID()} method
	 * @return a mock World instance
	 */
	public static World createMockWorld(final String worldName, final UUID worldUid) {
		World mockWorld = mock(World.class, "MockWorld");
		when(mockWorld.getName()).thenReturn(worldName);
		when(mockWorld.getUID()).thenReturn(worldUid);
		return mockWorld;
	}


	/**
	 * Creates a mock Player instance for testing
	 *
	 * @param playerName the player name to be returned by the {@code getName()} method
	 * @param playerUid  the player UUID to be returned by the {@code getUID()} method
	 * @return a mock Player instance
	 */
	public static Player createMockPlayer(final String playerName, final UUID playerUid) {
		Player mockPlayer = mock(Player.class, "MockPlayer");
		when(mockPlayer.getName()).thenReturn(playerName);
		when(mockPlayer.getUniqueId()).thenReturn(playerUid);
		return mockPlayer;
	}


	/**
	 * Creates a mock YamlLanguageFileLoader instance for testing
	 *
	 * @return a mock YamlLanguageFileLoader instance
	 */
	public static YamlLanguageFileLoader createMockLanguageFileLoader() {
		YamlLanguageFileLoader mockLanguageFileLoader = mock(YamlLanguageFileLoader.class, "MockLanguageFileLoader");
		when(mockLanguageFileLoader.reload()).thenReturn(MockUtility.loadConfigurationFromResource("language/en-US.yml"));
		when(mockLanguageFileLoader.getConfiguration()).thenReturn(MockUtility.loadConfigurationFromResource("language/en-US.yml"));
		return mockLanguageFileLoader;
	}

	/**
	 * Loads a {@link FileConfiguration} from a YAML resource without performing any real file I/O.
	 * This method is intended for testing and mocking purposes, providing a strict boundary where
	 * higher-level classes only work with {@link FileConfiguration} objects.
	 *
	 * @param resourcePath the path to the YAML resource file, relative to the getQueryHandler classpath
	 * @return a {@link FileConfiguration} object containing the parsed YAML data
	 * @throws IllegalArgumentException if the resource cannot be found or read
	 */
	public static FileConfiguration loadConfigurationFromResource(String resourcePath) {
		InputStream resourceStream = getResourceStream(resourcePath);

		if (resourceStream == null) {
			throw new IllegalArgumentException("Resource not found: " + resourcePath);
		}

		// Load the YAML data directly into a FileConfiguration object
		return YamlConfiguration.loadConfiguration(new InputStreamReader(resourceStream));
	}

}
