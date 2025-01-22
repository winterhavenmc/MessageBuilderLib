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

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.*;

import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_EMPTY;
import static com.winterhavenmc.util.messagebuilder.util.LocalizedException.MessageKey.PARAMETER_NULL;


public final class MockUtility {

	/**
	 * Private constructor to prevent instantiation
	 * @throws InstantiationException if instantiated from within this class
	 */
	private MockUtility() throws InstantiationException { throw new InstantiationException(); }


	/**
	 * Loads a {@link FileConfiguration} from a YAML resource without performing any real file I/O.
	 * This method is intended for testing and mocking purposes, providing a strict boundary where
	 * higher-level classes only work with {@link FileConfiguration} objects.
	 *
	 * @param resourcePath the path to the YAML resource file, relative to the test classpath
	 * @return a {@link FileConfiguration} object containing the parsed YAML data
	 * @throws IllegalArgumentException if the resource cannot be found or read
	 */
	public static FileConfiguration loadConfigurationFromResource(String resourcePath) {
		InputStream resourceStream = getResourceStream(resourcePath);

		if (resourceStream == null) {
			throw new IllegalArgumentException("ResourceType not found: " + resourcePath);
		}

		// Load the YAML data directly into a FileConfiguration object
		return YamlConfiguration.loadConfiguration(new InputStreamReader(resourceStream));
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
	 * Get a resource as a File object for the named resource
	 *
	 * @param name the name of the resource to return as a file object
	 * @return a file object for the named resource
	 * @throws URISyntaxException Let's hope it doesn't.
	 * @deprecated use {@code getResourceStream}
	 */
	@Deprecated
	public static File getResourceFile(final String name) throws URISyntaxException {
		return Paths.get(Thread.currentThread().getContextClassLoader().getResource(name).toURI()).toAbsolutePath().toFile();
	}


	// Keep for now (1/5/25)
	public Configuration getConfig() {

		Configuration configuration = new MemoryConfiguration();
		configuration.set("locale", "en-US");
		configuration.set("language", "en-US");

		return configuration;
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
		if (resourceName == null) { throw new LocalizedException(PARAMETER_NULL, "resourceName"); }
		if (resourceName.isEmpty()) { throw new LocalizedException(PARAMETER_EMPTY, "resourceName"); }
		if (targetDirPath == null) { throw new LocalizedException(PARAMETER_NULL, "targetDirPath"); }

		// Ensure the target directory exists
		Files.createDirectories(targetDirPath);

		// Get the resource as an InputStream
		try (var inputStream = getResourceStream(resourceName)) {
			if (inputStream == null) {
				throw new IOException("ResourceType '" + resourceName + "' not found in the classpath.");
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

}
